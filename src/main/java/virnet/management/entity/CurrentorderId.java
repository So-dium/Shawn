package virnet.management.entity;



/**
 * CurrentorderId entity. @author MyEclipse Persistence Tools
 */

public class CurrentorderId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 4125414241104880035L;
	private Integer currentorderId;
     private Integer currentorderClassArrangeId;


    // Constructors

    /** default constructor */
    public CurrentorderId() {
    }

    
    /** full constructor */
    public CurrentorderId(Integer currentorderId, Integer currentorderClassArrangeId) {
        this.currentorderId = currentorderId;
        this.currentorderClassArrangeId = currentorderClassArrangeId;
    }

   
    // Property accessors

    public Integer getCurrentorderId() {
        return this.currentorderId;
    }
    
    public void setCurrentorderId(Integer currentorderId) {
        this.currentorderId = currentorderId;
    }

    public Integer getCurrentorderClassArrangeId() {
        return this.currentorderClassArrangeId;
    }
    
    public void setCurrentorderClassArrangeId(Integer currentorderClassArrangeId) {
        this.currentorderClassArrangeId = currentorderClassArrangeId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CurrentorderId) ) return false;
		 CurrentorderId castOther = ( CurrentorderId ) other; 
         
		 return ( (this.getCurrentorderId()==castOther.getCurrentorderId()) || ( this.getCurrentorderId()!=null && castOther.getCurrentorderId()!=null && this.getCurrentorderId().equals(castOther.getCurrentorderId()) ) )
 && ( (this.getCurrentorderClassArrangeId()==castOther.getCurrentorderClassArrangeId()) || ( this.getCurrentorderClassArrangeId()!=null && castOther.getCurrentorderClassArrangeId()!=null && this.getCurrentorderClassArrangeId().equals(castOther.getCurrentorderClassArrangeId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCurrentorderId() == null ? 0 : this.getCurrentorderId().hashCode() );
         result = 37 * result + ( getCurrentorderClassArrangeId() == null ? 0 : this.getCurrentorderClassArrangeId().hashCode() );
         return result;
   }   





}