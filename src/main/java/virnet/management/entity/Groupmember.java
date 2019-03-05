package virnet.management.entity;

/**
 * Groupmember entity. @author MyEclipse Persistence Tools
 */

public class Groupmember implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8814613081986533609L;
	private Integer classgroupmemberId;
	private Integer classgroupmemberGroupId;
	private Integer classgroupmemberUserId;
	private Integer classgroupmemberLeaderFlag;

	// Constructors

	/** default constructor */
	public Groupmember() {
	}

	/** minimal constructor */
	public Groupmember(Integer classgroupmemberGroupId,
			Integer classgroupmemberUserId) {
		this.classgroupmemberGroupId = classgroupmemberGroupId;
		this.classgroupmemberUserId = classgroupmemberUserId;
	}

	/** full constructor */
	public Groupmember(Integer classgroupmemberGroupId,
			Integer classgroupmemberUserId, Integer classgroupmemberLeaderFlag) {
		this.classgroupmemberGroupId = classgroupmemberGroupId;
		this.classgroupmemberUserId = classgroupmemberUserId;
		this.classgroupmemberLeaderFlag = classgroupmemberLeaderFlag;
	}

	// Property accessors

	public Integer getClassgroupmemberId() {
		return this.classgroupmemberId;
	}

	public void setClassgroupmemberId(Integer classgroupmemberId) {
		this.classgroupmemberId = classgroupmemberId;
	}

	public Integer getClassgroupmemberGroupId() {
		return this.classgroupmemberGroupId;
	}

	public void setClassgroupmemberGroupId(Integer classgroupmemberGroupId) {
		this.classgroupmemberGroupId = classgroupmemberGroupId;
	}

	public Integer getClassgroupmemberUserId() {
		return this.classgroupmemberUserId;
	}

	public void setClassgroupmemberUserId(Integer classgroupmemberUserId) {
		this.classgroupmemberUserId = classgroupmemberUserId;
	}

	public Integer getClassgroupmemberLeaderFlag() {
		return this.classgroupmemberLeaderFlag;
	}

	public void setClassgroupmemberLeaderFlag(Integer classgroupmemberLeaderFlag) {
		this.classgroupmemberLeaderFlag = classgroupmemberLeaderFlag;
	}

}