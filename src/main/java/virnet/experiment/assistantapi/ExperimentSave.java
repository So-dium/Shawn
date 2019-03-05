package virnet.experiment.assistantapi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import virnet.experiment.combinedao.ExpConfigCDAO;
import virnet.experiment.combinedao.ExpDeviceConfigCDAO;
import virnet.experiment.combinedao.ResultConfigCDAO;
import virnet.experiment.combinedao.ResultDeviceConfigCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.PCExecute;

public class ExperimentSave {
	
	private String operationServerIP = null;
	private static final int operationServerPort = 8342;
	private static final String FILEPATH = "D:/VirnetFileForSave";	
	//private int timeout = 5000;
	private String filepath_info="";
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	
	private DataInputStream isFromServer = null;	
	private InputStream is = null;
	private String result = null;
    private String detail = null;
    private String cabinet_NUM;	
    private String expId;
    private String expTaskOrder;
    private Integer equipmentNumber;
    private String expRole;
    private Integer resultTaskId;
    private ConcurrentHashMap<String, FacilityConfigure> groupFacilityConfigureMap;
    private ConcurrentHashMap<WebSocketSession, String> userMap ;
    
    public ExperimentSave(String cabinet_num,String expId,String expTaskOrder,
    		String equipmentNumber,String expRole,Integer resultTaskId, String operationServerIP) {
    	this.cabinet_NUM = cabinet_num;
    	this.expId = expId;
    	this.expTaskOrder = expTaskOrder;
    	this.equipmentNumber = Integer.parseInt(equipmentNumber);
    	this.expRole = expRole;
    	this.resultTaskId = resultTaskId;
    	this.operationServerIP = operationServerIP;
    	
    	
    }
    public boolean save(ConcurrentHashMap<WebSocketSession, String> userMap ,ArrayList<WebSocketSession> expUsers,WebSocketSession wss) {
    
    	try {
			connectToServer = new Socket(operationServerIP, operationServerPort);
			//connectToServer.setSoTimeout(timeout);
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
    	
    	
		
    	/*
    	 * 交换机 路由器设备配置保存
    	 */
    	
		JSONObject outputdata = new JSONObject();
		try {
			outputdata.put("command_name", "save");
			outputdata.put("cabinet_num", cabinet_NUM);
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
		
		/***蒋家盛****/
		JSONObject ss = new JSONObject();
		ss.put("type", "progress");
		ss.put("replyType", "ExperimentSave");
		//ss.put("reply", "start");//返回前端信息
		ss.put("content", 0+"##"+40);
		sendToGroup(wss, ss, userMap, expUsers);
		/****蒋家盛****/
		
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
    	int readCount = 0; 
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
        	String t = new String(buffer);
        	System.out.println("buffer:"+t);
			returnjson = new JSONObject(new String(buffer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        try {
        	System.out.println(returnjson.toString());
			result = returnjson.getString("result");
			detail = returnjson.getString("detail");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        if(result.equals("fail")) {
        	System.out.println("fail");
        	return false;
        }
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String filenum_str = null;	
		int filenum;
		try {
			filenum_str = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		File f = new File(FILEPATH);  
        if(!f.exists()){  
            f.mkdir();    
        }
		filenum = Integer.parseInt(filenum_str);
		System.out.println("文件数"+filenum);
		String[] filepath_arr = new String[filenum];
		
		ExpDeviceConfigCDAO dcDAO = new ExpDeviceConfigCDAO();
		ResultDeviceConfigCDAO rdcDAO = new ResultDeviceConfigCDAO();
		Integer configId = 0;
		
		if (expRole.equals("GM")){
			
			//修改实验模板配置表  其中 filenum和devicenum是等价的
			ExpConfigCDAO ccDAO = new ExpConfigCDAO();
			
			//获得实验模板设备配置表的Id，+4是PC的数量
			configId = ccDAO.edit(expId, expTaskOrder, filenum+4);
			
			//删除实验模板设备配置表相关信息，准备重写
			dcDAO.delete(configId);
		}
		else if(expRole.equals("stu")){
			
			//修改实验结果配置表  其中 filenum和devicenum是等价的
			ResultConfigCDAO rcDAO = new ResultConfigCDAO();
			
			//获得实验结果设备配置表的Id，+4是PC的数量
			configId = rcDAO.edit(resultTaskId,filenum+4);
			
			//删除实验结果设备配置表相关信息，准备重写
			rdcDAO.delete(configId);	
		}
		else
			return false;
		
		boolean flag = true;
		//操作是否成功的返回值
        boolean success = true;
        //每次保存一个除PC的设备的文件
		for(int i = 0;i < filenum;i++) {			
			try {
				String thisfilepath = FILEPATH+"/";
				
				String thisfilename = br.readLine();             //本次操作的文件
				System.out.println(thisfilename);
				
				String filelen = br.readLine();              //文件长度
				System.out.println(filelen);

				int len = Integer.parseInt(filelen);
				thisfilepath += thisfilename;
				filepath_arr[i] = thisfilepath;
				FileOutputStream fos = new FileOutputStream(new File(thisfilepath)); 
				
				//该字节流数组用于接受一层发来的配置文件内容
                byte[] inputByte = new byte[len];                  
                //该string类用于写入数据库
                String configInfo="";
                
                //sum与length用于判断本次读取是否完整
                int length;
                long sum = 0;
                while ((length = isFromServer.read(inputByte, 0, inputByte.length)) > 0) {   //按长度读取内容
                	
                	//写入本地文件，以后讨论是否需要在本地建立文件，此处先保留本地操作
                    fos.write(inputByte, 0, length);  
                    fos.flush();
                    sum += length;
                    
                    String temp = new String(inputByte);
                    configInfo = configInfo + temp;
                    
                    //本设备读取完整，则写入数据库
                    if(sum==len){
                    	//filenum是从0开始的，因此设备序号应为i+1
                    	System.out.println(configInfo);
                    	
                    	if(expRole.equals("GM")){
                    		flag = dcDAO.edit(configId, i+1, configInfo);
                    	}
                    	else if(expRole.equals("stu")){
                    		flag = rdcDAO.edit(configId, i+1, configInfo);
                    	}
                    	else
                    		flag = false;
                    	
                    	if(flag == false)
                    		success = false;
                    	break;
                    }                 	
                }
                fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int j = 0;j < filenum;j++) {
			filepath_info = filepath_info + filepath_arr[j] + "##";
		}
		filepath_info = filepath_info.substring(0, filepath_info.length() - 2);
		

    	/*
		 * 网卡配置保存(复用了向网卡发送get命令的方法)，此处没有做文件保存
		 */
		Integer PCNumber =  equipmentNumber - 3;
		PCConfigureInfo pcInfo = new PCConfigureInfo(cabinet_NUM,equipmentNumber,this.operationServerIP);
		//Info保存了4个PC的配置信息，首字符串是操作状态信息
		String Info[] = pcInfo.getPCConfigureInfo();
		
		if(Info[0].equals("success")){
			for(int k=0;k<4;k++){
			//写入数据库
			if(expRole.equals("GM")){
            	flag = dcDAO.edit(configId, PCNumber+k, Info[k+1]);
            }
            else if(expRole.equals("stu")){
            	flag = rdcDAO.edit(configId, PCNumber+k, Info[k+1]);
            }
            else
            	flag = false;
        	if(flag == false)
        		success = false;
			}
		}
		else  
			success = false;
		
		return success;
    }
    
   
	public String getReturnDetail() {
		return detail;
	}

	public String getFilePathInfo() {
		return filepath_info;
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
