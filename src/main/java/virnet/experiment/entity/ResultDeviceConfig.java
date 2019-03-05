package virnet.experiment.entity;

public class ResultDeviceConfig implements java.io.Serializable {
	private static final long serialVersionUID = 8564895392516349187L;
	/**
	 *  实验结果设备配置表
	 */
	private Integer resultDeviceConfigId;
	private Integer resultConfigId;
	private Integer DeviceOrder;
	private String configFile;
		
	public ResultDeviceConfig() {
		
	}

	public ResultDeviceConfig(Integer resultDeviceConfigId,Integer resultConfigId, Integer deviceOrder, String configFile) {
		super();
		this.resultDeviceConfigId = resultDeviceConfigId;
		this.resultConfigId = resultConfigId;
		this.DeviceOrder = deviceOrder;
		this.configFile = configFile;
	}

	
	public Integer getResultDeviceConfigId() {
		return resultDeviceConfigId;
	}


	public void setResultDeviceConfigId(Integer resultDeviceConfigId) {
		this.resultDeviceConfigId = resultDeviceConfigId;
	}


	public Integer getResultConfigId() {
		return resultConfigId;
	}

	public void setResultConfigId(Integer resultConfigId) {
		this.resultConfigId = resultConfigId;
	}

	public Integer getDeviceOrder() {
		return DeviceOrder;
	}

	public void setDeviceOrder(Integer deviceOrder) {
		DeviceOrder = deviceOrder;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
