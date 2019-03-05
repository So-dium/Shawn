package virnet.management.entity;

/**
 * Classgroup entity. @author MyEclipse Persistence Tools
 */

public class Classgroup implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -79512197485718487L;
	private Integer classgroupId;
	private String classgroupName;
	private Integer classgroupTotal;
	private String classgroupType;
	private Integer classgroupClassId;

	// Constructors

	/** default constructor */
	public Classgroup() {
	}

	/** minimal constructor */
	public Classgroup(String classgroupName) {
		this.classgroupName = classgroupName;
	}

	/** full constructor */
	public Classgroup(String classgroupName, Integer classgroupTotal,
			String classgroupType, Integer classgroupClassId) {
		this.classgroupName = classgroupName;
		this.classgroupTotal = classgroupTotal;
		this.classgroupType = classgroupType;
		this.classgroupClassId = classgroupClassId;
	}

	// Property accessors

	public Integer getClassgroupId() {
		return this.classgroupId;
	}

	public void setClassgroupId(Integer classgroupId) {
		this.classgroupId = classgroupId;
	}

	public String getClassgroupName() {
		return this.classgroupName;
	}

	public void setClassgroupName(String classgroupName) {
		this.classgroupName = classgroupName;
	}

	public Integer getClassgroupTotal() {
		return this.classgroupTotal;
	}

	public void setClassgroupTotal(Integer classgroupTotal) {
		this.classgroupTotal = classgroupTotal;
	}

	public String getClassgroupType() {
		return this.classgroupType;
	}

	public void setClassgroupType(String classgroupType) {
		this.classgroupType = classgroupType;
	}

	public Integer getClassgroupClassId() {
		return this.classgroupClassId;
	}

	public void setClassgroupClassId(Integer classgroupClassId) {
		this.classgroupClassId = classgroupClassId;
	}

}