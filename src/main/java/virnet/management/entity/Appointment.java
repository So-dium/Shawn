package virnet.management.entity;

import java.util.Date;

public class Appointment implements java.io.Serializable {

	
	private static final long serialVersionUID = 5846227340990550662L;
	/**
	 * 实验机柜预约表
	 */
	private Integer appointmentId;
	private String expCaseId;
	private Integer appointmentTimeId;
	private Integer appointmentCabinetId;
	private Integer appointmentGroupId;
	private Date appointmentCreateTime;
	private String appointmentExpName;
	
	public Appointment() {
	}
	public Appointment(Integer appointmentId, String expCaseId, Integer appointmentTimeId,
			Integer appointmentCabinetId, Integer appointmentGroupId, Date appointmentCreateTime,String appointmentExpName) {
		this.appointmentId = appointmentId;
		this.expCaseId = expCaseId;
		this.appointmentTimeId = appointmentTimeId;
		this.appointmentCabinetId = appointmentCabinetId;
		this.appointmentGroupId = appointmentGroupId;
		this.appointmentCreateTime = appointmentCreateTime;
		this.appointmentExpName = appointmentExpName;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getExpCaseId() {
		return expCaseId;
	}
	public void setExpCaseId(String expCaseId) {
		this.expCaseId = expCaseId;
	}
	public Integer getAppointmentTimeId() {
		return appointmentTimeId;
	}
	public void setAppointmentTimeId(Integer appointmentTimeId) {
		this.appointmentTimeId = appointmentTimeId;
	}
	public Integer getAppointmentCabinetId() {
		return appointmentCabinetId;
	}
	public void setAppointmentCabinetId(Integer appointmentCabinetId) {
		this.appointmentCabinetId = appointmentCabinetId;
	}
	public Integer getAppointmentGroupId() {
		return appointmentGroupId;
	}
	public void setAppointmentGroupId(Integer appointmentGroupId) {
		this.appointmentGroupId = appointmentGroupId;
	}
	public Date getAppointmentCreateTime() {
		return appointmentCreateTime;
	}
	public void setAppointmentCreateTime(Date appointmentCreateTime) {
		this.appointmentCreateTime = appointmentCreateTime;
	}
	public String getAppointmentExpName() {
		return appointmentExpName;
	}
	public void setAppointmentExpName(String appointmentExpName) {
		this.appointmentExpName = appointmentExpName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
	

	
}
