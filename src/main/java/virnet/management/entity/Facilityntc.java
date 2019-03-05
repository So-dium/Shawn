package virnet.management.entity;

public class Facilityntc implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4488010857987833125L;
	private Integer facilityntcId;
	private Integer facilityId;
	private Integer ntcId;
	private Integer ntcPortNum;
	private Integer status;
	private Integer portNum;
	
	public Facilityntc(Integer facilityntcId, Integer facilityId, Integer ntcId, Integer ntcPortNum, Integer status,
			Integer portNum) {
		super();
		this.facilityntcId = facilityntcId;
		this.facilityId = facilityId;
		this.ntcId = ntcId;
		this.ntcPortNum = ntcPortNum;
		this.status = status;
		this.portNum = portNum;
	}

	public Facilityntc() {
		super();
	}

	public Integer getFacilityntcId() {
		return facilityntcId;
	}

	public void setFacilityntcId(Integer facilityntcId) {
		this.facilityntcId = facilityntcId;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getNtcId() {
		return ntcId;
	}

	public void setNtcId(Integer ntcId) {
		this.ntcId = ntcId;
	}

	public Integer getNtcPortNum() {
		return ntcPortNum;
	}

	public void setNtcPortNum(Integer ntcPortNum) {
		this.ntcPortNum = ntcPortNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPortNum() {
		return portNum;
	}

	public void setPortNum(Integer portNum) {
		this.portNum = portNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}