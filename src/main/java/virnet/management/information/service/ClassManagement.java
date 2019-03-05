package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.combinedao.CourseInfoCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.entity.Class;
import virnet.management.entity.Course;
import virnet.management.entity.User;
import virnet.management.util.PageUtil;

public class ClassManagement  implements InformationQuery{

	private ClassDAO ClassDAO = new ClassDAO();
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private CourseInfoCDAO coDAO = new CourseInfoCDAO();
	
	/**
	 * @param
	 * page required page in database
	 * @return
	 * map : "data" the query list
	 *       "page" total pages
	 * @see virnet.management.information.service.InformationQuery#query(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		Map<String, Object> head_id = new HashMap<String, Object>();
		head_id.put("name", "班级编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, Object> head_name = new HashMap<String, Object>();
		head_name.put("name", "班级名称");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, Object> head_course = new HashMap<String, Object>();
		head_course.put("name", "所属课程");
		head_course.put("class", "");
		head.add(head_course);
		
		Map<String, Object> head_teacher = new HashMap<String, Object>();
		head_teacher.put("name", "班级教师组");
		head_teacher.put("class", "");
		head.add(head_teacher);
		
		list.add(head);
		
		PageUtil<Class> pageUtil = new PageUtil<Class>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		List<Class> Classlist = new ArrayList<Class>();
		int recordSize = ClassDAO.getList().size();
		System.out.println("记录数："+recordSize);
		if (recordSize > 0) {
			this.ClassDAO.getListByPage(pageUtil);
			Classlist = pageUtil.getList();
		}

		int size = Classlist.size();
		System.out.println("Class list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, Object>> ClassInfo = new ArrayList<Map<String, Object>>();
			
			int cid = Classlist.get(i).getClassId();
			
			Map<String, Object> map_id = new HashMap<String, Object>();
			map_id.put("name", cid + "");
			map_id.put("class", "");
			ClassInfo.add(map_id);
			
			String classname = this.cDAO.getClassName(cid); 
			Map<String, Object> map_name = new HashMap<String, Object>();
			map_name.put("name", classname);
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + classname + "' , 'class');");
			ClassInfo.add(map_name);
		
			String coursename = this.coDAO.getCourse(Classlist.get(i).getClassCourseId()).getCourseName();
			Map<String, Object> map_course = new HashMap<String, Object>();
			map_course.put("name", coursename);
			map_course.put("class", "btn btn-link");
			map_course.put("onclick", "showDetail('" + coursename + "' , 'course');");
			ClassInfo.add(map_course);
			
			Map<String, Object> map_teacher = new HashMap<String, Object>();
			
			List<User> ulist = this.cDAO.getClassTeacher(cid);
			List<Object> teacherlist = new ArrayList<Object>();
			int usize = ulist.size();
			for(int i1 = 0; i1 < usize; i1++){
				Map<String, Object> umap = new HashMap<String, Object>();
				
				umap.put("name", ulist.get(i1).getUserNickname());
				umap.put("onclick", "showDetail('" +  ulist.get(i1).getUserNickname() + "' , 'user');");
				umap.put("class",  "btn btn-link");
				
				teacherlist.add(umap);
			}
			
			map_teacher.put("name", teacherlist);
			map_teacher.put("class", "collapse");
			ClassInfo.add(map_teacher);
			
			System.out.println("index : " + i + ", Class id : " + Classlist.get(i).getClassId() + ", Class name : " + Classlist.get(i).getClassName());
			
			list.add(ClassInfo);
		}
		
		int total = this.ClassDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新建班级");
		button.put("class", "btn button-new");
		button.put("click", "addContent('class-management');");
		
		map.put("button_new", button);		
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}
}
