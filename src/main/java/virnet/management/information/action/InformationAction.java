package virnet.management.information.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import virnet.management.information.service.InformationService;

import com.opensymphony.xwork2.ActionSupport;

public class InformationAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2480610387419106153L;

	private InformationService infoService = new InformationService();
	private HttpServletRequest request;
	
	private Map<String, Object> results = new HashMap<String, Object>();
	private Map<String, Object> detail = new HashMap<String, Object>();
	private Map<String, Object> edit = new HashMap<String, Object>();
	private Map<String, Object> submit = new HashMap<String, Object>();
	private Map<String, Object> addTask = new HashMap<String, Object>();
	private Map<String, Object> add = new HashMap<String, Object>();
	private Map<String, Object> delete = new HashMap<String, Object>();
	private List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
	
	public String loadInfo(){
		//question remaining : user power is required or not when query object details?
		//A user with two or more role may want to see more information about the object
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String page = this.request.getParameter("page");
		String select = this.request.getParameter("select");
		
		System.out.println("user : " + user + ", id : " + id);
		
		this.results = this.infoService.loadInfo(user, id, Integer.parseInt(page), select);
		
		return SUCCESS;
	}
	
	public String loadFacilitiesInfo(){

		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String page = this.request.getParameter("page");
		String select = this.request.getParameter("select");
		String physicsMachinesName =this.request.getParameter("physicsMachinesName");
		
		this.results = this.infoService.loadFacilitiesInfo(user, id, Integer.parseInt(page), select, physicsMachinesName);
		
		return SUCCESS;
	}
	
	/*
	 * 
	 * @return
	 * detail
	 */
	public String showDetail() throws ParseException{
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String key = this.request.getParameter("key");
		String name = this.request.getParameter("name");
		String orderId = this.request.getParameter("orderId");
		
		System.out.println("user : " + user + ", id : " + id + ", key : " + key + ", name : " + name + " orderId : " + orderId);
		
		//user check
		
		//Divide by key : user, class, course, experiment, group
		
		this.setDetail(this.infoService.showDetail(user, id, key, name , orderId));

		return SUCCESS;
	}
	
	public String edit() throws ParseException{
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String name = this.request.getParameter("name");
		
		this.setEdit(this.infoService.Edit(user, id, name));
		
		return SUCCESS;
	}
	
	public String submit(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String name = this.request.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, String[]> data = this.request.getParameterMap();
		
		Set<String> k = data.keySet();
		Iterator<String> i = k.iterator();
		while(i.hasNext()){
			String s = i.next();
			if(s.contains("data[")){
				if(s.contains("[]")){				
					map.put(s.substring(5, s.indexOf("[]") - 1), data.get(s));
				}
				else{
					String key = s.substring(5, s.length() - 1);
					map.put(key, data.get(s)[0]);
				}	
			}
		}
		
		Map<String, Object> key = this.infoService.submit(user, id, name, map);
		
		if(key != null && (boolean) key.get("isSuccess")){
			key.put("returndata", "更新成功！");
			key.put("key", key.get("key"));
			key.put("name", key.get("name"));	
		}
		
		this.setSubmit(key);
		
		return SUCCESS;
	}
	
	public String addtask() throws ParseException{
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String name = this.request.getParameter("name");
		
		this.setAddTask(this.infoService.addtask(user, id,name));
		
		return SUCCESS;
	}
	

	public String add(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
	
		this.setAdd(this.infoService.add(user, id));
		
		return SUCCESS;
	}
	
	public String delete(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String info = this.request.getParameter("data");
		Map<String,Object> deleteInfo = new HashMap<String,Object>();
		
		//map以字符串的形式传过来 ，  形式为     key1@@value1##key2@@value2
		String[] entrySet= info.split("##");
		for(String entry : entrySet){
			String[] key_value =   entry.split("@@");
			deleteInfo.put(key_value[0], key_value[1]);
		}
		
		
		this.setDelete(this.infoService.delete(user, id,deleteInfo));
		
		delete.put("data", "删除成功");
		return SUCCESS;
	}
	
	//返回班级列表
	public String queryClassList(){
		this.setClassList(this.infoService.queryClassList());
		return SUCCESS;
	}
	
	public InformationService getInformationService(){
		return this.infoService;
	}
	
	public void setInformationService(InformationService info){
		this.infoService = info;
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
	
	public void setDetail(Map<String, Object> list){
		this.detail = list;
	}
	
	public Map<String, Object> getDetail(){
		return this.detail;
	}
	
	public void setEdit(Map<String, Object> list){
		this.edit = list;
	}
	
	public Map<String, Object> getEdit(){
		return this.edit;
	}
	
	public void setSubmit(Map<String, Object> s){
		this.submit = s;
	}
	public Map<String, Object> getSubmit(){
		return this.submit;
	}
	
	public Map<String, Object> getAddTask(){
		return this.addTask;
	}
	
	public void setAddTask(Map<String, Object> s){
		this.addTask = s;
	}
	
	public void setAdd(Map<String, Object> s){
		this.add = s;
	}
	
	public Map<String, Object> getAdd(){
		return this.add;
	}
	
	public Map<String, Object> getDelete() {
		return delete;
	}

	public void setDelete(Map<String, Object> delete) {
		this.delete = delete;
	}

	public List<Map<String, Object>> getClassList() {
		return classList;
	}

	public void setClassList(List<Map<String, Object>> classList) {
		this.classList = classList;
	}
}
