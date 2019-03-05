package virnet.management.entity;

/**
 * CharacterPower entity. @author MyEclipse Persistence Tools
 */

public class CharacterPower implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8894446706249595514L;
	private Integer characterPowerId;
	private Integer characterPowerCharacterId;
	private Integer characterPowerPowerId;

	// Constructors

	/** default constructor */
	public CharacterPower() {
	}

	/** full constructor */
	public CharacterPower(Integer characterPowerId,
			Integer characterPowerCharacterId, Integer characterPowerPowerId) {
		this.characterPowerId = characterPowerId;
		this.characterPowerCharacterId = characterPowerCharacterId;
		this.characterPowerPowerId = characterPowerPowerId;
	}

	// Property accessors

	public Integer getCharacterPowerId() {
		return this.characterPowerId;
	}

	public void setCharacterPowerId(Integer characterPowerId) {
		this.characterPowerId = characterPowerId;
	}

	public Integer getCharacterPowerCharacterId() {
		return this.characterPowerCharacterId;
	}

	public void setCharacterPowerCharacterId(Integer characterPowerCharacterId) {
		this.characterPowerCharacterId = characterPowerCharacterId;
	}

	public Integer getCharacterPowerPowerId() {
		return this.characterPowerPowerId;
	}

	public void setCharacterPowerPowerId(Integer characterPowerPowerId) {
		this.characterPowerPowerId = characterPowerPowerId;
	}

}