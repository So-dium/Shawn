package virnet.management.entity;

/**
 * Class entity. @author MyEclipse Persistence Tools
 */

public class Class implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1985694730075174703L;
	private Integer classId;
	private String className;
	private Integer classCourseId;
	private Integer classTeacherId;

	// Constructors

	/** default constructor */
	public Class() {
	}

	/** minimal constructor */
	public Class(Integer classId, Integer classCourseId) {
		this.classId = classId;
		this.classCourseId = classCourseId;
	}

	/** full constructor */
	public Class(Integer classId, String className, Integer classCourseId,
			Integer classTeacherId) {
		this.classId = classId;
		this.className = className;
		this.classCourseId = classCourseId;
		this.classTeacherId = classTeacherId;
	}

	// Property accessors

	public Integer getClassId() {
		return this.classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getClassCourseId() {
		return this.classCourseId;
	}

	public void setClassCourseId(Integer classCourseId) {
		this.classCourseId = classCourseId;
	}

	public Integer getClassTeacherId() {
		return this.classTeacherId;
	}

	public void setClassTeacherId(Integer classTeacherId) {
		this.classTeacherId = classTeacherId;
	}

}