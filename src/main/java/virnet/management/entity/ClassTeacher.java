package virnet.management.entity;

/**
 * ClassTeacher entity. @author MyEclipse Persistence Tools
 */

public class ClassTeacher implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1602719561465099922L;
	private Integer classTeacherId;
	private Integer classTeacherClassId;
	private Integer classTeacherTeacherId;

	// Constructors

	/** default constructor */
	public ClassTeacher() {
	}

	/** full constructor */
	public ClassTeacher(Integer classTeacherId, Integer classTeacherClassId,
			Integer classTeacherTeacherId) {
		this.classTeacherId = classTeacherId;
		this.classTeacherClassId = classTeacherClassId;
		this.classTeacherTeacherId = classTeacherTeacherId;
	}

	// Property accessors

	public Integer getClassTeacherId() {
		return this.classTeacherId;
	}

	public void setClassTeacherId(Integer classTeacherId) {
		this.classTeacherId = classTeacherId;
	}

	public Integer getClassTeacherClassId() {
		return this.classTeacherClassId;
	}

	public void setClassTeacherClassId(Integer classTeacherClassId) {
		this.classTeacherClassId = classTeacherClassId;
	}

	public Integer getClassTeacherTeacherId() {
		return this.classTeacherTeacherId;
	}

	public void setClassTeacherTeacherId(Integer classTeacherTeacherId) {
		this.classTeacherTeacherId = classTeacherTeacherId;
	}

}