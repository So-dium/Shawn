package virnet.management.timemanagement.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.dao.PeriodarrangeWeekDAO;
import virnet.management.entity.Course;
import virnet.management.entity.Periodarrange;
import virnet.management.entity.PeriodarrangeWeek;
import virnet.management.entity.Class;
import virnet.management.util.DateUtil;

public class TimeManagementService {
	private DateUtil dateutil = new DateUtil();
	private Map<String, Object> datemap = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> showTimeArrange(String Sweek, String date){
		Map<String, Object> map = new HashMap<String, Object>();
		int returnweek = 0;
		String sweek = new String();
		String returndate = new String();
		dateutil.setSysInfo();
		
		int week;
		List<Object> list = new ArrayList<Object>();
		
		try{
			week = Integer.parseInt(Sweek) ;
        }
		catch(Exception e){
			week = 0;
        }
		//既没有周数，也没有日期信息，所以为空
		if(week == -1 && date.equals("0")){        
			list = new ArrayList<Object>();
			list.clear();	
		}
		//只有日期
		else if(week == -1){                          
			//use date
			this.datemap = this.dateutil.CalDate(date);
			
			returnweek = (int) datemap.get("weekNO");
			if(returnweek == 0 || returnweek > dateutil.getTotalWeek()){
				list = defaultWeek();
				sweek = "0";
				returndate = "";
			}
			else{
				list = selectWeek((List<String>) datemap.get("datelist"));
				sweek = returnweek + "";
				returndate = ((List<String>) datemap.get("datelist")).get(0);
			}
		}
		 //默认课表
		else if(week == 0){                       
			//use default week
			list = defaultWeek();
			returnweek = 0;
			sweek = "0";
			returndate = "";
		}
		//只有周数
		else{                                
			//use select week
			this.datemap = this.dateutil.CalWeek(week);
			
			returnweek = (int) datemap.get("weekNO");
			if(returnweek == 0 || returnweek > dateutil.getTotalWeek()){
				list = defaultWeek();
				sweek = "0";
				returndate = "";
			}
			else{
				list = selectWeek((List<String>) datemap.get("datelist"));
				sweek = returnweek + "";
				returndate = ((List<String>) datemap.get("datelist")).get(0);
			}
		}
		
		map.put("data", list);
		map.put("date", returndate);
		map.put("week", sweek);
		
		return map;
	}

