package virnet.management.entity;

/**
 * Exp entity. @author MyEclipse Persistence Tools
 */

public class Exp implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6099168731821800531L;
	private Integer expId;
	private String expName;
	private String expType;
	private String expStanTime;
	private String expInstruct;
	private String expProfile;
	private Integer expCabinetTempletId;
	private String expRoleTask;
	private Integer expExpTopoId;
	private Integer expExpConfigId;
	private String expCole;

	// Constructors

	/** default constructor */
	public Exp() {
	}

	/** minimal constructor */
	public Exp(Integer expId, String expName) {
		this.expId = expId;
		this.expName = expName;
	}

	/** full constructor */
	public Exp(Integer expId, String expName, String expType,
			String expStanTime, String expInstruct, String expProfile,
			Integer expCabinetTempletId, String expRoleTask,
			Integer expExpTopoId, Integer expExpConfigId, String expCole) {
		this.expId = expId;
		this.expName = expName;
		this.expType = expType;
		this.expStanTime = expStanTime;
		this.expInstruct = expInstruct;
		this.expProfile = expProfile;
		this.expCabinetTempletId = expCabinetTempletId;
		this.expRoleTask = expRoleTask;
		this.expExpTopoId = expExpTopoId;
		this.expExpConfigId = expExpConfigId;
		this.expCole = expCole;
	}

	// Property accessors

	public Integer getExpId() {
		return this.expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	public String getExpType() {
		return this.expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getExpStanTime() {
		return this.expStanTime;
	}

	public void setExpStanTime(String expStanTime) {
		this.expStanTime = expStanTime;
	}

	public String getExpInstruct() {
		return this.expInstruct;
	}
	

	public void setExpInstruct(String expInstruct) {
		this.expInstruct = expInstruct;
	}

	public String getExpProfile() {
		return this.expProfile;
	}

	public void setExpProfile(String expProfile) {
		this.expProfile = expProfile;
	}

	public Integer getExpCabinetTempletId() {
		return this.expCabinetTempletId;
	}

	public void setExpCabinetTempletId(Integer expCabinetTempletId) {
		this.expCabinetTempletId = expCabinetTempletId;
	}

	public String getExpRoleTask() {
		return this.expRoleTask;
	}

	public void setExpRoleTask(String expRoleTask) {
		this.expRoleTask = expRoleTask;
	}

	public Integer getExpExpTopoId() {
		return this.expExpTopoId;
	}

	public void setExpExpTopoId(Integer expExpTopoId) {
		this.expExpTopoId = expExpTopoId;
	}

	public Integer getExpExpConfigId() {
		return this.expExpConfigId;
	}

	public void setExpExpConfigId(Integer expExpConfigId) {
		this.expExpConfigId = expExpConfigId;
	}

	public String getExpCole() {
		return this.expCole;
	}

	public void setExpCole(String expCole) {
		this.expCole = expCole;
	}

}