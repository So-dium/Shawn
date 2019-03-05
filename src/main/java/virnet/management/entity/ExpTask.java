package virnet.management.entity;

public class ExpTask implements java.io.Serializable {

	/**
	 * 实验模板任务表
	 */
	private static final long serialVersionUID = 5606861693387912292L;
	private Integer expTaskId;
	private Integer expId;
	private Integer expTaskOrder;
	private Integer expTaskType;
	private String  expTaskContent;
	private String expTaskPing;
	private Integer expTaskTopoScore;
	private Integer expTaskConfigScore;
	private Integer expTaskPingScore;
	
	
	/** default constructor */
	public ExpTask(){	
	}
	
	
	/** minimal constructor */
	public ExpTask(Integer expTaskId, Integer expId) {
		super();
		this.expTaskId = expTaskId;
		this.expId = expId;
	}

	/** full constryctor */
	public ExpTask(Integer expTaskId, Integer expId, Integer expTaskOrder, Integer expTaskType, String expTaskContent, String expTaskPing, Integer expTaskTopoScore, Integer expTaskConfigScore, Integer expTaskPingScore) {
		super();
		this.expTaskId = expTaskId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.expTaskType = expTaskType;
		this.expTaskContent = expTaskContent;
		this.expTaskPing = expTaskPing;
		this.expTaskTopoScore = expTaskTopoScore;
		this.expTaskConfigScore = expTaskConfigScore;
		this.expTaskPingScore = expTaskPingScore;
	}

	public Integer getExpTaskId() {
		return expTaskId;
	}

	public void setExpTaskId(Integer expTaskId) {
		this.expTaskId = expTaskId;
	}

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public Integer getExpTaskOrder() {
		return expTaskOrder;
	}

	public void setExpTaskOrder(Integer expTaskOrder) {
		this.expTaskOrder = expTaskOrder;
	}

	public Integer getExpTaskType() {
		return expTaskType;
	}

	public void setExpTaskType(Integer expTaskType) {
		this.expTaskType = expTaskType;
	}

	public String getExpTaskContent() {
		return expTaskContent;
	}

	public void setExpTaskContent(String expTaskContent) {
		this.expTaskContent = expTaskContent;
	}

	public String getExpTaskPing() {
		return expTaskPing;
	}
	
	public void setExpTaskPing(String expTaskPing) {
		this.expTaskPing = expTaskPing;
	}

	public Integer getExpTaskTopoScore() {
		return expTaskTopoScore;
	}


	public void setExpTaskTopoScore(Integer expTaskTopoScore) {
		this.expTaskTopoScore = expTaskTopoScore;
	}


	public Integer getExpTaskConfigScore() {
		return expTaskConfigScore;
	}


	public void setExpTaskConfigScore(Integer expTaskConfigScore) {
		this.expTaskConfigScore = expTaskConfigScore;
	}


	public Integer getExpTaskPingScore() {
		return expTaskPingScore;
	}


	public void setExpTaskPingScore(Integer expTaskPingScore) {
		this.expTaskPingScore = expTaskPingScore;
	}
}
