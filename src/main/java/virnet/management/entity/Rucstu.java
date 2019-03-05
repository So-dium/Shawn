package virnet.management.entity;

/**
 * Rucstu entity. @author MyEclipse Persistence Tools
 */

public class Rucstu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9005122942067221991L;
	private Integer rucstuId;
	private Integer rucstuUserId;
	private String rucstuName;
	private String rucstuSchool;
	private String rucstuNumber;
	private String rucstuGrade;
	private String rucstuClass;

	// Constructors

	/** default constructor */
	public Rucstu() {
	}

	/** minimal constructor */
	public Rucstu(Integer rucstuId, Integer rucstuUserId) {
		this.rucstuId = rucstuId;
		this.rucstuUserId = rucstuUserId;
	}

	/** full constructor */
	public Rucstu(Integer rucstuId, Integer rucstuUserId, String rucstuName,
			String rucstuSchool, String rucstuNumber, String rucstuGrade,
			String rucstuClass) {
		this.rucstuId = rucstuId;
		this.rucstuUserId = rucstuUserId;
		this.rucstuName = rucstuName;
		this.rucstuSchool = rucstuSchool;
		this.rucstuNumber = rucstuNumber;
		this.rucstuGrade = rucstuGrade;
		this.rucstuClass = rucstuClass;
	}

	// Property accessors

	public Integer getRucstuId() {
		return this.rucstuId;
	}

	public void setRucstuId(Integer rucstuId) {
		this.rucstuId = rucstuId;
	}

	public Integer getRucstuUserId() {
		return this.rucstuUserId;
	}

	public void setRucstuUserId(Integer rucstuUserId) {
		this.rucstuUserId = rucstuUserId;
	}

	public String getRucstuName() {
		return this.rucstuName;
	}

	public void setRucstuName(String rucstuName) {
		this.rucstuName = rucstuName;
	}

	public String getRucstuSchool() {
		return this.rucstuSchool;
	}

	public void setRucstuSchool(String rucstuSchool) {
		this.rucstuSchool = rucstuSchool;
	}

	public String getRucstuNumber() {
		return this.rucstuNumber;
	}

	public void setRucstuNumber(String rucstuNumber) {
		this.rucstuNumber = rucstuNumber;
	}

	public String getRucstuGrade() {
		return this.rucstuGrade;
	}

	public void setRucstuGrade(String rucstuGrade) {
		this.rucstuGrade = rucstuGrade;
	}

	public String getRucstuClass() {
		return this.rucstuClass;
	}

	public void setRucstuClass(String rucstuClass) {
		this.rucstuClass = rucstuClass;
	}

}