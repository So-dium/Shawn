package virnet.management.entity;

/**
 * UserCharacter entity. @author MyEclipse Persistence Tools
 */

public class UserCharacter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4947100225206620838L;
	private Integer userCharacterId;
	private Integer userCharacterUserId;
	private Integer userCharacterCharacterId;

	// Constructors

	/** default constructor */
	public UserCharacter() {
	}

	/** full constructor */
	public UserCharacter(Integer userCharacterId, Integer userCharacterUserId,
			Integer userCharacterCharacterId) {
		this.userCharacterId = userCharacterId;
		this.userCharacterUserId = userCharacterUserId;
		this.userCharacterCharacterId = userCharacterCharacterId;
	}

	// Property accessors

	public Integer getUserCharacterId() {
		return this.userCharacterId;
	}

	public void setUserCharacterId(Integer userCharacterId) {
		this.userCharacterId = userCharacterId;
	}

	public Integer getUserCharacterUserId() {
		return this.userCharacterUserId;
	}

	public void setUserCharacterUserId(Integer userCharacterUserId) {
		this.userCharacterUserId = userCharacterUserId;
	}

	public Integer getUserCharacterCharacterId() {
		return this.userCharacterCharacterId;
	}

	public void setUserCharacterCharacterId(Integer userCharacterCharacterId) {
		this.userCharacterCharacterId = userCharacterCharacterId;
	}



}