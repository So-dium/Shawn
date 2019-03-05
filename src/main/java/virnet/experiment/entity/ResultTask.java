package virnet.experiment.entity;

public class ResultTask implements java.io.Serializable {

	private static final long serialVersionUID = -1649695575201344021L;
	/**
	 * 实验结果任务表
	 */

	private Integer resultTaskId;
	private String resultCaseId;
	private Integer resultExpId;
	private Integer resultTaskOrder;
	private Integer resultTaskType;
	private String resultTaskContent;
	private Integer topoScore;
	private Integer configScore;
	private Integer pingScore;
	private Integer groupScore;

	public ResultTask() {

	}

	public ResultTask(Integer resultTaskId, String resultCaseId, Integer resultExpId, Integer resultTaskOrder,
			Integer resultTaskType, String resultTaskContent, Integer topoScore, Integer configScore, Integer pingScore,
			Integer groupScore) {
		super();
		this.resultTaskId = resultTaskId;
		this.resultCaseId = resultCaseId;
		this.resultExpId = resultExpId;
		this.resultTaskOrder = resultTaskOrder;
		this.resultTaskType = resultTaskType;
		this.resultTaskContent = resultTaskContent;
		this.topoScore = topoScore;
		this.configScore = configScore;
		this.pingScore = pingScore;
		this.groupScore = groupScore;
	}

	public ResultTask(Integer resultTaskId, String resultCaseId, Integer resultExpId, Integer resultTaskOrder,
			Integer topoScore, Integer configScore, Integer pingScore, Integer groupScore) {
		super();
		this.resultTaskId = resultTaskId;
		this.resultCaseId = resultCaseId;
		this.resultExpId = resultExpId;
		this.resultTaskOrder = resultTaskOrder;
		this.topoScore = topoScore;
		this.configScore = configScore;
		this.pingScore = pingScore;
		this.groupScore = groupScore;
	}

	public Integer getResultTaskId() {
		return resultTaskId;
	}

	public void setResultTaskId(Integer resultTaskId) {
		this.resultTaskId = resultTaskId;
	}

	public String getResultCaseId() {
		return resultCaseId;
	}

	public void setResultCaseId(String resultCaseId) {
		this.resultCaseId = resultCaseId;
	}

	public Integer getResultExpId() {
		return resultExpId;
	}

	public void setResultExpId(Integer resultExpId) {
		this.resultExpId = resultExpId;
	}

	public Integer getResultTaskOrder() {
		return resultTaskOrder;
	}

	public void setResultTaskOrder(Integer resultTaskOrder) {
		this.resultTaskOrder = resultTaskOrder;
	}

	public Integer getResultTaskType() {
		return resultTaskType;
	}

	public void setResultTaskType(Integer resultTaskType) {
		this.resultTaskType = resultTaskType;
	}

	public String getResultTaskContent() {
		return resultTaskContent;
	}

	public void setResultTaskContent(String resultTaskContent) {
		this.resultTaskContent = resultTaskContent;
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

	public Integer getGroupScore() {
		return groupScore;
	}

	public void setGroupScore(Integer groupScore) {
		this.groupScore = groupScore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
