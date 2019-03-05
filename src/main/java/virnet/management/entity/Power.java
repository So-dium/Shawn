package virnet.management.entity;

/**
 * Power entity. @author MyEclipse Persistence Tools
 */

public class Power implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7519114353342204921L;
	private Integer powerId;
	private String powerInfo;

	// Constructors

	/** default constructor */
	public Power() {
	}

	/** minimal constructor */
	public Power(Integer powerId) {
		this.powerId = powerId;
	}

	/** full constructor */
	public Power(Integer powerId, String powerInfo) {
		this.powerId = powerId;
		this.powerInfo = powerInfo;
	}

	// Property accessors

	public Integer getPowerId() {
		return this.powerId;
	}

	public void setPowerId(Integer powerId) {
		this.powerId = powerId;
	}

	public String getPowerInfo() {
		return this.powerInfo;
	}

	public void setPowerInfo(String powerInfo) {
		this.powerInfo = powerInfo;
	}

}