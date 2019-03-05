package virnet.experiment.entity;

public class ExpTopoPosition implements java.io.Serializable {

	/**
	 * 实验模板拓扑位置表
	 */
	private static final long serialVersionUID = 5842882650003832389L;
	
	private Integer expTopoPositionId;
	private Integer expTopoId;
	private Integer deviceOrder;
	private Double  gridX;
	private Double  gridY;
	
	public ExpTopoPosition() {
		
	}

	public ExpTopoPosition(Integer expTopoPositionId, Integer expTopoId, Integer deviceOrder, Double gridX,
			Double gridY) {
		super();
		this.expTopoPositionId = expTopoPositionId;
		this.expTopoId = expTopoId;
		this.deviceOrder = deviceOrder;
		this.gridX = gridX;
		this.gridY = gridY;
	}

	public Integer getExpTopoPositionId() {
		return expTopoPositionId;
	}

	public void setExpTopoPositionId(Integer expTopoPositionId) {
		this.expTopoPositionId = expTopoPositionId;
	}

	public Integer getExpTopoId() {
		return expTopoId;
	}

	public void setExpTopoId(Integer expTopoId) {
		this.expTopoId = expTopoId;
	}

	public Integer getDeviceOrder() {
		return deviceOrder;
	}

	public void setDeviceOrder(Integer deviceOrder) {
		this.deviceOrder = deviceOrder;
	}

	public Double getGridX() {
		return gridX;
	}

	public void setGridX(Double gridX) {
		this.gridX = gridX;
	}

	public Double getGridY() {
		return gridY;
	}

	public void setGridY(Double gridY) {
		this.gridY = gridY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}