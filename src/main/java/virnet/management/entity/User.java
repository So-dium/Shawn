package virnet.management.entity;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8552990432540677801L;
	private Integer userId;
	private Integer userKeyResult;
	private String userIdentity;
	private String userNickname;
	private String userPhone;
	private String userEmail;
	private String userOther;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(Integer userId) {
		this.userId = userId;
	}

	/** full constructor */
	public User(Integer userId, Integer userKeyResult, String userIdentity,
			String userNickname, String userPhone, String userEmail,
			String userOther) {
		this.userId = userId;
		this.userKeyResult = userKeyResult;
		this.userIdentity = userIdentity;
		this.userNickname = userNickname;
		this.userPhone = userPhone;
		this.userEmail = userEmail;
		this.userOther = userOther;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserKeyResult() {
		return this.userKeyResult;
	}

	public void setUserKeyResult(Integer userKeyResult) {
		this.userKeyResult = userKeyResult;
	}

	public String getUserIdentity() {
		return this.userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public String getUserNickname() {
		return this.userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserOther() {
		return this.userOther;
	}

	public void setUserOther(String userOther) {
		this.userOther = userOther;
	}

}