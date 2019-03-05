package virnet.management.entity;

import java.util.Date;

/**
 * Semester entity. @author MyEclipse Persistence Tools
 */

public class Semester implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2858905975654839410L;
	private Integer semesterId;
	private Date semesterStartdate;
	private Integer semesterTotalweek;

	// Constructors

	/** default constructor */
	public Semester() {
	}

	/** full constructor */
	public Semester(Integer semesterId, Date semesterStartdate,
			Integer semesterTotalweek) {
		this.semesterId = semesterId;
		this.semesterStartdate = semesterStartdate;
		this.semesterTotalweek = semesterTotalweek;
	}

	// Property accessors

	public Integer getSemesterId() {
		return this.semesterId;
	}

	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
	}

	public Date getSemesterStartdate() {
		return this.semesterStartdate;
	}

	public void setSemesterStartdate(Date semesterStartdate) {
		this.semesterStartdate = semesterStartdate;
	}

	public Integer getSemesterTotalweek() {
		return this.semesterTotalweek;
	}

	public void setSemesterTotalweek(Integer semesterTotalweek) {
		this.semesterTotalweek = semesterTotalweek;
	}

}