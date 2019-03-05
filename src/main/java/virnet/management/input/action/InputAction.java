package virnet.management.input.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import virnet.management.input.service.InputService;
import com.opensymphony.xwork2.ActionSupport;


public class InputAction extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = -4201404041429996118L;
	
	private InputService inputService = new InputService();
	private HttpServletRequest request;
	
	private Map<String, Object> results = new HashMap<String, Object>();
	private Map<String, Object> submit = new HashMap<String, Object>();
	
	public String InputInfo(){
	
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		
		System.out.println(user + " " +id);
		
		switch(id){
			case"student":this.setResults(this.inputService.inputStuInfo(user, id));break;
			case"teacher":this.setResults(this.inputService.inputTeaInfo(user, id));break;
			case"exp-staff":this.setResults(this.inputService.inputExpStaffInfo(user, id));break;
		}
		
		return SUCCESS;
	}
	
	public String submitInfo(){
		
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		Integer classId = Integer.parseInt(this.request.getParameter("classId"));
		String infomation = this.request.getParameter("infomation");
		
		System.out.println(user + " " +id+" "+classId);
		
		switch(id){
			case"student":this.setSubmit(this.inputService.submitStuInfo(user, id, classId, infomation));break;
			case"teacher":this.setSubmit(this.inputService.submitTeaInfo(user, id, classId, infomation));break;
			case"exp-staff":this.setSubmit(this.inputService.submitExpStaffInfo(user, id, infomation));break;
		}
		
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
		
	}
	public void setResults(Map<String, Object> list){
		this.results = list;
	}

	public Map<String, Object> getResults() {
		return results;
	}
	
	public void setSubmit(Map<String, Object> list){
		this.submit = list;
	}

	public Map<String, Object> getSubmit() {
		return submit;
	}
}
