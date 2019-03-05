package virnet.management.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import virnet.management.dao.SemesterDAO;
import virnet.management.entity.Semester;

public class DateUtil {
	private Date sysdate = new Date();
	private int totalWeek = 0;
	private SemesterDAO sDAO = new SemesterDAO();
	
	//整个课时是连续的，不能跨周，所以一般来说  dayStart 和dayEnd是同一天
	public List<String> processDateToCTable(int dayStart, int timeStart, int dayEnd, int timeEnd){
		List<String> list = new ArrayList<String>();
		
		for(int i = dayStart; i <= dayEnd; i++){
			
			for(int j = (i == dayStart? timeStart : 0); j < (i == dayEnd? timeEnd : 8); j++){
				list.add(j + "" + i + "");
			}
		}
		
		return list;
	}
	
	public String processDate(int day_start, int classNO_start, int day_end, int classNO_end){
		String time;
		
		if(day_start == day_end){
			time = new String(getWeek(day_start) + " \n" + getStartTime(classNO_start) + " ~ " + getEndTime(classNO_end));
		}
		else{
			time = new String(getWeek(day_start) + " " + getStartTime(classNO_start) + " ~ \n" + getWeek(day_end) + " " + getEndTime(classNO_end));
		}
		
		return time;
	}
	
	@SuppressWarnings("unchecked")
	public void setSysInfo(){
		List<Semester> slist = this.sDAO.getList();
		
		//some judge is required on whether the data is correct
		
		this.sysdate = slist.get(0).getSemesterStartdate();
		this.totalWeek = slist.get(0).getSemesterTotalweek();
	}
	
