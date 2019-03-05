package virnet.management.entity;

/**
 * PeriodarrangeWeek entity. @author MyEclipse Persistence Tools
 */

public class PeriodarrangeWeek implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5240072418566759873L;
	private Integer periodarrangeWeekId;
	private Integer periodarrangeWeekClassid;
	private Integer periodarrangeWeekCabinetNum;
	private Short periodarrangeWeekStartDay;
	private Integer periodarrangeWeekStartTime;
	private Short periodarrangeWeekEndDay;
	private Integer periodarrangeWeekEndTime;
	private Integer periodarrangeWeekSetupuserId;

	// Constructors

	/** default constructor */
	public PeriodarrangeWeek() {
	}

	/** full constructor */
	public PeriodarrangeWeek(Integer periodarrangeWeekId,
			Integer periodarrangeWeekClassid,
			Integer periodarrangeWeekCabinetNum,
			Short periodarrangeWeekStartDay,
			Integer periodarrangeWeekStartTime, Short periodarrangeWeekEndDay,
			Integer periodarrangeWeekEndTime,
			Integer periodarrangeWeekSetupuserId) {
		this.periodarrangeWeekId = periodarrangeWeekId;
		this.periodarrangeWeekClassid = periodarrangeWeekClassid;
		this.periodarrangeWeekCabinetNum = periodarrangeWeekCabinetNum;
		this.periodarrangeWeekStartDay = periodarrangeWeekStartDay;
		this.periodarrangeWeekStartTime = periodarrangeWeekStartTime;
		this.periodarrangeWeekEndDay = periodarrangeWeekEndDay;
		this.periodarrangeWeekEndTime = periodarrangeWeekEndTime;
		this.periodarrangeWeekSetupuserId = periodarrangeWeekSetupuserId;
	}

	// Property accessors

	public Integer getPeriodarrangeWeekId() {
		return this.periodarrangeWeekId;
	}

	public void setPeriodarrangeWeekId(Integer periodarrangeWeekId) {
		this.periodarrangeWeekId = periodarrangeWeekId;
	}

	public Integer getPeriodarrangeWeekClassid() {
		return this.periodarrangeWeekClassid;
	}

	public void setPeriodarrangeWeekClassid(Integer periodarrangeWeekClassid) {
		this.periodarrangeWeekClassid = periodarrangeWeekClassid;
	}

	public Integer getPeriodarrangeWeekCabinetNum() {
		return this.periodarrangeWeekCabinetNum;
	}

	public void setPeriodarrangeWeekCabinetNum(
			Integer periodarrangeWeekCabinetNum) {
		this.periodarrangeWeekCabinetNum = periodarrangeWeekCabinetNum;
	}

	public Short getPeriodarrangeWeekStartDay() {
		return this.periodarrangeWeekStartDay;
	}

	public void setPeriodarrangeWeekStartDay(Short periodarrangeWeekStartDay) {
		this.periodarrangeWeekStartDay = periodarrangeWeekStartDay;
	}

	public Integer getPeriodarrangeWeekStartTime() {
		return this.periodarrangeWeekStartTime;
	}

	public void setPeriodarrangeWeekStartTime(Integer periodarrangeWeekStartTime) {
		this.periodarrangeWeekStartTime = periodarrangeWeekStartTime;
	}

	public Short getPeriodarrangeWeekEndDay() {
		return this.periodarrangeWeekEndDay;
	}

	public void setPeriodarrangeWeekEndDay(Short periodarrangeWeekEndDay) {
		this.periodarrangeWeekEndDay = periodarrangeWeekEndDay;
	}

	public Integer getPeriodarrangeWeekEndTime() {
		return this.periodarrangeWeekEndTime;
	}

	public void setPeriodarrangeWeekEndTime(Integer periodarrangeWeekEndTime) {
		this.periodarrangeWeekEndTime = periodarrangeWeekEndTime;
	}

	public Integer getPeriodarrangeWeekSetupuserId() {
		return this.periodarrangeWeekSetupuserId;
	}

	public void setPeriodarrangeWeekSetupuserId(
			Integer periodarrangeWeekSetupuserId) {
		this.periodarrangeWeekSetupuserId = periodarrangeWeekSetupuserId;
	}

}