	public Map<String, Object> editTimeArrange(String username, String sweek, String date,String classId,String operation, String period) throws ParseException{
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		System.out.println(username + " " + sweek + " " + date + " " + classId + " " + operation);
		System.out.println(period);
		
		String[] periodList = period.split(",");
		Integer week = Integer.parseInt(sweek);		
		if (week == 0) { // 修改默认课表

			if (Integer.parseInt(operation) == 0) { // 新增	
				
				//修改 periodArrangeWeek表
				for (int i = 0; i < periodList.length; i++) {

					// periodList[i] 的结构类似 26 最后一位表示星期
					String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
					String startTime = periodList[i].substring(0, periodList[i].length() - 1);

					PeriodarrangeWeek newPeriodWeek = new PeriodarrangeWeek();
					newPeriodWeek.setPeriodarrangeWeekClassid(Integer.parseInt(classId));
					newPeriodWeek.setPeriodarrangeWeekCabinetNum(8);
					newPeriodWeek.setPeriodarrangeWeekStartDay((short) Integer.parseInt(startDay));
					// StartDay 和 EndDay 是一样的，因为每一个记录只表示一个小时段
					newPeriodWeek.setPeriodarrangeWeekEndDay((short) Integer.parseInt(startDay));
					newPeriodWeek.setPeriodarrangeWeekStartTime(Integer.parseInt(startTime));
					newPeriodWeek.setPeriodarrangeWeekEndTime(Integer.parseInt(startTime) + 1);
					newPeriodWeek.setPeriodarrangeWeekSetupuserId(Integer.parseInt(username));

					PeriodarrangeWeekDAO pawDAO = new PeriodarrangeWeekDAO();
					pawDAO.add(newPeriodWeek);
				}
				
				//按照periodArrangeWeek表 修改periodArrange表
				int totalWeek =  (int) this.courseTableInfo().get("totalweek");   //获得本学期周数
				for(int weekNo = 1; weekNo <= totalWeek ; weekNo++){
					
					//获取本周的所有日期
					this.datemap = this.dateutil.CalWeek(weekNo);
					
					for (int i = 0; i < periodList.length; i++) {
						// periodList[i] 的结构类似 26 最后一位表示星期,表示周六第二个时段
						String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
						@SuppressWarnings("unchecked")
						//获得对应日期是时段
						String startDate =((List<String>)datemap.get("datelist")).get(Integer.parseInt(startDay)-1);
						java.sql.Date startdate = java.sql.Date.valueOf(startDate);
						String startTime = periodList[i].substring(0, periodList[i].length() - 1);

						Periodarrange newPeriod = new Periodarrange();
						newPeriod.setPeriodarrangeCabinetNum(8);
						newPeriod.setPeriodarrangeClassId(Integer.parseInt(classId));
						// StartDate和 EndDate 是一样的，因为每一个记录只表示一个小时段
						newPeriod.setPeriodarrangeStartDate(startdate);
						newPeriod.setPeriodarrangeEndDate(startdate);
						newPeriod.setPeriodarrangeStartTime(Integer.parseInt((startTime)));
						newPeriod.setPeriodarrangeEndTime(Integer.parseInt((startTime)) + 1);
						newPeriod.setPeriodarrangeSetUpUserId(Integer.parseInt(username));
						newPeriod.setPeriodarrangeAllClassNum(1);

						PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
						paDAO.add(newPeriod);
						
					}
				}
				
				r.put("isSuccess", true);
			}
			else if (Integer.parseInt(operation) == 1){   //删除
				
				for (int i = 0; i < periodList.length; i++) {

					// periodList[i] 的结构类似 26 最后一位表示星期 即周六的第三个时段
					String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
					String startTime = periodList[i].substring(0, periodList[i].length() - 1);

					PeriodarrangeWeekDAO pawDAO = new PeriodarrangeWeekDAO();
					String[] para = { "periodarrangeWeekStartDay", startDay, "periodarrangeWeekStartTime", startTime };
					PeriodarrangeWeek periodArrangeWeek = (PeriodarrangeWeek) pawDAO.getByNProperty(para);

					pawDAO.delete(periodArrangeWeek);
				}
				
				//按照periodArrangeWeek表 修改periodArrange表
				int totalWeek =  (int) this.courseTableInfo().get("totalweek");   //获得本学期周数
				for(int weekNo = 1; weekNo <= totalWeek ; weekNo++){
					
					//获取本周的所有日期
					this.datemap = this.dateutil.CalWeek(weekNo);
					
					for (int i = 0; i < periodList.length; i++) {
						// periodList[i] 的结构类似 26 最后一位表示星期,表示周六第二个时段
						String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
						@SuppressWarnings("unchecked")
						//获得对应日期是时段
						String startDate =((List<String>)datemap.get("datelist")).get(Integer.parseInt(startDay)-1);
						String startTime = periodList[i].substring(0, periodList[i].length() - 1);

						PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
						String[] para = {"periodarrangeStartDate",startDate,"periodarrangeStartTime",startTime};
						Periodarrange periodArrange = (Periodarrange) paDAO.getByNProperty(para);
						paDAO.delete(periodArrange);	
					}
				}
				
				
				r.put("isSuccess", true);
				
			}
		}
		//根据日期或者周数判断 修改的是 peiodarrange表
		else{
			//根据日期
			if(week == -1){
				this.datemap = this.dateutil.CalDate(date);
			}
			//根据周数
			else{
				//datemap记录了本周的7天
				datemap = this.dateutil.CalWeek(week);
			}
			if (Integer.parseInt(operation) == 0) { // 新增

				for (int i = 0; i < periodList.length; i++) {

					// periodList[i] 的结构类似 26 最后一位表示星期
					String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
					@SuppressWarnings("unchecked")
					String startDate = ((List<String>)datemap.get("datelist")).get(Integer.parseInt(startDay)-1);
					
					System.out.println(startDate);

					java.sql.Date startdate = java.sql.Date.valueOf(startDate);
					String startTime = periodList[i].substring(0, periodList[i].length() - 1);

					Periodarrange newPeriod = new Periodarrange();
					newPeriod.setPeriodarrangeCabinetNum(8);
					newPeriod.setPeriodarrangeClassId(Integer.parseInt(classId));
					// StartDate和 EndDate 是一样的，因为每一个记录只表示一个小时段
					newPeriod.setPeriodarrangeStartDate(startdate);
					newPeriod.setPeriodarrangeEndDate(startdate);
					newPeriod.setPeriodarrangeStartTime(Integer.parseInt((startTime)));
					newPeriod.setPeriodarrangeEndTime(Integer.parseInt((startTime)) + 1);
					newPeriod.setPeriodarrangeSetUpUserId(Integer.parseInt(username));
					newPeriod.setPeriodarrangeAllClassNum(1);

					PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
					if(!paDAO.add(newPeriod)){
						r.put("isSuccess", false);
						return r;
					};

				}
				r.put("isSuccess", true);
			}
			else if (Integer.parseInt(operation) == 1){   //删除
				
				for (int i = 0; i < periodList.length; i++) {

					// periodList[i] 的结构类似 26 最后一位表示星期
					String startDay = periodList[i].substring(periodList[i].length() - 1, periodList[i].length());
					@SuppressWarnings("unchecked")
					String startDate = ((List<String>)datemap.get("datelist")).get(Integer.parseInt(startDay)-1);
					
					System.out.println(startDate);
					String startTime = periodList[i].substring(0, periodList[i].length() - 1);
					
					PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
					String[] para = {"periodarrangeStartDate",startDate,"periodarrangeStartTime",startTime};
					Periodarrange periodArrange = (Periodarrange) paDAO.getByNProperty(para);
					paDAO.delete(periodArrange);
				}
				r.put("isSuccess", true);
			}
		}
		
		
		return r;
	}
	
