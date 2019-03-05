package virnet.management.entity;

public class Facilities implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4488010857987833125L;
	private Integer facilitiesId;
	private String facilitiesBelongPhysicsMachines;
	private Integer facilitiesOrder;
	private Integer facilitiesType;
	private String facilitiesName;
	private Integer status;
	private Integer facilitiesNumberUsePort;
	private Integer facilitiesNumberPort;
	private String facilitiesFactory;
	private String facilitiesIp;
	private String facilitiesPort;
	private String facilitiesSetPort;
	private String facilitiesGetPort;

	//default constructor
	public Facilities(){
	}

	//minor constructor
	public Facilities(Integer facilitiesId, Integer facilitiesType) {

		this.facilitiesId = facilitiesId;
		this.facilitiesType = facilitiesType;
	}


	//standard constructor
	public Facilities(Integer facilitiesId, String facilitiesBelongPhysicsMachines, Integer facilitiesOrder,
			Integer facilitiesType, Integer status,Integer facilitiesNumberUsePort, Integer facilitiesNumberPort) {

		this.facilitiesId = facilitiesId;
		this.facilitiesBelongPhysicsMachines = facilitiesBelongPhysicsMachines;
		this.facilitiesOrder = facilitiesOrder;
		this.facilitiesType = facilitiesType;
		this.status = status;
		this.facilitiesNumberUsePort = facilitiesNumberUsePort;
		this.facilitiesNumberPort = facilitiesNumberPort;
	}

	//full constrcutor
	public Facilities(Integer facilitiesId, String facilitiesBelongPhysicsMachines, Integer facilitiesOrder,
			Integer facilitiesType,String facilitiesName, Integer status, Integer facilitiesNumberUsePort, Integer facilitiesNumberPort,
			String facilitiesFactory, String facilitiesIp, String facilitiesPort, String facilitiesSetPort,
			String facilitiesGetPort) {
		this.facilitiesId = facilitiesId;
		this.facilitiesBelongPhysicsMachines = facilitiesBelongPhysicsMachines;
		this.facilitiesOrder = facilitiesOrder;
		this.facilitiesType = facilitiesType;
		this.facilitiesName = facilitiesName;
		this.status = status;
		this.facilitiesNumberUsePort = facilitiesNumberUsePort;
		this.facilitiesNumberPort = facilitiesNumberPort;
		this.facilitiesFactory = facilitiesFactory;
		this.facilitiesIp = facilitiesIp;
		this.facilitiesPort = facilitiesPort;
		this.facilitiesSetPort = facilitiesSetPort;
		this.facilitiesGetPort = facilitiesGetPort;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFacilitiesId() {
		return facilitiesId;
	}


	public void setFacilitiesId(Integer facilitiesId) {
		this.facilitiesId = facilitiesId;
	}


	public String getFacilitiesBelongPhysicsMachines() {
		return facilitiesBelongPhysicsMachines;
	}


	public void setFacilitiesBelongPhysicsMachines(String facilitiesBelongPhysicsMachines) {
		this.facilitiesBelongPhysicsMachines = facilitiesBelongPhysicsMachines;
	}


	public Integer getFacilitiesOrder() {
		return facilitiesOrder;
	}


	public void setFacilitiesOrder(Integer facilitiesOrder) {
		this.facilitiesOrder = facilitiesOrder;
	}


	public Integer getFacilitiesType() {
		return facilitiesType;
	}


	public void setFacilitiesType(Integer facilitiesType) {
		this.facilitiesType = facilitiesType;
	}


	public Integer getFacilitiesNumberUsePort() {
		return facilitiesNumberUsePort;
	}


	public void setFacilitiesNumberUsePort(Integer facilitiesNumberUsePort) {
		this.facilitiesNumberUsePort = facilitiesNumberUsePort;
	}


	public Integer getFacilitiesNumberPort() {
		return facilitiesNumberPort;
	}


	public void setFacilitiesNumberPort(Integer facilitiesNumberPort) {
		this.facilitiesNumberPort = facilitiesNumberPort;
	}


	public String getFacilitiesFactory() {
		return facilitiesFactory;
	}


	public void setFacilitiesFactory(String facilitiesFactory) {
		this.facilitiesFactory = facilitiesFactory;
	}


	public String getFacilitiesIp() {
		return facilitiesIp;
	}


	public void setFacilitiesIp(String facilitiesIp) {
		this.facilitiesIp = facilitiesIp;
	}


	public String getFacilitiesPort() {
		return facilitiesPort;
	}


	public void setFacilitiesPort(String facilitiesPort) {
		this.facilitiesPort = facilitiesPort;
	}


	public String getFacilitiesSetPort() {
		return facilitiesSetPort;
	}


	public void setFacilitiesSetPort(String facilitiesSetPort) {
		this.facilitiesSetPort = facilitiesSetPort;
	}


	public String getFacilitiesGetPort() {
		return facilitiesGetPort;
	}


	public void setFacilitiesGetPort(String facilitiesGetPort) {
		this.facilitiesGetPort = facilitiesGetPort;
	}

	public String getFacilitiesName() {
		return facilitiesName;
	}

	public void setFacilitiesName(String facilitiesName) {
		this.facilitiesName = facilitiesName;
	}
}