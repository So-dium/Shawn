package virnet.management.entity;

import java.util.Date;


/**
 * Period entity. @author MyEclipse Persistence Tools
 */

public class Period  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -3401880149749997861L;
	private Integer periodId;
     private Integer periodWeekId;
     private Integer periodarrangeStartId;
     private Integer periodarrangeEndId;
     private Integer periodClassId;
     private Integer periodCabinetNum;
     private Date periodDate;
     private Integer periodSetUpUserId;


    // Constructors

    /** default constructor */
    public Period() {
    }

	/** minimal constructor */
    public Period(Integer periodId, Integer periodWeekId, Integer periodarrangeStartId, Integer periodarrangeEndId, Integer periodClassId, Integer periodCabinetNum) {
        this.periodId = periodId;
        this.periodWeekId = periodWeekId;
        this.periodarrangeStartId = periodarrangeStartId;
        this.periodarrangeEndId = periodarrangeEndId;
        this.periodClassId = periodClassId;
        this.periodCabinetNum = periodCabinetNum;
    }
    
    /** full constructor */
    public Period(Integer periodId, Integer periodWeekId, Integer periodarrangeStartId, Integer periodarrangeEndId, Integer periodClassId, Integer periodCabinetNum, Date periodDate, Integer periodSetUpUserId) {
        this.periodId = periodId;
        this.periodWeekId = periodWeekId;
        this.periodarrangeStartId = periodarrangeStartId;
        this.periodarrangeEndId = periodarrangeEndId;
        this.periodClassId = periodClassId;
        this.periodCabinetNum = periodCabinetNum;
        this.periodDate = periodDate;
        this.periodSetUpUserId = periodSetUpUserId;
    }

   
    // Property accessors

    public Integer getPeriodId() {
        return this.periodId;
    }
    
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getPeriodWeekId() {
        return this.periodWeekId;
    }
    
    public void setPeriodWeekId(Integer periodWeekId) {
        this.periodWeekId = periodWeekId;
    }

    public Integer getPeriodarrangeStartId() {
        return this.periodarrangeStartId;
    }
    
    public void setPeriodarrangeStartId(Integer periodarrangeStartId) {
        this.periodarrangeStartId = periodarrangeStartId;
    }

    public Integer getPeriodarrangeEndId() {
        return this.periodarrangeEndId;
    }
    
    public void setPeriodarrangeEndId(Integer periodarrangeEndId) {
        this.periodarrangeEndId = periodarrangeEndId;
    }

    public Integer getPeriodClassId() {
        return this.periodClassId;
    }
    
    public void setPeriodClassId(Integer periodClassId) {
        this.periodClassId = periodClassId;
    }

    public Integer getPeriodCabinetNum() {
        return this.periodCabinetNum;
    }
    
    public void setPeriodCabinetNum(Integer periodCabinetNum) {
        this.periodCabinetNum = periodCabinetNum;
    }

    public Date getPeriodDate() {
        return this.periodDate;
    }
    
    public void setPeriodDate(Date periodDate) {
        this.periodDate = periodDate;
    }

    public Integer getPeriodSetUpUserId() {
        return this.periodSetUpUserId;
    }
    
    public void setPeriodSetUpUserId(Integer periodSetUpUserId) {
        this.periodSetUpUserId = periodSetUpUserId;
    }
   








}