package virnet.management.entity;

/**
 * Character entity. @author MyEclipse Persistence Tools
 */

public class Character implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883539890027811093L;
	private Integer characterId;
	private String characterName;
	private String characterInfo;

	// Constructors

	/** default constructor */
	public Character() {
	}

	/** minimal constructor */
	public Character(Integer characterId) {
		this.characterId = characterId;
	}

	/** full constructor */
	public Character(Integer characterId, String characterName,
			String characterInfo) {
		this.characterId = characterId;
		this.characterName = characterName;
		this.characterInfo = characterInfo;
	}

	// Property accessors

	public Integer getCharacterId() {
		return this.characterId;
	}

	public void setCharacterId(Integer characterId) {
		this.characterId = characterId;
	}

	public String getCharacterName() {
		return this.characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getCharacterInfo() {
		return this.characterInfo;
	}

	public void setCharacterInfo(String characterInfo) {
		this.characterInfo = characterInfo;
	}

}