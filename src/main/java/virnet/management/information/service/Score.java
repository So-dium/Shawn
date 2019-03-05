package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat; 
import java.util.Date; 
import virnet.management.util.DateUtil;
import virnet.experiment.dao.ResultTaskDAO;
import virnet.experiment.entity.ResultTask;
import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.combinedao.CourseInfoCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassarrangeCaseDAO;
import virnet.management.dao.ClassarrangeDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.dao.UserDAO;
import virnet.management.dao.expArrangeDAO;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.entity.Periodarrange;
import virnet.management.entity.User;
import virnet.management.entity.Class;
import virnet.management.entity.Classarrange;
import virnet.management.entity.ClassarrangeCase;
import virnet.management.util.PageUtil;
import virnet.management.util.ViewUtil;

public class Score implements InformationQuery{
	
	private static String EditTittle = "班级信息";
	private expArrangeDAO eDAO = new expArrangeDAO();
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private CourseInfoCDAO coDAO = new CourseInfoCDAO();
	private PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
	private UserDAO userDAO = new UserDAO();
	private CourseDAO courseDAO = new CourseDAO();
	@SuppressWarnings("unused")
	private ClassDAO classDAO = new ClassDAO();
	@SuppressWarnings("unused")
	private ExpDAO expDAO = new ExpDAO();
	private ResultTaskDAO resulttaskDAO = new ResultTaskDAO();
	private ClassarrangeDAO caDAO = new ClassarrangeDAO();
	private ClassarrangeCaseDAO cacDAO = new ClassarrangeCaseDAO();
	private ViewUtil vutil = new ViewUtil();
	private DateUtil dateutil = new DateUtil();
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> head_name = new HashMap<String, Object>();
		head_name.put("name", "班级名称");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, Object> head_course = new HashMap<String, Object>();
		head_course.put("name", "所属课程");
		head_course.put("class", "");
		head.add(head_course);
		
		Map<String, Object> head_explist = new HashMap<String, Object>();
		head_explist.put("name", "实验列表");
		head_explist.put("class", "");
		head.add(head_explist);
		
		list.add(head);
		
		PageUtil<Class> pageUtil = new PageUtil<Class>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		this.classDAO.getListByPage(pageUtil);
		
		List<Class> Classlist = new ArrayList<Class>();
		Classlist = pageUtil.getList();
		
		int size = Classlist.size();
		//System.out.println("Class list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, Object>> ClassInfo = new ArrayList<Map<String, Object>>();
			
			int cid = Classlist.get(i).getClassId();
			
			String classname = this.cDAO.getClassName(cid); 
			Map<String, Object> map_name = new HashMap<String, Object>();
			map_name.put("name", classname);
			map_name.put("class", "");
			//map_name.put("onclick", "showDetail('" + classname + "' , 'class');");
			ClassInfo.add(map_name);
		
			String coursename = this.coDAO.getCourse(Classlist.get(i).getClassCourseId()).getCourseName();
			Map<String, Object> map_course = new HashMap<String, Object>();
			map_course.put("name", coursename);
			map_course.put("class", "");
			//map_course.put("onclick", "showDetail('" + coursename + "' , 'course');");
			ClassInfo.add(map_course);
			
			Map<String, Object> map_explist = new HashMap<String, Object>();

	
			List<Object> expnamelist = new ArrayList<Object>();
			//根据班级、实验的值，获得所有在预约时段期间的实验活动，并显示他们的时间
			String hql1 = "select distinct t1.expArrangeId "
					+ " from expArrange as t1 ,Periodarrange as t2 , Classarrange as t3, ClassarrangeCase as t4 "
					+ "where t2.periodarrangeClassId = " + cid 
					+ " and t2.periodarrangeId = t3.classarrangePeriodArrangeId "
					+ " and t3.classarrangeExpArrangeId = t1.expArrangeId "
					+ " and t4.classarrangeCaseExpArrangeId = t1.expArrangeId ";
			List<Object> expArrange = pDAO.getListByHql(hql1);
			for(int i1 = 0; i1 < expArrange.size(); i1++){
				String hql2 = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
						+ " from Periodarrange as t2 , Classarrange as t3 "
						+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
						+ " and t3.classarrangeExpArrangeId = " + expArrange.get(i1);
				List<Object> periodList = pDAO.getListByHql(hql2);
				Map<String, Object> umap = new HashMap<String, Object>();
				// String sd = new SimpleDateFormat("EEE, d MMM yyyy").format(plist.get(i1).getPeriodarrangeStartDate());  
				umap.put("name", dateutil.TimeFormat(periodList));
				umap.put("onclick", "showDetail('" + expArrange.get(i1)  + "' , 'score');");
				umap.put("class",  "btn btn-link");
				expnamelist.add(umap);
					/*List<ClassarrangeCase> caclist = new ArrayList<ClassarrangeCase>();
					caclist = this.cacDAO.getListByProperty("classarrangeCaseExpArrangeId", expArrange.get(i1));
					int cacsize = caclist.size();
					for(int i3 = 0; i3 < cacsize; i3++){
						List<Exp> explist = new ArrayList<Exp>();
						explist = this.expDAO.getListByProperty("expId", caclist.get(i3).getClassarrangeCaseExpId());
						int esize = explist.size();
						for(int i4 = 0; i4 < esize; i4++){
							Map<String, Object> umap = new HashMap<String, Object>();
							// String sd = new SimpleDateFormat("EEE, d MMM yyyy").format(plist.get(i1).getPeriodarrangeStartDate()); 
							 String ctitle = classname+" "+explist.get(i4).getExpName();
							umap.put("name", dateutil.TimeFormat(periodList)+"  "+explist.get(i4).getExpName());
							umap.put("onclick", "showDetail('" + ctitle  + "' , 'score');");
							umap.put("class",  "btn btn-link");
							expnamelist.add(umap);
						}
					}*/
				
			}
			
			map_explist.put("name", expnamelist);
			map_explist.put("class", "collapse");
			ClassInfo.add(map_explist);
			
			System.out.println("index : " + i + ", Class id : " + Classlist.get(i).getClassId() + ", Class name : " + Classlist.get(i).getClassName());
			
			list.add(ClassInfo);
		}
		
		int total = this.classDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
