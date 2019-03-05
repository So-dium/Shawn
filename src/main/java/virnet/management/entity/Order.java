package virnet.management.entity;



/**
 * Order entity. @author MyEclipse Persistence Tools
 */

public class Order  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -5096155198935426382L;
	private Integer orderId;
     private String orderSetUpDate;
     private String orderSetUpTime;
     private Integer orderSetUpUserId;
     private Integer orderExpArrangeId;
     private Integer orderExpId;
     private Integer orderCaseResourceId;
     private String orderStatus;


    // Constructors

    /** default constructor */
    public Order() {
    }

	/** minimal constructor */
    public Order(Integer orderId, Integer orderSetUpUserId, Integer orderExpArrangeId) {
        this.orderId = orderId;
        this.orderSetUpUserId = orderSetUpUserId;
        this.orderExpArrangeId = orderExpArrangeId;
    }
    
    /** full constructor */
    public Order(Integer orderId, String orderSetUpDate, String orderSetUpTime, Integer orderSetUpUserId, Integer orderExpArrangeId, Integer orderExpId, Integer orderCaseResourceId, String orderStatus) {
        this.orderId = orderId;
        this.orderSetUpDate = orderSetUpDate;
        this.orderSetUpTime = orderSetUpTime;
        this.orderSetUpUserId = orderSetUpUserId;
        this.orderExpArrangeId = orderExpArrangeId;
        this.orderExpId = orderExpId;
        this.orderCaseResourceId = orderCaseResourceId;
        this.orderStatus = orderStatus;
    }

   
    // Property accessors

    public Integer getOrderId() {
        return this.orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderSetUpDate() {
        return this.orderSetUpDate;
    }
    
    public void setOrderSetUpDate(String orderSetUpDate) {
        this.orderSetUpDate = orderSetUpDate;
    }

    public String getOrderSetUpTime() {
        return this.orderSetUpTime;
    }
    
    public void setOrderSetUpTime(String orderSetUpTime) {
        this.orderSetUpTime = orderSetUpTime;
    }

    public Integer getOrderSetUpUserId() {
        return this.orderSetUpUserId;
    }
    
    public void setOrderSetUpUserId(Integer orderSetUpUserId) {
        this.orderSetUpUserId = orderSetUpUserId;
    }

    public Integer getOrderExpArrangeId() {
        return this.orderExpArrangeId;
    }
    
    public void setOrderExpArrangeId(Integer orderExpArrangeId) {
        this.orderExpArrangeId = orderExpArrangeId;
    }

    public Integer getOrderExpId() {
        return this.orderExpId;
    }
    
    public void setOrderExpId(Integer orderExpId) {
        this.orderExpId = orderExpId;
    }

    public Integer getOrderCaseResourceId() {
        return this.orderCaseResourceId;
    }
    
    public void setOrderCaseResourceId(Integer orderCaseResourceId) {
        this.orderCaseResourceId = orderCaseResourceId;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
   








}