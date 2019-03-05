package virnet.management.entity;


public class Case implements java.io.Serializable {

	private static final long serialVersionUID = 8654572226154286050L;

	/**
	 * 实验实例表
	 */
	private String caseId;
	private Integer caseResourceId;
	private Integer topoScore;
	private Integer configScore;
	private Integer pingScore;
	private Integer caseExpStuNum;
	private Integer groupScore;
	private Integer caseExpArrangeId;
	private String status;
	
	public Case(){
		
	}

	public Case(String caseId, Integer caseResourceId, Integer topoScore, Integer configScore, Integer pingScore, Integer caseExpStuNum, Integer groupScore, Integer caseExpArrangeId,String status) {
		super();
		this.caseId = caseId;
		this.caseResourceId = caseResourceId;
		this.topoScore = topoScore;
		this.configScore = configScore;
		this.pingScore = pingScore;
		this.caseExpStuNum = caseExpStuNum;
		this.groupScore = groupScore;
		this.caseExpArrangeId = caseExpArrangeId;
		this.status = status;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public Integer getCaseResourceId() {
		return caseResourceId;
	}

	public void setCaseResourceId(Integer caseResourceId) {
		this.caseResourceId = caseResourceId;
	}

	public Integer getTopoScore() {
		return topoScore;
	}

	public void setTopoScore(Integer topoScore) {
		this.topoScore = topoScore;
	}

	public Integer getConfigScore() {
		return configScore;
	}

	public void setConfigScore(Integer configScore) {
		this.configScore = configScore;
	}

	public Integer getPingScore() {
		return pingScore;
	}

	public void setPingScore(Integer pingScore) {
		this.pingScore = pingScore;
	}
	
	public Integer getCaseExpStuNum() {
		return caseExpStuNum;
	}

	public void setCaseExpStuNum(Integer caseExpStuNum) {
		this.caseExpStuNum = caseExpStuNum;
	}

	public Integer getGroupScore() {
		return groupScore;
	}

	public void setGroupScore(Integer groupScore) {
		this.groupScore = groupScore;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCaseExpArrangeId() {
		return caseExpArrangeId;
	}

	public void setCaseExpArrangeId(Integer caseExpArrangeId) {
		this.caseExpArrangeId = caseExpArrangeId;
	}
	
	
}
