package virnet.management.entity;



/**
 * Ordermember entity. @author MyEclipse Persistence Tools
 */

public class Ordermember  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -2417840036589319594L;
	private Integer ordermemberId;
     private Integer ordermemberOrderId;
     private Integer ordermemberUserId;
     private Integer ordermemberConfirmStatus;


    // Constructors

    /** default constructor */
    public Ordermember() {
    }

    
    /** full constructor */
    public Ordermember(Integer ordermemberId, Integer ordermemberOrderId, 
    		Integer ordermemberUserId , Integer ordermemberConfirmStatus) {
        this.ordermemberId = ordermemberId;
        this.ordermemberOrderId = ordermemberOrderId;
        this.ordermemberUserId = ordermemberUserId;
        this.ordermemberConfirmStatus = ordermemberConfirmStatus;
    }

   
    // Property accessors

    public Integer getOrdermemberId() {
        return this.ordermemberId;
    }
    
    public void setOrdermemberId(Integer ordermemberId) {
        this.ordermemberId = ordermemberId;
    }

    public Integer getOrdermemberOrderId() {
        return this.ordermemberOrderId;
    }
    
    public void setOrdermemberOrderId(Integer ordermemberOrderId) {
        this.ordermemberOrderId = ordermemberOrderId;
    }

    public Integer getOrdermemberUserId() {
        return this.ordermemberUserId;
    }
    
    public void setOrdermemberUserId(Integer ordermemberUserId) {
        this.ordermemberUserId = ordermemberUserId;
    }


	public Integer getOrdermemberConfirmStatus() {
		return ordermemberConfirmStatus;
	}


	public void setOrdermemberConfirmStatus(Integer ordermemberConfirmStatus) {
		this.ordermemberConfirmStatus = ordermemberConfirmStatus;
	}
    

}