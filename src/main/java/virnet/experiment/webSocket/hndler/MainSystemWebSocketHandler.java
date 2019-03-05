/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virnet.experiment.webSocket.hndler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.sf.json.JSONObject;
import virnet.experiment.assistantapi.AutoRefuse;
import virnet.experiment.assistantapi.ExperimentSave;
import virnet.experiment.assistantapi.FacilityOutPut;
import virnet.experiment.assistantapi.MonitorPlatform;
import virnet.experiment.assistantapi.PCConfigureInfo;
import virnet.experiment.assistantapi.PingVerify;
import virnet.experiment.assistantapi.TopoSave;
import virnet.experiment.assistantapi.afterQueue;
import virnet.experiment.assistantapi.reconnect;
import virnet.experiment.assistantapi.savePingResultToDatabase;
import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.NTCEdit;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.resourceapi.CabinetResource;
import virnet.experiment.resourceapi.ResourceRelease;
import virnet.management.combinedao.OrderCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Course;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.ExpTask;
import virnet.management.entity.Class;
import virnet.management.util.ViewUtil;

@Component
public class MainSystemWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {

	private static final ArrayList<WebSocketSession> arrangeUsers;

	static {
		arrangeUsers = new ArrayList<>();
	}

	private static final ArrayList<WebSocketSession> expUsers;

	static {
		expUsers = new ArrayList<>();
	}

	private static final ArrayList<WebSocketSession> monitorUsers;

	static {
		monitorUsers = new ArrayList<>();
	}

	private static final ArrayList<String> groupExisted = new ArrayList<>();

	// 静态变量，用来记录新产生的分组数量
	public static int newGroupNum = 0;

	// 用来标记实验资源分配的次数,按组记录
	public static ConcurrentHashMap<String, Integer> groupResourceDistribution = new ConcurrentHashMap<String, Integer>();

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private WebSocketSession session;

	private Object jsonString;

	// 记录实验主界面用户名和用户组的session，用来传递用户名
	private static ConcurrentHashMap<WebSocketSession, String> MapUserName = new ConcurrentHashMap<WebSocketSession, String>();

	// 记录排队时用户和用户组的map，用来分配不同的实验组，线程安全
	private static ConcurrentHashMap<WebSocketSession, String> userGroupMap = new ConcurrentHashMap<WebSocketSession, String>();

	// 记录管理界面进入时用户和用户组的map，线程安全
	private static ConcurrentHashMap<WebSocketSession, String> userGroupMapPro = new ConcurrentHashMap<WebSocketSession, String>();

	// 记录实验主界面中的用户和用户组的map,用与组内通讯
	private static ConcurrentHashMap<WebSocketSession, String> userMap = new ConcurrentHashMap<WebSocketSession, String>();

	// 存储用户组（group）和实验机柜编号的map
	private static ConcurrentHashMap<String, String> MapEquipment = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和实验机柜ip的map
	private static ConcurrentHashMap<String, String> MapEquipmentIp = new ConcurrentHashMap<String, String>();

	// 记录实验组中成员的序号
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> groupMemberMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();

	// 存储用户组（group）和实验设备名的map map<groupId, map<userName, memberNum>>
	private static ConcurrentHashMap<String, String> MapEquipmentName = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和实验设备序号的map
	private static ConcurrentHashMap<String, String> MapEquipmentNum = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和实验设备端口的map
	private static ConcurrentHashMap<String, String> MapEquipmentPort = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和FacilityConfigure的map
	private static ConcurrentHashMap<String, FacilityConfigure> groupFacilityConfigureMap = new ConcurrentHashMap<String, FacilityConfigure>();

	// 存储用户组（group）和FacilityConfigure的map
	private static ConcurrentHashMap<String, FacilityOutPut> groupFacilityOutPut = new ConcurrentHashMap<String, FacilityOutPut>();

	// 存储用户组（group）和PCExecute的map
	private static ConcurrentHashMap<String, PCExecute> groupPcConfigureMap = new ConcurrentHashMap<String, PCExecute>();

	// 存储用户（user）和PCExecute Key的map
	private static ConcurrentHashMap<String, String> userPcConfigureMap = new ConcurrentHashMap<String, String>();

	// 存储用户（user）和FacilityConfigure Key的map
	private static ConcurrentHashMap<String, String> userFacilityConfigureMap = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和实验号的map
	private static ConcurrentHashMap<String, String> MapExpId = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和实验号的map
	private static ConcurrentHashMap<String, String> MapGroupEndTime = new ConcurrentHashMap<String, String>();

	// 存储用户组（group）和拓扑信息的map
	private static ConcurrentHashMap<String, String> MapTopo = new ConcurrentHashMap<String, String>();

	// 临时保存目标websocketSession
	private static ConcurrentHashMap<String, WebSocketSession> MapWebsocket = new ConcurrentHashMap<String, WebSocketSession>();

	// 临时保存websocketSession的角色
	private static ConcurrentHashMap<WebSocketSession, String> MapRole = new ConcurrentHashMap<WebSocketSession, String>();

	// 记录各小组的求助
	private static ConcurrentHashMap<String, String> MapGroupProblem = new ConcurrentHashMap<String, String>();

	// 记录各小组同意人数zyj
	private static ConcurrentHashMap<String, Integer> MapAgreementNum = new ConcurrentHashMap<String, Integer>();

	// 记录小组所有的命令操作历史 key值为小组预约号，value是存储小组所有设备的命令字符串
	private static ConcurrentHashMap<String, List<String>> MapCommandHistory = new ConcurrentHashMap<String, List<String>>();

	// 记录是否有已经保存最新更改，0为未进行操作，1为最后操作为保存拓扑，2为最后操作为保存配置
	private static ConcurrentHashMap<String, String> MapHaveSave = new ConcurrentHashMap<String, String>();

	// 记录小组当前正在操作的任务号
	private static ConcurrentHashMap<String, Integer> MapTaskOrder = new ConcurrentHashMap<String, Integer>();

	// 记录小组是否正在保存配置的map
	private static ConcurrentHashMap<String, Integer> MapSavingConfigure = new ConcurrentHashMap<String, Integer>();

	// 记录小组实例Id的map
	private static ConcurrentHashMap<String, String> MapCaseId = new ConcurrentHashMap<String, String>();

	// 记录合法投票操作 发起时间
	private static ConcurrentHashMap<String, String> MapLegalOpeTime = new ConcurrentHashMap<String, String>();

