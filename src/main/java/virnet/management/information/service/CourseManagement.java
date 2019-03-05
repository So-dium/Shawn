package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.CourseDAO;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.util.PageUtil;

public class CourseManagement implements InformationQuery{
	private CourseDAO courseDAO = new CourseDAO();

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "课程编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "课程名称");
		head_name.put("class", "");
		head.add(head_name);
		
		list.add(head);
		
		PageUtil<Course> pageUtil = new PageUtil<Course>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		List<Course> Courselist = new ArrayList<Course>();
		int recordSize = courseDAO.getList().size();
		System.out.println("记录数："+recordSize);
		if (recordSize > 0) {
			this.courseDAO.getListByPage(pageUtil);
			Courselist = pageUtil.getList();
		}
		
		int size = Courselist.size();
		System.out.println("Course list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> CourseInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", Courselist.get(i).getCourseId() + "");
			map_id.put("class", "");
			CourseInfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", Courselist.get(i).getCourseName());
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + Courselist.get(i).getCourseName() + "' , 'course');");
			CourseInfo.add(map_name);
			
			System.out.println("index : " + i + ", Course id : " + Courselist.get(i).getCourseId() + ", Course name : " + Courselist.get(i).getCourseName());
			
			list.add(CourseInfo);
		}
		
		int total = this.courseDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新建课程");
		button.put("class", "btn button-new");
		button.put("click", "addContent('course-management');");
		
		map.put("button_new", button);		
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