	//返回本学期周数
	public Map<String, Object> courseTableInfo(){
		DateUtil dateutil = new DateUtil();
		dateutil.setSysInfo();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalweek", dateutil.getTotalWeek());
		
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private List<Object> selectWeek(List<String> datelist){
		List<Object> list = new ArrayList<Object>();
		PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
		//get the id of each class who is in period table
		
		int s = datelist.size();
		for(int i = 0; i < s; i++){
			//get each date's p list
			int[] intdate = this.dateutil.StringToIntlist(datelist.get(i));
			Date date = new Date(intdate[0] - 1900, intdate[1] -1, intdate[2]);
			
			List<Periodarrange> plist = pDAO.getListByProperty("periodarrangeStartDate", date);
			
			int l = plist.size();
			for(int j = 0; j < l; j++){
				int classid = plist.get(j).getPeriodarrangeClassId();
				ClassDAO cDAO = new ClassDAO();
				List<Class> clist = cDAO.getListByProperty("classId", classid);
				
				int courseid = clist.get(0).getClassCourseId();
				CourseDAO courseDAO = new CourseDAO();
				List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
				
				String cname = courselist.get(0).getCourseName() + clist.get(0).getClassName();
				
				int[] tempdate = this.dateutil.StringToIntlist(this.dateutil.dateToString(plist.get(j).getPeriodarrangeStartDate()));
				int startday = this.dateutil.weekdayJudge(tempdate[0], tempdate[1], tempdate[2]) - 1;
				if(startday == 0){
					startday = 7;
				}
				
				tempdate = this.dateutil.StringToIntlist(this.dateutil.dateToString(plist.get(j).getPeriodarrangeEndDate()));
				int endday = this.dateutil.weekdayJudge(tempdate[0], tempdate[1], tempdate[2]) - 1;
				if(endday == 0){
					endday = 7;
				}
				
				int has = 0;
				int size = list.size();
				for(int z = 0; z < size; z++){
					Map<String, Object> map = (Map<String, Object>) list.get(z);
					
					if(map.get("class").equals(cname)){
						((List<String>) ((Map<String, Object>) list.get(z)).get("timelist")).addAll(this.dateutil.processDateToCTable(startday, plist.get(j).getPeriodarrangeStartTime(), endday, plist.get(j).getPeriodarrangeEndTime()));
						
						has = 1;
						break;
					}
				}
				
				if(has == 0){
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("class", cname);
					map.put("timelist", this.dateutil.processDateToCTable(startday, plist.get(j).getPeriodarrangeStartTime(), endday, plist.get(j).getPeriodarrangeEndTime()));
					System.out.println("!!!!"+map.get("timelist"));
					list.add(map);
				}
			}
		}
		
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object> defaultWeek(){
		PeriodarrangeWeekDAO pDAO = new PeriodarrangeWeekDAO();
		List<Object> dwlist = new ArrayList<Object>();
		
		//get the id of each class who is in period table
		String hql = "SELECT model from " + PeriodarrangeWeek.class.getName() + " as model group by model.periodarrangeWeekClassid";
		List<PeriodarrangeWeek> plist = pDAO.getListByHql(hql);
		
		int s = plist.size();
		
		for(int i = 0; i < s; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			
			//find class name
			ClassDAO cDAO = new ClassDAO();
			
			int classid = plist.get(i).getPeriodarrangeWeekClassid();
			List<Class> clist = cDAO.getListByProperty("classId", classid);
			
			int courseid = clist.get(0).getClassCourseId();
			CourseDAO courseDAO = new CourseDAO();
			List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
			
			String cname = courselist.get(0).getCourseName() + clist.get(0).getClassName();
									
			//find class time
			
			List<PeriodarrangeWeek> pclist = pDAO.getListByProperty("periodarrangeWeekClassid", classid);
			
			int l = pclist.size();
			
			List<String> timelist = new ArrayList<String>();
			for(int j = 0; j < l; j++){
				timelist.addAll(this.dateutil.processDateToCTable(pclist.get(j).getPeriodarrangeWeekStartDay(), pclist.get(j).getPeriodarrangeWeekStartTime(), pclist.get(j).getPeriodarrangeWeekEndDay(), pclist.get(j).getPeriodarrangeWeekEndTime()));
			}
			
			map.put("class", cname);
			map.put("timelist", timelist);
			
			dwlist.add(map);
		}
		
		return dwlist;
	}
}
