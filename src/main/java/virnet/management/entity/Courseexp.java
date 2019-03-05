package virnet.management.entity;

/**
 * Courseexp entity. @author MyEclipse Persistence Tools
 */

public class Courseexp implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482234724037138088L;
	private Integer courseexpId;
	private Integer courseexpCourseId;
	private Integer courseexpExpId;

	// Constructors

	/** default constructor */
	public Courseexp() {
	}

	/** full constructor */
	public Courseexp(Integer courseexpId, Integer courseexpCourseId,
			Integer courseexpExpId) {
		this.courseexpId = courseexpId;
		this.courseexpCourseId = courseexpCourseId;
		this.courseexpExpId = courseexpExpId;
	}

	// Property accessors

	public Integer getCourseexpId() {
		return this.courseexpId;
	}

	public void setCourseexpId(Integer courseexpId) {
		this.courseexpId = courseexpId;
	}

	public Integer getCourseexpCourseId() {
		return this.courseexpCourseId;
	}

	public void setCourseexpCourseId(Integer courseexpCourseId) {
		this.courseexpCourseId = courseexpCourseId;
	}

	public Integer getCourseexpExpId() {
		return this.courseexpExpId;
	}

	public void setCourseexpExpId(Integer courseexpExpId) {
		this.courseexpExpId = courseexpExpId;
	}

}