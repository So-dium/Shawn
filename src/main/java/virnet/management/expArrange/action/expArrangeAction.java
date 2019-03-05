package virnet.management.expArrange.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;


import com.opensymphony.xwork2.ActionSupport;

import virnet.management.expArrange.service.expArrangeService;
public class expArrangeAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 520008794051754685L;
	
	
	private expArrangeService expArrangeService = new expArrangeService();
	
	private HttpServletRequest request;
	
	private Map<String, Object> results = new HashMap<String, Object>();
	private Map<String, Object> info = new HashMap<String, Object>();
	
	public String getExplist(){
		String classInfo = this.request.getParameter("classinfo");
		System.out.println("!!get class info:"+classInfo);
		
		this.results = this.expArrangeService.getExplist(classInfo);
		
		System.out.println("!!get result:hhh");
		return SUCCESS;
	}
	public String perriodOccupy() {
		String classInfo = this.request.getParameter("classinfo");
		String date = this.request.getParameter("date");
		int classnum = Integer.parseInt(classInfo);
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = new Date();
		try {
			date1 = fmt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.results = this.expArrangeService.perriodOccupy(classnum, date1);
		return SUCCESS;
	}
	public String showTimeSpan() {
		System.out.println("Action showTimeSpan:");
		String classInfo = this.request.getParameter("classinfo");
		String chooseDateStr = this.request.getParameter("date");
		System.out.println("classinfo:"+classInfo+"chooseDate:"+chooseDateStr);
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		Date chooseDate = new Date();
		try {
			chooseDate = fmt.parse(chooseDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.results = this.expArrangeService.showTimeSpan(classInfo, chooseDate);
		return SUCCESS;
	}
	public String newARG() throws ParseException {
		Map<String, String[]> paraMap = this.request.getParameterMap();
		System.out.println("ParameterMap:"+paraMap);
		this.results = this.expArrangeService.newARG(paraMap);
		return SUCCESS;
	}
	public String newARG_2() throws ParseException {
		Map<String, String[]> paraMap = this.request.getParameterMap();
		System.out.println("ParameterMap:"+paraMap);
		this.results = this.expArrangeService.newARG_2(paraMap);
		return SUCCESS;
	}
	
	public String expDateARG(){
		System.out.println("expDateARG:");
		String classInfo = this.request.getParameter("classinfo");
		this.results = this.expArrangeService.expDateARG(classInfo);
		return SUCCESS;
	}
	
	public String editARG() throws ParseException {
		Map<String, String[]> paraMap = this.request.getParameterMap();
		System.out.println("ParameterMap:"+paraMap);
		this.results =  this.expArrangeService.editARG(paraMap);
		return SUCCESS;
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }
	
	public void setResults(Map<String, Object> list){
		this.results = list;
	}
	
	public Map<String, Object> getResults(){
		return this.results;
	}
	
	public void setInfo(Map<String, Object> list){
		this.info = list;
	}
	
	public Map<String, Object> getInfo(){
		return this.info;
	}


	public expArrangeService getExpArrangeService() {
		return expArrangeService;
	}


	public void setExpArrangeService(expArrangeService expArrangeService) {
		this.expArrangeService = expArrangeService;
	}
}