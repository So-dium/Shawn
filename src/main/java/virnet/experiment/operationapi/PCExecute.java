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

public class PCExecute {
	
	private static final int operationServerPort = 8342;
	private String cabinet_NUM;		
	private String facility_NUM;	
	private String operationServerIP =null;
	private int timeout = 5000;
	
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	
	private DataInputStream isFromServer = null;	
	private InputStream is = null;
	private String result = null;
    private String detail = null;
	
	public PCExecute(String cabinet_num, String facility_num, String operationServerIP) {
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
			outputdata.put("command_name", "execute");
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
        if(result.equals("success")) {
        	return true;
        }
        else
        	return false;
	}
	/*�õ�����pc������������ϸ��Ϣ*/
	public String getReturnDetail() {
		return detail;
	}
	/*�õ�����pc��������������������Ϣ*/
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
	/*��pc��������ã�����Ϊ��������*/
	public boolean execute(String command) {
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
