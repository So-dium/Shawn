package virnet.experiment.assistantapi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;
import virnet.experiment.combinedao.ExpConnectCDAO;
import virnet.experiment.combinedao.ExpTopoPositionCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.dao.ExpTopoDAO;
import virnet.experiment.entity.ExpTopo;
import virnet.experiment.resourceapi.CabinetResource;
import virnet.experiment.resourceapi.ResourceAllocate;
import virnet.management.combinedao.CabinetTempletDeviceInfoCDAO;
import virnet.management.combinedao.OrderCDAO;
import virnet.management.combinedao.TaskInfoCDAO;
import virnet.management.dao.CaseDAO;
import virnet.management.dao.CaseExpDAO;
import virnet.management.dao.CaseMemberDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.entity.Case;
import virnet.management.entity.CaseExp;
import virnet.management.entity.CaseMember;
import virnet.management.entity.Exp;
import virnet.management.entity.ExpTask;
import virnet.management.entity.Order;
import virnet.management.util.DateUtil;

public class afterQueue {
	
	public afterQueue() {

	}

	public boolean createNewExpPlatform(JSONObject jsonString ,
			                            WebSocketSession wss,
			                            ConcurrentHashMap<String, String> MapEquipment,
			                            ConcurrentHashMap<String, String> MapEquipmentIp,
			                            ConcurrentHashMap<String, String> MapEquipmentName,
			                            ConcurrentHashMap<String, String> MapEquipmentNum,
			                            ConcurrentHashMap<String, String> MapEquipmentPort,
			                            ConcurrentHashMap<String, String> MapExpId,
			                            ConcurrentHashMap<String, String> MapTopo,
			                            ConcurrentHashMap<WebSocketSession, String> userMap,
			                            ArrayList<WebSocketSession> expUsers,
			                            ArrayList<String> groupExisted,
			                            ArrayList<WebSocketSession> monitorUsers,
			                            ConcurrentHashMap<String, Integer> groupResourceDistribution,
			                            ConcurrentHashMap<String, String> MapGroupEndTime,
			                            ConcurrentHashMap<WebSocketSession, String> MapUserName,
			                            ConcurrentHashMap<String, List<String>>  MapCommandHistory,
			                            ConcurrentHashMap<String, String>  MapHaveSave,
			                            ConcurrentHashMap<String, Integer>MapTaskOrder,
			                            ConcurrentHashMap<String, Integer> MapSavingConfigure,
			                            ConcurrentHashMap<String, String>  MapCaseId,
			                            ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> groupMemberMap,
			                            ConcurrentHashMap<String, String>MapLegalOpeTime,
			                	        ConcurrentHashMap<String, WebSocketSession> MapLegalOpeUser
			                            
			                            ){
		String hql;
		String expId = jsonString.getString("expId");
		String groupid = userMap.get(wss);
		JSONObject ss = jsonString;
		System.out.println("expId:" + expId);
		// 生成按钮
		// 以实验Id查询设备数量
		
		CabinetTempletDeviceInfoCDAO ctdDAO = new CabinetTempletDeviceInfoCDAO();

		// 记录设备数量
		System.out.println("开始取设备数量");
		Integer equipmentNumber = ctdDAO.equipmentNumber(expId);
		System.out.println("完成取设备数量");
		// 设备名串，“##”隔开，排列顺序即为设备在实验机柜中的序号(RT##SW2##SW2#SW3#PC##PC)
		System.out.println("开始取设备名串");
		String name_str = ctdDAO.equipment(expId);
		System.out.println("完成取设备名串");
		// 任务个数
		TaskInfoCDAO tcDAO = new TaskInfoCDAO();
		Integer taskNum = tcDAO.taskNum(expId);
		//任务名称
		ExpTaskDAO etDAO = new ExpTaskDAO();
		ExpTask exptask = new ExpTask();
		ExpDAO expDAO = new ExpDAO();
		Exp exp = new Exp();
		exptask = (ExpTask)etDAO.getByNProperty("expId",expId,"expTaskOrder","1");
		exp = (Exp)expDAO.getUniqueByProperty("expId", expId);
		String taskname = exptask.getExpTaskContent();
		String expname = exp.getExpName();
		
		jsonString.put("type", "requestEquipment");
		jsonString.put("equipmentNumber", "" + equipmentNumber);
		jsonString.put("equipmentName", name_str);
		jsonString.put("expTaskNum", "" + taskNum);
		jsonString.put("expTaskOrder", "1");
		jsonString.put("TaskName", taskname);
		jsonString.put("expName", expname);
		System.out.println("接收到新的小组");
		sendToGroup(wss, jsonString,userMap,expUsers);
		
		String groupId = userMap.get(wss);
		
		/* 资源分配 */
		String cabinet_num = "";
		// 设备名串，“##”隔开，排列顺序即为设备在实验机柜中的序号(RT##SW2##SW2#SW3#PC##PC)

		//实际上这个duration已经不再使用了
		String duration = "90"; // 该实验最长持续时间(90)
		long start = System.currentTimeMillis();

		// 申请物理机柜,其中allocate为加锁方法，为了限制每一次只有一个小组申请并修改机柜表
		String cabinetIp = new CabinetResource().allocate();

		if (cabinetIp != null) {

			// 存储用户组和物理机柜ip的map
			MapEquipmentIp.put(userMap.get(wss), cabinetIp);

			ResourceAllocate resourceAllocate = new ResourceAllocate(name_str, duration, cabinetIp);
			
			jsonString.put("type", "error");
			jsonString.put("content",  "正在连接机柜,请耐心等候.........");
			sendToGroup(wss,jsonString,userMap,expUsers);
			
			if (resourceAllocate.allocate()) {
				cabinet_num = resourceAllocate.getCabinetNum(); // 实验机柜编号
				String num_str = resourceAllocate.getNumStr(); // 设备序号串   1##2##3##4
				String port_str = resourceAllocate.getPortInfoStr();// 设备序号串对应下的各设备可用端口号串(1@2@3@4@5@6##1@2@3@4@6##1##1@2@3@4@5@6##1@2@3@4@5@6)
				// experimentInit.setCabinet_num(cabinet_num);
				// //将实验机柜编号暂时保存*/
				
				// 将参数传递到前端
				jsonString.put("type", "sendEquipment");
				// jsonString.put("equipmentName",name_str);
				jsonString.put("equipmentNumStr", num_str);
				jsonString.put("equipmentPortStr", port_str);
				jsonString.put("cabinet_num", cabinet_num);
				sendToGroup(wss, jsonString,userMap,expUsers);
				System.out.println("机柜号" + cabinet_num);
				System.out.println("设备序号串" + num_str);
				// System.out.println(port_str);

				// 存储用户组和实验机柜编号的map
				MapEquipment.put(groupId, cabinet_num);

				// 存储用户组和实验设备名的map
				MapEquipmentName.put(groupId, name_str);

				// 存储用户组和实验设备序号的map
				MapEquipmentNum.put(groupId, num_str);

				// 存储用户组和实验设备端口的map
				MapEquipmentPort.put(groupId, port_str);

				// 存储用户组和实验号的map
				MapExpId.put(groupId, expId);
				
				//存储用户组的所有设备的操作历史
				Integer equipmentNum = num_str.split("##").length;
				List<String> commandHistory = new ArrayList<String>();
				for(Integer i=0;i<equipmentNum;i++){
					commandHistory.add("");
				}
				MapCommandHistory.put(groupId, commandHistory);
				
				//初始化用户保存状态
				MapHaveSave.put(groupId, "0");
				
				//初始化用户组正在完成的任务
				MapTaskOrder.put(groupId, 1);
				
				//初始化判断用户是否正在保存配置及ping的map
				MapSavingConfigure.put(groupId, 0);
				
				/*
				 * 周雨佳设置倒计时并自动释放线程
				 * */
				String endTime = "";
				if(jsonString.getString("expRole").equals("stu")){
					String orderId = groupId;
					
					hql = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
						+ " from Periodarrange as t2 , Classarrange as t3 , Order as t4"
						+ " where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
						+ " and t3.classarrangeExpArrangeId = t4.orderExpArrangeId"
						+"  and t4.orderId = " + orderId;
					
					
					//获取本次实验活动的截止时间
					List<Object> periodList = new OrderDAO().getListByHql(hql);
					String period = new DateUtil().TimeFormat(periodList);
					endTime = period.split("~")[1];
					
					MapGroupEndTime.put(groupId, endTime);
					
				}
				else if(jsonString.getString("expRole").equals("GM") ){
					
					//管理员占用的机柜，如果忘记释放了，每天结束的时候会统一释放
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String currentDate = sdf.format(new Date());
					endTime = currentDate + " 23:59:59";
					
					MapGroupEndTime.put(groupId, endTime);
				}
				
				jsonString.put("type", "timeRemain");
				jsonString.put("content",endTime);
				sendToGroup(wss,jsonString,userMap,expUsers);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = null;
				try {
					date = sdf.parse(endTime);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				autoRelease sleepThread = new autoRelease(date,jsonString, wss, MapEquipment, MapEquipmentIp, MapEquipmentName, MapEquipmentNum, MapEquipmentPort, MapExpId, MapTopo, userMap, expUsers, 
					    groupExisted, monitorUsers,groupResourceDistribution, MapGroupEndTime, MapUserName,MapCommandHistory,MapHaveSave,MapTaskOrder,MapSavingConfigure,MapCaseId,jsonString.getString("expRole"),
					    MapLegalOpeTime,MapLegalOpeUser,groupMemberMap);
				System.out.println("startsleep");
				sleepThread.start();
				

				jsonString.put("experimentStatus", "");

				// 显示初始拓扑，没有则按默认位置放置

				// 画图所需要的信息
				String leftNUM_Str = "";
				String rightNUM_Str = "";
				String leftport_Str = "";
				String rightport_Str = "";
				String position = "";

				ExpTopoDAO tDAO = new ExpTopoDAO();
				String para[] = { "expId", expId, "expTaskOrder", "0" };
				ExpTopo topo = (ExpTopo) tDAO.getByNProperty(para);
				// 不存在初始拓扑
				if (topo == null) {
					
					// 各设备采用默认位置
					ExpTopoPositionCDAO tpDAO = new ExpTopoPositionCDAO();
					// 返回各个设备的默认位置，参数为设备名串
					//position = tpDAO.defaultPosition(name_str);
					position = "";
					ss.put("position", position);
					ss.put("type", "equipConnectionInfo");
					sendToGroup(wss, ss,userMap,expUsers);
				}
				// 存在初始拓扑
				else {

					// 返回各个设备的位置，参数为实验模板拓扑Id
					ExpTopoPositionCDAO tpDAO = new ExpTopoPositionCDAO();
					position = tpDAO.position(topo.getExpTopoId());

					ExpConnectCDAO ctDAO = new ExpConnectCDAO();
					String connectInfo = ctDAO.connectInfo(topo.getExpTopoId());

					String connect[] = connectInfo.split(",");
					// 返回左端设备串
					leftNUM_Str = connect[0];
					// 返回右端设备串
					rightNUM_Str = connect[1];
					// 返回左端设备端口
					leftport_Str = connect[2];
					// 返回右端设备端口
					rightport_Str = connect[3];

					ss.put("position", position);
					ss.put("leftNUM_Str", leftNUM_Str);
					ss.put("rightNUM_Str", rightNUM_Str);
					ss.put("leftport_Str", leftport_Str);
					ss.put("rightport_Str", rightport_Str);
					
					// 暂存本次拓扑连接结果到服务器
					String topoInfo = position + "@" + leftNUM_Str + "@" + rightNUM_Str + "@" + leftport_Str + "@"
							+ rightport_Str;
					MapTopo.put(groupId, topoInfo);

					ss.put("type", "equipConnectionInfo");
					System.out.println("sss");
					sendToGroup(wss, ss,userMap,expUsers);
					System.out.println(ss);
				}
				
				/*创建实验实例*/
				/*现在每次进入实验平台都会产生一个新的实验实例 对于继续实验沿用同样的实例号需要多增加status判断
				 * 并且实例表和预约表需要有参照*/
				
				//在实验平台使用的小组号在三层对应的是预约号
				if (jsonString.getString("expRole").equals("stu")) {
					
					Integer orderId = Integer.parseInt(groupId);
					OrderDAO oDAO = new OrderDAO();
					Order order = (Order) oDAO.getUniqueByProperty("orderId", orderId);
					Integer expArrange = order.getOrderExpArrangeId();
					sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					String exp_case_id = sdf.format(new Date());// 将时间转换为字符串形式作为exp_case_id的一部分
					System.out.println("实验实例号：" + exp_case_id);
					Case newCase = new Case();
					newCase.setCaseId(exp_case_id);
					newCase.setCaseExpArrangeId(expArrange);
					newCase.setCaseExpStuNum(4);
					newCase.setStatus("实验开始");
					CaseDAO cDAO = new CaseDAO();
					cDAO.add(newCase);
					// 根据预约表找组员
					hql = "select t1.ordermemberUserId " + " from Ordermember as t1 "
							+ " where t1.ordermemberOrderId = " + orderId;
					System.out.println(hql);
					@SuppressWarnings("unchecked")
					List<Object> allmember = oDAO.getListByHql(hql);
					CaseMemberDAO cmDAO = new CaseMemberDAO();
					// 修改实例成员表
					for (Object member : allmember) {
						Integer UserId = (Integer) member;
						CaseMember cm = new CaseMember();
						cm.setCaseMemberCaseId(exp_case_id);
						cm.setCaseMemberStuId(UserId);
						cmDAO.add(cm);
					}
					// 修改实例实验表
					CaseExpDAO ceDAO = new CaseExpDAO();
					CaseExp ce = new CaseExp();
					ce.setCaseExpCaseId(exp_case_id);
					ce.setCaseExpExpId(Integer.parseInt(expId));
					ceDAO.add(ce);
					//修改实验结果任务表
					ResultTaskCDAO rtcDAO = new ResultTaskCDAO();
					rtcDAO.init(exp_case_id, Integer.parseInt(expId));
					System.out.println("创建新实例成功");
					
					//修改预约状态
					OrderCDAO oCDAO = new OrderCDAO();
					oCDAO.changeOrderStatus(Integer.parseInt(groupId), "正在实验");
					
					MapCaseId.put(groupId, exp_case_id);
				}
				
				//更新监控列表
				if (jsonString.getString("expRole").equals("stu")) {
					try {
						// 存储用户组和实验结束时间的map
						
						
						//更新监控列表
						Map<String, Object> groupInfoMap = new HashMap<String, Object>();
						// 小组号
						groupInfoMap.put("groupId", groupid);
						
						//小组成员
						List<String> allMember = null;
						try {
							// 小组成员
							 hql = "SELECT t1.userNickname " 
							 + "FROM User as t1, Order as t2 ,Ordermember as t3 "
							 + "WHERE t2.orderId = " + groupid
							 + " and t2.orderId = t3.ordermemberOrderId "
							 + "and t3.ordermemberUserId = t1.userId";
							 System.out.println(hql);
							
							 @SuppressWarnings("unchecked")
							 OrderDAO oDAO = new OrderDAO();
							 allMember = oDAO.getListByHql(hql);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						 String member = "";
						 for (String memberName : allMember) {
						 member = member + memberName + "#";
						 }
						 System.out.println("小组成员是:" + member);
						 groupInfoMap.put("groupMember", member);
						ClassgroupDAO cgDAO = new ClassgroupDAO();
						// 课程名班级名
						hql = "SELECT t3.courseName , t2.className "
								+ "FROM Class as t2 ,Course as t3,Periodarrange as t1 , Classarrange as t5 , Order as t4"
								+ " WHERE t4.orderId = " + groupid 
								+ " and t1.periodarrangeClassId = t2.classId "
								+ " and t2.classCourseId = t3.courseId"
								+ " and t4.orderExpArrangeId = t5.classarrangeExpArrangeId "
								+ " and t5.classarrangePeriodArrangeId = t1.periodarrangeId ";
						Object result = cgDAO.getUniqueByHql(hql);
						Object[] results = (Object[]) result;
						groupInfoMap.put("courseName", results[0]);
						groupInfoMap.put("className", results[1]);
						// 实验名称
						ExpDAO eDAO = new ExpDAO();
						String expName = ((Exp) eDAO.getUniqueByProperty("expId", expId)).getExpName();
						groupInfoMap.put("expName", expName);
						// 实验Id
						groupInfoMap.put("expId", expId);
						// 结束时间
						groupInfoMap.put("endTime", endTime);
						//机柜Ip
						groupInfoMap.put("ip", cabinetIp);
						JSONObject updateMonitorList = jsonString;
						updateMonitorList.put("type", "addExpGroup");
						updateMonitorList.put("monitorInfo", groupInfoMap);
						sendToMonitor(wss, updateMonitorList,monitorUsers);
						jsonString.put("type", "communication");
						jsonString.put("user", "系统消息");
						jsonString.put("content", "小组( " + groupid + " )开始了实验");
						// 发送给监控员
						sendToMonitor(wss, jsonString,monitorUsers);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					getAllExpUsers(wss,userMap,expUsers,MapUserName,groupMemberMap);
				}
			} else {
				jsonString.put("type", "error");
				jsonString.put("content",  "机柜连接失败,请稍后再尝试.......");
				// 删除组信息，进入实验时需要重新排队 LJJ 2017/4/29
				groupExisted.remove(groupId);
				groupResourceDistribution.remove(groupid);
				groupMemberMap.remove(groupId);
				
				sendToGroup(wss,jsonString,userMap,expUsers);
			
				//考虑要不要坑其他组
				//new CabinetResource().release(cabinetIp);
				return false;
			}
		} else {
			System.out.println("false!");
			System.out.println("没有可用机柜");
			jsonString.put("type", "error");
			jsonString.put("content", "没有可用机柜,请稍后再尝试.......");
			// 删除组信息，进入实验时需要重新排队 LJJ 2017/4/29
			groupExisted.remove(groupId);
			groupResourceDistribution.remove(groupid);
			groupMemberMap.remove(groupId);
			
			sendToGroup(wss,jsonString,userMap,expUsers);
			
			//考虑要不要坑其他组
			//new CabinetResource().release(cabinetIp);
			return false;
		}
		long end = System.currentTimeMillis();
		System.out.println("资源分配用时：" + (end - start) + "ms");
		return true;
	}
	
	public void sendToGroup(WebSocketSession wss, JSONObject jsonString,
							ConcurrentHashMap<WebSocketSession, String> userMap,
							ArrayList<WebSocketSession> expUsers) {

		String groupid = userMap.get(wss);
		String mess = jsonString.toString();
		System.out.println("组号" + groupid);

		for (WebSocketSession user : expUsers) {
			try {
				if (user.isOpen() && (userMap.get(user).equals(groupid))) {
					user.sendMessage(new TextMessage(mess));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendToMonitor(WebSocketSession wss, JSONObject jsonString,ArrayList<WebSocketSession> monitorUsers) {

		String mess = jsonString.toString();
		for (WebSocketSession user : monitorUsers) {
			try {
				if (user.isOpen()) {
					user.sendMessage(new TextMessage(mess));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	//lrc
	@SuppressWarnings("rawtypes")
	public void getAllExpUsers(WebSocketSession wss,
								ConcurrentHashMap<WebSocketSession, String> userMap,
								ArrayList<WebSocketSession> expUsers,
								ConcurrentHashMap<WebSocketSession, String> MapUserName,
								ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> groupMemberMap){
		System.out.println("getAllExpUsers....");
		String allUsers = new String();
		JSONObject job = new JSONObject();
		String groupid = userMap.get(wss);
		ConcurrentHashMap<String, Integer> MemberMap = groupMemberMap.get(groupid);
		ArrayList<String> names = new ArrayList<String>();
		Iterator iterator = (MemberMap.values()).iterator();
		int maxMemberNum = 0;
		System.out.println("values!!!!!:"+MemberMap.values());
		try {
			maxMemberNum = 0;
			while(iterator.hasNext()) {
				int temp = (int)iterator.next();
				if(temp > maxMemberNum)
					maxMemberNum = temp;
				//这里的Object就是你的集合里的数据类型，不知道可以object.getClass看看
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<maxMemberNum;i++) {
			names.add(" ");
		}
		try {
			Iterator iterator2 = (MemberMap.keySet()).iterator();
			while(iterator2.hasNext()) {
				String userName = (String)iterator2.next();
				int index = MemberMap.get(userName);
				names.set(index-1, userName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<names.size();i++) {
			allUsers = allUsers + (i+1) +"."+ names.get(i) + ";";
		}
		job.put("type", "getAllUsers");
		job.put("content",allUsers);
		for (WebSocketSession user : expUsers){
			try {
				if (user.isOpen() && (userMap.get(user).equals(groupid))) {
					int memberNum = MemberMap.get(MapUserName.get(user));
					System.out.println("Your num is:"+ memberNum);
					job.put("user", memberNum);
					user.sendMessage(new TextMessage(job.toString()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
