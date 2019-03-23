package virnet.management.input.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.dao.StuClassDAO;
import virnet.management.dao.UserCharacterDAO;
import virnet.management.dao.UserDAO;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Course;
import virnet.management.entity.Groupmember;
import virnet.management.entity.StuClass;
import virnet.management.entity.User;
import virnet.management.entity.UserCharacter;
import virnet.management.util.ViewUtil;


public class InputService {
	
	private ViewUtil vutil = new ViewUtil();

	//生成学生信息导入的界面
	@SuppressWarnings("unchecked")
	public Map<String, Object> inputStuInfo(String user, String id){
		Map<String, Object> r = new HashMap<String, Object>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "学生信息导入 <i class='icon-double-angle-right'></i> ");
		
		//根据操作人的id 筛选出属于这个老师的课程 的下拉框
		
		String hql = "SELECT model from " + ClassTeacher.class.getName() + " as model "
				+ "where model.classTeacherTeacherId ='" + user + "'";
		List<ClassTeacher> ctlist = new ArrayList<ClassTeacher>();
		ClassTeacherDAO ctDAO = new ClassTeacherDAO();
		ctlist = ctDAO.getListByHql(hql);
		
		int size = ctlist.size();
		System.out.println("xxxxxx");
		System.out.println("Class list size : " + size);
		
		//结构： 课程名 班级名
		List<Map<String, Object>> cclist = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < size; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			
			int classId = ctlist.get(i).getClassTeacherClassId();
			
			String hql2 = "SELECT t1.className, t2.courseName "
					   + "From Class as t1, Course as t2 "
					   + "WHERE t1.classCourseId = t2.courseId and t1.classId = " + classId;
			
			ClassDAO classDAO= new ClassDAO();
			Object queryResult = classDAO.getUniqueByHql(hql2);
			Object[] queryResults = (Object[]) queryResult;
			
			cmap.put("name",  queryResults[1] + " " + queryResults[0]);
			cmap.put("value", classId);
			
			cclist.add(cmap);
		}
		
		List<Map<String, Object>> cName = this.vutil.createList("", "", "", cclist, "singleselect", "", "inputClassName",(Integer)cclist.get(0).get("value"));
		
		
		Map<String, Object> guide = new HashMap<String, Object>();
		guide.put("data", "请输入用户学号，以回车分割，请勿重复输入");
		
		//输入框
		Map<String, Object> infomation = new HashMap<String, Object>();
		infomation.put("class", "");
		
		//提交按钮
		Map<String, Object> submit = new HashMap<String, Object>();
		submit.put("class", "btn button-new");
		submit.put("content", "提交");
		submit.put("click", "submitInputPage()");
		
		r.put("tittle", tittle);
		r.put("select", cName);
		r.put("guide", guide);
		r.put("infomation", infomation);
		r.put("button", submit);

		return r;
	}
	
	public Map<String, Object> inputTeaInfo(String user, String id){
		Map<String, Object> r = new HashMap<String, Object>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "教师信息导入 <i class='icon-double-angle-right'></i> ");
		
		Map<String, Object> guide = new HashMap<String, Object>();
		guide.put("data", "请输入教师工号，以回车分割，请勿重复输入");
		
//		//所有班级
//		ClassDAO cDAO = new ClassDAO();
//
//		@SuppressWarnings("unchecked")
//		List<Class> clist = cDAO.getList();
//		
//		int size = clist.size();
//		System.out.println("Class list size : " + size);
//		
//		List<Map<String, Object>> cclist = new ArrayList<Map<String, Object>>();
//		for(int i = 0; i < size; i++){
//			Map<String, Object> cmap = new HashMap<String, Object>();
//			
//			int courseid = clist.get(i).getClassCourseId();
//			CourseDAO courseDAO = new CourseDAO();
//			Course course = (Course) courseDAO.getUniqueByProperty("courseId", courseid);
//			
//			cmap.put("name",  course.getCourseName() + " " + clist.get(i).getClassName());
//			cmap.put("value", clist.get(i).getClassId());
//			
//			cclist.add(cmap);
//		}
		
//		List<Map<String, Object>> cName = this.vutil.createList("", "", "", cclist, "singleselect", "", "inputClassName",(Integer)cclist.get(0).get("value"));

		//输入框
		Map<String, Object> infomation = new HashMap<String, Object>();
		infomation.put("class", "");
		
		//提交按钮
		Map<String, Object> submit = new HashMap<String, Object>();
		submit.put("class", "btn button-new");
		submit.put("content", "提交");
		submit.put("click", "submitInputPage()");
		
		r.put("tittle", tittle);
		r.put("guide", guide);
