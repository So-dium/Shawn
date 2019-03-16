package virnet.management.information.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.OrderCDAO;
import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.combinedao.CourseInfoCDAO;
import virnet.management.combinedao.ExpInfoCDAO;
import virnet.management.combinedao.GroupInfoCDAO;
import virnet.management.combinedao.PhysicsMachinesInfoCDAO;
import virnet.management.combinedao.ScoreCDAO;
import virnet.management.combinedao.TaskInfoCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.combinedao.FacilitiesInfoCDAO;
import virnet.management.entity.Class;

public class InformationService {
	
	private FacilitiesInformationQuery Fquery;
	private InformationQuery query;
	private OrderCDAO oDAO = new OrderCDAO();
	private ExpInfoCDAO eDAO = new ExpInfoCDAO();
	@SuppressWarnings("unused")
	private GroupInfoCDAO gDAO = new GroupInfoCDAO();
	private CourseInfoCDAO cDAO = new CourseInfoCDAO();
	private ClassInfoCDAO classDAO = new ClassInfoCDAO();
	private FacilitiesInfoCDAO fDAO =new FacilitiesInfoCDAO();
	private PhysicsMachinesInfoCDAO pDAO = new PhysicsMachinesInfoCDAO();
	private ScoreCDAO sDAO = new ScoreCDAO();
	private SelfInfo selfInfo = new SelfInfo();

	
	public Map<String, Object> loadInfo(String user, String id, int page, String select){
		Map<String, Object> list = new HashMap<String, Object>();
		
		switch(id){
		case "exp-management": query = new ExpManagement(); break;
		case "course-management": query = new CourseManagement(); break;
		case "class-management": query = new ClassManagement(); break;
		case "time-management": query = new TimeManagement(); break;
		case "exp-staff": query = new ExpStaff(); break;
		case "teacher": query = new Teacher(); break;
		case "student": query = new Student(); break;
		case "class-detail": query = new ClassDetail(); break;
		case "group": query = new Group(); break;
		case "exp-arrangement": query = new ExpArrangement(); break;
		case "my-class": query = new MyClass(); break;
		case "my-group": query = new MyGroup(); break;
		case "my-exp": query = new MyExp(); break;
		case "score" : query = new Score();break;
		case "physicsMachines-management":query =new PhysicsMachinesManagement();break;
//		case "exp-appointment":query = new ExpAppointment(); break;
		default: break;
		}
		
		list = query.query(user, page, select);
		return list;
	}
	
	public Map<String, Object> loadFacilitiesInfo(String user, String id, int page, String select, String physicsMachinesName){
		Map<String, Object> list = new HashMap<String, Object>();
		
		switch(id){
			case "facilities-management":Fquery =new FacilitiesManagement();break;
			default : break;
		}
		list = Fquery.Facilitiesquery(user, page, select, physicsMachinesName);
		return list;
	}

	public Map<String, Object> showDetail(String user, String id, String key, String name, String orderId) throws ParseException{
		
		Map<String, Object> map = new HashMap<String, Object>();

		//Divided by key : user, experiment, course, class, group
		switch(key){
		case "selfInfo" : map = new SelfInfo().showSelfInfoDetail(id,name);break;
		case "exp" : map = this.eDAO.showExpDetail(id, name , orderId);break;
		case "course" : map = this.cDAO.showCourseDetail(id, name); break;
		case "class" : map = this.classDAO.showClassDetail(id, name); break;
		case "group" : break;
		case "facilities" :	 map = this.fDAO.showFacilitiesDetail(id,name);break;
		case "physicsMachines" : map = this.pDAO.showPhysicsMachinesDetail(id, name);break;
		case "score" : map = this.sDAO.showScoreDetail(id, name); break;
		default : break;
		}
	
		return map;
	}

	public Map<String, Object> Edit(String user, String id, String name) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(id);
		switch(id){
		case "exp-management" : map = this.eDAO.Edit(name); break;
		case "course-management" : map = this.cDAO.Edit(name); break;
		case "class-management" : map = this.classDAO.Edit(name); break;
		case "time-management" :
		case "facilities-management" : 	map = this.fDAO.Edit(name); break;
		case "physicsMachines-management" : map = this.pDAO.Edit(name); break;
		case "selfInfo": map = this.selfInfo.Edit(name);break;
		case "changePassword": map = this.selfInfo.changePassword(name);break;
		}
		
		return map;
	}
	
	public Map<String, Object> add(String user, String id){
		Map<String, Object> map = new HashMap<String, Object>();
		
		switch(id){
//		case "exp-appointment": map = this.DAO.Add(id);break;
		case "exp-management" : map = this.eDAO.Add(); break;
		case "course-management" : map = this.cDAO.Add(); break;
		case "class-management" : map = this.classDAO.Add();break;
		case "facilities-management" :map = this.fDAO.Add();break;
		case "physicsMachines-management" :map = this.pDAO.Add();break;
		}
		
		return map;
	}
	
	public Map<String, Object> delete(String user, String id,Map<String,Object> deleteInfo){
		Map<String, Object> map = new HashMap<String, Object>();
		
		switch(id){
		case "task-management": TaskInfoCDAO ticDAO = new TaskInfoCDAO();
								ticDAO.deleteTask(Integer.parseInt((String) deleteInfo.get("expId")), Integer.parseInt((String) deleteInfo.get("expTaskOrder")));
								break;
		case "exp-management": ExpInfoCDAO eicDAO = new ExpInfoCDAO();
								eicDAO.deleteExp((String) deleteInfo.get("expName"));
								break;
		case "physicsMachines-management":PhysicsMachinesInfoCDAO pmicDao = new PhysicsMachinesInfoCDAO();
											pmicDao.deletePhysicsMachine((String) deleteInfo.get("machineName"));
											break;
		}
		
		return map;
	}
	
	public Map<String, Object> submit(String user, String id, String name, Map<String, Object> map){

		Map<String, Object> r = null;
		try{
		switch(id){
//		case "exp-appointment" : r = this.aDAO.save(name, id, map, user); break;
		case "exp-management" : r = this.eDAO.save(name, map); break;
		case "course-management" : r = this.cDAO.save(name, map);break;
		case "facilities-management" :	r = this.fDAO.save(name, map);break;
		case "physicsMachines-management" :	r = this.pDAO.save(name, map);break;
		case "class-management" : r = this.classDAO.save(name, id, map, user);break;
		
		//个人信息使用用户id为主键
		case "selfInfo": r = this.selfInfo.save(user,map);break;
										
		default : r = null;
		
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return r;
		
	}
	
	public Map<String, Object> addtask(String user, String id, String name) throws ParseException{
		Map<String, Object>  map = new HashMap<String, Object>();
		map = this.eDAO.addtask(name);
		return map;
	}
	
	//查询班级列表
	public List<Map<String, Object>> queryClassList() {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		ClassDAO cDAO = new ClassDAO();
		@SuppressWarnings({ "unchecked" })
		List<Class> clist = cDAO.getList();
		for(int i=0;i<clist.size();i++){
			Map<String, Object>  map = new HashMap<String, Object>();
			map.put("classId", clist.get(i).getClassId());
			map.put("className",  clist.get(i).getClassName());
			resultList.add(map);
		}
		return resultList;	
	}
}
