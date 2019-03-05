package virnet.management.entity;



/**
 * PeriodclassId entity. @author MyEclipse Persistence Tools
 */

public class PeriodclassId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 5957193099652457474L;
	private Integer periodclassId;
     private String periodclassName;
     private Integer periodclassPeriodId;
     private Integer periodclassarrangeStartId;
     private Integer periodclassarrangeEndId;
     private Integer periodclassClassId;


    // Constructors

    /** default constructor */
    public PeriodclassId() {
    }

	/** minimal constructor */
    public PeriodclassId(Integer periodclassId) {
        this.periodclassId = periodclassId;
    }
    
    /** full constructor */
    public PeriodclassId(Integer periodclassId, String periodclassName, Integer periodclassPeriodId, Integer periodclassarrangeStartId, Integer periodclassarrangeEndId, Integer periodclassClassId) {
        this.periodclassId = periodclassId;
        this.periodclassName = periodclassName;
        this.periodclassPeriodId = periodclassPeriodId;
        this.periodclassarrangeStartId = periodclassarrangeStartId;
        this.periodclassarrangeEndId = periodclassarrangeEndId;
        this.periodclassClassId = periodclassClassId;
    }

   
    // Property accessors

    public Integer getPeriodclassId() {
        return this.periodclassId;
    }
    
    public void setPeriodclassId(Integer periodclassId) {
        this.periodclassId = periodclassId;
    }

    public String getPeriodclassName() {
        return this.periodclassName;
    }
    
    public void setPeriodclassName(String periodclassName) {
        this.periodclassName = periodclassName;
    }

    public Integer getPeriodclassPeriodId() {
        return this.periodclassPeriodId;
    }
    
    public void setPeriodclassPeriodId(Integer periodclassPeriodId) {
        this.periodclassPeriodId = periodclassPeriodId;
    }

    public Integer getPeriodclassarrangeStartId() {
        return this.periodclassarrangeStartId;
    }
    
    public void setPeriodclassarrangeStartId(Integer periodclassarrangeStartId) {
        this.periodclassarrangeStartId = periodclassarrangeStartId;
    }

    public Integer getPeriodclassarrangeEndId() {
        return this.periodclassarrangeEndId;
    }
    
    public void setPeriodclassarrangeEndId(Integer periodclassarrangeEndId) {
        this.periodclassarrangeEndId = periodclassarrangeEndId;
    }

    public Integer getPeriodclassClassId() {
        return this.periodclassClassId;
    }
    
    public void setPeriodclassClassId(Integer periodclassClassId) {
        this.periodclassClassId = periodclassClassId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof PeriodclassId) ) return false;
		 PeriodclassId castOther = ( PeriodclassId ) other; 
         
		 return ( (this.getPeriodclassId()==castOther.getPeriodclassId()) || ( this.getPeriodclassId()!=null && castOther.getPeriodclassId()!=null && this.getPeriodclassId().equals(castOther.getPeriodclassId()) ) )
 && ( (this.getPeriodclassName()==castOther.getPeriodclassName()) || ( this.getPeriodclassName()!=null && castOther.getPeriodclassName()!=null && this.getPeriodclassName().equals(castOther.getPeriodclassName()) ) )
 && ( (this.getPeriodclassPeriodId()==castOther.getPeriodclassPeriodId()) || ( this.getPeriodclassPeriodId()!=null && castOther.getPeriodclassPeriodId()!=null && this.getPeriodclassPeriodId().equals(castOther.getPeriodclassPeriodId()) ) )
 && ( (this.getPeriodclassarrangeStartId()==castOther.getPeriodclassarrangeStartId()) || ( this.getPeriodclassarrangeStartId()!=null && castOther.getPeriodclassarrangeStartId()!=null && this.getPeriodclassarrangeStartId().equals(castOther.getPeriodclassarrangeStartId()) ) )
 && ( (this.getPeriodclassarrangeEndId()==castOther.getPeriodclassarrangeEndId()) || ( this.getPeriodclassarrangeEndId()!=null && castOther.getPeriodclassarrangeEndId()!=null && this.getPeriodclassarrangeEndId().equals(castOther.getPeriodclassarrangeEndId()) ) )
 && ( (this.getPeriodclassClassId()==castOther.getPeriodclassClassId()) || ( this.getPeriodclassClassId()!=null && castOther.getPeriodclassClassId()!=null && this.getPeriodclassClassId().equals(castOther.getPeriodclassClassId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getPeriodclassId() == null ? 0 : this.getPeriodclassId().hashCode() );
         result = 37 * result + ( getPeriodclassName() == null ? 0 : this.getPeriodclassName().hashCode() );
         result = 37 * result + ( getPeriodclassPeriodId() == null ? 0 : this.getPeriodclassPeriodId().hashCode() );
         result = 37 * result + ( getPeriodclassarrangeStartId() == null ? 0 : this.getPeriodclassarrangeStartId().hashCode() );
         result = 37 * result + ( getPeriodclassarrangeEndId() == null ? 0 : this.getPeriodclassarrangeEndId().hashCode() );
         result = 37 * result + ( getPeriodclassClassId() == null ? 0 : this.getPeriodclassClassId().hashCode() );
         return result;
   }   





}