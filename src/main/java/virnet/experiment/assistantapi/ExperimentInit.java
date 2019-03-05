package virnet.experiment.assistantapi;

import java.util.HashMap;
import java.util.Map;

public class ExperimentInit {
	private String cabinet_num = null; //ʵ������
	private String connection_str = null;		//�豸����״̬��
	private String configureCommand_str = null;		//�豸�������
	private Map<String,String> configureCommandMap = null;	//�洢�豸��������
	private Map<String,String> executeCommandMap = null;	//�洢�豸��������
	private String executeCommand_str = null;		//PC��������
	private String filepath_info= null;	//ʵ����RT��SW2��SW3���豸�������ļ�·����Ϣ
	
	public ExperimentInit() {
		this.cabinet_num = new String();
		this.connection_str = new String();
		this.configureCommand_str = new String();
		this.configureCommandMap = new HashMap();
		this.executeCommandMap = new HashMap();
		this.executeCommand_str = new String();
	}
	public String getCabinet_num() {
		return cabinet_num;
	}

	public void setCabinet_num(String cabinet_num) {
		this.cabinet_num = cabinet_num;
	}

	public String getConnection_str() {
		return connection_str;
	}

	public void setConnection_str(String connection_str) {
		this.connection_str = connection_str;
	}

	public String getConfigureCommand_str() {
		for(Map.Entry<String, String> entry : configureCommandMap.entrySet()) {
			String thiscommand_str = entry.getValue();
			thiscommand_str = thiscommand_str.substring(0, thiscommand_str.length()-1);
			configureCommand_str = configureCommand_str + entry.getKey() + "%" + thiscommand_str + "##";
		}
		configureCommand_str = configureCommand_str.substring(0, configureCommand_str.length()-2);
		return configureCommand_str;
	}

	/*public void setConfigureCommand_str(String configureCommand_str) {
		this.configureCommand_str = configureCommand_str;
	}*/

	public String getExecuteCommand_str() {
		for(Map.Entry<String, String> entry : executeCommandMap.entrySet()) {
			executeCommand_str = executeCommand_str + entry.getKey() + "%" + entry.getValue() + "##"; 
		}
		executeCommand_str = executeCommand_str.substring(0, executeCommand_str.length() - 2);
		return executeCommand_str;
	}

	/*public void setExecuteCommand_str(String executeCommand_str) {
		this.executeCommand_str = executeCommand_str;
	}*/
	public void addConfigureCommand(String facility_num, String command) {
		String command_str = null;
		if(configureCommandMap.containsKey(facility_num)) 
			command_str = configureCommandMap.get(facility_num) + command + "\n";
		else 
			command_str = command + "\n";
		configureCommandMap.put(facility_num, command_str);
	}
	public void addExecuteCommand(String facility_num, String command) {
		if(command.startsWith("set")) {
			executeCommandMap.put(facility_num, command);
		}
	}
	public String getFilepath_info() {
		return filepath_info;
	}
	public void setFilepath_info(String filepath_info) {
		this.filepath_info = filepath_info;
	}
}