//		r.put("select", cName);
		r.put("infomation", infomation);
		r.put("button", submit);

		return r;
	}
	
	//导入实验员
	public Map<String, Object> inputExpStaffInfo(String user, String id){
		Map<String, Object> r = new HashMap<String, Object>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "实验员信息导入 <i class='icon-double-angle-right'></i> ");
		
		Map<String, Object> guide = new HashMap<String, Object>();
		guide.put("data", "请输入实验员工号，以回车分割，请勿重复输入");

		//输入框
		Map<String, Object> infomation = new HashMap<String, Object>();
		infomation.put("class", "");
		
		//提交按钮
		Map<String, Object> submit = new HashMap<String, Object>();
		submit.put("class", "btn button-new");
		submit.put("content", "提交");
		submit.put("click", "submitInputPage()");
		
		r.put("tittle", tittle);
		r.put("guide", guide);
		r.put("infomation", infomation);
		r.put("button", submit);

		return r;
	}
	
	//提交导入的学生信息
	public Map<String, Object> submitStuInfo(String user, String id, Integer classId, String information) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		
			UserDAO  usrDAO = new  UserDAO();
			
			String[] studentId = information.split("\n");
			System.out.println("学生个数"+studentId.length);
			if(studentId[0].equals("")){
				r.put("isSuccess", false);
				r.put("data", "导入失败，输入不合法");
				return r;
			}
			for(String ss:studentId)
				System.out.println(ss);
			
			//判断同一组是否有多个相同的Id
			Set<String> set = new HashSet<String>();
			for(String student : studentId){
			   set.add(student);
			}
			if(set.size() != studentId.length){
			    //有重复
				r.put("isSuccess", false);
				r.put("data", "导入失败，新用户中有重复学号");
				return r;
			}
			

			//首先判断该班是否有已经重复出现的学生
			for(int i=0;i<studentId.length;i++){
				
				String hql = "select t1.stuClassId "
						+ "from StuClass as t1 "
						+ "where t1.stuClassUserId = " + studentId[i]
						+ " and t1.stuClassClassId = " + classId;
				
				System.out.println(hql);
				
				try {
					if(usrDAO.getUniqueByHql(hql)!=null){
						r.put("isSuccess", false);
						r.put("data", "导入失败，该班存在已有学生");
						return r;	
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			//修改用户表  user
			for(int i=0;i<studentId.length;i++){
				
				//如果是新用户，而不是新角色
				if(usrDAO.getUniqueByProperty("userId",studentId[i])==null){	
				
					User usr = new User();
					usr.setUserNickname(studentId[i]);
					usr.setUserId(Integer.parseInt((String)studentId[i]));
					//默认密码与用户Id一致
					usr.setUserKeyResult(studentId[i].hashCode());
					if(!usrDAO.add(usr)){
						r.put("isSuccess", false);
						r.put("data", "导入新用户失败");
						return r;
					}
				}
			}
			
			for(int i=0;i<studentId.length;i++){
				
				UserCharacterDAO ucDAO = new UserCharacterDAO();
				String[] para = {"userCharacterUserId",studentId[i],"userCharacterCharacterId",""+3}; 
				if(ucDAO.getByNProperty(para)!=null)
					continue;
				
				//修改用户角色表  user_character
				UserCharacter uc = new UserCharacter();
				uc.setUserCharacterUserId(Integer.parseInt((String)studentId[i]));
				uc.setUserCharacterCharacterId(3);
				
				if(!ucDAO.add(uc)){
					r.put("isSuccess", false);
					r.put("data", "修改角色表失败");
					return r;
				}
			}
			
			for(int i=0;i<studentId.length;i++){

				//修改学生上课表  stu_class
				StuClass stuClass = new StuClass();
				stuClass.setStuClassClassId(classId);
				stuClass.setStuClassUserId(Integer.parseInt((String)studentId[i]));
				
				StuClassDAO scDAO = new StuClassDAO();
				if(!scDAO.add(stuClass)){
					r.put("isSuccess", false);
					r.put("data", "修改学生上课表失败");
					return r;
				}
			}
			
			r.put("isSuccess", true);
			r.put("data", "导入成功");

		return r;	
	}
	
public Map<String, Object> submitTeaInfo(String user, String id, String information) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		UserDAO  usrDAO = new  UserDAO();
		
		String[] teacherId = information.split("\n");
		if(teacherId[0].equals("")){
			r.put("isSuccess", false);
			r.put("data", "导入失败，输入不合法");
			return r;
		}
		System.out.println("教师个数"+teacherId.length);
		for(String ss:teacherId)
			System.out.println(ss);
		
		
		//判断同一次是否有多个相同的Id
		Set<String> set = new HashSet<String>();
		for(String teacher : teacherId){
		   set.add(teacher);
		}
		if(set.size() != teacherId.length){
		    //有重复
			r.put("isSuccess", false);
			r.put("data", "导入失败，新用户中有重复教师工号");
			return r;
		}
		

//		//首先判断该班是否有已经重复出现的教师
//		for(int i=0;i<teacherId.length;i++){
//			
//			String hql = "select t1.classTeacherTeacherId "
//					+ "from ClassTeacher as t1 "
//					+ "where t1.classTeacherTeacherId = " + teacherId[i]
//					+ " and t1.classTeacherClassId = " + classId;
//			
//			System.out.println(hql);
//			
//			if(usrDAO.getUniqueByHql(hql)!=null){
//				r.put("isSuccess", false);
//				r.put("data", "导入失败，该班存在已有教师");
//				return r;
//			}
//		}
		
		for(int i=0;i<teacherId.length;i++){
			
			//如果是新用户，而不是新角色
			if(usrDAO.getUniqueByProperty("userId",teacherId[i])==null){	
			
				User usr = new User();
				usr.setUserNickname(teacherId[i]);
				usr.setUserId(Integer.parseInt((String)teacherId[i]));
				//默认密码与用户Id一致
				usr.setUserKeyResult(teacherId[i].hashCode());
				if(!usrDAO.add(usr)){
					r.put("isSuccess", false);
					r.put("data", "导入新用户失败");
					return r;
				}
			}
		}
		for(int i=0;i<teacherId.length;i++){
			
			UserCharacterDAO ucDAO = new UserCharacterDAO();
			String[] para = {"userCharacterUserId",teacherId[i],"userCharacterCharacterId",""+2}; 
			if(ucDAO.getByNProperty(para)!=null)
				continue;
			
			//修改用户角色表  user_character
			UserCharacter uc = new UserCharacter();
			uc.setUserCharacterUserId(Integer.parseInt((String)teacherId[i]));
			uc.setUserCharacterCharacterId(2);
			
			if(!ucDAO.add(uc)){
				r.put("isSuccess", false);
				r.put("data", "修改角色表失败");
				return r;
			}
		}
		
		
//		//修改班级教师表
//		for(int i=0;i<teacherId.length;i++){
//			 
//			ClassTeacher classTeacher = new ClassTeacher();
//			classTeacher.setClassTeacherClassId(classId);
//			classTeacher.setClassTeacherTeacherId(Integer.parseInt(teacherId[i]));
//			
//			ClassTeacherDAO classTeacherDAO = new ClassTeacherDAO();
//			if(!classTeacherDAO.add(classTeacher)){
//				r.put("isSuccess", false);
//				r.put("data", "修改教师上课表失败");
//				return r;
//			}
//		}
			
		r.put("isSuccess", true);
		r.put("data", "导入成功");

		return r;	
	}
public Map<String, Object> submitExpStaffInfo(String user, String id, String information) {
	
	Map<String, Object> r = new HashMap<String, Object>();
	
	UserDAO  usrDAO = new  UserDAO();
	
	String[] expStaffId = information.split("\n");
	if(expStaffId[0].equals("")){
		r.put("isSuccess", false);
		r.put("data", "导入失败，输入不合法");
		return r;
	}
	System.out.println("实验员个数"+expStaffId.length);
	for(String ss:expStaffId)
		System.out.println(ss);
	
	
	//判断同一次是否有多个相同的Id
	Set<String> set = new HashSet<String>();
	for(String teacher : expStaffId){
	   set.add(teacher);
	}
	if(set.size() != expStaffId.length){
	    //有重复
		r.put("isSuccess", false);
		r.put("data", "导入失败，新用户中有重复实验员工号");
		return r;
	}

	for(int i=0;i<expStaffId.length;i++){
		
		//如果是新用户，而不是新角色
		if(usrDAO.getUniqueByProperty("userId",expStaffId[i])==null){	
		
			User usr = new User();
			usr.setUserNickname(expStaffId[i]);
			usr.setUserId(Integer.parseInt((String)expStaffId[i]));
			//默认密码与用户Id一致
			usr.setUserKeyResult(expStaffId[i].hashCode());
			if(!usrDAO.add(usr)){
				r.put("isSuccess", false);
				r.put("data", "导入新用户失败");
				return r;
			}
		}
	}
	
	//首先判断是否有已经重复出现的实验员
	for(int i=0;i<expStaffId.length;i++){
		
		UserCharacterDAO ucDAO = new UserCharacterDAO();
		String[] para = {"userCharacterUserId",expStaffId[i],"userCharacterCharacterId",""+1}; 
		if(ucDAO.getByNProperty(para)!=null){
			r.put("isSuccess", false);
			r.put("data", "导入失败，已存在该实验员");
			return r;
		}

		
		//修改用户角色表  user_character
		UserCharacter uc = new UserCharacter();
		uc.setUserCharacterUserId(Integer.parseInt((String)expStaffId[i]));
		uc.setUserCharacterCharacterId(1);
		
		if(!ucDAO.add(uc)){
			r.put("isSuccess", false);
			r.put("data", "修改角色表失败");
			return r;
		}
	}
		
	r.put("isSuccess", true);
	r.put("data", "导入成功");

	return r;	
}
}
