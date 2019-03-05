package virnet.management.entity;

public class PhysicsMachines implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7956261917689095242L;
	/**
	 * 
	 */
	private Integer physicsMachinesId;
	private String physicsMachinesName;
	private Integer physicsMachinesLabId;
	private Integer status;
	private String physicsMachinesFactory;
	private String physicsMachinesIp;
	private Integer physicsMachinesRtNumber;
	private Integer physicsMachinesLayer3SwNumber;
	private Integer physicsMachinesLayer2SwNumber;
	private Integer physicsMachinesUpport;
	private Integer physicsMachinesNumberPort;

	//default constructor
	public  PhysicsMachines (){
	}

	//minor constructor
	public  PhysicsMachines (Integer physicsMachinesId, Integer physicsMachinesLabId) {

		this.physicsMachinesId = physicsMachinesId;
		this.physicsMachinesLabId = physicsMachinesLabId;
	}


	//standard constructor
	public PhysicsMachines(Integer physicsMachinesId, Integer physicsMachinesLabId, Integer physicsMachinesRtNumber, Integer physicsMachinesLayer3SwNumber ,
			Integer physicsMachinesLayer2SwNumber, Integer physicsMachinesUpport, Integer physicsMachinesNumberPort) {

		this.physicsMachinesId = physicsMachinesId;
		this.physicsMachinesLabId = physicsMachinesLabId;
		this.physicsMachinesRtNumber = physicsMachinesRtNumber;
		this.physicsMachinesLayer3SwNumber = physicsMachinesLayer3SwNumber;
		this.physicsMachinesLayer2SwNumber = physicsMachinesLayer2SwNumber;
		this.physicsMachinesUpport = physicsMachinesUpport;
		this.physicsMachinesNumberPort = physicsMachinesNumberPort;
	}

	//full constrcutor
	public PhysicsMachines(Integer physicsMachinesId, Integer physicsMachinesLabId,Integer status, String physicsMachinesFactory,
			String physicsMachinesIp, Integer physicsMachinesRtNumber, Integer physicsMachinesLayer3SwNumber ,
			Integer physicsMachinesLayer2SwNumber, Integer physicsMachinesUpport, Integer physicsMachinesNumberPort) {

		this.physicsMachinesId = physicsMachinesId;
		this.physicsMachinesLabId = physicsMachinesLabId;
		this.status = status;
		this.physicsMachinesFactory = physicsMachinesFactory;
		this.physicsMachinesIp = physicsMachinesIp;
		this.physicsMachinesRtNumber = physicsMachinesRtNumber;
		this.physicsMachinesLayer3SwNumber = physicsMachinesLayer3SwNumber;
		this.physicsMachinesLayer2SwNumber = physicsMachinesLayer2SwNumber;
		this.physicsMachinesUpport = physicsMachinesUpport;
		this.physicsMachinesNumberPort = physicsMachinesNumberPort;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getPhysicsMachinesId() {
		return physicsMachinesId;
	}


	public void setPhysicsMachinesId(Integer physicsMachinesId) {
		this.physicsMachinesId = physicsMachinesId;
	}

	
	public String getPhysicsMachinesName() {
		return physicsMachinesName;
	}

	public void setPhysicsMachinesName(String physicsMachinesName) {
		this.physicsMachinesName = physicsMachinesName;
	}

	public Integer getPhysicsMachinesLabId() {
		return physicsMachinesLabId;
	}


	public void setPhysicsMachinesLabId(Integer physicsMachinesLabId) {
		this.physicsMachinesLabId = physicsMachinesLabId;
	}


	public String getPhysicsMachinesFactory() {
		return physicsMachinesFactory;
	}


	public void setPhysicsMachinesFactory(String physicsMachinesFactory) {
		this.physicsMachinesFactory = physicsMachinesFactory;
	}


	public String getphysicsMachinesIp() {
		return physicsMachinesIp;
	}


	public void setphysicsMachinesIp(String physicsMachinesIp) {
		this.physicsMachinesIp = physicsMachinesIp;
	}


	public Integer getPhysicsMachinesRtNumber() {
		return physicsMachinesRtNumber;
	}


	public void setPhysicsMachinesRtNumber(Integer physicsMachinesRtNumber) {
		this.physicsMachinesRtNumber = physicsMachinesRtNumber;
	}


	public Integer getPhysicsMachinesLayer3SwNumber() {
		return physicsMachinesLayer3SwNumber;
	}


	public void setPhysicsMachinesLayer3SwNumber(Integer physicsMachinesLayer3SwNumber) {
		this.physicsMachinesLayer3SwNumber = physicsMachinesLayer3SwNumber;
	}


	public Integer getPhysicsMachinesLayer2SwNumber() {
		return physicsMachinesLayer2SwNumber;
	}


	public void setPhysicsMachinesLayer2SwNumber(Integer physicsMachinesLayer2SwNumber) {
		this.physicsMachinesLayer2SwNumber = physicsMachinesLayer2SwNumber;
	}


	public Integer getPhysicsMachinesUpport() {
		return physicsMachinesUpport;
	}


	public void setPhysicsMachinesUpport(Integer physicsMachinesUpport) {
		this.physicsMachinesUpport = physicsMachinesUpport;
	}


	public Integer getPhysicsMachinesNumberPort() {
		return physicsMachinesNumberPort;
	}


	public void setPhysicsMachinesNumberPort(Integer physicsMachinesNumberPort) {
		this.physicsMachinesNumberPort = physicsMachinesNumberPort;
	}

}