	//根据日期判断属于哪一个周，并返回这个周的7天
	public Map<String, Object> CalDate(String date){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//check the date format --- required but no done yet
		int[] intdate = StringToIntlist(date);
		
		this.setSysInfo();
		Date sysdate = this.getSysdate();
		int[] intsysdate = StringToIntlist(this.dateToString(sysdate));
		
		int weekIn = weekdayJudge(intdate[0], intdate[1], intdate[2]) - 1;
		if(weekIn == 0){
			weekIn = 7;
		}
		
		List<String> list = new ArrayList<String>();
		Calendar calin = Calendar.getInstance(Locale.CHINA);
		calin.set(intdate[0], intdate[1] - 1, intdate[2]);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calin.add(Calendar.DAY_OF_YEAR, -(weekIn - 1));
		for(int i = 0; i < 7; i++){			
			list.add(sdf.format(calin.getTime()));
			calin.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		int weekNO = getWeekNO(intsysdate, intdate);
		
		map.put("weekNO", weekNO);
		map.put("datelist", list);
		
		return map;
	}
	
	//根据周数返回这个周的
	public Map<String, Object> CalWeek(int week){
		Map<String, Object> map = new HashMap<String, Object>();
		
		this.setSysInfo();
		Date sysdate = this.getSysdate();
		int[] intsysdate = StringToIntlist(this.dateToString(sysdate));
		
		Calendar syscal = Calendar.getInstance(Locale.CHINA);
		syscal.set(intsysdate[0], intsysdate[1] - 1, intsysdate[2]);
		syscal.add(Calendar.DAY_OF_YEAR, (week - 1) * 7);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		map = CalDate(sdf.format(syscal.getTime()));
		System.out.println(map.get("weekNO"));
		
		return map;
	}
	
	public int getTotalWeek(){
		return this.totalWeek;
	}
	
	public Date getSysdate(){
		return this.sysdate;
	}
	
	private String getStartTime(int number){
		String time;
		switch(number){
		case 0: time = new String("08:00:00");break;
		case 1: time = new String("10:00:00");break;
		case 2: time = new String("12:00:00");break;
		case 3: time = new String("14:00:00");break;
		case 4: time = new String("16:00:00");break;
		case 5: time = new String("18:00:00");break;
		case 6: time = new String("20:00:00");break;
		case 7: time = new String("22:00:00");break;
		default: time = new String("UnKnown Time");
		}
		return time;
	}
	
	private String getEndTime(int number){
		String time;
		switch(number){
		case 1: time = new String("09:30:00");break;
		case 2: time = new String("11:30:00");break;
		case 3: time = new String("13:30:00");break;
		case 4: time = new String("15:30:00");break;
		case 5: time = new String("17:30:00");break;
		case 6: time = new String("19:30:00");break;
		case 7: time = new String("21:30:00");break;
		case 8: time = new String("23:30:00");break;
		default: time = new String("UnKnown Time");
		}
		return time;
	}
	
	private String getWeek(int week){
		String day;
		switch(week){
		case 1: day = new String("周一");break;
		case 2: day = new String("周二");break;
		case 3: day = new String("周三");break;
		case 4: day = new String("周四");break;
		case 5: day = new String("周五");break;
		case 6: day = new String("周六");break;
		case 7: day = new String("周日");break;
		default: day = new String("UnKnown day");break;
		}
		
		return day;
	}
	
	//返回周几
	public int weekdayJudge(int year, int month, int day){
		String[] weekDays = DateFormatSymbols.getInstance(Locale.CHINA)
                .getWeekdays();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        
        System.out.print(sdf.format(cal.getTime()) + "是 ");
        System.out.println(weekDays[week]);
        
        return week;
	}
	
	public String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public int[] StringToIntlist(String date){
		String[] datelist = date.split("-");
		
		int[] intdate = new int[3];
		
		intdate[0] = Integer.parseInt(datelist[0]);
		intdate[1] = Integer.parseInt(datelist[1]);
		intdate[2] =Integer.parseInt(datelist[2]);
		
		System.out.println("year : " + intdate[0] + ", month : " + intdate[1] + ", day : " + intdate[2]);
		
		return intdate;
	}
	
	private int getWeekNO(int[] sys, int[] in){
		Calendar sysdate = Calendar.getInstance(Locale.CHINA);
		sysdate.set(sys[0], sys[1] - 1, sys[2]);
		int weekSys = weekdayJudge(sys[0], sys[1], sys[2]) - 1;
		if(weekSys == 0){
			weekSys = 7;
		}
		sysdate.add(Calendar.DAY_OF_YEAR, -(weekSys - 1));
		
		Calendar indate = Calendar.getInstance(Locale.CHINA);
		indate.set(in[0], in[1] - 1, in[2]);
		int weekIn = weekdayJudge(in[0], in[1], in[2]) - 1;
		if(weekIn == 0){
			weekIn = 7;
		}
		indate.add(Calendar.DAY_OF_YEAR, -(weekIn - 1));
		
		int yearadd = 0;
		
		if(indate.get(Calendar.WEEK_OF_YEAR) == 1 && indate.get(Calendar.MONTH) == 11){
			yearadd++;
		}
		else if(sysdate.get(Calendar.WEEK_OF_YEAR) == 1 && sysdate.get(Calendar.MONTH) == 11){
			yearadd--;
		}
		
		int week = indate.get(Calendar.WEEK_OF_YEAR) - sysdate.get(Calendar.WEEK_OF_YEAR) + 1 + 52 * (indate.get(Calendar.YEAR) - sysdate.get(Calendar.YEAR) + yearadd);
		
		//System.out.println("week is " + week + ", and input week is " + indate.get(Calendar.WEEK_OF_YEAR) + ", and sys week is " + sysdate.get(Calendar.WEEK_OF_YEAR));
		
		if(week <= 0){
			week = 0;
		}
		
		return week;
		
	}
public String TimeFormat(List<Object> periodList){
		
		
		List<Date> startList = new ArrayList<Date>();
		List<Date> endList = new ArrayList<Date>();
		
		for(int i=0;i<periodList.size();i++){
				
			Object[] period =  (Object[]) periodList.get(i);
				
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			String Date = sdf1.format(period[0]);
			String Time = getStartTime((Integer)period[1]);
			String dateTime = Date + " " +Time;
				
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = null;
			try {
				startTime = sdf.parse(dateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(startTime);
			startList.add(startTime);
				
			Date = sdf1.format(period[2]);
			Time = getEndTime((Integer)period[3]);
			dateTime = Date + " " +Time;
				
			Date endTime = null;
			try {
				endTime = sdf.parse(dateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(endTime);
			endList.add(endTime);
		}
		
		System.out.println(endList.size());
		
		Collections.sort(startList, new Comparator<Date>() {  
	        @Override  
	        public int compare(Date c1, Date c2) {  
	            return c1.compareTo(c2);  
	        }  
	    });
		
		Collections.sort(endList, new Comparator<Date>() {  
	        @Override  
	        public int compare(Date c1, Date c2) {  
	            return c1.compareTo(c2);  
	        }  
	    }); 	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(startList.get(0)) + "~" +sdf.format(endList.get(endList.size()-1));
	}

	//计算时间差 格式   HH:mm:ss
	public String TimeDifference( Date fromTime, Date toTime){
		
		long from = fromTime.getTime();  
		long to = toTime.getTime();
		int hours = (int) (to-from)/(1000 * 60 * 60); 
		int minutes = (int) ((to - from) - (hours * 1000 * 60 * 60))/(1000 * 60);
		int seconds = (int)  ((to - from) - (minutes * 1000 * 60) - (hours * 1000 * 60 * 60))/1000 ;
		
		if(hours < 0 || minutes < 0 || seconds < 0)
			return "";
		
		System.out.println(fromTime + " " + toTime);
		
		String result= "";
		if(hours != 0){
			if(hours < 10)
				result = result + "0" + hours + ":";
			else
				result = result + hours + ":";
		}
		if(minutes < 10)
			result = result + "0" + minutes + ":";
		else
			result = result + minutes + ":";

		if(seconds < 10)
			result = result + "0" + seconds;
		else
			result = result + seconds;
		
		return result;
	}

}
