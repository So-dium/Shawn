package virnet.experiment.entity;

public class ExpConnect implements java.io.Serializable {

	/**
	 * 实验模板连接表
	 */
	private static final long serialVersionUID = 4148541139315849343L;
	private Integer expConnectId;
	private Integer expTopoId;
	private Integer device1Order;
	private Integer device1PortOrder;
	private Integer device2Order;
	private Integer device2PortOrder;
	
	public ExpConnect() {
		
	}

	public ExpConnect(Integer expConnectId, Integer expTopoId, Integer device1Order, Integer device1PortOrder,
			Integer device2Order, Integer device2PortOrder) {
		super();
		this.expConnectId = expConnectId;
		this.expTopoId = expTopoId;
		this.device1Order = device1Order;
		this.device1PortOrder = device1PortOrder;
		this.device2Order = device2Order;
		this.device2PortOrder = device2PortOrder;
	}

	public ExpConnect(Integer expConnectId, Integer expTopoId) {
		super();
		this.expConnectId = expConnectId;
		this.expTopoId = expTopoId;
	}

	public Integer getExpConnectId() {
		return expConnectId;
	}

	public void setExpConnectId(Integer expConnectId) {
		this.expConnectId = expConnectId;
	}

	
	public Integer getExpTopoId() {
		return expTopoId;
	}

	public void setExpTopoId(Integer expTopoId) {
		this.expTopoId = expTopoId;
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