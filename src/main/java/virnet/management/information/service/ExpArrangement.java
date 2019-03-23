package virnet.management.information.service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.ClassarrangeCaseDAO;
import virnet.management.dao.ClassarrangeDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.dao.expArrangeDAO;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classarrange;
import virnet.management.entity.ClassarrangeCase;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.entity.Periodarrange;
import virnet.management.entity.expArrange;
import virnet.management.util.DateUtil;
import virnet.management.util.PageUtil;
import virnet.management.util.UserInfoProcessUtil;

public class ExpArrangement implements InformationQuery {

	private ClassarrangeCaseDAO cacDAO = new ClassarrangeCaseDAO();
	private ClassarrangeDAO caDAO = new ClassarrangeDAO();
	private PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
	private ClassTeacherDAO ctDAO = new ClassTeacherDAO();
	private ExpDAO expDAO = new ExpDAO();
	private DateUtil dateutil = new DateUtil();
	
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
		head_id.put("name", "时段编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, Object> head_name = new HashMap<String, Object>();
		head_name.put("name", "实验时间");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, Object> head_time = new HashMap<String, Object>();
		head_time.put("name", "预约时间");
		head_time.put("class", "");
		head.add(head_time);
		
		Map<String, Object> head_member = new HashMap<String, Object>();
		head_member.put("name", "课程实验");
		head_member.put("class", "");
		head.add(head_member);
		
		list.add(head);
		
		//search the class teacher table, to find out the user's class list, and then find out the class experiment arrangement
		
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
		System.out.println("select:"+select);
		if(select.length() != 0) {
			try {
				for(int i = 0; i < s; i++){
					if(Integer.parseInt(select) == (int)(((Map<String, Object>) selectlist.get(i)).get("id"))){
						classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
					}
					System.out.println((int)((Map<String, Object>) selectlist.get(i)).get("id"));
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("!!!!!!!!!classid = "+classid);
		PageUtil<Class> pageUtil = new PageUtil<Class>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);

		//根据班级、实验的值，获得所有在预约时段期间的实验活动，并显示他们的时间
		String ghql = "select distinct t1.expArrangeId "
				+ " from expArrange as t1 ,Periodarrange as t2 , Classarrange as t3, ClassarrangeCase as t4 "
				+ "where t2.periodarrangeClassId = " + classid
				+ " and t2.periodarrangeId = t3.classarrangePeriodArrangeId "
				+ " and t3.classarrangeExpArrangeId = t1.expArrangeId "
				+ " and t4.classarrangeCaseExpArrangeId = t1.expArrangeId ";
		System.out.println("ghql:"+ghql);
		PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
		List<Object> expArgList = paDAO.getListByHql(ghql);
		List<Map<String, Object>> timelist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < expArgList.size(); i++) {
			
			System.out.println("本次实验活动为" + expArgList.get(i));
			
			Map<String, Object> timemap = new HashMap<String,Object>();
			//获取本次实验活动的时段集合
			String hql3 = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
				+ " from Periodarrange as t2 , Classarrange as t3 "
				+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
				+ " and t3.classarrangeExpArrangeId = " + expArgList.get(i);
			
			//获取本次实验活动的时段集合
			System.out.println("hql3:"+hql3);
			List<Object> periodList = pDAO.getListByHql(hql3);
			
			timemap.put("name",dateutil.TimeFormat(periodList));
			timemap.put("value", expArgList.get(i));                    //保存实验排课Id到value域
			timelist.add(timemap);
			List<Map<String, Object>> pInfo = new ArrayList<Map<String, Object>>();
			//编号和实验时间
			Map<String, Object> map_id = new HashMap<String, Object>();
			map_id.put("name", i + "");
			map_id.put("class", "");
			pInfo.add(map_id);
			pInfo.add(timemap);
			//预约时间
			Map<String, Object>map_appointTime = new HashMap<String, Object>();
			expArrangeDAO eaDAO = new expArrangeDAO();
			
			String timeHql = "select e.appointmentStartDate, e.appointmentStartTime, e.appointmentEndDate, "
					+ "e.appointmentEndTime "
					+ "from expArrange as e "
					+ "where e.expArrangeId = "+expArgList.get(i);
			
			System.out.println("timeHql:"+timeHql);
			List<Object> allTimes =  new ArrayList<Object>();
			try {
				allTimes = eaDAO.getListByHql(timeHql);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Object[] timeL = (Object[])allTimes.get(0);
			if(timeL[0] == null) {
				map_appointTime.put("name", "固定时间");
			}
			else{
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				
				String Date0 = new String();
				if(sdf1.format(timeL[0]) != null){
					Date0 = sdf1.format(timeL[0]);
				}
				else Date0 = "固定预约";
				
				String Date1 = new String();
				if(sdf1.format(timeL[2]) != null){
					Date1 = sdf1.format(timeL[2]);
				}
				else Date1 = "固定预约";
				
				
				String time0 = new String();
				if(timeL[1].toString() == null){
					time0 = "固定预约";
				}
				else{
					time0 = timeL[1].toString();
				}
					
				
				String time1 = new String();
				if(timeL[3].toString() == null){
					time1 = "固定预约";
				}
					
				else{
					time1 = timeL[3].toString();
				}
					
				String time = Date0 + " " + time0 + "~" + Date1 + " " + time1;
				map_appointTime.put("name", time);
			}
			pInfo.add(map_appointTime);
			//实验项目
			Map<String, Object> map_exp = new HashMap<String, Object>();
			map_exp.put("class", "collapse");
			List<Object> explist = new ArrayList<Object>();
			List<ClassarrangeCase> calist = new ArrayList<ClassarrangeCase>();
			ClassarrangeCaseDAO cacDAO = new ClassarrangeCaseDAO();
			try {
				calist = cacDAO.getListByProperty("classarrangeCaseExpArrangeId", expArgList.get(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//List<ClassarrangeCase> calist= this.cacDAO.getListByProperty("classarrangeCaseExpArrangeId", expArgList.get(i));
			System.out.println("calist get");
			for(int j=0; j<calist.size();j++){
				int expId = calist.get(j).getClassarrangeCaseExpId();
				List<Exp> exp = this.expDAO.getListByProperty("expId", expId);
				if(exp.size() == 0){
					continue;
				}
				Map<String, Object> mapexplist = new HashMap<String, Object>();
				mapexplist.put("name", exp.get(0).getExpName());
				mapexplist.put("class", "btn btn-link");
				mapexplist.put("onclick", "showDetail('" + exp.get(0).getExpName() + "', 'exp');");
				explist.add(mapexplist);
			}
			
			map_exp.put("name", explist);
			pInfo.add(map_exp);		
			list.add(pInfo);
		}
		/*String hql2 = " select a.periodarrangeId "
				+ " from periodarrange as a  "
				+ " where not in (" + "select b.periodarrangeId " + "from classarrange as b )";
		System.out.println("hql2:"+hql2);
		List<Periodarrange> pRemain = new ArrayList<Periodarrange>();
		PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
		try {
			pRemain = pDAO.getListByHql(hql2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> pRemainId = new ArrayList<Integer>();
		for(int i=0; i<pRemainId.size(); i++){
			pRemainId.add(pRemain.get(i).getPeriodarrangeId());
		}*/
		
		int total = this.ctDAO.getListByHql(ghql).size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "<编辑>");
		button.put("id", "editExp");
		button.put("class", "btn button-new");
		button.put("click", "expArgEdit()");
		map.put("button_new", button);
		
		/*button.put("content", "<新建>");
		button.put("id", "editExp");
		button.put("class", "btn button-switch");
		button.put("click", "addContent('exp-arrangement')");
		map.put("button_switch", button);*/
		
		
		map.put("select_change", selectlist);
		map.put("data", list);
		map.put("page", pageNO);
		
//		map.put("times", pRemain);
		return map;
	}

}
