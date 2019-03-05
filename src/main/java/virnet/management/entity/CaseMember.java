package virnet.management.entity;


public class CaseMember implements java.io.Serializable {

	private static final long serialVersionUID = 8654572226154286050L;

	/**
	 * 实验实例成员表
	 */
	private Integer caseMemberId;
	private String caseMemberCaseId;
	private Integer caseMemberStuId;
	private String  caseMemberRole;
	private Integer caseMemberScore;
	
	public CaseMember(){
		
	}
	
	public CaseMember(Integer caseMemberId, String caseMemberCaseId, Integer caseMemberStuId, String caseMemberRole,
			Integer caseMemberScore) {
		super();
		this.caseMemberId = caseMemberId;
		this.caseMemberCaseId = caseMemberCaseId;
		this.caseMemberStuId = caseMemberStuId;
		this.caseMemberRole = caseMemberRole;
		this.caseMemberScore = caseMemberScore;
	}

	public Integer getCaseMemberId() {
		return caseMemberId;
	}

	public void setCaseMemberId(Integer caseMemberId) {
		this.caseMemberId = caseMemberId;
	}

	public String getCaseMemberCaseId() {
		return caseMemberCaseId;
	}

	public void setCaseMemberCaseId(String caseMemberCaseId) {
		this.caseMemberCaseId = caseMemberCaseId;
	}

	public Integer getCaseMemberStuId() {
		return caseMemberStuId;
	}

	public void setCaseMemberStuId(Integer caseMemberStuId) {
		this.caseMemberStuId = caseMemberStuId;
	}

	public String getCaseMemberRole() {
		return caseMemberRole;
	}

	public void setCaseMemberRole(String caseMemberRole) {
		this.caseMemberRole = caseMemberRole;
	}

	public Integer getCaseMemberScore() {
		return caseMemberScore;
	}

	public void setCaseMemberScore(Integer caseMemberScore) {
		this.caseMemberScore = caseMemberScore;
	}

}
