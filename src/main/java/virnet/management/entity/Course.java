package virnet.management.entity;

/**
 * Course entity. @author MyEclipse Persistence Tools
 */

public class Course implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6198205344042277584L;
	private Integer courseId;
	private String courseName;
	private Integer courseTeacherId;

	// Constructors

	/** default constructor */
	public Course() {
	}

	/** minimal constructor */
	public Course(Integer courseId, String courseName) {
		this.courseId = courseId;
		this.courseName = courseName;
	}

	/** full constructor */
	public Course(Integer courseId, String courseName, Integer courseTeacherId) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseTeacherId = courseTeacherId;
	}

	// Property accessors

	public Integer getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getCourseTeacherId() {
		return this.courseTeacherId;
	}

	public void setCourseTeacherId(Integer courseTeacherId) {
		this.courseTeacherId = courseTeacherId;
	}

}