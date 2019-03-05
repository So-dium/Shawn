package virnet.experiment.assistantapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import net.sf.json.JSONObject;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.resourceapi.CabinetResource;
import virnet.experiment.resourceapi.ResourceRelease;

public class autoRelease extends Thread{
	private Date endTime;
	JSONObject jsonString;
    WebSocketSession wss;
    ConcurrentHashMap<String, String> MapEquipment;
    ConcurrentHashMap<String, String> MapEquipmentIp;
    ConcurrentHashMap<String, String> MapEquipmentName;
    ConcurrentHashMap<String, String> MapEquipmentNum;
    ConcurrentHashMap<String, String> MapEquipmentPort;
    ConcurrentHashMap<String, String> MapExpId;
    ConcurrentHashMap<String, String> MapTopo;
    ConcurrentHashMap<WebSocketSession, String> userMap;
    ArrayList<WebSocketSession> expUsers;
    ArrayList<String> groupExisted;
    ArrayList<WebSocketSession> monitorUsers;
    ConcurrentHashMap<String, Integer> groupResourceDistribution;
    ConcurrentHashMap<String, String> MapGroupEndTime;
    ConcurrentHashMap<WebSocketSession, String> MapUserName;
    ConcurrentHashMap<String, List<String>>  MapCommandHistory;
    ConcurrentHashMap<String, String>  MapHaveSave;
    ConcurrentHashMap<String, Integer>MapTaskOrder;
    ConcurrentHashMap<String, Integer> MapSavingConfigure;
    ConcurrentHashMap<String, String> MapCaseId;
    private String ExpRole;
    ConcurrentHashMap<String, String> MapLegalOpeTime;
    ConcurrentHashMap<String, WebSocketSession> MapLegalOpeUser;
    ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> groupMemberMap;
    
    


	public autoRelease(Date endTime, JSONObject jsonString, WebSocketSession wss,
			ConcurrentHashMap<String, String> mapEquipment, ConcurrentHashMap<String, String> mapEquipmentIp,
			ConcurrentHashMap<String, String> mapEquipmentName, ConcurrentHashMap<String, String> mapEquipmentNum,
			ConcurrentHashMap<String, String> mapEquipmentPort, ConcurrentHashMap<String, String> mapExpId,
			ConcurrentHashMap<String, String> mapTopo, ConcurrentHashMap<WebSocketSession, String> userMap,
			ArrayList<WebSocketSession> expUsers, ArrayList<String> groupExisted,
			ArrayList<WebSocketSession> monitorUsers, ConcurrentHashMap<String, Integer> groupResourceDistribution,
			ConcurrentHashMap<String, String> mapGroupEndTime, ConcurrentHashMap<WebSocketSession, String> mapUserName,
			ConcurrentHashMap<String, List<String>> mapCommandHistory, ConcurrentHashMap<String, String> mapHaveSave,
			ConcurrentHashMap<String, Integer> mapTaskOrder, ConcurrentHashMap<String, Integer> mapSavingConfigure,
			ConcurrentHashMap<String, String> mapCaseId, String expRole,
			ConcurrentHashMap<String, String> mapLegalOpeTime,
			ConcurrentHashMap<String, WebSocketSession> mapLegalOpeUser,
			ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> groupMemberMap) {
		super();
		this.endTime = endTime;
		this.jsonString = jsonString;
		this.wss = wss;
		MapEquipment = mapEquipment;
		MapEquipmentIp = mapEquipmentIp;
		MapEquipmentName = mapEquipmentName;
		MapEquipmentNum = mapEquipmentNum;
		MapEquipmentPort = mapEquipmentPort;
		MapExpId = mapExpId;
		MapTopo = mapTopo;
		this.userMap = userMap;
		this.expUsers = expUsers;
		this.groupExisted = groupExisted;
		this.monitorUsers = monitorUsers;
		this.groupResourceDistribution = groupResourceDistribution;
		MapGroupEndTime = mapGroupEndTime;
		MapUserName = mapUserName;
		MapCommandHistory = mapCommandHistory;
		MapHaveSave = mapHaveSave;
		MapTaskOrder = mapTaskOrder;
		MapSavingConfigure = mapSavingConfigure;
		MapCaseId = mapCaseId;
		ExpRole = expRole;
		MapLegalOpeTime = mapLegalOpeTime;
		MapLegalOpeUser = mapLegalOpeUser;
		this.groupMemberMap = groupMemberMap;
	}


