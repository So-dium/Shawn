package virnet.management.entity;



/**
 * Currentorder entity. @author MyEclipse Persistence Tools
 */

public class Currentorder  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -562797311789489023L;
	private CurrentorderId id;


    // Constructors

    /** default constructor */
    public Currentorder() {
    }

    
    /** full constructor */
    public Currentorder(CurrentorderId id) {
        this.id = id;
    }

   
    // Property accessors

    public CurrentorderId getId() {
        return this.id;
    }
    
    public void setId(CurrentorderId id) {
        this.id = id;
    }
   








}