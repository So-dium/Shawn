package virnet.experiment.resourceapi;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceAllocate {
	
	private String resourceServerIP = null;

	private static final int resourceServerPort = 8341;
	private String name_Str;	
	private String duration;
	private String operationServerIP = null;
	private String model_NUM = "###";
	private int timeout = 10000;
	
	private Socket connectToServer = null;
	DataOutputStream osToServer = null;	
	DataInputStream isFromServer = null;	
	/*OutputStream osToServer = null;	
	InputStream isFromServer = null;	//�ӷ����������������*/
	private String result = null;	
    private String detail = null;	
    private JSONObject returnjson = null;	
    private String cabinetNum = null;	
    private String numStr = null;	
    private String portInfoStr = null;	
	
	public ResourceAllocate(String name_str, String duration,String resourceServerIP) {
		this.name_Str = name_str;
		this.duration = duration;
		this.resourceServerIP = resourceServerIP;
	}
	
	public ResourceAllocate(String model_num) {
		this.model_NUM = model_num;
	}
	
	public boolean allocate() {
		try {
			System.out.println(resourceServerIP);
			System.out.println(resourceServerPort);
			connectToServer = new Socket(resourceServerIP, resourceServerPort);
			//connectToServer.setSoTimeout(timeout);
			
			// Create an input stream to receive data from the server
			isFromServer = new DataInputStream(connectToServer.getInputStream());
		    // Create an output stream to send data to the server
			osToServer = new DataOutputStream(connectToServer.getOutputStream());
			/*isFromServer = connectToServer.getInputStream();
		    // Create an output stream to send data to the server
			osToServer = connectToServer.getOutputStream();*/
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
			outputdata.put("command_name", "allocate");
			outputdata.put("name_str", name_Str);
			outputdata.put("duration", duration);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				connectToServer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		String outputdata_str = outputdata.toString();
		System.out.println(outputdata_str);
		try {
			//osToServer.write(outputdata_str.getBytes(), 0, outputdata_str.getBytes().length);
			byte[] buffer = outputdata_str.getBytes();
			String tmp = new String(buffer);
			//System.out.println(buffer+":"+tmp);
			osToServer.write(buffer);
			osToServer.flush();
			System.out.println("writing!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				connectToServer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
        
        try {
        	System.out.println("����json��"+new String(buffer));
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
        	try {
				cabinetNum = returnjson.getString("cabinet_num");
				numStr = returnjson.getString("NUM_str");
				portInfoStr = returnjson.getString("portinfo_str");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
        	return true;
        }
        else
        	return false;
        
	}
	/*�õ���Դ���������������ϸ��Ϣ*/
	public String getReturnDetail() {
		return detail;
	}
	public JSONObject getAllocation() {
		return returnjson;
	}
	public String getCabinetNum() {
		return cabinetNum;
	}
	public String getNumStr() {
		return numStr;
	}
	public String getPortInfoStr() {
		return portInfoStr;
	}
}
