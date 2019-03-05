package virnet.management.entity;

/**
 * Classarrange entity. @author MyEclipse Persistence Tools
 */

public class Classarrange implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1635055167869150021L;
	private Integer classarrangeId;
	private Integer classarrangePeriodArrangeId;
	private Integer classarrangeExpArrangeId;

	// Constructors

	/** default constructor */
	public Classarrange() {
	}


	/** full constructor */
	public Classarrange(Integer classarrangePeriodArrangeId,Integer classarrangeExpArrangeId) {
		this.classarrangePeriodArrangeId = classarrangePeriodArrangeId;
		this.classarrangeExpArrangeId = classarrangeExpArrangeId;
	}

	// Property accessors

	public Integer getClassarrangeId() {
		return this.classarrangeId;
	}

	public void setClassarrangeId(Integer classarrangeId) {
		this.classarrangeId = classarrangeId;
	}

	public Integer getClassarrangePeriodArrangeId() {
		return this.classarrangePeriodArrangeId;
	}

	public void setClassarrangePeriodArrangeId(
			Integer classarrangePeriodArrangeId) {
		this.classarrangePeriodArrangeId = classarrangePeriodArrangeId;
	}


	public Integer getClassarrangeExpArrangeId() {
		return classarrangeExpArrangeId;
	}


	public void setClassarrangeExpArrangeId(Integer classarrangeExpArrangeId) {
		this.classarrangeExpArrangeId = classarrangeExpArrangeId;
	}

	

}