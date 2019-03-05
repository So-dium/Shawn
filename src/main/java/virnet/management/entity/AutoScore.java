package virnet.management.entity;

import java.util.Date;

public class AutoScore implements java.io.Serializable {

	
	private static final long serialVersionUID = 5846227340990550662L;
	/**
	 * 自动评分得分
	 */
	private String caseId;
	private Integer taskId;
	private Integer topo;
	private Integer config;
	private Integer ping;
	private Integer grade;
	
	public AutoScore() {
	}
	public AutoScore(String caseId, Integer taskId, Integer topo, Integer config, Integer ping, Integer grade) {
		super();
		this.caseId = caseId;
		this.taskId = taskId;
		this.topo = topo;
		this.config = config;
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
	public Integer getTopo() {
		return topo;
	}
	public void setTopo(Integer topo) {
		this.topo = topo;
	}
	public Integer getConfig() {
		return config;
	}
	public void setConfig(Integer config) {
		this.config = config;
	}
	public Integer getPing() {
		return ping;
	}
	public void setPing(Integer ping) {
		this.ping = ping;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}