package virnet.experiment.entity;

public class ResultTopoPosition implements java.io.Serializable {

	/**
	 * 实验模板拓扑位置表
	 */
	private static final long serialVersionUID = 5842882650003832389L;
	
	private Integer resultTopoPositionId;
	private Integer resultTopoId;
	private Integer deviceOrder;
	private Double  gridX;
	private Double  gridY;
	
	public ResultTopoPosition() {
		
	}

	public ResultTopoPosition(Integer resultTopoPositionId, Integer resultTopoId, Integer deviceOrder, Double gridX,
			Double gridY) {
		super();
		this.resultTopoPositionId = resultTopoPositionId;
		this.resultTopoId = resultTopoId;
		this.deviceOrder = deviceOrder;
		this.gridX = gridX;
		this.gridY = gridY;
	}

	public Integer getResultTopoPositionId() {
		return resultTopoPositionId;
	}

	public void setResultTopoPositionId(Integer resultTopoPositionId) {
		this.resultTopoPositionId = resultTopoPositionId;
	}

	public Integer getResultTopoId() {
		return resultTopoId;
	}

	public void setResultTopoId(Integer resultTopoId) {
		this.resultTopoId = resultTopoId;
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