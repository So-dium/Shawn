package virnet.experiment.assistantapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.NTCEdit;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.resourceapi.ResourceAllocate;

public class ExperimentLoad {
	
	private String operationServerIP = null;
	private static final int operationServerPort = 8342;
	private String cabinet_NUM;		
	//private int timeout = 5000;
	private String filepath_info;
	private InputStream is = null;
	private Socket connectToServer;
	private DataOutputStream osToServer = null;	
	private DataInputStream isFromServer = null;	
	private DataInputStream isFromServer1 = null;	
	private String result = null;
    private String detail = "";
	
	private String connection_str = null;		
	private String executeCommand_str = null;		
	private Map<String,String> CommandMap = null;	
	
	public ExperimentLoad(String cabinet_NUM, String connection_str, String executeCommand_str, 
			String filepath_info,String operationServerIP) {
		this.cabinet_NUM = cabinet_NUM;
		this.connection_str = connection_str;
		this.executeCommand_str = executeCommand_str;
		this.filepath_info = filepath_info;
		this.CommandMap = new HashMap();
		this.operationServerIP = operationServerIP;
	}
	public boolean load() {
		//������������
		if(connection_str != null) {
			String[] connection_arr = connection_str.split("%%");
			//String status_str = connection_arr[0];		//�༭״̬����0Ϊ�Ͽ���1Ϊ����
			String leftNUM_str = connection_arr[0];		//����豸��Ŵ�����##������
			String rightNUM_str = connection_arr[1];	//�Ҷ��豸��Ŵ�����##������
			String leftport_str = connection_arr[2];	//����豸�˿���Ŵ�����##������
			String rightport_str = connection_arr[3];	//�Ҷ��豸�˿���Ŵ�����##������
			//��ʼ������ͼ�༭����
			NTCEdit ntcEdit = new NTCEdit(cabinet_NUM, leftNUM_str, rightNUM_str, leftport_str, rightport_str,this.operationServerIP);
			//����ִ�б༭����
			if(!(ntcEdit.edit())) {
				return false;
			}
			System.out.println("�������Ӽ������");
		}
		//���������
		if(executeCommand_str != null) {
			String[] executeCommand_arr = executeCommand_str.split("##");
			for(int i = 0;i < executeCommand_arr.length;i++) {
				String[] executeCommand_info = executeCommand_arr[i].split("%");
				String facility_num = executeCommand_info[0];
				String executeCommand = executeCommand_info[1];
				if(executeCommand.startsWith("set")) {
					PCExecute pcExecute = new PCExecute(cabinet_NUM, facility_num,this.operationServerIP);
					if(pcExecute.connect()) {
						pcExecute.execute(executeCommand);
						is = pcExecute.getInputStream();
						isFromServer1 = new DataInputStream(is);
						String input = readInputStream();
						pcExecute.end();
				        String executecommand = executeCommand + "\n" + input;
				        CommandMap.put(facility_num, executecommand);
					}
					else {
						detail = detail + "�豸"+facility_num+"����ʧ��\n";
						return false;
					}
				}
			}
			System.out.println("������������");
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�����豸���ò���
		if(filepath_info != null) {
			try {
				connectToServer = new Socket(this.operationServerIP, operationServerPort);
				//connectToServer.setSoTimeout(timeout);
				// Create an input stream to receive data from the server
				isFromServer = new DataInputStream(connectToServer.getInputStream());
			    // Create an output stream to send data to the server
				osToServer = new DataOutputStream(connectToServer.getOutputStream());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				detail = detail + "�޷������������������\n";
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				detail = detail + "�޷������������������\n";
				return false;
			}
			JSONObject outputdata = new JSONObject();
			try {
				outputdata.put("command_name", "load");
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
				detail = detail + "socket��дʧ��\n";
				return false;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(isFromServer));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(osToServer));
			String[] filepath = filepath_info.split("##");
			try {
				//System.out.println(filepath.length);
				bw.write(filepath.length+"\n");	//��������ύ�����ļ�����
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				detail = detail + "socket��дʧ��\n";
				return false;
			}
			for(int j = 0;j < filepath.length;j++) {
				//ͨ���ļ����ȡ�������ļ����Ӧ���豸���
				String[] tmp = filepath[j].split("_");
				String facility_num = tmp[tmp.length-1].substring(0, tmp[tmp.length-1].length()-4);
				try {
					bw.write(facility_num+"\n");	//��������ύ�����ļ���Ӧ�豸���
					bw.flush();
					File file = new File(filepath[j]);
					BufferedReader file_br = new BufferedReader(new FileReader(file));
					String inline = null;					
					while((inline = file_br.readLine()) != null ) {
						//System.out.println(inline);
						bw.write(inline+"\n");	//������������ύ�����ļ���������
						bw.flush();
					}
					bw.write("END\n");	//��ǰ�����ļ��ύ����
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					detail = detail + "socket���ļ���дʧ��\n";
					return false;
				}
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*FacilityConfigure facilityConfigure = new FacilityConfigure(cabinet_NUM, facility_num);
				File file = new File(filepath[j]);
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(facilityConfigure.connect()) {
					is = facilityConfigure.getInputStream();
					isFromServer = new DataInputStream(is);
					String configurecommand = "";
					//�����豸
					String[] cmd = {"\n","return\n","reboot\n","Y\n","Y\r","Y\n"};
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(1000);
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(1000);
					for ( int i = 0; i < 6; i++ ) {
						facilityConfigure.configure(cmd[i]);
						configurecommand += readInputStream();
					}
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(10000);
					//System.out.println("Yes\n");
					Thread.sleep(120000);
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(1000);
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(1000);
					facilityConfigure.configure(cmd[0]);
					configurecommand += readInputStream();
					Thread.sleep(1000);
					
					String inline = null;
					try {
						while((inline = br.readLine()) != null ) {
							facilityConfigure.configure(inline);
							configurecommand += readInputStream();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					CommandMap.put(facility_num, configurecommand);
				}
				else {
					detail = detail + "�豸"+facility_num+"����ʧ��\n";
					return false;
				}*/
			}
		}
		
		return true;
	}
	private String readInputStream() {
		String input = null;
		//ByteArrayOutputStream returnFromServer = null;
		//returnFromServer = new ByteArrayOutputStream();
		int count = 0;
		try {
			count = isFromServer1.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		byte[] buffer=new byte[count];
    	int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
    	while (readCount < count) {
    		try {
				readCount += isFromServer1.read(buffer, readCount, count - readCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
        input = new String(buffer);
        return input;
	}
	private Map<String, String> getCommandMap() {
		return CommandMap;
	}
	public String getDetail() {
		return detail;
	}
}