	// 记录合法投票操作 发起人
	private static ConcurrentHashMap<String, WebSocketSession> MapLegalOpeUser = new ConcurrentHashMap<String, WebSocketSession>();

	// 标准实验人数
	//private static int standardExpNum = 3;
	
	// 记录每一个组的实际实验人数
	private static ConcurrentHashMap<String, Integer> MapRealExpStuNum = new ConcurrentHashMap<String, Integer>();
	
	// 最大设备数
	private static int equipmentMaxNum = 10;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("connect to the websocket success......");

		// 从web socket session中取回用户名、组别、实验人数
		String userName = (String) session.getAttributes().get("WS_USER_Name");
		String workgroup = (String) session.getAttributes().get("WS_USER_WorkGroup");
		String pageType = (String) session.getAttributes().get("WS_USER_pageType");
		String expRole = (String) session.getAttributes().get("WS_USER_expRole");

		MapUserName.put(session, userName);

		if (pageType.equals("experiment")) {
			//每个用户归属的组
			userGroupMapPro.put(session, workgroup);
			
			//所有的在线用户列表
			arrangeUsers.add(session);
			JSONObject jsonString = new JSONObject();
			if (!expRole.equals("monitor")) {//非监控时需要判断实际实验人数
				System.out.println("check");
				System.out.println("expRole:"+expRole);
				String realExpStuNum = (String) session.getAttributes().get("WS_USER_RealExpStuNum");
				
				if (!MapRealExpStuNum.containsKey(workgroup)) {
					MapRealExpStuNum.put(workgroup, Integer.parseInt(realExpStuNum));
					jsonString.put("type", "connectestablish");
					String mess = jsonString.toString();
					session.sendMessage(new TextMessage(mess));
				} else {
					if (MapRealExpStuNum.get(workgroup) == Integer.parseInt(realExpStuNum)) {
						System.out.println("实际实验人数匹配正确");
						jsonString.put("type", "connectestablish");
						String mess = jsonString.toString();
						session.sendMessage(new TextMessage(mess));
					} else {
						System.out.println("实际实验人数匹配失败");
						jsonString.put("type", "error");
						jsonString.put("content", "您提交的“本次实验人数”与其他组员" + "<br>不同，请与组员确认之后再次进入...");
						String mess = jsonString.toString();
						session.sendMessage(new TextMessage(mess));
						//退出重进
					}
				} 
			}
			else{//监控时无需实验人数
				jsonString.put("type", "connectestablish");
				String mess = jsonString.toString();
				session.sendMessage(new TextMessage(mess));
			}
		}

