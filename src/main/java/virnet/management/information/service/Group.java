package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Course;
import virnet.management.entity.Groupmember;
import virnet.management.entity.User;
import virnet.management.util.PageUtil;
import virnet.management.util.UserInfoProcessUtil;

public class Group implements InformationQuery {

	private ClassgroupDAO gDAO = new ClassgroupDAO();
	private GroupmemberDAO mDAO = new GroupmemberDAO();
	private ClassTeacherDAO ctDAO = new ClassTeacherDAO();
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		UserInfoProcessUtil usercheck = new UserInfoProcessUtil();
		int userid = usercheck.checkUsername(user);
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		Map<String, Object> head_id = new HashMap<String, Object>();
		head_id.put("name", "小组编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, Object> head_name = new HashMap<String, Object>();
		head_name.put("name", "小组名称");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, Object> head_member = new HashMap<String, Object>();
		head_member.put("name", "小组成员");
		head_member.put("class", "");
		head.add(head_member);
		
		list.add(head);
		
		//search the class teacher table, to find out the user's class list, and then find out the class group 
		
		String hql = "SELECT model from " + ClassTeacher.class.getName() + " as model where model.classTeacherTeacherId ='" + userid + "'";
		
		List<ClassTeacher> ctlist = new ArrayList<ClassTeacher>();
		ctlist = this.ctDAO.getListByHql(hql);
		
		int size = ctlist.size();
		System.out.println("Class list size : " + size);
		
		List<Object> selectlist = new ArrayList<Object>();
		for(int i = 0; i < size; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			
			int classid = ctlist.get(i).getClassTeacherClassId();
			cmap.put("id", classid);
			
			ClassDAO cDAO = new ClassDAO();
			List<Class> clist = cDAO.getListByProperty("classId", classid);
			int courseid = clist.get(0).getClassCourseId();
			CourseDAO courseDAO = new CourseDAO();
			List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
			
			cmap.put("class", courselist.get(0).getCourseName() + " " + clist.get(0).getClassName());
			selectlist.add(cmap);
		}
		
		
		int s = selectlist.size();
		int classid;
		if(s == 0){
			//list is null
			classid = -1;
		}
		else{
			classid = (int) ((Map<String, Object>) selectlist.get(0)).get("id");
		}
		
		for(int i = 0; i < s; i++){
			if(select.equals(((Map<String, Object>) selectlist.get(i)).get("class"))){
				classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
			}
		}
		
		
		PageUtil<Class> pageUtil = new PageUtil<Class>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		String ghql =  "SELECT model from " + Classgroup.class.getName() + " as model where model.classgroupClassId ='" + classid + "'";
		this.gDAO.getListByPage(ghql, pageUtil);
		
		List<Classgroup> glist = pageUtil.getList();
		
		int l = glist.size();
		for(int i = 0; i < l; i++){
			List<Map<String, Object>> GroupInfo = new ArrayList<Map<String, Object>>();
			
			int gid = glist.get(i).getClassgroupId();
			Map<String, Object> map_id = new HashMap<String, Object>();
			map_id.put("name", gid + "");
			map_id.put("class", "");
			GroupInfo.add(map_id);
			
			Map<String, Object> map_name = new HashMap<String, Object>();
			map_name.put("name", glist.get(i).getClassgroupName());
			map_name.put("class", "");
			//map_name.put("onclick", "showDetail('" + glist.get(i).getClassgroupName() + "', 'group');");
			GroupInfo.add(map_name);
			
			List<Groupmember> mlist = this.mDAO.getListByProperty("classgroupmemberGroupId", gid);
			Map<String, Object> map_member = new HashMap<String, Object>();
			
			int ms = mlist.size();
			List<Object> member = new ArrayList<Object>();
			for(int j = 0; j < ms; j++){
				User u = usercheck.getUser(mlist.get(j).getClassgroupmemberUserId());
				if(u != null){
					Map<String, Object> mapmember = new HashMap<String, Object>();
					mapmember.put("name", u.getUserNickname());
					mapmember.put("class", "btn btn-link");
					mapmember.put("onclick", "showDetail('" + u.getUserNickname() + "', 'user');");
					member.add(mapmember);
				}
			}
			map_member.put("name", member);
			map_member.put("class", "collapse");
			GroupInfo.add(map_member);
			
			list.add(GroupInfo);
		}
		
		int total = this.ctDAO.getListByHql(hql).size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增分组");
		button.put("class", "btn button-new");
		button.put("click", "groupArrange('0')");
		
		map.put("select", selectlist);
		map.put("button_new", button);
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