	public void run(){
		String groupId = userMap.get(wss);
		long start = System.currentTimeMillis();
		long end = this.endTime.getTime();
		long remain = end - start;
		System.out.println(remain);
		try {
			Thread.sleep(remain);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(groupExisted.contains(groupId))){//已经提交并退出
			return;
		}
		else{
			if(MapSavingConfigure.get(groupId) == 1){//正在保存配置
				jsonString.put("type", "permit");
				jsonString.put("content", "goodbye");
				String mess = jsonString.toString();
				for (WebSocketSession user : expUsers) {
	                try {
	                    if (user.isOpen()&&(userMap.get(user).equals(groupId))) {
	                        user.sendMessage(new TextMessage(mess));
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
				while(true){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(MapSavingConfigure.get(groupId) == 0){
						break;
					}
						
				}
			}
			else{//其他情况
				
				jsonString.put("type", "permit");
				jsonString.put("content", "goodbye");
				String mess = jsonString.toString();
				for (WebSocketSession user : expUsers) {
	                try {
	                    if (user.isOpen()&&(userMap.get(user).equals(groupId))) {
	                        user.sendMessage(new TextMessage(mess));
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
				if(MapHaveSave.get(groupId).equals("1")){//只保存了拓扑
					System.out.println("saving config");
					String cabinet_NUM = MapEquipment.get(groupId);
					String expId = MapExpId.get(groupId);
					String expTaskOrder = MapTaskOrder.get(groupId).toString();
					String[] equipSequence = MapEquipmentNum.get(groupId).split("##");
					int length = equipSequence.length;
					String equipmentNumber = equipSequence[length-1];
					String expRole = this.ExpRole;
					String[] equiporder = MapTopo.get(groupId).split("@");
					boolean success = false;
					String leftNUM_Str = equiporder[1]; // 左端设备序号串，“##”隔开
					String rightNUM_Str = equiporder[2]; // 右端设备序号串，“##”隔开
					String leftport_Str = equiporder[3]; // 左端设备端口序号串，“##”隔开
					String rightport_Str = equiporder[4]; // 右端设备端口序号串，“##”隔开
					
//					String leftNUM_Str = "2##2##2##2"; // 左端设备序号串，“##”隔开
//					String rightNUM_Str = "4##5##6##7"; // 右端设备序号串，“##”隔开
//					String leftport_Str = "1##2##3##4"; // 左端设备端口序号串，“##”隔开
//					String rightport_Str = "1##1##1##1"; // 右端设备端口序号串，“##”隔开
					
					String expCaseId = MapCaseId.get(groupId);
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
					System.out.println("aaa");
					//保存配置的核心类及方法，前期视为参数准备
					ExperimentSave es = new ExperimentSave(cabinet_NUM, expId, expTaskOrder, equipmentNumber,
							expRole, resultTaskId, cabinetIp);
					success = es.save(userMap,expUsers,wss);
					
					//等待配置过程释放网卡
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					PCConfigureInfo pcInfo = new PCConfigureInfo(cabinet_NUM, Integer.parseInt(equipmentNumber),
							cabinetIp);
					// 获取ip地址
					String[] pcip = pcInfo.getIpAddress();
					System.out.println("bbb");
					PingVerify pv = new PingVerify(cabinet_NUM,Integer.parseInt(equipmentNumber),cabinetIp);
					String[] verifyResult = pv.getVerifyResult(pcip, userMap, expUsers, wss);
					
					savePingResultToDatabase SPRTDB = new savePingResultToDatabase(verifyResult, cabinet_NUM, expId, expTaskOrder,
							equipmentNumber, leftNUM_Str, rightNUM_Str,leftport_Str, rightport_Str,expRole, expCaseId);
					success = SPRTDB.save(pcip);
				}
				
			}
			
			String cabinet_NUM = MapEquipment.get(groupId); // 实验机柜编号
			String cabinetIp = MapEquipmentIp.get(groupId);

			// 释放机柜
			new CabinetResource().release(cabinetIp);
			ResourceRelease resourceRelease = new ResourceRelease(cabinet_NUM, cabinetIp);
			if (resourceRelease.release()) {
				System.out.println("成功释放资源");
			} else {
				System.out.println("false!");
				System.out.println(resourceRelease.getReturnDetail());
			}
			groupExisted.remove(groupId);
			
			//删除实验相关的中间变量
			MapCommandHistory.remove(groupId);
			MapHaveSave.remove(groupId);
			MapTaskOrder.remove(groupId);
			MapSavingConfigure.remove(groupId);
			MapCaseId.remove(groupId);
			groupMemberMap.remove(groupId);
			MapLegalOpeTime.remove(groupId);
			MapLegalOpeUser.remove(groupId);
			
			// 删除资源信息 LJJ 2017/4/29
			MapEquipment.remove(groupId);
			MapEquipmentName.remove(groupId);
			MapEquipmentNum.remove(groupId);
			MapEquipmentPort.remove(groupId);
			MapEquipmentIp.remove(groupId);
			MapTopo.remove(groupId);

			// 删除资源分配记录 LJJ 2.17/4/29
			groupResourceDistribution.remove(groupId);
			return;
		}
	}
	
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


}