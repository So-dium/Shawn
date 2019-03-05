package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.PeriodarrangeWeekDAO;
import virnet.management.entity.Class;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.entity.PeriodarrangeWeek;
import virnet.management.util.DateUtil;
import virnet.management.util.PageUtil;

public class TimeManagement implements InformationQuery {
	private PeriodarrangeWeekDAO pDAO = new PeriodarrangeWeekDAO();
	private DateUtil dateutil = new DateUtil();
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
			List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
//			Map<String, Object> head_id = new HashMap<String, Object>();
//			head_id.put("name", "时段编号");
//			head_id.put("class", "");
//			head.add(head_id);
			
			Map<String, Object> head_name = new HashMap<String, Object>();
			head_name.put("name", "班级名称");
			head_name.put("class", "");
			head.add(head_name);	
			
			Map<String, Object> head_start = new HashMap<String, Object>();
			head_start.put("name", "上机时间");
			head_start.put("class", "");
			head.add(head_start);	
			
			list.add(head);
			
			PageUtil<Exp> pageUtil = new PageUtil<Exp>();
			if(page == 0){
				page = 1;
			}
			pageUtil.setPageNo(page);
			
			List<PeriodarrangeWeek> pclist = new ArrayList<PeriodarrangeWeek>();
			
			int recordSize = pDAO.getList().size();
			System.out.println("记录数："+recordSize);
			if (recordSize > 0) {
				//get the id of each class who is in period table
				String hql = "SELECT model from " + PeriodarrangeWeek.class.getName()
						+ " as model group by model.periodarrangeWeekClassid";
				this.pDAO.getListByPage(hql, pageUtil);
				pclist = pageUtil.getList();
			}
			int l = pclist.size();
			for(int i = 0; i < l; i++){		
				//set return data, period number : i, class name : name, and period list of each class
				
				//period NO.
				List<Map<String, Object>> pInfo = new ArrayList<Map<String, Object>>();
//				Map<String, Object> map_id = new HashMap<String, Object>();
//				map_id.put("name", i + "");
//				map_id.put("class", "");
//				pInfo.add(map_id);
				
				//class name and course name
				Map<String, Object> map_name = new HashMap<String, Object>();
				int classid = pclist.get(i).getPeriodarrangeWeekClassid();
				ClassDAO classDAO = new ClassDAO();
				List<Class> Classlist = new ArrayList<Class>();
				Classlist = classDAO.getListByProperty("classId", classid);
				
				int courseid = Classlist.get(0).getClassCourseId();
				CourseDAO courseDAO = new CourseDAO();
				List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
				
				map_name.put("name", courselist.get(0).getCourseName() + " " + Classlist.get(0).getClassName());
				map_name.put("class", "btn btn-link");
				map_name.put("onclick", "showDetail('" + courselist.get(0).getCourseName() + " " + Classlist.get(0).getClassName() + "' , 'class');");
				pInfo.add(map_name);
					
				//for each class, get the period arrange list
				List<PeriodarrangeWeek> plist = new ArrayList<PeriodarrangeWeek>();
				plist = this.pDAO.getListByProperty("periodarrangeWeekClassid", pclist.get(i).getPeriodarrangeWeekClassid());
			
				Map<String, Object> map_time = new HashMap<String, Object>();
				
				int size = plist.size();
				List<Object> time = new ArrayList<Object>();
				for(int j = 0; j < size; j++){
					//need the date util to process start and end date;		
					Map<String, Object> maptime = new HashMap<String, Object>();
					maptime.put("name", this.dateutil.processDate(plist.get(j).getPeriodarrangeWeekStartDay(), plist.get(j).getPeriodarrangeWeekStartTime(), plist.get(j).getPeriodarrangeWeekEndDay(), plist.get(j).getPeriodarrangeWeekEndTime()));
					maptime.put("class", "");
					maptime.put("onclick", "");
					time.add(maptime);
				}
				map_time.put("name", time);
				map_time.put("class", "collapse");
				pInfo.add(map_time);
				
				list.add(pInfo);
			}
			
			int total = l;
			int pagesize = pageUtil.getPageSize();
			int pageNO = total / pagesize + 1;
			
			Map<Object, Object> button = new HashMap<Object, Object>();
			button.put("content", "< 课时安排  >");
			button.put("class", "btn button-new");
			button.put("id", "editTimeManagement");
			button.put("click", "editTimeManagement(0);");
			
			Map<Object, Object> button_switch = new HashMap<Object, Object>();
			button_switch.put("content", "< 在课程表内显示   >");
			button_switch.put("class", "btn button-new");
			button_switch.put("click", "showInChart(false);");
			
			map.put("button_switch", button_switch);		
			map.put("button_new", button);
			map.put("data", list);
			map.put("page", pageNO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return map;
	}

}
