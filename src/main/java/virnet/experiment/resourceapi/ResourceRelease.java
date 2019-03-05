package virnet.experiment.resourceapi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceRelease {
	
	private String resourceServerIP = null;
	private static final int operationServerPort = 8341;
	private String cabinet_NUM;		//ʵ������
	private int timeout = 5000;
	
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	//������������������
	private DataInputStream isFromServer = null;	//�ӷ����������������
	private JSONObject returnjson = null;	//����json��ݴ�
	private String result = null;	//�������
    private String detail = null;	//������ϸ��Ϣ
	
	public ResourceRelease(String cabinet_num,String resourceServerIP) {
		this.cabinet_NUM = cabinet_num;
		this.resourceServerIP = resourceServerIP;
	}
	
	public boolean release() {
		try {
			connectToServer = new Socket(resourceServerIP, operationServerPort);
			connectToServer.setSoTimeout(timeout);
			// Create an input stream to receive data from the server
			isFromServer = new DataInputStream(connectToServer.getInputStream());
		    // Create an output stream to send data to the server
			osToServer = new DataOutputStream(connectToServer.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject outputdata = new JSONObject();
		try {
			outputdata.put("command_name", "release");
			outputdata.put("cabinet_num", cabinet_NUM);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String outputdata_str = outputdata.toString();
		try {
			osToServer.write(outputdata_str.getBytes(), 0, outputdata_str.getBytes().length);
			osToServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
        try {
			returnjson = new JSONObject(new String(buffer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			result = returnjson.getString("result");
			detail = returnjson.getString("detail");
		} catch (JSONException e) {
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
