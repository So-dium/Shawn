package virnet.experiment.entity;

public class ResultConnect implements java.io.Serializable {

	private static final long serialVersionUID = 4370264565965280160L;
	/**
	 * 实验结果连接表
	 */
	private Integer resultConnectId;
	private Integer resultTopoId;
	private Integer device1Order;
	private Integer device1PortOrder;
	private Integer device2Order;
	private Integer device2PortOrder;
	
	public ResultConnect() {

	}

	public ResultConnect(Integer resultConnectId, Integer resultTopoId, Integer device1Order, Integer device1PortOrder,
			Integer device2Order, Integer device2PortOrder) {
		super();
		this.resultConnectId = resultConnectId;
		this.resultTopoId = resultTopoId;
		this.device1Order = device1Order;
		this.device1PortOrder = device1PortOrder;
		this.device2Order = device2Order;
		this.device2PortOrder = device2PortOrder;
	}

	public ResultConnect(Integer resultConnectId, Integer resultTopoId) {
		super();
		this.resultConnectId = resultConnectId;
		this.resultTopoId = resultTopoId;
	}

	public Integer getResultConnectId() {
		return resultConnectId;
	}

	public void setResultConnectId(Integer resultConnectId) {
		this.resultConnectId = resultConnectId;
	}

	public Integer getResultTopoId() {
		return resultTopoId;
	}

	public void setResultTopoId(Integer resultTopoId) {
		this.resultTopoId = resultTopoId;
	}

	public Integer getDevice1Order() {
		return device1Order;
	}

	public void setDevice1Order(Integer device1Order) {
		this.device1Order = device1Order;
	}

	public Integer getDevice1PortOrder() {
		return device1PortOrder;
	}

	public void setDevice1PortOrder(Integer device1PortOrder) {
		this.device1PortOrder = device1PortOrder;
	}

	public Integer getDevice2Order() {
		return device2Order;
	}

	public void setDevice2Order(Integer device2Order) {
		this.device2Order = device2Order;
	}

	public Integer getDevice2PortOrder() {
		return device2PortOrder;
	}

	public void setDevice2PortOrder(Integer device2PortOrder) {
		this.device2PortOrder = device2PortOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}