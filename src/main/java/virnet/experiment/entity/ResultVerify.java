package virnet.experiment.entity;

public class ResultVerify implements java.io.Serializable {

	private static final long serialVersionUID = 8209362982125164266L;
	/**
	 * 实验结果验证表
	 */
	private Integer resultVerifyId;
	private Integer resultTaskId;
	private Integer resultVerifyType;
	
	public ResultVerify() {

	}

	public ResultVerify(Integer resultVerifyId, Integer resultTaskId, Integer resultVerifyType) {
		super();
		this.resultVerifyId = resultVerifyId;
		this.resultTaskId = resultTaskId;
		this.resultVerifyType = resultVerifyType;
	}

	public ResultVerify(Integer resultVerifyId, Integer resultTaskId) {
		super();
		this.resultVerifyId = resultVerifyId;
		this.resultTaskId = resultTaskId;
	}

	public Integer getResultVerifyId() {
		return resultVerifyId;
	}

	public void setResultVerifyId(Integer resultVerifyId) {
		this.resultVerifyId = resultVerifyId;
	}

	public Integer getResultTaskId() {
		return resultTaskId;
	}

	public void setResultTaskId(Integer resultTaskId) {
		this.resultTaskId = resultTaskId;
	}

	public Integer getResultVerifyType() {
		return resultVerifyType;
	}

	public void setResultVerifyType(Integer resultVerifyType) {
		this.resultVerifyType = resultVerifyType;
	}
}