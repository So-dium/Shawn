package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.CourseexpDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.entity.Class;
import virnet.management.entity.Course;
import virnet.management.entity.Courseexp;
import virnet.management.entity.Exp;
import virnet.management.util.ViewUtil;

public class CourseInfoCDAO {
	private static String EditTittle = "课程信息";
	
	private CourseDAO cDAO = new CourseDAO();
	private CourseexpDAO ceDAO = new CourseexpDAO();
	private ClassDAO classDAO = new ClassDAO();
	private ExpInfoCDAO eDAO = new ExpInfoCDAO();
	private ClassInfoCDAO ciDAO = new ClassInfoCDAO();
	
	private ViewUtil vutil = new ViewUtil();
	
	public Map<String, Object> showCourseDetail(String id, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "课程信息 <i class='icon-double-angle-right'></i> " + name);		
		
		switch(id){
		case "exp-management" : 
		case "time-management" :
		case "class-management" : 
		case "course-management" : list = courseManagement(name);
								   Map<String, Object> button = new HashMap<String, Object>();
								   button.put("content", "修改课程信息");
								   button.put("class", "btn button-new");
								   button.put("click", "editContent();");
								   map.put("button", button);
								   break;
		}
		
		map.put("data", list);
		map.put("tittle", tittle);
		
