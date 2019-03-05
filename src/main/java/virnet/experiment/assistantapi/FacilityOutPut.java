package virnet.experiment.assistantapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;

public class FacilityOutPut extends Thread {
	public volatile boolean stop = false;
	InputStream isFromFacility = null;	
	String feedbackFromFacility = null;
	WebSocketSession wss = null;
	JSONObject jsonString = null;
	ArrayList<WebSocketSession> expUsers;
	ConcurrentHashMap<WebSocketSession, String> userMap;
	ConcurrentHashMap<String, List<String>> MapCommandHistory;
	
	public FacilityOutPut(InputStream isFromFacility,WebSocketSession wss, JSONObject jsonString, 
			ArrayList<WebSocketSession> expUsers, ConcurrentHashMap<WebSocketSession, String> userMap,
			ConcurrentHashMap<String, List<String>> MapCommandHistory) {
		this.isFromFacility = isFromFacility;
		this.wss =  wss;
		this.jsonString = jsonString;
		this.expUsers = expUsers;
		this.userMap = userMap;
		this.MapCommandHistory = MapCommandHistory;
	}
	@Override
	public void run() {
		
		Integer equipmentNumber = Integer.parseInt(jsonString.getString("inputEquipmentNumber"));
		
		while(!stop){
//            int count = 0;
//        	while (count == 0&&(!stop)) {
//        		try {
//					count = isFromFacility.available();
//					System.out.println(count);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					stop = true;
//					break;
//				}
//        	}
//        	if(stop)
//    			break;
//        	//System.out.println(count);
//        	byte[] buffer=new byte[count];
//        	int readCount = 0; // 
//        	while (readCount < count) {
//        		try {
//					readCount += isFromFacility.read(buffer, readCount, count - readCount);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					stop = true;
//					break;
//				}
//        	}
//        	feedbackFromFacility = new String(buffer);
			/*20180510解决编辑窗口消息监听死循环问题*/
	    	int count_n = 0;
	    	byte[] buffer=new byte[2048];
	    	try {
	    		isFromFacility.read(buffer, 0, 2048);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();几秒钟没有输入就会一直警告
			}
	    	for(int i=0;i<2048;i++){
	    		if(buffer[i]==0){
	    			count_n=i;
	    			break;
	    		}
	    	}
	    	byte[] newBuffer=new byte[count_n];
	    	for(int i=0;i<count_n;i++){
	    		newBuffer[i]=buffer[i];
	    	}
	    	feedbackFromFacility = new String(newBuffer);
	        /*20180510解决编辑窗口消息监听死循环问题*/
        	System.out.print(feedbackFromFacility);
        	
        	String groupId = userMap.get(wss);
    		String previousHistory = MapCommandHistory.get(groupId).get(equipmentNumber);
    		MapCommandHistory.get(groupId).set(equipmentNumber, previousHistory + feedbackFromFacility);
    		
        	jsonString.put("type", "command");
        	jsonString.put("content", feedbackFromFacility);
        	String mess = jsonString.toString();
			String groupid = userMap.get(wss);
    		for (WebSocketSession user : expUsers) {
                try {
                    if (user.isOpen()&&(userMap.get(user).equals(groupid))) {
                        synchronized(user){
                        	user.sendMessage(new TextMessage(mess));
                        }                   		
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 	            
		}
	}
	
	public void stopThread(){
		this.stop = true;
	}
	public String returnMessage(String message){
		
		return message;
		
	}
}
