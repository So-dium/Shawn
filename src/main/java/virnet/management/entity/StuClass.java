package virnet.management.entity;

/**
 * StuClass entity. @author MyEclipse Persistence Tools
 */

public class StuClass implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5104521350754797204L;
	private Integer stuClassId;
	private Integer stuClassUserId;
	private Integer stuClassClassId;

	// Constructors

	/** default constructor */
	public StuClass() {
	}

	/** full constructor */
	public StuClass(Integer stuClassId, Integer stuClassUserId,
			Integer stuClassClassId) {
		this.stuClassId = stuClassId;
		this.stuClassUserId = stuClassUserId;
		this.stuClassClassId = stuClassClassId;
	}

	// Property accessors

	public Integer getStuClassId() {
		return this.stuClassId;
	}

	public void setStuClassId(Integer stuClassId) {
		this.stuClassId = stuClassId;
	}

	public Integer getStuClassUserId() {
		return this.stuClassUserId;
	}

	public void setStuClassUserId(Integer stuClassUserId) {
		this.stuClassUserId = stuClassUserId;
	}

	public Integer getStuClassClassId() {
		return this.stuClassClassId;
	}

	public void setStuClassClassId(Integer stuClassClassId) {
		this.stuClassClassId = stuClassClassId;
	}

}