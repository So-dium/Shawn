package virnet.management.entity;


public class CaseExp implements java.io.Serializable {

	private static final long serialVersionUID = 8654572226154286050L;

	/**
	 * 实验实例实验表
	 */
	private Integer caseExpId;
	private String caseExpCaseId;
	private Integer caseExpExpId;

	
	public CaseExp(){
		
	}


	public CaseExp(Integer caseExpId, String caseExpCaseId, Integer caseExpExpId) {
		super();
		this.caseExpId = caseExpId;
		this.caseExpCaseId = caseExpCaseId;
		this.caseExpExpId = caseExpExpId;
	}


	public Integer getCaseExpId() {
		return caseExpId;
	}


	public void setCaseExpId(Integer caseExpId) {
		this.caseExpId = caseExpId;
	}


	public String getCaseExpCaseId() {
		return caseExpCaseId;
	}


	public void setCaseExpCaseId(String caseExpCaseId) {
		this.caseExpCaseId = caseExpCaseId;
	}


	public Integer getCaseExpExpId() {
		return caseExpExpId;
	}


	public void setCaseExpExpId(Integer caseExpExpId) {
		this.caseExpExpId = caseExpExpId;
	}
	
	

	
}
