package virnet.management.entity;

import java.util.Date;

public class ExpAutoTopo implements java.io.Serializable {

	
	private static final long serialVersionUID = 5846227340990550662L;
	/**
	 * 自动评分拓扑图标准答案
	 */
	private Integer id;
	private String caseId;
	private Integer taskId;
	private Integer facilitiesOrder;
	private Integer connect;
	
	public ExpAutoTopo() {
	}
	public ExpAutoTopo(Integer id, String caseId, Integer taskId, Integer facilitiesOrder, Integer connect) {
		super();
		this.id = id;
		this.caseId = caseId;
		this.taskId = taskId;
		this.facilitiesOrder = facilitiesOrder;
		this.connect = connect;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getFacilitiesOrder() {
		return facilitiesOrder;
	}
	public void setFacilitiesOrder(Integer facilitiesOrder) {
		this.facilitiesOrder = facilitiesOrder;
	}
	public Integer getConnect() {
		return connect;
	}
	public void setConnect(Integer connect) {
		this.connect = connect;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
