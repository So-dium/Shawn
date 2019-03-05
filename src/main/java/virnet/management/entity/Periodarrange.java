package virnet.management.entity;

import java.util.Date;

/**
 * Periodarrange entity. @author MyEclipse Persistence Tools
 */

public class Periodarrange implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8742091587017518163L;
	private Integer periodarrangeId;
	private Integer periodarrangeClassId;
	private Integer periodarrangeCabinetNum;
	private Date periodarrangeStartDate;
	private Integer periodarrangeStartTime;
	private Date periodarrangeEndDate;
	private Integer periodarrangeEndTime;
	private Integer periodarrangeAllClassNum;
	private Integer periodarrangeSetUpUserId;

	// Constructors

	/** default constructor */
	public Periodarrange() {
	}

	/** minimal constructor */
	public Periodarrange(Integer periodarrangeClassId,
			Date periodarrangeStartDate, Integer periodarrangeStartTime,
			Integer periodarrangeSetUpUserId) {
		this.periodarrangeClassId = periodarrangeClassId;
		this.periodarrangeStartDate = periodarrangeStartDate;
		this.periodarrangeStartTime = periodarrangeStartTime;
		this.periodarrangeSetUpUserId = periodarrangeSetUpUserId;
	}

	/** full constructor */
	public Periodarrange(Integer periodarrangeClassId,
			Integer periodarrangeCabinetNum, Date periodarrangeStartDate,
			Integer periodarrangeStartTime, Date periodarrangeEndDate,
			Integer periodarrangeEndTime, Integer periodarrangeAllClassNum,
			Integer periodarrangeSetUpUserId) {
		this.periodarrangeClassId = periodarrangeClassId;
		this.periodarrangeCabinetNum = periodarrangeCabinetNum;
		this.periodarrangeStartDate = periodarrangeStartDate;
		this.periodarrangeStartTime = periodarrangeStartTime;
		this.periodarrangeEndDate = periodarrangeEndDate;
		this.periodarrangeEndTime = periodarrangeEndTime;
		this.periodarrangeAllClassNum = periodarrangeAllClassNum;
		this.periodarrangeSetUpUserId = periodarrangeSetUpUserId;
	}

	// Property accessors

	public Integer getPeriodarrangeId() {
		return this.periodarrangeId;
	}

	public void setPeriodarrangeId(Integer periodarrangeId) {
		this.periodarrangeId = periodarrangeId;
	}

	public Integer getPeriodarrangeClassId() {
		return this.periodarrangeClassId;
	}

	public void setPeriodarrangeClassId(Integer periodarrangeClassId) {
		this.periodarrangeClassId = periodarrangeClassId;
	}

	public Integer getPeriodarrangeCabinetNum() {
		return this.periodarrangeCabinetNum;
	}

	public void setPeriodarrangeCabinetNum(Integer periodarrangeCabinetNum) {
		this.periodarrangeCabinetNum = periodarrangeCabinetNum;
	}

	public Date getPeriodarrangeStartDate() {
		return this.periodarrangeStartDate;
	}

	public void setPeriodarrangeStartDate(Date periodarrangeStartDate) {
		this.periodarrangeStartDate = periodarrangeStartDate;
	}

	public Integer getPeriodarrangeStartTime() {
		return this.periodarrangeStartTime;
	}

	public void setPeriodarrangeStartTime(Integer periodarrangeStartTime) {
		this.periodarrangeStartTime = periodarrangeStartTime;
	}

	public Date getPeriodarrangeEndDate() {
		return this.periodarrangeEndDate;
	}

	public void setPeriodarrangeEndDate(Date periodarrangeEndDate) {
		this.periodarrangeEndDate = periodarrangeEndDate;
	}

	public Integer getPeriodarrangeEndTime() {
		return this.periodarrangeEndTime;
	}

	public void setPeriodarrangeEndTime(Integer periodarrangeEndTime) {
		this.periodarrangeEndTime = periodarrangeEndTime;
	}

	public Integer getPeriodarrangeAllClassNum() {
		return this.periodarrangeAllClassNum;
	}

	public void setPeriodarrangeAllClassNum(Integer periodarrangeAllClassNum) {
		this.periodarrangeAllClassNum = periodarrangeAllClassNum;
	}

	public Integer getPeriodarrangeSetUpUserId() {
		return this.periodarrangeSetUpUserId;
	}

	public void setPeriodarrangeSetUpUserId(Integer periodarrangeSetUpUserId) {
		this.periodarrangeSetUpUserId = periodarrangeSetUpUserId;
	}

}