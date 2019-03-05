package virnet.experiment.assistantapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;

public class AutoRefuse extends Thread{
	private String time;
	JSONObject jsonString;
    WebSocketSession wss;
    ConcurrentHashMap<String, String> MapLegalOpeTime;
    ConcurrentHashMap<WebSocketSession, String> userMap;
    ArrayList<WebSocketSession> expUsers;

	public AutoRefuse(String time, JSONObject jsonString, WebSocketSession wss,
			ConcurrentHashMap<String, String> mapLegalOpeTime, ConcurrentHashMap<WebSocketSession, String> userMap,
			ArrayList<WebSocketSession> expUsers) {
		super();
		this.time = time;
		this.jsonString = jsonString;
		this.wss = wss;
		MapLegalOpeTime = mapLegalOpeTime;
		this.userMap = userMap;
		this.expUsers = expUsers;
	}


	public void run(){
		String groupId = userMap.get(wss);
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(MapLegalOpeTime.get(groupId).equals(time)){
			jsonString.put("type", "openbutton");
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
			return;
		}
		else{
			return;
		}
	}
}