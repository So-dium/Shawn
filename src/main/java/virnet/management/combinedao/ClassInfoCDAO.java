package virnet.management.combinedao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.management.dao.CaseDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.dao.UserDAO;
import virnet.management.entity.Appointment;
import virnet.management.entity.Case;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.entity.Groupmember;
import virnet.management.entity.User;
import virnet.management.util.ViewUtil;

public class ClassInfoCDAO {
	private static String EditTittle = "班级信息";
	
	private ClassDAO cDAO = new ClassDAO();
	private ClassTeacherDAO ctDAO = new ClassTeacherDAO();
	private GroupInfoCDAO gDAO = new GroupInfoCDAO();
	private UserInfoCDAO uDAO = new UserInfoCDAO();
	private CourseDAO courseDAO = new CourseDAO();
	private UserDAO userDAO = new UserDAO();
	
	private ViewUtil vutil = new ViewUtil();
	
	public Map<String, Object> Add(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> 新增班级");
		
		List<Map<String, Object>> classname = this.vutil.createList("班级名称", "", "", "", "btn btn-link edit", "editable(this);", "ClassInfoName",0);
		list.add(classname);
		
		List<Course> clist = this.courseDAO.getList();
		List<Map<String, Object>> courselist = new ArrayList<Map<String, Object>>();
		int cs = clist.size();
		for(int i = 0; i < cs; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			cmap.put("name", clist.get(i).getCourseName());
			cmap.put("value", clist.get(i).getCourseId());
			
			courselist.add(cmap);
		}
		
		List<Map<String, Object>> belongs = this.vutil.createList("所属课程", "", "", courselist, "singleselect", "", "BelongsToCourseID",0);
		
		List<Object> tlist = new ArrayList<Object>();
		String hql = "select t1.userId,t1.userNickname "
				+ "from User as t1, UserCharacter as t2 "
				+ "where t1.userId = t2.userCharacterUserId and t2.userCharacterCharacterId = 2";
		tlist = userDAO.getListByHql(hql);
		int size = tlist.size();
		
		List<Map<String, Object>> teacherslist = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < size; i++){
			Object obj = tlist.get(i);
			Object[] teacher = (Object[])obj;
			Map<String, Object> teachersmap = new HashMap<String, Object>();
			teachersmap.put("name", teacher[1]);
			teachersmap.put("value", teacher[0]);
			
			teacherslist.add(teachersmap);
		}
		
		List<Map<String, Object>> teacher = this.vutil.createList("班级老师", "", "", teacherslist, "singleselect", "", "TeacherInfoID",0);
		
		list.add(belongs);
		list.add(teacher);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存班级信息");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	public Map<String, Object> save(String name, String id,Map<String, Object> map,String user){
		Map<String, Object> r = new HashMap<String, Object>();//判断更新或新建是否成功的域

			Class c;
			Integer UserID = 0;
			String ClassName = "";
			String CourseName = "";
			ClassName = (String)map.get("ClassInfoName");
			if(name.equals("")){
				
				Class  duplicate = (Class)this.cDAO.getUniqueByProperty("className", ClassName);
				if(duplicate != null){
					r.put("isSuccess", false);
					r.put("returndata", "班级名已存在");
					return r;
				}
				c = new Class();
			}else{
				Class  duplicate = (Class)this.cDAO.getUniqueByProperty("className", ClassName);
				if(duplicate != null && !ClassName.equals(name.split(" ")[1])){
					r.put("isSuccess", false);
					r.put("returndata", "班级名已存在");
					return r;
				}
				c = (Class) this.cDAO.getUniqueByProperty("className", name.split(" ")[1]);
			}

			Set<String> key = map.keySet();
			Iterator<String> keylist0 = key.iterator();
			System.out.println(map);			
				while(keylist0.hasNext()){
					String k = keylist0.next();
					System.out.println(k);
					switch(k){
					case "ClassInfoName" :ClassName = (String)(map.get(k));
							  			c.setClassName(ClassName);
							  			break;
					case "BelongsToCourseID" :
						
						Course singlecourse = new Course();
						singlecourse = (Course)courseDAO.getUniqueByProperty("courseName", ((String[])map.get(k))[0]);
						CourseName = singlecourse.getCourseName();
						Integer CourseID = singlecourse.getCourseId();
						c.setClassCourseId(CourseID);

						break;
					case "TeacherInfoID" :
						System.out.println(((String[])map.get(k))[0]);
						User singleuser = new User();
						singleuser = (User)userDAO.getUniqueByProperty("userNickname", ((String[])map.get(k))[0]);
						UserID = singleuser.getUserId();
					}
				}