		if (pageType.equals("monitor")) {
			monitorUsers.add(session);
		}
		this.session = session;
	}

	@Override
	public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
		String message = wsm.getPayload().toString();
		String groupId = userMap.get(wss);
		System.out.println("handleMessage1:" + message);
		// json 解析
		JSONObject jsonString = JSONObject.fromObject(message);

		// 判断是否属于排队页面通信
		if (jsonString.getString("flag").equals("arrange")) {
			String arrangeStatus = jsonString.getString("arrangeStatus");

			if (arrangeStatus.equals("true")) {
				System.out.println("排队页面通信");
				userGroupMap.put(wss, userGroupMapPro.get(wss)); // 将该用户的分组信息加入分组map中
				//queueingLogic(standardExpNum, jsonString, wss); // 预约之后的实验需要多人排队
				queueingLogic(jsonString, wss); // 预约之后的实验需要多人排队
			}
		}
		// 判断是否属于监控页面通信
		else if (jsonString.getString("flag").equals("monitor")) {

			MonitorPlatform MP = new MonitorPlatform();
			MP.show(jsonString, groupExisted, wss, MapGroupEndTime, MapExpId, MapGroupProblem, MapEquipmentIp);
		}
		// 判断是否属于实验界面
		else if (jsonString.getString("flag").equals("experiment")) {

			// 实验开始初始化资源域
			if (jsonString.getString("experimentStatus").equals("start")) {
				//String groupid = null;
				try {
					JSONObject ss = jsonString;
					//groupId = userMap.get(wss);
					MapRole.put(wss, jsonString.getString("expRole"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!groupResourceDistribution.containsKey(groupId)) {
					// 第一个进入实验桌面的同学
					groupResourceDistribution.put(groupId, 1);
					// lrc
					ConcurrentHashMap<String, Integer> memberMap = new ConcurrentHashMap<String, Integer>();
					String userName = MapUserName.get(wss);
					System.out.println("第一个进入实验的同学:" + userName);
					memberMap.put(userName, 1);
					groupMemberMap.put(groupId, memberMap);
				} else {
					Integer waitingNum = groupResourceDistribution.get(groupId);
					groupResourceDistribution.put(groupId, waitingNum + 1);
					// lrc
					ConcurrentHashMap<String, Integer> memberMap = groupMemberMap.get(groupId);
					String userName = MapUserName.get(wss);
					System.out.println("其他进入实验的同学:" + userName);

					try {
						if (!memberMap.containsKey(userName)) {
							@SuppressWarnings("rawtypes")
							Iterator iterator = null;
							iterator = (memberMap.values()).iterator();
							int maxMemberNum = (int) iterator.next();
							while (iterator.hasNext()) {
								int temp = (int) iterator.next();
								if (temp > maxMemberNum)
									maxMemberNum = temp;
								// 这里的Object就是你的集合里的数据类型，不知道可以object.getClass看看
							}
							maxMemberNum++;
							groupMemberMap.get(groupId).put(userName, maxMemberNum);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("全部进入实验桌面："+groupMemberMap.get(groupId));
				}
				// 只有所有人都到齐了才开始分配操作操作
//				if (groupResourceDistribution.get(groupId) == standardExpNum) {
				if (groupResourceDistribution.get(groupId) == MapRealExpStuNum.get(groupId)) {
					groupResourceDistribution.put(groupId, 0);
					afterQueue aq = new afterQueue();
					System.out.println("新建实验桌面");
					if(aq.createNewExpPlatform(jsonString, wss, MapEquipment, MapEquipmentIp, MapEquipmentName,
							MapEquipmentNum, MapEquipmentPort, MapExpId, MapTopo, userMap, expUsers, groupExisted,
							monitorUsers, groupResourceDistribution, MapGroupEndTime, MapUserName, MapCommandHistory,
							MapHaveSave, MapTaskOrder, MapSavingConfigure, MapCaseId, groupMemberMap, MapLegalOpeTime,
							MapLegalOpeUser)){
//						groupResourceDistribution.put(groupId, standardExpNum);//20181027蒋家盛修改，临时设置为0，保证新建失败下次不是重连
						groupResourceDistribution.put(groupId, MapRealExpStuNum.get(groupId));//20181027蒋家盛修改，临时设置为0，保证新建失败下次不是重连
					}
					
				}
				// 增加重连功能 LJJ 2017/4/29
				// 有人中途掉线后重新进来
//				else if (groupResourceDistribution.get(groupId) > standardExpNum) { 
				else if (groupResourceDistribution.get(groupId) > MapRealExpStuNum.get(groupId)) { 
					System.out.println("重连实验桌面");
					reconnect rec = new reconnect();
					rec.recover(jsonString, wss, userMap, MapEquipmentName, MapEquipmentNum, MapEquipmentPort,
							MapEquipment, MapTopo, MapGroupProblem, groupFacilityConfigureMap, groupPcConfigureMap,
							expUsers, monitorUsers, MapCommandHistory, MapGroupEndTime, MapTaskOrder, MapUserName,
							groupMemberMap, userFacilityConfigureMap, userPcConfigureMap);
				}
			}

			// Jtopu提交后，拓扑连接域
			else if (jsonString.getString("type").equals("topoedit")) {

				JSONObject ss = jsonString;

				long start = System.currentTimeMillis();
				String cabinet_NUM = MapEquipment.get(userMap.get(wss)); // 实验机柜编号
				String position = jsonString.getString("position"); // 设备坐标
				String leftNUM_Str = jsonString.getString("leftNUM_Str"); // 左端设备序号串，“##”隔开
				String rightNUM_Str = jsonString.getString("rightNUM_Str"); // 右端设备序号串，“##”隔开
				String leftport_Str = jsonString.getString("leftport_Str"); // 左端设备端口序号串，“##”隔开
				String rightport_Str = jsonString.getString("rightport_Str"); // 右端设备端口序号串，“##”隔开

				String cabinetIp = MapEquipmentIp.get(userMap.get(wss));
				NTCEdit ntcEdit = new NTCEdit(cabinet_NUM, leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str,
						cabinetIp);
				if (ntcEdit.edit()) {
					String connection_str = leftNUM_Str + "%%" + rightNUM_Str + "%%" + leftNUM_Str + "%%"
							+ rightport_Str;
					jsonString.put("success", true);
				} else {
					jsonString.put("success", false);
				}
				long end = System.currentTimeMillis();
				String mess1 = jsonString.toString();
				wss.sendMessage(new TextMessage(mess1));

				ss.put("type", "equipConnectionInfo");
				String mess2 = ss.toString();

				for (WebSocketSession user : expUsers) {
					try {
						if (user.isOpen() && (userMap.get(user).equals(groupId)) && (!user.equals(wss))){
							user.sendMessage(new TextMessage(mess2));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// 暂存本次拓扑连接结果到服务器
				String topoInfo = position + "@" + leftNUM_Str + "@" + rightNUM_Str + "@" + leftport_Str + "@"
						+ rightport_Str;
				MapTopo.put(userMap.get(wss), topoInfo);
			}
			// 保存为任务标准拓扑，任务号为0时为初始拓扑
			else if (jsonString.getString("type").equals("topoSaveToDatabase")) {// **20171122蒋家盛封装拓扑保存函数

				String expCaseId = MapCaseId.get(userMap.get(wss));
				String expRole = MapRole.get(wss);
				// 保存拓扑的核心类及方法
				TopoSave topoSave = new TopoSave();
				boolean isSuccess = topoSave.save(groupId, MapTopo, MapTaskOrder, MapExpId, expRole, expCaseId);
				jsonString.put("success", isSuccess);
				sendToGroup(wss, jsonString);
				MapHaveSave.put(userMap.get(wss), "1");
			}

			// 输入的设备命令和输出设备结果处理域
			else if (jsonString.getString("type").equals("command")) {
				JSONObject ss = jsonString;
				String equipmentNumber = jsonString.getString("inputEquipmentNumber");
				String commandDetail = jsonString.getString("content");
				String[] sourceStrArray = jsonString.getString("equipmentName").split("##");
				if (sourceStrArray[Integer.parseInt(jsonString.getString("inputEquipmentNumber"))].equals("PC")) {

					String inputSentence = "Administrator:/>" + commandDetail + "\n";
					ss.put("content", inputSentence);

					String previousHistory = MapCommandHistory.get(groupId).get(Integer.parseInt(equipmentNumber));
					MapCommandHistory.get(groupId).set(Integer.parseInt(equipmentNumber),
							previousHistory + inputSentence);

					sendToGroup(wss, ss);
					pcCommandConfigure(equipmentNumber, commandDetail, wss);
				} else {
					FacilityCommandConfigure(equipmentNumber, commandDetail, wss);
				}
			}

			// 聊天信息处理域
			else if (jsonString.getString("type").equals("communication")) {
				// 监控管理员消息，全员播报

				if (monitorUsers.contains(wss)) {

					String mess = jsonString.toString();
					for (WebSocketSession user : expUsers) {

						if (user.isOpen())
							user.sendMessage(new TextMessage(mess));
					}
					for (WebSocketSession user : monitorUsers) {

						if (user.isOpen())
							user.sendMessage(new TextMessage(mess));
					}
				}
				// 组内消息
				else {
					sendToGroup(wss, jsonString);
				}
				// /******/
				// AutoScore aa = new AutoScore();
				// aa.autoTopoScore("1", "2017072415431002");
				// /******/
			}
			// 发起操作zyj
			else if (jsonString.getString("type").equals("agreement")) {
				if (jsonString.getString("content").equals("next")) {
					if (MapHaveSave.get(userMap.get(wss)).equals("0")) {
						jsonString.put("content", "noSaveToNext");
					}
				}
				if (jsonString.getString("content").equals("exit")) {
					if (MapHaveSave.get(userMap.get(wss)).equals("0")) {
						jsonString.put("content", "noSaveToExit");
					}
				}
				String groupNum = userMap.get(wss);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate = sdf.format(new Date());
				MapLegalOpeTime.put(groupNum, currentDate);// 发起时间
				MapLegalOpeUser.put(groupNum, wss);// 发起人
				MapAgreementNum.put(groupNum, 0);
				AutoRefuse RefuseThread = new AutoRefuse(currentDate, jsonString, wss, MapLegalOpeTime, userMap,
						expUsers);
				RefuseThread.start();
				sendToGroupWithoutMonitor(wss, jsonString);
			} else if (jsonString.getString("type").equals("agree")) {
				String groupNum = userMap.get(wss);
				Integer number = MapAgreementNum.get(groupNum);
				number++;
				MapAgreementNum.put(groupNum, number);
				
				//监控员的聊天框也要显示谁同意了，所以sendToGroup而不是sendToGroupWithoutMonitor
				sendToGroup(wss, jsonString);
				
//				if (number == standardExpNum) {
				if (number == MapRealExpStuNum.get(groupNum)) {
					MapLegalOpeTime.put(groupNum, "0");
					MapAgreementNum.put(groupNum, 0);
					jsonString.put("type", "openbutton");
					sendToGroup(wss, jsonString);
					jsonString.put("type", "permit");

					if (jsonString.getString("content").equals("agree saveconfigure")
							|| jsonString.getString("content").equals("agree savetopo")) {
						String mess = jsonString.toString();
						
						//谁发起谁执行
						WebSocketSession user = MapLegalOpeUser.get(groupNum);
						if (user.isOpen())
							user.sendMessage(new TextMessage(mess));
					}

					else if (jsonString.getString("content").equals("agree exit")
							|| jsonString.getString("content").equals("agree noSaveToExit")) {
						if (MapHaveSave.get(userMap.get(wss)).equals("1")) {
							jsonString.put("type", "alert");
							jsonString.put("content", "请保存配置后再操作");
							
							//此消息监控员无需收到
							sendToGroupWithoutMonitor(wss, jsonString);
						} else {
							String mess = jsonString.toString();
							for (WebSocketSession user : expUsers) {

								// 只需要其中一名组员
								if (user.isOpen() && (userMap.get(user).equals(groupNum))) {
									user.sendMessage(new TextMessage(mess));
									break;
								}
							}
							Thread.sleep(500);
							jsonString.put("content", "goodbye");
							
							//如果实验结束了监控员也要离开
							sendToGroup(wss, jsonString);
						}
					} else {// jsonString.getString("content").equals("agree
							// next")
						if (MapHaveSave.get(userMap.get(wss)).equals("1")) {
							jsonString.put("type", "alert");
							jsonString.put("content", "请保存配置后再操作");
							
							//此消息监控员无需收到
							sendToGroupWithoutMonitor(wss, jsonString);
						} else {
							MapHaveSave.put(userMap.get(wss), "0");
							Integer taskorder = MapTaskOrder.get(userMap.get(wss)) + 1;
							MapTaskOrder.put(userMap.get(wss), taskorder);
							jsonString.put("expTaskOrder", taskorder + "");
							ExpTaskDAO etDAO = new ExpTaskDAO();
							ExpTask exptask = new ExpTask();
							String expId = MapExpId.get(userMap.get(wss));
							exptask = (ExpTask) etDAO.getByNProperty("expId", expId, "expTaskOrder", taskorder + "");
							String taskname = exptask.getExpTaskContent();
							jsonString.put("TaskName", taskname);
							
							//监控员也要跟随进入下一个任务
							sendToGroup(wss, jsonString);
						}
					}

				}
			} else if (jsonString.getString("type").equals("refuse")) {
				String groupNum = userMap.get(wss);
				MapLegalOpeTime.put(groupNum, "0");
				MapAgreementNum.put(groupNum, 0);
				sendToGroup(wss, jsonString);
			}
			// 求助域
			else if (jsonString.getString("type").equals("help")) {

				jsonString.put("type", "communication");
				jsonString.put("user", "系统消息");

				if (monitorUsers.size() == 0) {
					jsonString.put("content", "监控员未上线");
					sendToGroup(wss, jsonString);
				} else {

					// 获取组号及组名

					jsonString.put("content", "收到小组帮助请求");
					jsonString.put("groupId", groupId);

					MapGroupProblem.put(groupId, jsonString.getString("problemList"));
					// 发送给监控员
					sendToMonitor(wss, jsonString);
					// 发送给本组组员
					jsonString.put("content", MapUserName.get(wss) + "发送帮助信息");
					sendToGroup(wss, jsonString);
				}
			}
			// 加锁域
			else if (jsonString.getString("type").equals("lock")) {
				boolean flag = true;
				String[] sourceStrArray = jsonString.getString("equipmentName").split("##");
				if (sourceStrArray[Integer.parseInt(jsonString.getString("inputEquipmentNumber"))].equals("PC")) {
					if (jsonString.getString("lock").equals("lock")) {
						flag = pcInitial(jsonString.getString("inputEquipmentNumber"), wss, jsonString);
					}
					if (jsonString.getString("lock").equals("unlock")) {
						pcCancel(jsonString.getString("inputEquipmentNumber"), wss);
					}
				} else {
					if (jsonString.getString("lock").equals("lock")) {
						flag = FacilityInitial(jsonString.getString("inputEquipmentNumber"), wss, jsonString);
					}
					if (jsonString.getString("lock").equals("unlock")) {
						FacilityCancel(jsonString.getString("inputEquipmentNumber"), wss);
					}
				}
				if (flag) {
					// lrc
					int controllerNum = groupMemberMap.get(userMap.get(wss)).get(MapUserName.get(wss));
					jsonString.put("equipmentControler", controllerNum);
					int equipnum = Integer.parseInt(jsonString.getString("inputEquipmentNumber"));
					// lrc
					sendToOtherGroupMember(wss, jsonString);
				}
			}
			// 保存实验配置及验证ping
			else if (jsonString.getString("type").equals("saveConfigureFile")) {

				// 表示开始保存配置及ping
				MapSavingConfigure.put(groupId, 1);

				JSONObject ss = jsonString;
				ss.put("type", "loading");
				sendToGroup(wss, ss);
				jsonString.put("type", "saveConfigureFile");

				boolean lock = true; // 判断是否已经释放所有的设备

				// 判断有无属于本组的设备但是没有释放
				int number = Integer.parseInt(jsonString.getString("equipmentNumber"));
				for (int i = 0; i < number; i++) {
					if (groupFacilityConfigureMap.get(groupId + i) != null) {
						lock = false;
						break;
					} else if (groupPcConfigureMap.get(groupId + i) != null) {
						lock = false;
						break;
					}
				}

				if (!lock) {
					jsonString.put("success", "plzRelease");
				} else {
					String cabinet_num = jsonString.getString("cabinet_num");
					String expId = jsonString.getString("expId");
					String expTaskOrder = jsonString.getString("expTaskOrder");
					String equipmentNumber = jsonString.getString("equipmentNumber");
					String expRole = jsonString.getString("expRole");

					String leftNUM_Str = jsonString.getString("leftNUM_Str"); // 左端设备序号串，“##”隔开
					String rightNUM_Str = jsonString.getString("rightNUM_Str"); // 右端设备序号串，“##”隔开
					String leftport_Str = jsonString.getString("leftport_Str"); // 左端设备端口序号串，“##”隔开
					String rightport_Str = jsonString.getString("rightport_Str"); // 右端设备端口序号串，“##”隔开
					String expCaseId = MapCaseId.get(groupId);

					boolean success = false;

					String cabinetIp = MapEquipmentIp.get(groupId);
					Integer resultTaskId = 0;
					// 保存到实验结果的表
					if (expRole.equals("stu")) {
						ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
						resultTaskId = taskcDAO.getResultTaskId(expCaseId, expId, expTaskOrder);
					}
					// 保存到实验模板的表
					else {
						// 管理员操作不需要实验结果Id参数
						resultTaskId = 0;
					}

					// 保存配置的核心类及方法，前期视为参数准备
					ExperimentSave es = new ExperimentSave(cabinet_num, expId, expTaskOrder, equipmentNumber, expRole,
							resultTaskId, cabinetIp);
					success = es.save(userMap, expUsers, wss);// 20171031蒋家盛新增进度条

					ss.put("type", "loading");
					sendToGroup(wss, ss);
					jsonString.put("type", "pingVerify");

					// 等待配置过程释放网卡
					Thread.sleep(3000);

					// ping验证分为三个步骤，获取各pc ip地址，pc互ping，保存到数据库

					PCConfigureInfo pcInfo = new PCConfigureInfo(cabinet_num, Integer.parseInt(equipmentNumber),
							cabinetIp);
					// 获取ip地址
					String[] pcip = pcInfo.getIpAddress();

					PingVerify pv = new PingVerify(cabinet_num, Integer.parseInt(equipmentNumber), cabinetIp);
					String[] verifyResult = pv.getVerifyResult(pcip, userMap, expUsers, wss);

					savePingResultToDatabase SPRTDB = new savePingResultToDatabase(verifyResult, cabinet_num, expId,
							expTaskOrder, equipmentNumber, leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str,
							expRole, expCaseId);
					success = SPRTDB.save(pcip);

					// 已经保存了最新版本，拓扑和配置结果是一致的
					MapHaveSave.put(groupId, "2");

					jsonString.put("success", success);
				}

				// 表示开始结束配置及ping
				MapSavingConfigure.put(groupId, 0);

				sendToGroup(wss, jsonString);
			}
			// // ping验证
			// else if (jsonString.getString("type").equals("pingVerify")) {
			// JSONObject ss = jsonString;
			// ss.put("type", "loading");
			// sendToGroup(wss, ss);
			// jsonString.put("type", "pingVerify");
			// boolean lock = true; // 判断是否已经释放所有的设备
			//
			// // 判断有无属于本组的设备但是没有释放
			// int number =
			// Integer.parseInt(jsonString.getString("equipmentNumber"));
			// for (int i = 0; i < number; i++) {
			// if (groupFacilityConfigureMap.get(userMap.get(wss) + i) != null)
			// {
			// lock = false;
			// break;
			// } else if (groupPcConfigureMap.get(userMap.get(wss) + i) != null)
			// {
			// lock = false;
			// break;
			// }
			// }
			//
			// if (!lock) {
			// jsonString.put("success", "plzRelease");
			// }
			//
			// else {
			// boolean success = true;
			//
			// String cabinet_num = jsonString.getString("cabinet_num");
			// String expId = jsonString.getString("expId");
			// String expTaskOrder = jsonString.getString("expTaskOrder");
			// String equipmentNumber = jsonString.getString("equipmentNumber");
			//
			// String leftNUM_Str = jsonString.getString("leftNUM_Str"); //
			// 左端设备序号串，“##”隔开
			// String rightNUM_Str = jsonString.getString("rightNUM_Str"); //
			// 右端设备序号串，“##”隔开
			// String leftport_Str = jsonString.getString("leftport_Str"); //
			// 左端设备端口序号串，“##”隔开
			// String rightport_Str = jsonString.getString("rightport_Str"); //
			// 右端设备端口序号串，“##”隔开
			//
			// String expRole = jsonString.getString("expRole");
			// String expCaseId = jsonString.getString("expCaseId");
			//
			//
			// //ping验证分为四个步骤，获得机柜ip地址，获取各pc ip地址，pc互ping，保存到数据库
			//
			// // 获取各网卡ip地址,首先获得机柜ip
			// String cabinetIp = MapEquipmentIp.get(userMap.get(wss));
			// PCConfigureInfo pcInfo = new PCConfigureInfo(cabinet_num,
			// Integer.parseInt(equipmentNumber),
			// cabinetIp);
			// // 获取地址
			// String[] pcip = pcInfo.getIpAddress();
			//
			// PingVerify pv = new
			// PingVerify(cabinet_num,Integer.parseInt(equipmentNumber),cabinetIp);
			// String[] verifyResult = pv.getVerifyResult(pcip, userMap,
			// expUsers, wss);
			//
			// savePingResultToDatabase SPRTDB = new
			// savePingResultToDatabase(verifyResult, cabinet_num, expId,
			// expTaskOrder,
			// equipmentNumber, leftNUM_Str, rightNUM_Str,leftport_Str,
			// rightport_Str,expRole, expCaseId);
			// success = SPRTDB.save(pcip);
			//
			// jsonString.put("success", success);
			// }
			// sendToGroup(wss, jsonString);
			// }
			// 释放资源域
			else if (jsonString.getString("type").equals("release")) {
				releaseEquipment(wss, jsonString);
			}
		}

	}

	@Override
	public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
		if (wss.isOpen()) {

			JSONObject jsonString = new JSONObject();

			jsonString.put("type", "lock");
			jsonString.put("lock", "unlock");
			for (Integer i = 0; i < equipmentMaxNum; i++) {
				if (userPcConfigureMap.containsKey(wss + i.toString())) {
					pcCancel(i + "", wss);
					jsonString.put("inputEquipmentNumber", i);
					onlySendToOtherGroupMember(wss, jsonString);
				} else if (userFacilityConfigureMap.containsKey(wss + i.toString())) {
					FacilityCancel(i + "", wss);
					jsonString.put("inputEquipmentNumber", i);
					onlySendToOtherGroupMember(wss, jsonString);
				}
			}

			jsonString.put("type", "communication");
			jsonString.put("user", "系统消息");
			if (monitorUsers.contains(wss)) {
				jsonString.put("content", "(管理员)" + MapUserName.get(wss) + "退出了本小组实验平台");
			} else if (expUsers.contains(wss)) {
				jsonString.put("content", MapUserName.get(wss) + "退出了本小组实验平台");
			}
			if (jsonString.containsKey("content")) {
				onlySendToOtherGroupMember(wss, jsonString);
			}
			
			//不确定是在排队阶段还是在实验阶段，因此需要都判定一下
			String groupId = null;
			if (userGroupMapPro.containsKey(wss))
				groupId = userGroupMapPro.get(wss);
			else if(userMap.containsKey(wss))
				groupId = userMap.get(wss);

//			if(isGroupEmpty(wss)){//小组为空时自动释放机柜
//				jsonString.put("type","release");
//				releaseEquipment(wss, jsonString);
//			}
			if (expUsers.contains(wss))
				expUsers.remove(wss);
			if (userMap.containsKey(wss))
				userMap.remove(wss);
			if (userGroupMapPro.containsKey(wss))
				userGroupMapPro.remove(wss);
			if (userGroupMap.containsKey(wss))
				userGroupMap.remove(wss);
			if (MapUserName.containsKey(wss))
				MapUserName.remove(wss);
			if (MapRole.containsKey(wss))
				MapRole.remove(wss);
			if (arrangeUsers.contains(wss))
				arrangeUsers.remove(wss);
			if (monitorUsers.contains(wss))
				monitorUsers.remove(wss);
			
			try{
			   if (MapRealExpStuNum.containsKey(groupId)
			    && !userMap.containsValue(groupId) 
			    && !userGroupMap.containsValue(groupId) )
				   MapRealExpStuNum.remove(groupId);
		    }catch(Exception e){
			    System.out.println("以上的map如果本来是空的话会出错");
		   }
			wss.close();
		}
		System.out.println("websocket connection closed......ERROR");

	}

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {

		try {
			JSONObject jsonString = new JSONObject();

			jsonString.put("type", "lock");
			jsonString.put("lock", "unlock");
			for (Integer i = 0; i < equipmentMaxNum; i++) {
				if (userPcConfigureMap.containsKey(wss + i.toString())) {
					pcCancel(i + "", wss);
					jsonString.put("inputEquipmentNumber", i);
					onlySendToOtherGroupMember(wss, jsonString);
				} else if (userFacilityConfigureMap.containsKey(wss + i.toString())) {
					FacilityCancel(i + "", wss);
					jsonString.put("inputEquipmentNumber", i);
					onlySendToOtherGroupMember(wss, jsonString);
				}
			}
			
			//不确定是在排队阶段还是在实验阶段，因此需要都判定一下
			String groupId = null;
			if (userGroupMapPro.containsKey(wss))
				groupId = userGroupMapPro.get(wss);
			else if(userMap.containsKey(wss))
				groupId = userMap.get(wss);
			
//			 if(isGroupEmpty(wss) && userMap.get(wss) != null
//			 &&MapEquipment.get(userMap.get(wss))!=null){
//			 System.out.println("是否小组已空：" + isGroupEmpty(wss));
//			 String role = MapRole.get(wss);
//			 jsonString.put("expRole",role);
//			 jsonString.put("type","release");
//			 releaseEquipment(wss, jsonString);
//			 }


			jsonString.put("type", "communication");
			jsonString.put("user", "系统消息");
			if (monitorUsers.contains(wss)) {
				jsonString.put("content", "(管理员)" + MapUserName.get(wss) + "退出了本小组实验平台");
			} else if (expUsers.contains(wss)) {
				jsonString.put("content", MapUserName.get(wss) + "退出了本小组实验平台");
			}
			if (jsonString.containsKey("content")) {
				onlySendToOtherGroupMember(wss, jsonString);
			}

			if (expUsers.contains(wss))
				expUsers.remove(wss);
			if (userMap.containsKey(wss))
				userMap.remove(wss);
			if (userGroupMapPro.containsKey(wss))
				userGroupMapPro.remove(wss);
			if (userGroupMap.containsKey(wss))
				userGroupMap.remove(wss);
			if (MapUserName.containsKey(wss))
				MapUserName.remove(wss);
			if (MapRole.containsKey(wss))
				MapRole.remove(wss);
			if (arrangeUsers.contains(wss))
				arrangeUsers.remove(wss);
			if (monitorUsers.contains(wss))
				monitorUsers.remove(wss);
			try{
			    if (MapRealExpStuNum.containsKey(groupId) 
					 && !userMap.containsValue(groupId) 
					 && !userGroupMap.containsValue(groupId))
						MapRealExpStuNum.remove(groupId);
			}catch(Exception e){
				System.out.println("以上的map如果本来是空的话会出错");
			}

			wss.close();
			System.out.println("websocket connection closed......CLOSE");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 以下是自定义方法部分
	 */
	public void sendMessageo(String message) throws IOException {
		this.session.sendMessage(new TextMessage(message));
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MainSystemWebSocketHandler.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		MainSystemWebSocketHandler.onlineCount--;
	}
	
	
	//排队逻辑
	// 分组逻辑 加锁
	public synchronized void queueingLogic(JSONObject jsonString, WebSocketSession wss) throws IOException {
		System.out.println("enter the method!");
		List<WebSocketSession> record = new ArrayList<WebSocketSession>();

		String group = userGroupMap.get(wss);
		int number = 0;
		if (!groupExisted.contains(group)) {
			for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) {
				if (entry.getValue().equals(group)) {
					number++;
				}
			}
			System.out.println("当前有" + number + "人在队列中");
			jsonString.put("replyType", "queue");

			jsonString.put("type", "progress");
			jsonString.put("content", 100 * number / MapRealExpStuNum.get(group));
			System.out.println("jsonString.get('expId'):" + jsonString.get("expId"));
			sendToArrangeUsers(wss, jsonString);

			if (number >= MapRealExpStuNum.get(group)) {
				for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) {
					if (entry.getValue().equals(group)) {
						System.out.println("ready to send");
						record.add(entry.getKey());
					}
				}
				groupExisted.add(group);
			}

		} else {

			record.add(wss);
		}
		// ConcurrentArrayList在迭代的时候是不允许remove的，所以不能再循环里面进行
		for (WebSocketSession successMatch : record) {

			// 将用户从排队状态正式转换为实验状态
			expUsers.add(successMatch);
			userMap.put(successMatch, group);

			// 取消排队状态
			arrangeUsers.remove(successMatch);
			userGroupMap.remove(successMatch);
			userGroupMapPro.remove(successMatch);

			sendStatausToGroup(jsonString, successMatch, group);
		}
	}


	// 给默认用户组的的用户发送状态消息
	public static synchronized void sendStatausToGroup(JSONObject jsonString, WebSocketSession webSS, String finalGroup)
			throws IOException {
		System.out.println("sendStatausToGroup");
		// jsonString.put("ready", "true");
		// System.out.println(jsonString.getString("ready"));
		// jsonString.put("finalGroup", finalGroup);
		// System.out.println(jsonString.getString("finalGroup"));
		// System.out.println("before jump:");
		jsonString.put("type", "ready");
		String mess = jsonString.toString();
		webSS.sendMessage(new TextMessage(mess));
	}

	public void job1() {
		System.out.println("loading job1");
	}

	//发送消息给所有正在排队的同组成员     *排队阶段*
	public void sendToArrangeUsers(WebSocketSession wss, JSONObject jsonString) {
			// 向还在排队的组员发送信息，即未生成确定的小组
			String groupid = userGroupMap.get(wss);
			String mess = jsonString.toString();

			for (WebSocketSession user : arrangeUsers) {
				try {
					if (user.isOpen() && userGroupMap.get(user) != null && (userGroupMap.get(user).equals(groupid))) {
						synchronized(user){
							user.sendMessage(new TextMessage(mess));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	//发送消息给所有组员（包括管理员和自己）    *实验阶段*
	public void sendToGroup(WebSocketSession wss, JSONObject jsonString) {

		String groupid = userMap.get(wss);
		String mess = jsonString.toString();
		System.out.println("sendToGroup组号" + groupid);

		for (WebSocketSession user : expUsers) {
			try {
				if (user.isOpen() && (userMap.get(user).equals(groupid))) {
					synchronized(user){
						user.sendMessage(new TextMessage(mess));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//发送消息给除了监控员之外的所有组员（包括管理员和自己）   *实验阶段*
	public void sendToGroupWithoutMonitor(WebSocketSession wss, JSONObject jsonString) {

			String groupid = userMap.get(wss);
			String mess = jsonString.toString();
			System.out.println("sendToGroup组号" + groupid);

			for (WebSocketSession user : expUsers) {
				try {
					if (user.isOpen() && (userMap.get(user).equals(groupid)) && !monitorUsers.contains(user)) {
						synchronized(user){
							user.sendMessage(new TextMessage(mess));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	//发送消息给所有组员（包括管理员和自己），但是自己会有isMyself：true的标记    *实验阶段*
	// 还是会发送给自己的，不过有一个isMyself的域
	public void sendToOtherGroupMember(WebSocketSession wss, JSONObject jsonString) throws IOException {

		String groupid = userMap.get(wss);
		for (WebSocketSession user : expUsers) {

			// 发给除了自己以外的组员
			if (user.isOpen() && (userMap.get(user).equals(groupid) && (!user.equals(wss)))) {
				jsonString.put("isMyself", false);
				String mess = jsonString.toString();
				synchronized(user){
					user.sendMessage(new TextMessage(mess));
				}
			}
			if (user.isOpen() && (userMap.get(user).equals(groupid) && (user.equals(wss)))) {
				jsonString.put("isMyself", true);
				String mess = jsonString.toString();
				synchronized(user){
					user.sendMessage(new TextMessage(mess));
				}
			}
		}

	}

	//发送消息到其他组员（包括管理员），不会发给自己     *实验阶段*
	// 真的只会发送给组员
	public void onlySendToOtherGroupMember(WebSocketSession wss, JSONObject jsonString) throws IOException {

		try {
			String groupid = userMap.get(wss);
			for (WebSocketSession user : expUsers) {

				// 发给除了自己以外的组员
				if (user.isOpen() && (userMap.get(user).equals(groupid) && (!user.equals(wss)))) {
					String mess = jsonString.toString();
					synchronized(user){
						user.sendMessage(new TextMessage(mess));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 判断是否还有组员未退出
	public boolean isGroupEmpty(WebSocketSession wss) {

		String groupId = userMap.get(wss);
		for (WebSocketSession user : expUsers) {
			try {
				if (user.isOpen() && userMap.get(user).equals(groupId) && !user.equals(wss)) {

					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	//发送消息给所有在线的管理员     *实验阶段*
	public void sendToMonitor(WebSocketSession wss, JSONObject jsonString) {

		String mess = jsonString.toString();
		for (WebSocketSession user : monitorUsers) {
			try {
				if (user.isOpen()) {
					synchronized(user){
						user.sendMessage(new TextMessage(mess));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 初始化设备连接
	public boolean FacilityInitial(String equipmentNumber, WebSocketSession wss, JSONObject jsonString) {
		System.out.println(equipmentNumber + "号设备已控制");
		String cabinet_NUM = MapEquipment.get(userMap.get(wss)); // 实验机柜编号
		String facility_NUM = (Integer.parseInt(equipmentNumber) + 1) + "";
		String cabinetIp = MapEquipmentIp.get(userMap.get(wss));
		FacilityConfigure facilityConfigure = new FacilityConfigure(cabinet_NUM, facility_NUM, cabinetIp);
		if (facilityConfigure.connect()) {

			FacilityOutPut facilityOutPutThread = new FacilityOutPut(facilityConfigure.getInputStream(), wss,
					jsonString, expUsers, userMap, MapCommandHistory);
			facilityOutPutThread.start();
			groupFacilityConfigureMap.put(userMap.get(wss) + equipmentNumber, facilityConfigure);
			groupFacilityOutPut.put(userMap.get(wss) + equipmentNumber, facilityOutPutThread);
			userFacilityConfigureMap.put(wss + equipmentNumber, userMap.get(wss) + equipmentNumber);
			return true;

		} else {
			return false;
		}
	}

	// 注销设备连接
	public void FacilityCancel(String equipmentNumber, WebSocketSession wss) {
		FacilityConfigure facilityConfigure = groupFacilityConfigureMap.get(userMap.get(wss) + equipmentNumber);
		FacilityOutPut facilityOutPutThread = groupFacilityOutPut.get(userMap.get(wss) + equipmentNumber);
		facilityOutPutThread.stopThread();
		System.out.println("设备结束配置");
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		facilityConfigure.end();
		groupFacilityConfigureMap.remove(userMap.get(wss) + equipmentNumber);
		groupFacilityOutPut.remove(userMap.get(wss) + equipmentNumber);
		userFacilityConfigureMap.remove(wss + equipmentNumber);
	}

	// PC连接
	public boolean pcInitial(String equipmentNumber, WebSocketSession wss, JSONObject jsonString) {
		System.out.println(equipmentNumber + "号PC已控制");
		String cabinet_NUM = MapEquipment.get(userMap.get(wss)); // 实验机柜编号
		String facility_NUM = (Integer.parseInt(equipmentNumber) + 1) + "";
		String cabinetIp = MapEquipmentIp.get(userMap.get(wss));
		PCExecute pcExecute = new PCExecute(cabinet_NUM, facility_NUM, cabinetIp);
		if (pcExecute.connect()) {

			FacilityOutPut facilityOutPutThread = new FacilityOutPut(pcExecute.getInputStream(), wss, jsonString,
					expUsers, userMap, MapCommandHistory);
			facilityOutPutThread.start();
			groupPcConfigureMap.put(userMap.get(wss) + equipmentNumber, pcExecute);
			groupFacilityOutPut.put(userMap.get(wss) + equipmentNumber, facilityOutPutThread);
			userPcConfigureMap.put(wss + equipmentNumber, userMap.get(wss) + equipmentNumber);
			return true;

		} else {
			return false;
		}
	}

	public void pcCancel(String equipmentNumber, WebSocketSession wss) {
		PCExecute pcExecute = groupPcConfigureMap.get(userMap.get(wss) + equipmentNumber);
		FacilityOutPut facilityOutPutThread = groupFacilityOutPut.get(userMap.get(wss) + equipmentNumber);
		facilityOutPutThread.stopThread();
		System.out.println("PC结束配置");
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		pcExecute.end();
		groupFacilityOutPut.remove(userMap.get(wss) + equipmentNumber);
		groupPcConfigureMap.remove(userMap.get(wss) + equipmentNumber);
		userPcConfigureMap.remove(wss + equipmentNumber);
	}

	// 与第一层进行设备交互，输入命令，返回输出命令
	public void FacilityCommandConfigure(String equipmentNumber, String commandDetail, WebSocketSession wss) {
		FacilityConfigure facilityConfigure = groupFacilityConfigureMap.get(userMap.get(wss) + equipmentNumber);

		if (commandDetail.equals("end")) {
		}
		if (commandDetail.equals("")) {
			commandDetail = "NEWLINE";
		}

		facilityConfigure.configure(commandDetail);
	}

	// 与第一层进行PC交互，输入命令，返回输出命令
	public void pcCommandConfigure(String equipmentNumber, String commandDetail, WebSocketSession wss) {
		PCExecute pcExecute = groupPcConfigureMap.get(userMap.get(wss) + equipmentNumber);

		if (commandDetail.equals("end")) {
		}
		if (commandDetail.equals("")) {
			commandDetail = "NEWLINE";
		}
		pcExecute.execute(commandDetail);
	}

	// 释放机柜资源
	public void releaseEquipment(WebSocketSession wss, JSONObject jsonString) {
		long start = System.currentTimeMillis();
		String groupId = userMap.get(wss);
		String cabinet_NUM = MapEquipment.get(groupId); // 实验机柜编号
		String cabinetIp = MapEquipmentIp.get(groupId);

		// 释放机柜
		new CabinetResource().release(cabinetIp);
		ResourceRelease resourceRelease = new ResourceRelease(cabinet_NUM, cabinetIp);

		if (resourceRelease.release()) {
			System.out.println("成功释放资源");
			jsonString.put("success", true);
		} else {
			jsonString.put("success", false);
		}
		long end = System.currentTimeMillis();
		System.out.println("资源释放用时：" + (end - start) + "ms");

		// 删除组信息，进入实验时需要重新排队 LJJ 2017/4/29
		groupExisted.remove(groupId);

		// 删除实验相关的中间变量
		MapCommandHistory.remove(groupId);
		MapHaveSave.remove(groupId);
		MapTaskOrder.remove(groupId);
		MapSavingConfigure.remove(groupId);
		MapCaseId.remove(groupId);
		groupMemberMap.remove(groupId);
		MapLegalOpeTime.remove(groupId);
		MapLegalOpeUser.remove(groupId);
		MapRealExpStuNum.remove(groupId);

		// 删除资源信息 LJJ 2017/4/29
		MapEquipment.remove(groupId);
		MapEquipmentName.remove(groupId);
		MapEquipmentNum.remove(groupId);
		MapEquipmentPort.remove(groupId);
		MapEquipmentIp.remove(groupId);
		MapTopo.remove(groupId);

		// 删除资源分配记录 LJJ 2.17/4/29
		groupResourceDistribution.remove(groupId);

		sendToGroup(wss, jsonString);

		if (jsonString.getString("expRole").equals("stu")) {

			jsonString.put("type", "communication");
			jsonString.put("user", "系统消息");
			jsonString.put("content", "小组( " + groupId + " )退出了实验");
			sendToMonitor(wss, jsonString);

			jsonString.put("type", "deleteExpGroup");
			jsonString.put("groupId", groupId);
			sendToMonitor(wss, jsonString);

			// 修改预约状态
			OrderCDAO oCDAO = new OrderCDAO();
			oCDAO.changeOrderStatus(Integer.parseInt(groupId), "实验结束");
		}

	}
}
