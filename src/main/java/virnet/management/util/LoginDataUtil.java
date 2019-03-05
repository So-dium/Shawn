package virnet.management.util;

public class LoginDataUtil {
	private String powerinfo = new String();
	private String classname = new String();
	private String idname = new String();
	
	public String getPowerinfo(){
		return this.powerinfo;
	}
	
	public void setPowerinfo(String powerinfo){
		this.powerinfo = powerinfo;
	}
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String classname){
		this.classname = classname;
	}
	
	public String getIdname(){
		return this.idname;
	}
	
	public void setIdname(int id){
		String idname = new String();
		switch(id){
		case 0: idname="exp-management";break;
		case 1: idname="course-management";break;
		case 2: idname="class-management";break;
		case 3: idname="time-management";break;
		case 4: idname="exp-staff";break;
		case 5: idname="teacher";break;
		case 6: idname="student";break;
		case 7: idname="class-detail";break;
		case 8: idname="group";break;
		case 9: idname="exp-arrangement";break;
		case 10: idname="my-class";break;
		case 11: idname="my-group";break;
		case 12: idname="my-exp";break;
		case 13: idname="selfInfo";break;
		case 14: idname="physicsMachines-management";break;
		//case 15: idname="exp-appointment";break;
		case 16: idname="exp-monitor";break;
		case 17: idname="score";break;
		default: idname="error"; break;
		}
		this.idname = idname;
	}
}
