package virnet.experiment.entity;
public class ResultTopo implements java.io.Serializable {

	private static final long serialVersionUID = 2351358221273657437L;
	/**
	 * 实验结果拓扑表
	 */

	private Integer resultTopoId;
	private Integer resultTaskId;
	private Integer resultTopoConnetNum;
	private String  resultTopoRemark;
	
	public ResultTopo() {

	}

	public ResultTopo(Integer resultTopoId, Integer resultTaskId, Integer resultTopoConnetNum, String resultTopoRemark) {
		super();
		this.resultTopoId = resultTopoId;
		this.resultTaskId = resultTaskId;
		this.resultTopoConnetNum = resultTopoConnetNum;
		this.resultTopoRemark = resultTopoRemark;
	}

	public ResultTopo(Integer resultTopoId, Integer resultTaskId) {
		super();
		this.resultTopoId = resultTopoId;
		this.resultTaskId = resultTaskId;
	}

	public Integer getResultTopoId() {
		return resultTopoId;
	}

	public void setResultTopoId(Integer resultTopoId) {
		this.resultTopoId = resultTopoId;
	}

	public Integer getResultTaskId() {
		return resultTaskId;
	}

	public void setResultTaskId(Integer resultTaskId) {
		this.resultTaskId = resultTaskId;
	}

	public Integer getresultTopoConnetNum() {
		return resultTopoConnetNum;
	}

	public void setresultTopoConnetNum(Integer resultTopoConnetNum) {
		this.resultTopoConnetNum = resultTopoConnetNum;
	}

	public String getresultTopoRemark() {
		return resultTopoRemark;
	}

	public void setresultTopoRemark(String resultTopoRemark) {
		this.resultTopoRemark = resultTopoRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}