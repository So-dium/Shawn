package virnet.experiment.entity;

public class ExpVerify implements java.io.Serializable {

	/**
	 * 实验模板验证表
	 */
	private static final long serialVersionUID = 8781036319591369180L;
	private Integer expVerifyId;
	private Integer expId;
	private Integer expTaskOrder;
	private Integer verifyType;
	
	
	public ExpVerify() {

	}

	public ExpVerify(Integer expVerifyId, Integer expId, Integer expTaskOrder, Integer verifyType) {
		super();
		this.expVerifyId = expVerifyId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.verifyType = verifyType;
	}

	public ExpVerify(Integer expVerifyId, Integer expId, Integer expTaskOrder) {
		super();
		this.expVerifyId = expVerifyId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
	}

	public Integer getExpVerifyId() {
		return expVerifyId;
	}

	public void setExpVerifyId(Integer expVerifyId) {
		this.expVerifyId = expVerifyId;
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

	public Integer getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(Integer verifyType) {
		this.verifyType = verifyType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}