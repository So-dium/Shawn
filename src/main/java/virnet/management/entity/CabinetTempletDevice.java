package virnet.management.entity;

public class CabinetTempletDevice implements java.io.Serializable {


	private static final long serialVersionUID = -2274029599019047934L;
	/**
	 * 实验机柜模板设备表
	 */	
	private Integer cabinetTempletDeviceId;
	private Integer cabinetTempletId;
	private Integer deviceOrder;
	private Integer deviceType;
	private Integer lanPortNum;
	private Integer wanPortNum;
	
	/** default constructor */
	public CabinetTempletDevice() {
	}
	
	public CabinetTempletDevice(Integer cabinetTempletDeviceId, Integer cabinetTempletId,
			Integer deviceOrder, Integer deviceType, Integer lanPortNum, Integer wanPortNum) {
		super();
		this.cabinetTempletDeviceId = cabinetTempletDeviceId;
		this.cabinetTempletId = cabinetTempletId;
		this.deviceOrder = deviceOrder;
		this.deviceType = deviceType;
		this.lanPortNum = lanPortNum;
		this.wanPortNum = wanPortNum;
	}
	
	public CabinetTempletDevice(Integer cabinetTempletDeviceId, Integer cabinetTempletId) {
		super();
		this.cabinetTempletDeviceId = cabinetTempletDeviceId;
		this.cabinetTempletId = cabinetTempletId;
	}

	public Integer getCabinetTempletDeviceId() {
		return cabinetTempletDeviceId;
	}
	public void setCabinetTempletDeviceId(Integer cabinetTempletDeviceId) {
		this.cabinetTempletDeviceId = cabinetTempletDeviceId;
	}
	public Integer getCabinetTempletId() {
		return cabinetTempletId;
	}
	public void setCabinetTempletId(Integer cabinetTempletId) {
		this.cabinetTempletId = cabinetTempletId;
	}
	public Integer getDeviceOrder() {
		return deviceOrder;
	}
	public void setDeviceOrder(Integer deviceOrder) {
		this.deviceOrder = deviceOrder;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getLanPortNum() {
		return lanPortNum;
	}
	public void setLanPortNum(Integer lanPortNum) {
		this.lanPortNum = lanPortNum;
	}
	public Integer getWanPortNum() {
		return wanPortNum;
	}
	public void setWanPortNum(Integer wanPortNum) {
		this.wanPortNum = wanPortNum;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