		return map;
	}
	
	private List<List<Map<String, Object>>> courseManagement(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Course c = this.queryByName(name);
		if(c != null){//course name, experiments, teachers, classes
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			Map<String, Object> map12 = new HashMap<String, Object>();
			map11.put("name", "课程名称");
			map12.put("name", name);
			list1.add(map11);
			list1.add(map12);
			list.add(list1);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			Map<String, Object> map22 = new HashMap<String, Object>();
			map21.put("name", "课程实验");
			map22.put("name", this.getCourseExp(c.getCourseId()));
			map22.put("class", "collapse");
			list2.add(map21);
			list2.add(map22);
			list.add(list2);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			Map<String, Object> map32 = new HashMap<String, Object>();
			map31.put("name", "下属班级");
			map32.put("name", this.getCourseClass(c.getCourseId()));
			map32.put("class", "collapse");
			list3.add(map31);
			list3.add(map32);
			list.add(list3);		
			
		}
		
		return list;
	}
	
	public Map<String, Object> Edit(String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> " + name);
		
		List<Map<String, Object>> cname = this.vutil.createList("课程名称", "", "", name, "btn btn-link edit", "editable(this);", "courseName",0);
		int courseid = this.queryByName(name).getCourseId();
		
		List<Exp> elist = this.eDAO.getAllExp();
		List<Map<String, Object>> explist = new ArrayList<Map<String, Object>>();
		int es = elist.size();
		for(int i = 0; i < es; i++){
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("name", elist.get(i).getExpName());
			emap.put("value", elist.get(i).getExpId());
			
			if(this.IsCourseExp(courseid, elist.get(i).getExpId())){
				emap.put("selected", "selected");
			}
			
			explist.add(emap);
		}
		
		List<Map<String, Object>> cexp = this.vutil.createList("课程实验", "", "", explist, "mutiselect", "", "Courseexp",0);
		
		list.add(cname);
		list.add(cexp);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	
	public Map<String, Object> Add(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> 新增课程");
		
		List<Map<String, Object>> cname = this.vutil.createList("课程名称", "", "", "", "btn btn-link edit", "editable(this);", "courseName",0);
		
		List<Exp> elist = this.eDAO.getAllExp();
		List<Map<String, Object>> explist = new ArrayList<Map<String, Object>>();
		int es = elist.size();
		for(int i = 0; i < es; i++){
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("name", elist.get(i).getExpName());
			emap.put("value", elist.get(i).getExpId());
			
			explist.add(emap);
		}
		
		List<Map<String, Object>> cexp = this.vutil.createList("课程实验", "", "", explist, "mutiselect", "", "Courseexp",0);
		
		list.add(cname);
		list.add(cexp);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存课程");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	
	public Map<String, Object> save(String name, Map<String, Object> map){
		Map<String, Object> r = new HashMap<String, Object>();
		
		Course c;
		String courseName = (String) map.get("courseName");
		if(name.equals("")){
			
			//课程名称不能重复
			Course  duplicate = (Course)this.cDAO.getUniqueByProperty("courseName", courseName);
			if(duplicate != null){
				r.put("isSuccess", false);
				r.put("returndata", "课程名已存在");
				return r;
			}
			
			c = new Course();
		}
		else{
			//课程名称不能重复
			Course  duplicate = (Course)this.cDAO.getUniqueByProperty("courseName", courseName);
			if(duplicate != null && !courseName.equals(name)){
				r.put("isSuccess", false);
				r.put("returndata", "课程名已存在");
				return r;
			}
			c = (Course) this.cDAO.getUniqueByProperty("courseName", name);
		}
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		
		String courseexplist = new String();
		String coursename = new String();
		
		while(keylist.hasNext()){
			String k = keylist.next();
			switch(k){
			case "courseName" : c.setCourseName((String) map.get(k)); coursename = (String) map.get(k); break;
			case "Courseexp" : courseexplist = k;  break;
			}
		}
		System.out.println(map);
		
		if(name.equals("")){
			if(this.cDAO.add(c)){
				c = (Course) this.cDAO.getUniqueByProperty("courseName", coursename);
				
				if(this.updateCourseExp(c.getCourseId(), (String[]) map.get(courseexplist))){
					r.put("isSuccess", true);
					r.put("name", coursename);
					r.put("key", "course");
				}
			}
			else{
				r.put("isSuccess", false);
				r.put("returndata","新增课程失败");
			}
		}
		else{
			if(this.cDAO.update(c) && this.updateCourseExp(c.getCourseId(), (String[]) map.get(courseexplist))){
				r.put("isSuccess", true);
				r.put("name", map.get("courseName"));
				r.put("key", "course");
			}
			else{
				r.put("isSuccess", false);
				r.put("returndata","课程信息更新失败");
			}
		}
		
		return r;
	}
	
	public boolean updateCourseExp(int courseid, String[] expNameList){
		List<Map<String, Object>> map = this.getCourseExp(courseid);
		
		System.out.println(expNameList);
		int s = map.size();
		int l = expNameList.length;
		
		boolean[] sn = new boolean[s];
		boolean[] ln = new boolean[l];
		
		for(int j = 0; j < l; j++){
			ln[j] = false;
		}
		
		for(int i = 0; i < s; i++){
			sn[i] = false;
			for(int j = 0; j < l; j++){
				if(map.get(i).get("name").equals(expNameList[j])){//remain
					sn[i] = true;
					ln[j] = true;
					break;
				}
			}
		}
		
		//delete array
		List<Courseexp> dlist = new ArrayList<Courseexp>();
		for(int i = 0; i < s; i++){
			if(!sn[i]){
				int expid = this.eDAO.getExp((String) map.get(i).get("name")).getExpId();
				dlist.add((Courseexp) this.ceDAO.getByNProperty("courseexpCourseId", courseid + "", "courseexpExpId", expid + ""));
			}
		}
		
		List<Courseexp> ilist = new ArrayList<Courseexp>();
		for(int j = 0; j < l; j++){
			if(!ln[j]){
				Courseexp ce = new Courseexp();
				int expid = this.eDAO.getExp((String) expNameList[j]).getExpId();
				ce.setCourseexpCourseId(courseid);
				ce.setCourseexpExpId(expid);
				ilist.add(ce);
			}
		}
		
		return this.ceDAO.update(dlist, ilist);
	}
	
	@SuppressWarnings("unchecked")
	public Course queryByName(String name){
		Course c = new Course();
		
		List<Course> clist = this.cDAO.getListByProperty("courseName", name);
		
		if(clist.isEmpty() || clist.size() > 1){
			return null;
		}
		else{
			c = clist.get(0);
		}
		
		return c;
	}

	public Course getCourse(int id){		
		Course c = (Course) this.cDAO.getUniqueByProperty("courseId", id);
		
		return c;
	}
	
	/**
	 * 根据课程编号得到课程实验
	 * @param id course id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCourseExp(int id){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<Courseexp> celist = this.ceDAO.getListByProperty("courseexpCourseId", id);
		
		int size = celist.size();
		for(int i = 0; i < size; i++){
			Map<String, Object> map  = new HashMap<String, Object>();
			map.put("name", this.eDAO.getExpName(celist.get(i).getCourseexpExpId()));
			map.put("onclick", "showDetail('" + this.eDAO.getExpName(celist.get(i).getCourseexpExpId()) + "', 'exp');");
			map.put("class", "btn btn-link");
			
			list.add(map);
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCourseClass(int id){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<Class> clist = this.classDAO.getListByProperty("classCourseId", id);
			
		int size = clist.size();
		for(int i = 0; i < size; i++){
			Map<String, Object> map  = new HashMap<String, Object>();
			map.put("name", this.ciDAO.getClassName(clist.get(i).getClassId()));
			map.put("onclick", "showDetail('" + this.ciDAO.getClassName(clist.get(i).getClassId()) + "', 'class');");
			map.put("class", "btn btn-link");
			
			list.add(map);
		}
		
		return list;		
	}

	public boolean IsCourseExp(int courseid, int expid){	
		if(this.ceDAO.getByNProperty("courseexpCourseId", courseid + "", "courseexpExpId", expid + "") == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * 根据课程编号得到课程实验但不提供链接
	 * @param id course id
	 * @return
	 */
	public List<Exp> getCourseExpName(int id){
		List<Exp> list = new ArrayList<Exp>();
		
		@SuppressWarnings("unchecked")
		List<Courseexp> celist = this.ceDAO.getListByProperty("courseexpCourseId", id);
		
		int size = celist.size();
		for(int i = 0; i < size; i++){
			ExpDAO expDAO = new ExpDAO();
			Exp e = (Exp)expDAO.getUniqueByProperty("expId", celist.get(i).getCourseexpExpId());
			list.add(e);
		}
		return list;
	}
}
