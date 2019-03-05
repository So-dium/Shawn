package virnet.experiment.operationapi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class FacilityConfigure {
	
	private static final int operationServerPort = 8342;
	private String cabinet_NUM;		
	private String facility_NUM;
	private String operationServerIP =null;
	private int timeout = 10000;
	
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	
	private DataInputStream isFromServer = null;	
	private InputStream is = null;
	private String result = null;
    private String detail = null;
	
	public FacilityConfigure(String cabinet_num, String facility_num, String operationServerIP) {
		this.cabinet_NUM = cabinet_num;
		this.facility_NUM = facility_num;
		this.operationServerIP = operationServerIP;
	}

	public boolean connect() {
		try {
			connectToServer = new Socket(operationServerIP, operationServerPort);
			connectToServer.setSoTimeout(timeout);
			// Create an input stream to receive data from the server
			is = connectToServer.getInputStream();
			isFromServer = new DataInputStream(is);
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
			outputdata.put("command_name", "configure");
			outputdata.put("cabinet_num", cabinet_NUM);
			outputdata.put("facility_num", facility_NUM);
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
//		int count = 0;
//		while (count == 0) {
//    		try {
//				count = isFromServer.available();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				break;
//			}
//    	}
//		byte[] buffer=new byte[count];
//    	int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
//    	while (readCount < count) {
//    		try {
//				readCount += isFromServer.read(buffer, readCount, count - readCount);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				break;
//			}
//    	}
		/*20180510解决编辑窗口消息监听死循环问题*/
    	int count_n = 0;
    	byte[] buffer=new byte[2048];
    	try {
    		isFromServer.read(buffer, 0, 2048);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
        JSONObject returnjson = null;
        System.out.println(new String(newBuffer)); 
        try {
			returnjson = new JSONObject(new String(newBuffer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        /*20180510解决编辑窗口消息监听死循环问题*/
        try {
			result = returnjson.getString("result");
			detail = returnjson.getString("detail");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        if(result.equals("success")) {
        	return true;
        }
        else
        	return false;
	}
	/*�õ������豸������������ϸ��Ϣ*/
	public String getReturnDetail() {
		return detail;
	}
	/*�õ������豸��������������������Ϣ*/
	public InputStream getInputStream() {
		return is;
	}
	/*��ȡ������Ϣ*/
	public String readInputStream() {
		String input = null;
		//ByteArrayOutputStream returnFromServer = null;
		//returnFromServer = new ByteArrayOutputStream();
		int count = 0;
		try {
			count = isFromServer.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
        input = new String(buffer);
        return input;
	}
	/*���豸�������ã�����Ϊ��������*/
	public boolean configure(String command) {
		try {
			osToServer.write(command.getBytes(), 0, command.getBytes().length);
			osToServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void end() {
		try {
			osToServer.write("end".getBytes(), 0, "end".getBytes().length);
			osToServer.flush();
			connectToServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