			if(name.equals("")){

					if(this.cDAO.add(c)){	
						ClassTeacher ct = new ClassTeacher();
						c = (Class)cDAO.getUniqueByProperty("className", ClassName);
						ct.setClassTeacherClassId(c.getClassId());
						ct.setClassTeacherTeacherId(UserID);
						ctDAO.add(ct);

						r.put("isSuccess", true);	
						r.put("name", CourseName + " " + ClassName);
						r.put("key", "class");
					}
					else{
						r.put("isSuccess", false);
						r.put("returndata", "新增班级失败");
					}

			}
			else{
				if(this.cDAO.update(c)){
					r.put("isSuccess", true);
					r.put("name", getClassName(c.getClassId()));
					r.put("key", "class");
				}
				else{
					r.put("isSuccess", false);
					r.put("returndata", "更新班级失败");
				}
			}
			
			

		return r;
	
}
	public Map<String, Object> showClassDetail(String id, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "班级信息 <i class='icon-double-angle-right'></i> " + name);		
		
		list = this.classManagement(name);
	    Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "修改班级信息");
		button.put("class", "btn button-new");
		button.put("click", "editContent();");
		map.put("button", button);
		
		map.put("data", list);
		map.put("tittle", tittle);
		
		return map;
	}
	
	private List<List<Map<String, Object>>> classManagement(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Class c = this.getClassbyname(name);
		
		if(c != null){
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			Map<String, Object> map12 = new HashMap<String, Object>();
			map11.put("name", "班级名称");
			map12.put("name", name);
			list1.add(map11);
			list1.add(map12);
			list.add(list1);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			Map<String, Object> map22 = new HashMap<String, Object>();
			map21.put("name", "所属课程");
			map22.put("name", name.split(" ")[0]);
			map22.put("onclick", "showDetail('" + name.split(" ")[0] + "' , 'course');");
			map22.put("class", "btn btn-link");
			list2.add(map21);
			list2.add(map22);
			list.add(list2);
			
			List<User> ulist = this.getClassTeacher(c.getClassId());
			List<Object> teacherlist = new ArrayList<Object>();
			int usize = ulist.size();
			for(int i = 0; i < usize; i++){
				Map<String, Object> umap = new HashMap<String, Object>();
				
				umap.put("name", ulist.get(i).getUserNickname());
				umap.put("onclick", "showDetail('" +  ulist.get(i).getUserNickname() + "' , 'user');");
				umap.put("class",  "btn btn-link");
				
				teacherlist.add(umap);
			}
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			Map<String, Object> map32 = new HashMap<String, Object>();
			map31.put("name", "班级教师");
			map32.put("name", teacherlist);
			map32.put("class", "collapse");
			list3.add(map31);
			list3.add(map32);
			list.add(list3);
		}
		
		return list;
	}
	
	/**
	 * 根据班级名称得到教师所教班级信息
	 * @param name class name
	 * @return
	 */
	public List<List<Map<String, Object>>> ClassOfTeacher(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Class c = this.getClassbyname(name);
		
		if(c != null){
			//get the class experiments arrangement, class student and group, and time
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			Map<String, Object> map12 = new HashMap<String, Object>();
			map11.put("name", "班级名称");
			map12.put("name", name);
			list1.add(map11);
			list1.add(map12);
			list.add(list1);
			
			List<User> ulist = this.getClassTeacher(c.getClassId());
			List<Object> teacherlist = new ArrayList<Object>();
			int usize = ulist.size();
			for(int i = 0; i < usize; i++){
				Map<String, Object> umap = new HashMap<String, Object>();
				
				umap.put("name", ulist.get(i).getUserNickname());
				umap.put("onclick", "showDetail('" +  ulist.get(i).getUserNickname() + "' , 'user');");
				umap.put("class",  "btn btn-link");
				
				teacherlist.add(umap);
			}
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			Map<String, Object> map22 = new HashMap<String, Object>();
			map21.put("name", "课程教师");
			map22.put("name", teacherlist);
			map22.put("class", "collapse");
			list2.add(map21);
			list2.add(map22);
			list.add(list2);
			
			List<Classgroup> glist = this.gDAO.getClassGroup(c.getClassId());
			List<Object> grouplist = new ArrayList<Object>();
			int gsize = glist.size();
			for(int i = 0; i < gsize; i++){
				GroupmemberDAO mDAO = new GroupmemberDAO();
				@SuppressWarnings("unchecked")
				List<Groupmember> mlist = mDAO.getListByProperty("classgroupmemberGroupId", glist.get(i).getClassgroupId());
				List<Object> map = new ArrayList<Object>();
				
				int size = mlist.size();
				String nameList = i+1 + ". ";
				for(int j = 0; j < size; j++){
					String username = this.uDAO.getUserName(mlist.get(j).getClassgroupmemberUserId());
					System.out.println(username);
					nameList = nameList + username + "&emsp;";
				}
					
				Map<String, Object> gmap = new HashMap<String, Object>();
				gmap.put("name", nameList);
				//gmap.put("onclick", "showDetail('" +  glist.get(i).getClassgroupName() + "', 'group');");
				gmap.put("class",  "");
				
				grouplist.add(gmap);
			}
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			Map<String, Object> map32 = new HashMap<String, Object>();
			map31.put("name", "课程小组");
			map32.put("name", grouplist);
			map32.put("class", "collapse");
			list3.add(map31);
			list3.add(map32);
			list.add(list3);
		}
		
		return list;
	}

	/**
	 * 根据班级编号得到班级实体类
	 * @param classid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getClass(int classid){
		List<Class> clist = this.cDAO.getListByProperty("classId", classid);
		
		if(clist.isEmpty() || clist.size() > 1){
			return null;
		}
		else{
			return clist.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getClassName(int classid){
		ClassDAO cDAO = new ClassDAO();
		CourseDAO courseDAO = new CourseDAO();
		
		String classname = new String();
		
		List<Class> clist = cDAO.getListByProperty("classId", classid);
		int courseid;
		if(clist.isEmpty() || clist.size() > 1){
			//database error
			courseid = -1;
		}
		else{
			courseid = clist.get(0).getClassCourseId();
		}
		
		List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
		
		if(courselist.isEmpty() || courselist.size() > 1){
			//database error
			
		}
		else{
			classname = courselist.get(0).getCourseName() + " " + clist.get(0).getClassName();
		}
		
		return classname;
	}
	
	public Class getClassbyname(String name){
		CourseInfoCDAO courseDAO = new CourseInfoCDAO();
		Class c = new Class();
		
		String[] slist = name.split(" ");
		
		c = (Class) this.cDAO.getByNProperty("className", slist[1], "classCourseId", courseDAO.queryByName(slist[0]).getCourseId() + "");

		return c;
	}

	@SuppressWarnings("unchecked")
	public List<Class> getMyClass(int teacherid){
		List<Class> clist = new ArrayList<Class>();
		
		List<ClassTeacher> ctlist = this.ctDAO.getListByProperty("classTeacherTeacherId", teacherid);
		int size = ctlist.size();
		for(int i = 0; i < size; i++){
			clist.add(this.getClass(ctlist.get(i).getClassTeacherClassId()));
		}
		
		return clist;
	}

	@SuppressWarnings("unchecked")
	public List<User> getClassTeacher(int classid){
		List<ClassTeacher> ctlist = this.ctDAO.getListByProperty("classTeacherClassId", classid);
		
		List<User> ulist = new ArrayList<User>();
		int size = ctlist.size();
		for(int i = 0; i < size; i++){
			User u = this.uDAO.getUser(ctlist.get(i).getClassTeacherTeacherId());
			
			if(u != null){
				ulist.add(u);
			}
		}
		
		return ulist;
	}
	
	//返回该班级的课程名
	public String getCourseName(int classid){
		
		Class c = (Class) this.cDAO.getUniqueByProperty("classId", classid);
		Integer courseId = c.getClassCourseId();
		Course course = (Course) this.courseDAO.getUniqueByProperty("courseId", courseId);
		String courseName = course.getCourseName();
		return courseName;
	}

	public Map<String, Object> Edit(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> " + name);
		
		String className = name.split(" ")[1];
		//get the class name
		List<Map<String, Object>> cname = this.vutil.createList("班级名称", "", "", className, "btn btn-link edit", "editable(this);", "ClassInfoName",0);
		int classid = this.getClassbyname(name).getClassId();
		
		//get the course list and the course
		
		
		//get the class teacher and course teacher
		
		list.add(cname);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}

}
