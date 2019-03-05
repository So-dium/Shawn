package virnet.management.entity;

import java.util.Date;

public class ExpAutoPing implements java.io.Serializable {

	
	private static final long serialVersionUID = 5846227340990550662L;
	/**
	 * 自动评分拓扑图标准答案
	 */
	private Integer id;
	private String caseId;
	private Integer taskId;
	private String pingSource;
	private String pingDest;
	private Integer ifSame;
	private Integer pingState; 
	public ExpAutoPing() {
	}
	public ExpAutoPing(Integer id, String caseId, Integer taskId, String pingSource, String pingDest, Integer ifSame, Integer pingState) {
		super();
		this.id = id;
		this.caseId = caseId;
		this.taskId = taskId;
		this.pingSource = pingSource;
		this.pingDest = pingDest;
		this.ifSame = ifSame;
		this.pingState = pingState;
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
	public String getPingSource() {
		return pingSource;
	}
	public void setPingSource(String pingSource) {
		this.pingSource = pingSource;
	}
	public String getPingDest() {
		return pingDest;
	}
	public void setPingDest(String pingDest) {
		this.pingDest = pingDest;
	}
	public Integer getIfSame() {
		return ifSame;
	}
	public void setIfSame(Integer ifSame) {
		this.ifSame = ifSame;
	}
	public Integer getPingState() {
		return pingState;
	}
	public void setPingState(Integer pingState) {
		this.pingState = pingState;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
