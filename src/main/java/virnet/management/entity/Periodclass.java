package virnet.management.entity;



/**
 * Periodclass entity. @author MyEclipse Persistence Tools
 */

public class Periodclass  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 5024803799345956422L;
	private PeriodclassId id;


    // Constructors

    /** default constructor */
    public Periodclass() {
    }

    
    /** full constructor */
    public Periodclass(PeriodclassId id) {
        this.id = id;
    }

   
    // Property accessors

    public PeriodclassId getId() {
        return this.id;
    }
    
    public void setId(PeriodclassId id) {
        this.id = id;
    }
   








}