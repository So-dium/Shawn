package virnet.management.util;

import java.util.ArrayList;
import java.util.List;

import virnet.management.entity.Power;

public class LoginReturnData {
	private int state;
	private String statement;
	private List<Power> powerlist = new ArrayList<Power>();
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int s){
		this.state = s;
	}
	
	public String getStatement(){
		return this.statement;
	}
	
	public void setStatement(String s){
		this.statement = new String(s);
	}
	
	public List<Power> getPowerlist(){
		return this.powerlist;
	}
	
	public void setPowerlist(List<Power> power){
		this.powerlist.clear();
		
		this.powerlist.addAll(power);
	}
}
