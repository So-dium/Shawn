package virnet.management.entity;

import java.sql.Date;
import java.sql.Time;
/**
 * Classarrange entity. @author MyEclipse Persistence Tools
 */

public class expArrange implements java.io.Serializable {


	private static final long serialVersionUID = -1634036932298317684L;
	private Integer expArrangeId;
	private Date appointmentStartDate;
	private Time appointmentStartTime;
	private Date appointmentEndDate;
	private Time appointmentEndTime;

	// Constructors

	/** default constructor */
	public expArrange() {
	}

	public expArrange(Integer expArrangeId, Date appointmentStartDate, Time appointmentStartTime,
			Date appointmentEndDate, Time appointmentEndTime) {
		super();
		this.expArrangeId = expArrangeId;
		this.appointmentStartDate = appointmentStartDate;
		this.appointmentStartTime = appointmentStartTime;
		this.appointmentEndDate = appointmentEndDate;
		this.appointmentEndTime = appointmentEndTime;
	}

	public Integer getExpArrangeId() {
		return expArrangeId;
	}

	public void setExpArrangeId(Integer expArrangeId) {
		this.expArrangeId = expArrangeId;
	}

	public Date getAppointmentStartDate() {
		return appointmentStartDate;
	}

	public void setAppointmentStartDate(Date appointmentStartDate) {
		this.appointmentStartDate = appointmentStartDate;
	}

	public Time getAppointmentStartTime() {
		return appointmentStartTime;
	}

	public void setAppointmentStartTime(Time appointmentStartTime) {
		this.appointmentStartTime = appointmentStartTime;
	}

	public Date getAppointmentEndDate() {
		return appointmentEndDate;
	}

	public void setAppointmentEndDate(Date appointmentEndDate) {
		this.appointmentEndDate = appointmentEndDate;
	}

	public Time getAppointmentEndTime() {
		return appointmentEndTime;
	}

	public void setAppointmentEndTime(Time appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}
	
	

}