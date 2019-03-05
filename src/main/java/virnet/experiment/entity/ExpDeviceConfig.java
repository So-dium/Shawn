package virnet.experiment.entity;

public class ExpDeviceConfig implements java.io.Serializable {

	/**
	 *  实验模板设备配置表
	 */
	private static final long serialVersionUID = -1174294037522711586L;
	private Integer expDeviceConfigId;
	private Integer expConfigId;
	private Integer expDeviceOrder;
	private String configFile;
	
	public ExpDeviceConfig() {
		
	}

	public ExpDeviceConfig(Integer expDeviceConfigId,Integer expConfigId, Integer expDeviceOrder, String configFile) {
		super();
		this.expDeviceConfigId = expDeviceConfigId;
		this.expConfigId = expConfigId;
		this.expDeviceOrder = expDeviceOrder;
		this.configFile = configFile;
	}

	public ExpDeviceConfig(Integer expDeviceConfigId,Integer expConfigId, Integer expDeviceOrder) {
		super();
		this.expDeviceConfigId = expDeviceConfigId;
		this.expConfigId = expConfigId;
		this.expDeviceOrder = expDeviceOrder;
	}

	public Integer getExpDeviceConfigId() {
		return expDeviceConfigId;
	}

	public void setExpDeviceConfigId(Integer expDeviceConfigId) {
		this.expDeviceConfigId = expDeviceConfigId;
	}

	public Integer getExpConfigId() {
		return expConfigId;
	}

	public void setExpConfigId(Integer expConfigId) {
		this.expConfigId = expConfigId;
	}

	public Integer getExpDeviceOrder() {
		return expDeviceOrder;
	}

	public void setExpDeviceOrder(Integer expDeviceOrder) {
		this.expDeviceOrder = expDeviceOrder;
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
