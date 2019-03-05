package virnet.experiment.entity;

public class ExpConfig implements java.io.Serializable {

	/**
	 * 实验模板配置表
	 */
	private static final long serialVersionUID = 7705946504004832015L;
	private Integer expConfigId;
	private Integer expId;
	private Integer expTaskOrder;
	private Integer  expConfigNum;
	
	public ExpConfig() {
		
	}

	public ExpConfig(Integer expConfigId, Integer expId, Integer expTaskOrder, Integer expConfigNum) {
		super();
		this.expConfigId = expConfigId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.expConfigNum = expConfigNum;
	}

	public ExpConfig(Integer expConfigId, Integer expId, Integer expTaskOrder) {
		super();
		this.expConfigId = expConfigId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
	}

	public Integer getExpConfigId() {
		return expConfigId;
	}

	public void setExpConfigId(Integer expConfigId) {
		this.expConfigId = expConfigId;
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

	public Integer getExpConfigNum() {
		return expConfigNum;
	}

	public void setExpConfigNum(Integer expConfigNum) {
		this.expConfigNum = expConfigNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
