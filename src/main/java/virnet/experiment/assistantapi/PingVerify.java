package virnet.experiment.assistantapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.sf.json.JSONObject;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.assistantapi.PCConfigureInfo;

public class PingVerify{
	
	private String cabinet_NUM;	
    private Integer equipmentNumber;
    private Integer PCNum;
    private String cabinetIp;
    
    public PingVerify(String cabinet_NUM,Integer equipmentNumber,String cabinetIp){
    	this.cabinet_NUM = cabinet_NUM;
    	this.equipmentNumber = equipmentNumber;	
    	this.cabinetIp = cabinetIp;
    	this.PCNum = 4;
    }
    
    public String[] getVerifyResult(String[] pcip,ConcurrentHashMap<WebSocketSession, String> userMap ,ArrayList<WebSocketSession> expUsers,WebSocketSession wss){
    	
    	//验证信息，首字符串为验证状态，success 或者 fail
    	String[] verifyResult = new String[PCNum*(PCNum-1)/2+1];
    	
    	//取第一个网卡为发送ping命令的载体
    	Integer PCNumber =  equipmentNumber - PCNum + 1;
    	PCExecute pcExecute = new PCExecute(cabinet_NUM,""+PCNumber,this.cabinetIp);
    	
    	//这里sleep是因为可能前面的操作中一层还没有完成断开之前的PC连接，此处等待一下
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	if(pcExecute.connect()){
    		
    		System.out.println("开始验证");
    		verifyResult[0] = "success";
    		
    		//验证信息记录号
    		Integer record = 1;
    		
			InputStream isFromFacility = pcExecute.getInputStream();
    		
    		//只需要单向ping 因此一个需要ping PCNum*(PCNum-1)/2 次
			int k=1;
        	for(int i=1 ;i < PCNum+1 ;i++)
        		for(int j=i+1; j<PCNum+1 ;j++){
        			
        			/***蒋家盛****/
					JSONObject ss = new JSONObject();
					ss.put("type", "progress");
					ss.put("replyType", "PingVerify");
					ss.put("reply", i+"##"+j);//返回前端信息
					ss.put("content", (40+10*(k-1))+"##"+(40+10*(k++)));
					sendToGroup(wss, ss, userMap, expUsers);
					/****蒋家盛****/
    				
        			String command = "ping " + pcip[i] + " " + pcip[j];
        			System.out.println("验证命令为："+command);
    				pcExecute.execute(command);
    				
    				boolean stop = false;
    				String feedback = "";
    				while(!stop){
    					
    					//接受反馈
    					int count = 0;
    					while (count == 0) {
    						try {
    							count = isFromFacility.available();
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							break;
    						}
    					}
    					//System.out.println(count);
    					byte[] PCbuffer=new byte[count];
    					int readCount = 0; // 
    					while (readCount < count) {
    						try {
    							readCount += isFromFacility.read(PCbuffer, readCount, count - readCount);
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							break;
    						}
    					}
    					//获取部分反馈
    					String feedback_temp = new String(PCbuffer);
    					 
    					//完整反馈
    					feedback = feedback + feedback_temp;
    					
    					//如果反馈中出现了"统计信息"字样，说明4条来自ping的回复都已经接受到，可以跳出接受反馈的循环
    					if(feedback.indexOf("统计信息")!=-1)
    						stop = true;
    				}
    				//出现了TTL表明ping连通了
					if(feedback.indexOf("TTL=")!=-1){
						verifyResult[record] = "connected";
						System.out.println("connected");
					}
					//没有连通
					else{
						verifyResult[record] = "disconnected";
						System.out.println("disconnected");
					}					
    				
					record ++;
        		}
        	pcExecute.end();
    	}
    	else{
    		System.out.println("验证失败");
    		verifyResult[0] = "fail";
    		System.out.println(pcExecute.getReturnDetail());
    	}
    	
    	return verifyResult;
    	
    	
    }
    public void sendToGroup(WebSocketSession wss, JSONObject jsonString, ConcurrentHashMap<WebSocketSession, String> userMap ,ArrayList<WebSocketSession> expUsers) {

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
}