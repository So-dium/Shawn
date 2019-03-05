package virnet.experiment.entity;

public class ResultVerifyPing implements java.io.Serializable {
	
	private static final long serialVersionUID = -8195908573099506223L;
	/**
	 * 实验结果ping验证表
	 */
	private Integer resultVerifyPingId;
	private Integer resultVerifyId;
	private Integer sourcePCOrder;
	private String sourcePCIp;
	private Integer sourcePCDeviceOrder;
	private Integer sourcePCPortOrder;
	private Integer destPCOrder;
	private String destPCIp;
	private Integer destPCDeviceOrder;
	private Integer destPCPortOrder;
	private Integer successFlag;
	
	public ResultVerifyPing() {

	}

	public ResultVerifyPing(Integer resultVerifyPingId, Integer resultVerifyId, Integer sourcePCOrder,
			String sourcePCIp, Integer sourcePCDeviceOrder, Integer sourcePCPortOrder, Integer destPCOrder,
			String destPCIp, Integer destPCDeviceOrder, Integer destPCPortOrder, Integer successFlag) {
		super();
		this.resultVerifyPingId = resultVerifyPingId;
		this.resultVerifyId = resultVerifyId;
		this.sourcePCOrder = sourcePCOrder;
		this.sourcePCIp = sourcePCIp;
		this.sourcePCDeviceOrder = sourcePCDeviceOrder;
		this.sourcePCPortOrder = sourcePCPortOrder;
		this.destPCOrder = destPCOrder;
		this.destPCIp = destPCIp;
		this.destPCDeviceOrder = destPCDeviceOrder;
		this.destPCPortOrder = destPCPortOrder;
		this.successFlag = successFlag;
	}

	public ResultVerifyPing(Integer resultVerifyPingId, Integer resultVerifyId) {
		super();
		this.resultVerifyPingId = resultVerifyPingId;
		this.resultVerifyId = resultVerifyId;
	}

	public Integer getResultVerifyPingId() {
		return resultVerifyPingId;
	}

	public void setResultVerifyPingId(Integer resultVerifyPingId) {
		this.resultVerifyPingId = resultVerifyPingId;
	}

	public Integer getResultVerifyId() {
		return resultVerifyId;
	}

	public void setResultVerifyId(Integer resultVerifyId) {
		this.resultVerifyId = resultVerifyId;
	}

	public Integer getSourcePCOrder() {
		return sourcePCOrder;
	}

	public void setSourcePCOrder(Integer sourcePCOrder) {
		this.sourcePCOrder = sourcePCOrder;
	}

	public String getSourcePCIp() {
		return sourcePCIp;
	}

	public void setSourcePCIp(String sourcePCIp) {
		this.sourcePCIp = sourcePCIp;
	}

	public Integer getSourcePCDeviceOrder() {
		return sourcePCDeviceOrder;
	}

	public void setSourcePCDeviceOrder(Integer sourcePCDeviceOrder) {
		this.sourcePCDeviceOrder = sourcePCDeviceOrder;
	}

	public Integer getSourcePCPortOrder() {
		return sourcePCPortOrder;
	}

	public void setSourcePCPortOrder(Integer sourcePCPortOrder) {
		this.sourcePCPortOrder = sourcePCPortOrder;
	}

	public Integer getDestPCOrder() {
		return destPCOrder;
	}

	public void setDestPCOrder(Integer destPCOrder) {
		this.destPCOrder = destPCOrder;
	}

	public String getDestPCIp() {
		return destPCIp;
	}

	public void setDestPCIp(String destPCIp) {
		this.destPCIp = destPCIp;
	}

	public Integer getDestPCDeviceOrder() {
		return destPCDeviceOrder;
	}

	public void setDestPCDeviceOrder(Integer destPCDeviceOrder) {
		this.destPCDeviceOrder = destPCDeviceOrder;
	}

	public Integer getDestPCPortOrder() {
		return destPCPortOrder;
	}

	public void setDestPCPortOrder(Integer destPCPortOrder) {
		this.destPCPortOrder = destPCPortOrder;
	}

	public Integer getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(Integer successFlag) {
		this.successFlag = successFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
}