package virnet.experiment.operationapi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class NTCEdit {
	
	private static final int operationServerPort = 8342;
	private String cabinet_NUM;		
	private String operationServerIP =null;
	//private String status_Str;	
	private String leftNUM_Str;		
	private String rightNUM_Str;	
	private String leftport_Str;	
	private String rightport_Str;	
	private int timeout = 10000;
	
	private Socket connectToServer;
	DataOutputStream osToServer = null;	
	DataInputStream isFromServer = null;	
	private String result = null;	
    private String detail = null;	
	
	public NTCEdit(String cabinet_num, String leftNUM_str, 
			String rightNUM_str, String leftport_str, String rightport_str, String operationServerIP) {
		this.cabinet_NUM = cabinet_num;
		//this.status_Str = status_str;
		this.leftNUM_Str = leftNUM_str;
		this.rightNUM_Str = rightNUM_str;
		this.leftport_Str = leftport_str;
		this.rightport_Str = rightport_str;
		this.operationServerIP = operationServerIP;;
	}
	
	public boolean edit() {
		try {
			connectToServer = new Socket(operationServerIP, operationServerPort);
			connectToServer.setSoTimeout(timeout);
			// Create an input stream to receive data from the server
			isFromServer = new DataInputStream(connectToServer.getInputStream());
		    // Create an output stream to send data to the server
			osToServer = new DataOutputStream(connectToServer.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		JSONObject outputdata = new JSONObject();
		try {
			outputdata.put("command_name", "topology");
			outputdata.put("cabinet_num", cabinet_NUM);
			//outputdata.put("status_str", status_Str);
			outputdata.put("leftNUM_str", leftNUM_Str);
			outputdata.put("rightNUM_str", rightNUM_Str);
			outputdata.put("leftport_str", leftport_Str);
			outputdata.put("rightport_str", rightport_Str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		String outputdata_str = outputdata.toString();
		try {
			osToServer.write(outputdata_str.getBytes(), 0, outputdata_str.getBytes().length);
			osToServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		//ByteArrayOutputStream returnFromServer = null;
		//returnFromServer = new ByteArrayOutputStream();
		int count = 0;
		while (count == 0) {
    		try {
				count = isFromServer.available();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
		byte[] buffer=new byte[count];
    	int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
    	while (readCount < count) {
    		try {
				readCount += isFromServer.read(buffer, readCount, count - readCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
        JSONObject returnjson = null;
        try {
			returnjson = new JSONObject(new String(buffer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        try {
			result = returnjson.getString("result");
			detail = returnjson.getString("detail");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        try {
			connectToServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(result.equals("success")) {
        	return true;
        }
        else
        	return false;
	}
	/*�õ���Դ�ͷŲ�����������ϸ��Ϣ*/
	public String getReturnDetail() {
		return detail;
	}
}
