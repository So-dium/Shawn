package virnet.management.expArrange.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.OrderCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassarrangeCaseDAO;
import virnet.management.dao.ClassarrangeDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.CourseexpDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.PeriodarrangeDAO;

import virnet.management.entity.Course;
import virnet.management.entity.Class;
import virnet.management.entity.Courseexp;
import virnet.management.entity.Exp;
import virnet.management.entity.Periodarrange;
import virnet.management.entity.Classarrange;
import virnet.management.entity.ClassarrangeCase;
import virnet.management.entity.expArrange;
import virnet.management.dao.expArrangeDAO;

public class expArrangeService {
	private ExpDAO eDAO = new ExpDAO();
	private CourseDAO cDAO = new CourseDAO();
	private CourseexpDAO ceDAO = new CourseexpDAO();
	private ClassDAO clDAO = new ClassDAO();
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getExplist(String classInfo){
		System.out.println("expArrange.getExplist:");
		Map<String, Object> map = new HashMap<String, Object>();
		String hql1 = "select texp.expName " +
					" from Exp as texp, Class as tcl, Courseexp as tce "+
					" where tcl.classId = "+ classInfo + 
					" and tcl.classCourseId = tce.courseexpCourseId "+
					" and tce.courseexpExpId = texp.expId";
		System.out.println("!!hql:"+hql1);
		List<Object> expNamelist = new ArrayList<Object>();
		
		try {
			expNamelist = eDAO.getListByHql(hql1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<expNamelist.size();i++) {
			System.out.println(""+expNamelist.get(i));
		}
		System.out.println("!!sql1 finished:");
		String hql2 = "select texp.expId " +
				" from Exp as texp, Class as tcl, Courseexp as tce "+
				" where tcl.classId = "+ classInfo + 
				" and tcl.classCourseId = tce.courseexpCourseId "+
				" and tce.courseexpExpId = texp.expId";
		System.out.println("!!hql:"+hql2);
		@SuppressWarnings("unchecked")
		List<Object> expIdlist = new ArrayList<Object>();
		try {
			expIdlist = eDAO.getListByHql(hql2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i<expIdlist.size();i++) {
			System.out.println(""+expIdlist.get(i));
		}
		System.out.println("!!sql2 finished:");
		map.put("name", expNamelist);
		map.put("id", expIdlist);
		return map;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object>perriodOccupy(int classInfo, Date date1) {
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("expArrange.perriodOccupy:");
		java.sql.Date date2 = new java.sql.Date(date1.getTime());
		String date2str = fmt.format(date2);
		System.out.println("dateGet:"+date2str);
		Map<String, Object> map = new HashMap<String, Object>();
		PeriodarrangeDAO pdao = new PeriodarrangeDAO();
		String hql = "select tp from Periodarrange as tp , Classarrange as tcla, ClassarrangeCase as tcc  "+
					 "where tp.periodarrangeClassId = "+classInfo + " and "+
					 "tp.periodarrangeId =  tcla.classarrangePeriodArrangeId and "+
					 "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId ";
		
		List<Periodarrange> plist = new ArrayList<Periodarrange>();
		try {
			plist = (List<Periodarrange>)pdao.getListByHql(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//List<Periodarrange> plist = pdao.getListByProperty("periodarrangeClassId", classInfo);
		List<Integer> pOccupied = new ArrayList<Integer>();
		List<String> expName = new ArrayList<String>();
		
		for(int i = 0; i < plist.size(); i++) {
			System.out.println("date in sql :"+ fmt.format(plist.get(i).getPeriodarrangeStartDate()));
			System.out.println(fmt.format(plist.get(i).getPeriodarrangeStartDate()).equals(date2str));
			if(fmt.format(plist.get(i).getPeriodarrangeStartDate()).equals(date2str)) {
				System.out.println("matched:"+date2str);
				int start = plist.get(i).getPeriodarrangeStartTime();
				int end = plist.get(i).getPeriodarrangeEndTime();
				for(int j = start; j < end; j++) {
					pOccupied.add(j);
				}
				int periodArrangeID = plist.get(i).getPeriodarrangeId();
				String hql2 = "select tc.expName from Exp as tc, Classarrange as tcla, ClassarrangeCase as tcc " +
							  "where tcla.classarrangePeriodArrangeId = " + periodArrangeID + " and " +
							  "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId and " +
							  "tc.expId = tcc.classarrangeCaseExpId";
				List<String> Name = new ArrayList<String>();
				try {
					Name = eDAO.getListByHql(hql2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				expName.add(Name.get(0));
			}
		}
		map.put("occupy", pOccupied);
		map.put("expName", expName);
		return map;
	}
	public Map<String, Object> newARG(Map<String, String[]> paraMap) throws ParseException {
		System.out.println("newARG:");
		System.out.println("ParameterMap:"+paraMap);
		Map<String, Object>map = new HashMap<String, Object>();
		String classInfo = paraMap.get("classinfo")[0];
		//System.out.println("classinfo:"+classInfo);
		
		List<String> pidListStr = new ArrayList<String>();
		if(paraMap.get("pidlist[]") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist[]"));
		}
		else if(paraMap.get("pidlist") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist"));
		}
		
		String expMode = paraMap.get("expmode")[0];
		//System.out.println("expMode:"+expMode);
		String argTime = paraMap.get("argtime")[0];
		//System.out.println("argTime:"+argTime);
		String expNum = paraMap.get("expnum")[0];
		//System.out.println("expNum:"+expNum);
		String experNum = paraMap.get("expernum")[0];
		//System.out.println("experNum:"+experNum);
		String user = paraMap.get("user")[0];
		//System.out.println("user:"+user);
		
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt2 =new SimpleDateFormat("HH-mm-ss");
		
		System.out.println("parameters are:");
		System.out.println("classInfo:"+classInfo);
		System.out.println("timeSpan:");
		for(int i=0;i<pidListStr.size();i++) {
			System.out.println(pidListStr.get(i));
		}
		System.out.println("expMode:"+expMode);
		System.out.println("argTime:"+argTime);
		System.out.println("expNum:"+expNum);
		System.out.println("experNum:"+experNum);
		System.out.println("user:"+user);
		//将pidlist转换为Int
		List<Integer>pidList = new ArrayList<Integer>();
		for(int i=0; i<pidListStr.size(); i++) {
			int pid = Integer.parseInt(pidListStr.get(i));
			pidList.add(pid);
		}
		//添加exparrange
		
		expArrangeDAO eaDAO = new expArrangeDAO();
		expArrange newexparg = new expArrange();
		if(Integer.parseInt(expMode) == 0) {
			String StartDateStr = argTime.split(" - ")[0].split(" ")[0];
			String StartTimeStr = argTime.split(" - ")[0].split(" ")[1];
			String EndDateStr = argTime.split(" - ")[1].split(" ")[0];
			String EndTimeStr = argTime.split(" - ")[1].split(" ")[1];
			
			System.out.println("StartDateStr:"+StartDateStr);
			System.out.println("StartTimeStr:"+StartTimeStr);
			System.out.println("EndDateStr:"+EndDateStr);
			System.out.println("EndTimeStr:"+EndTimeStr);
			
			Date StartDate = fmt.parse(StartDateStr);
			Date EndDate = fmt.parse(EndDateStr);
			java.sql.Time StartTime = java.sql.Time.valueOf(StartTimeStr);
			System.out.println("StartTime:"+StartTime);
			java.sql.Time EndTime = java.sql.Time.valueOf(EndTimeStr);
			System.out.println("EndTime:"+EndTime);
			newexparg.setAppointmentStartDate(new java.sql.Date(StartDate.getTime()));
			newexparg.setAppointmentEndDate(new java.sql.Date(EndDate.getTime()));
			newexparg.setAppointmentStartTime(StartTime);
			newexparg.setAppointmentEndTime(EndTime);
		}
		eaDAO.add(newexparg);
		System.out.println("finish add expArrange");
		int eaID = -1;
		String maxEaIdHql = "select max(tea.expArrangeId) from expArrange as tea";
		try {
			eaID = (int)eaDAO.getListByHql(maxEaIdHql).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("eaID:"+eaID);
		
		//添加classarrange
		Classarrange newcl = new Classarrange();
		for(int i=0;i<pidList.size();i++) {
			if(eaID == -1 || pidList.size()==0) {
				System.out.println("Failed in add classarrange");
				break;
			}
			newcl.setClassarrangePeriodArrangeId(pidList.get(i));
			newcl.setClassarrangeExpArrangeId(eaID);
			ClassarrangeDAO clDao = new ClassarrangeDAO();
			clDao.add(newcl);
		}
		System.out.println("classarrange");
		
		//添加classarrange_case
		if(eaID != -1) {
			ClassarrangeCase newcac = new ClassarrangeCase();
			newcac.setClassarrangeCaseExpArrangeId(eaID);
			newcac.setClassarrangeCaseExpId(Integer.parseInt(expNum));
			ClassarrangeCaseDAO cacDao = new ClassarrangeCaseDAO();
			cacDao.add(newcac);
			System.out.println("finish add classarrange");
		}
		/**/
		return map;
	}
	public Map<String, Object> newARG_2(Map<String, String[]> paraMap) throws ParseException {
		System.out.println("newARG_2:");
		System.out.println("ParameterMap:"+paraMap);
		Map<String, Object>map = new HashMap<String, Object>();
		System.out.println("classinfo:");
		System.out.println(paraMap.get("classinfo"));
		String classInfo = paraMap.get("classinfo")[0];
		//System.out.println("classinfo:"+classInfo);
		
		List<String> pidListStr = new ArrayList<String>();
		if(paraMap.get("pidlist[]") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist[]"));
		}
		else if(paraMap.get("pidlist") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist"));
		}
//		for(int i=0;i<pidListStr.size();i++) {
//			System.out.println("pidListStr"+i+":"+pidListStr.get(i));
//		}
		
		String expMode = paraMap.get("expmode")[0];
//		System.out.println("expMode:"+expMode);
		
		String argTime = new String();
		if(paraMap.get("argtime") != null) argTime = paraMap.get("argtime")[0];
//		System.out.println("argTime:"+argTime);
		
		String expNum = paraMap.get("expnum")[0];
//		System.out.println("expNum:"+expNum);
		
		List<String> experNum = new ArrayList<String>();
		if(paraMap.get("expernum[]") != null) {
			experNum = Arrays.asList(paraMap.get("expernum[]"));
		}
		else if(paraMap.get("expernum") != null) {
			experNum = Arrays.asList(paraMap.get("expernum"));
		}
//		for(int i=0;i<experNum.size();i++) {
//			System.out.println("experNum"+i+":"+experNum.get(i));
//		}
		
		String user = paraMap.get("user")[0];
//		System.out.println("user:"+user);
		
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmt2 =new SimpleDateFormat("HH-mm-ss");
		
		System.out.println("parameters are:");
		System.out.println("classinfo:"+classInfo);
		for(int i=0;i<pidListStr.size();i++) {
			System.out.println("pidListStr"+i+":"+pidListStr.get(i));
		}
		System.out.println("expMode:"+expMode);
		System.out.println("argTime:"+argTime);
		System.out.println("expNum:"+expNum);
		for(int i=0;i<experNum.size();i++) {
			System.out.println("experNum"+i+":"+experNum.get(i));
		}
		System.out.println("user:"+user);
		//将pidlist转换为Int
		
		List<List<Integer>>pidList = new ArrayList<List<Integer>>();
		for(int i=0; i<pidListStr.size(); i++) {
			String[] pidSubList = pidListStr.get(i).split(",");
			List<Integer> pidSubIntList = new ArrayList<Integer>();
			for(int j=0;j<pidSubList.length;j++) {
				int pid = Integer.parseInt(pidSubList[j]);
				pidSubIntList.add(pid);
			}
			pidList.add(pidSubIntList);
		}
		expArrangeDAO eaDAO = new expArrangeDAO();
		List<Integer>eaIdList = new ArrayList<Integer>();
		
		if(Integer.parseInt(expMode) == 1) {          //固定预约
			///添加expArrange
			System.out.println("fixed arrange:");
			for(int k=0;k<pidList.size();k++) {
				expArrange newexparg = new expArrange();
				eaDAO.add(newexparg);
				System.out.println("newexparg_id:"+newexparg.getExpArrangeId());
				
				System.out.println("finish add expArrange for num ");
				int eaID = -1;
				try {
					eaID = newexparg.getExpArrangeId();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("eaID"+":"+eaID);
				eaIdList.add(eaID);
				
				//添加classarrange
				Classarrange newcl = new Classarrange();
				for(int i=0;i<pidList.get(k).size();i++) {
					if(eaID == -1 || pidList.size()==0) {
						System.out.println("Failed in add classarrange");
						break;
					}
					newcl.setClassarrangePeriodArrangeId(pidList.get(k).get(i));
					newcl.setClassarrangeExpArrangeId(eaID);
					ClassarrangeDAO clDao = new ClassarrangeDAO();
					clDao.add(newcl);
				}
				System.out.println("classarrange");
				
				//添加classarrange_case
				for(int i=0;i<eaIdList.size();i++) {
					if(eaID != -1) {
						ClassarrangeCase newcac = new ClassarrangeCase();
						newcac.setClassarrangeCaseExpArrangeId(eaID);
						newcac.setClassarrangeCaseExpId(Integer.parseInt(expNum));
						ClassarrangeCaseDAO cacDao = new ClassarrangeCaseDAO();
						cacDao.add(newcac);
						System.out.println("finish add classarrange");
					}
				}
			}

			OrderCDAO ocDAO = new OrderCDAO();
			ocDAO.defaultSave(user, eaIdList, Integer.parseInt(classInfo), Integer.parseInt(expNum));


		}
		else if(Integer.parseInt(expMode) == 0) {          //自由预约
			///添加expArrange
			System.out.println("fixed arrange:");
			for(int k=0;k<pidList.size();k++) {
				expArrange newexparg = new expArrange();
				String StartDateStr = argTime.split(" - ")[0].split(" ")[0];
				String StartTimeStr = argTime.split(" - ")[0].split(" ")[1];
				String EndDateStr = argTime.split(" - ")[1].split(" ")[0];
				String EndTimeStr = argTime.split(" - ")[1].split(" ")[1];
				
				System.out.println("StartDateStr:"+StartDateStr);
				System.out.println("StartTimeStr:"+StartTimeStr);
				System.out.println("EndDateStr:"+EndDateStr);
				System.out.println("EndTimeStr:"+EndTimeStr);
				
				Date StartDate = fmt.parse(StartDateStr);
				Date EndDate = fmt.parse(EndDateStr);
				java.sql.Time StartTime = java.sql.Time.valueOf(StartTimeStr);
				System.out.println("StartTime:"+StartTime);
				java.sql.Time EndTime = java.sql.Time.valueOf(EndTimeStr);
				System.out.println("EndTime:"+EndTime);
				newexparg.setAppointmentStartDate(new java.sql.Date(StartDate.getTime()));
				newexparg.setAppointmentEndDate(new java.sql.Date(EndDate.getTime()));
				newexparg.setAppointmentStartTime(StartTime);
				newexparg.setAppointmentEndTime(EndTime);
				eaDAO.add(newexparg);
				System.out.println("newexparg_id:"+newexparg.getExpArrangeId());
				System.out.println("finish add expArrange for num ");
				
				int eaID = -1;
				
				try {
					eaID = newexparg.getExpArrangeId();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("eaID"+":"+eaID);
				eaIdList.add(eaID);
				
				//添加classarrange
				Classarrange newcl = new Classarrange();
				for(int i=0;i<pidList.get(k).size();i++) {
					if(eaID == -1 || pidList.size()==0) {
						System.out.println("Failed in add classarrange");
						break;
					}
					newcl.setClassarrangePeriodArrangeId(pidList.get(k).get(i));
					newcl.setClassarrangeExpArrangeId(eaID);
					ClassarrangeDAO clDao = new ClassarrangeDAO();
					clDao.add(newcl);
				}
				System.out.println("classarrange");
				
				//添加classarrange_case
				for(int i=0;i<eaIdList.size();i++) {
					if(eaID != -1) {
						ClassarrangeCase newcac = new ClassarrangeCase();
						newcac.setClassarrangeCaseExpArrangeId(eaID);
						newcac.setClassarrangeCaseExpId(Integer.parseInt(expNum));
						ClassarrangeCaseDAO cacDao = new ClassarrangeCaseDAO();
						cacDao.add(newcac);
						System.out.println("finish add classarrange");
					}
				}
			}

			//填写预约表（order表）
			OrderCDAO ocDAO = new OrderCDAO();
			ocDAO.dynamicDistribution(user, eaIdList, experNum, Integer.parseInt(classInfo));

		}
		/**/
		return map;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object>editARG(Map<String, String[]>paraMap) throws ParseException{
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object>map = new HashMap<String, Object>();
		System.out.println("editARG:");
		System.out.println("ParameterMap:"+paraMap);
		String user = paraMap.get("user")[0];
		String classInfo = paraMap.get("classinfo")[0];
		List<String> pidListStr = new ArrayList<String>(); 
		if(paraMap.get("pidlist[]") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist[]"));
		}
		else if(paraMap.get("pidlist") != null) {
			pidListStr = Arrays.asList(paraMap.get("pidlist"));
		}
		String expMode = paraMap.get("expmode")[0];
		String argTime = paraMap.get("argtime")[0];
		String expNum = paraMap.get("expnum")[0];
		String experNum = paraMap.get("expernum")[0];
		String oldDate = paraMap.get("oldDate")[0];
		String oldStartTime = paraMap.get("oldStartTime")[0];
		String oldEndTime = paraMap.get("oldEndTime")[0];
		
		//检查参数
		System.out.println("parameters are:");
		System.out.println("classInfo:"+classInfo);
		System.out.println("pidListStr:");
		for(int i=0;i<pidListStr.size();i++) {
			System.out.println(pidListStr.get(i));
		}
		System.out.println("expMode:"+expMode);
		System.out.println("argTime:"+argTime);
		System.out.println("expNum:"+expNum);
		System.out.println("experNum:"+experNum);
		System.out.println("user:"+user);
		System.out.println("oldStartTime:"+oldStartTime);
		System.out.println("oldEndTime:"+oldEndTime);
		//将pidlist转换为Int
		List<Integer>pidList = new ArrayList<Integer>();
		for(int i=0; i<pidListStr.size(); i++) {
			int pid = Integer.parseInt(pidListStr.get(i));
			pidList.add(pid);
			System.out.println(pid);
		}
		//获取选中的periodarrangeId 和 exparrangeId
		PeriodarrangeDAO pdao = new PeriodarrangeDAO();
		String selPidAndEidHql = "select tp, tea ,tcc "+
								 "from Periodarrange as tp, Classarrange as tcla, "+
								 "ClassarrangeCase as tcc, expArrange as tea "+ 
								 "where tp.periodarrangeStartDate = '" +oldDate+"' and "+
								 "tp.periodarrangeStartTime >= '"+oldStartTime+"' and "+
								 "tp.periodarrangeStartTime < '"+oldEndTime+"' and "+
								 "tp.periodarrangeClassId = "+classInfo+" and "+
								 "tp.periodarrangeId =  tcla.classarrangePeriodArrangeId and "+
								 "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId "+"and "+
								 "tcc.classarrangeCaseExpArrangeId = tea.expArrangeId ";
		
		List<Object[]> oldInfo = new ArrayList<Object[]>();
		try {
			oldInfo = (List<Object[]>)pdao.getListByHql(selPidAndEidHql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> oldPidList = new ArrayList<Integer>();
		for(int i=0; i<oldInfo.size(); i++) {
			Periodarrange tp = (Periodarrange)oldInfo.get(i)[0];
			oldPidList.add(tp.getPeriodarrangeId());
		}
		expArrange tea = (expArrange)oldInfo.get(0)[1];
		ClassarrangeCase tcc = (ClassarrangeCase)oldInfo.get(0)[2];
		int eaId = tea.getExpArrangeId();
		System.out.println("eaId:"+eaId);
		System.out.println("oldPidList:"+oldPidList);
		System.out.println("tccId:"+tcc.getClassarrangeCaseId());
		
		for(int i=0;i<oldPidList.size();i++)
		if(pidList.contains(oldPidList.get(i))) {
			//update
			System.out.println("update:");
			expArrangeDAO eaDAO = new expArrangeDAO();
			ClassarrangeCaseDAO ccDAO = new ClassarrangeCaseDAO();
			if(Integer.parseInt(expMode) == 0) {
				String StartDateStr = argTime.split(" - ")[0].split(" ")[0];
				String StartTimeStr = argTime.split(" - ")[0].split(" ")[1];
				String EndDateStr = argTime.split(" - ")[1].split(" ")[0];
				String EndTimeStr = argTime.split(" - ")[1].split(" ")[1];
				
				System.out.println("StartDateStr:"+StartDateStr);
				System.out.println("StartTimeStr:"+StartTimeStr);
				System.out.println("EndDateStr:"+EndDateStr);
				System.out.println("EndTimeStr:"+EndTimeStr);
				
				Date StartDate = fmt.parse(StartDateStr);
				Date EndDate = fmt.parse(EndDateStr);
				java.sql.Time StartTime = java.sql.Time.valueOf(StartTimeStr);
				System.out.println("StartTime:"+StartTime);
				java.sql.Time EndTime = java.sql.Time.valueOf(EndTimeStr);
				System.out.println("EndTime:"+EndTime);
				tea.setAppointmentStartDate(new java.sql.Date(StartDate.getTime()));
				tea.setAppointmentEndDate(new java.sql.Date(EndDate.getTime()));
				tea.setAppointmentStartTime(StartTime);
				tea.setAppointmentEndTime(EndTime);
			}
			else if(Integer.parseInt(expMode) == 1){
				tea.setAppointmentStartDate(null);
				tea.setAppointmentEndDate(null);
				tea.setAppointmentStartTime(null);
				tea.setAppointmentEndTime(null);
			}
			eaDAO.update(tea);
			tcc.setClassarrangeCaseExpId(Integer.parseInt(expNum));
			ccDAO.update(tcc);
		}
		else {
			//delete
			System.out.println("Delete: _pid="+oldPidList.get(i));
			ClassarrangeDAO caDAO = new ClassarrangeDAO();
			ClassarrangeCaseDAO ccDAO = new ClassarrangeCaseDAO();
			expArrangeDAO eaDAO = new expArrangeDAO();
			//找出应删除的连接period 和 expArrange之间的classarrange和classarrangecase
			String hql = "select tcla, tcc "+
						 "from Classarrange as tcla, ClassarrangeCase as tcc "+
						 "where tcla.classarrangePeriodArrangeId = "+oldPidList.get(i)+" and "+
						 "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId and "+
						 "tcc.classarrangeCaseExpArrangeId = "+eaId;
			
			List<Object[]> caAndcc = new ArrayList<Object[]>();
			try {
				caAndcc = caDAO.getListByHql(hql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Classarrange tca = (Classarrange)caAndcc.get(0)[0];
			System.out.println("tcaId:"+tca.getClassarrangeId());
			ClassarrangeCase tcc1 = (ClassarrangeCase)caAndcc.get(0)[1];
			System.out.println("tccId:"+tcc.getClassarrangeCaseId());
			try {
				caDAO.delete(tca);
				System.out.println("deleted!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//如果tca里没有eaId存在，则删除该classarrangeCase 和 expArrange
			String examHql = "select tca from Classarrange as tca "+
							 "where tca.classarrangeExpArrangeId = "+eaId;
			System.out.println("!!!!");
			List<Classarrange> tcaList = new ArrayList<Classarrange>();
			try {
				tcaList = caDAO.getListByHql(examHql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("tcaList.size:"+tcaList.size());
			if(tcaList.size() == 0) {
				System.out.println("delete expArrange&classarrangeCase");
				try {
					ccDAO.delete(tcc1);
					eaDAO.delete(tea);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("section2:");
		//添加新的排课
		int flag = 0;//判断是否有新的元素
		for(int i=0;i<pidList.size();i++) {
			if(!oldPidList.contains(pidList.get(i))) {
				System.out.println(pidList.get(i));
				flag = 1;
			}
		}
		System.out.println("flag:"+flag);
		if(flag == 1) {
			System.out.println("new:");
			//添加exparrange
			expArrangeDAO eaDAO = new expArrangeDAO();
			expArrange newexparg = new expArrange();
			if(Integer.parseInt(expMode) == 0) {
				String StartDateStr = argTime.split(" - ")[0].split(" ")[0];
				String StartTimeStr = argTime.split(" - ")[0].split(" ")[1];
				String EndDateStr = argTime.split(" - ")[1].split(" ")[0];
				String EndTimeStr = argTime.split(" - ")[1].split(" ")[1];
				
				System.out.println("StartDateStr:"+StartDateStr);
				System.out.println("StartTimeStr:"+StartTimeStr);
				System.out.println("EndDateStr:"+EndDateStr);
				System.out.println("EndTimeStr:"+EndTimeStr);
				
				Date StartDate = fmt.parse(StartDateStr);
				Date EndDate = fmt.parse(EndDateStr);
				java.sql.Time StartTime = java.sql.Time.valueOf(StartTimeStr);
				System.out.println("StartTime:"+StartTime);
				java.sql.Time EndTime = java.sql.Time.valueOf(EndTimeStr);
				System.out.println("EndTime:"+EndTime);
				newexparg.setAppointmentStartDate(new java.sql.Date(StartDate.getTime()));
				newexparg.setAppointmentEndDate(new java.sql.Date(EndDate.getTime()));
				newexparg.setAppointmentStartTime(StartTime);
				newexparg.setAppointmentEndTime(EndTime);
			}
			eaDAO.add(newexparg);
			System.out.println("finish add expArrange");
			int eaID = -1;
			String maxEaIdHql = "select max(tea.expArrangeId) from expArrange as tea";
			try {
				eaID = (int)eaDAO.getListByHql(maxEaIdHql).get(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("eaID:"+eaID);
			
			//添加classarrange
			Classarrange newcl = new Classarrange();
			for(int i=0;i<pidList.size();i++) {
				if(oldPidList.contains(pidList.get(i)))continue;
				if(eaID == -1 || pidList.size()==0) {
					System.out.println("Failed in add classarrange");
					break;
				}
				newcl.setClassarrangePeriodArrangeId(pidList.get(i));
				newcl.setClassarrangeExpArrangeId(eaID);
				ClassarrangeDAO clDao = new ClassarrangeDAO();
				clDao.add(newcl);
			}
			System.out.println("classarrange");
			//添加ClassarrangeCase
			if(eaID != -1) {
				ClassarrangeCase newcac = new ClassarrangeCase();
				newcac.setClassarrangeCaseExpArrangeId(eaID);
				newcac.setClassarrangeCaseExpId(Integer.parseInt(expNum));
				ClassarrangeCaseDAO cacDao = new ClassarrangeCaseDAO();
				cacDao.add(newcac);
				System.out.println("finish add classarrange");
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>showTimeSpan(String classInfo, Date chooseDate){
		System.out.println("service showTimeSpan:");
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object>map = new HashMap<String, Object>();
		//java.sql.Date chooseDateSql = new java.sql.Date(chooseDate.getTime());
		List<Integer> timeSpanExist = new ArrayList<Integer>();
		List<Integer> timtSpanOccupied = new ArrayList<Integer>();
		List<Periodarrange> plistExist = new ArrayList<Periodarrange>();
		List<Periodarrange> plistOccupied = new ArrayList<Periodarrange>();
		List<Integer> pidList = new ArrayList<Integer>();
		List<String> expName = new ArrayList<String>();
		PeriodarrangeDAO pdao = new PeriodarrangeDAO();
		//找到所有当天的排课时段
		String hqlExist = "select tp "+
						  "from Periodarrange as tp "+
						  "where tp.periodarrangeClassId = "+classInfo+" and "+
						  "tp.periodarrangeStartDate = '" + fmt.format(chooseDate)+ "'";
		
		try {
			plistExist = pdao.getListByHql(hqlExist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(hqlExist);
		System.out.println("plistExist_size:"+plistExist.size());
		for(int i=0; i<plistExist.size(); i++) {
			int startTime = plistExist.get(i).getPeriodarrangeStartTime();
			timeSpanExist.add(startTime);
			int pid = plistExist.get(i).getPeriodarrangeId();
			pidList.add(pid);
			System.out.println("pid:"+pid);
			System.out.println("timeSpanExist:"+startTime);
		}
		//找到被占用的排课时段并且找出该时段的实验
		String hqlOccypied = "select tp from Periodarrange as tp , Classarrange as tcla, ClassarrangeCase as tcc  "+
				 "where tp.periodarrangeClassId = "+classInfo + " and "+
				 "tp.periodarrangeStartDate = '" + fmt.format(chooseDate) + "' and "+
				 "tp.periodarrangeId =  tcla.classarrangePeriodArrangeId and "+
				 "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId ";
		try {
			plistOccupied = pdao.getListByHql(hqlOccypied);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("plistOccupied_size:"+plistOccupied.size());
		ExpDAO edao = new ExpDAO();
		for(int i=0; i<plistOccupied.size(); i++) {
			int startTime = plistOccupied.get(i).getPeriodarrangeStartTime();
			timtSpanOccupied.add(startTime);
			int periodArrangeID = plistOccupied.get(i).getPeriodarrangeId();
			String expHql = "select tc.expName from Exp as tc, Classarrange as tcla, ClassarrangeCase as tcc " +
					  "where tcla.classarrangePeriodArrangeId = " + periodArrangeID + " and " +
					  "tcla.classarrangeExpArrangeId = tcc.classarrangeCaseExpArrangeId and " +
					  "tc.expId = tcc.classarrangeCaseExpId";
			String ename = (String)edao.getListByHql(expHql).get(0);
			expName.add(ename);
			System.out.println("timeSpanOccupied:"+timtSpanOccupied);
			System.out.println("expName:"+ename);
		}
		
		//sort timeSpan List
		int STtemp,idTemp;
		for(int i=0;i<timeSpanExist.size()-1;i++) {
			for(int j=i+1;j<timeSpanExist.size()-i;j++) {
				if(timeSpanExist.get(i) > timeSpanExist.get(j)) {
					STtemp = timeSpanExist.get(i);
					idTemp = pidList.get(i);
					timeSpanExist.set(i, timeSpanExist.get(j));
					pidList.set(i, pidList.get(j));
					timeSpanExist.set(j, STtemp);
					pidList.set(j, idTemp);
				}
			}
		}
		System.out.println("timeSpanExist:"+timeSpanExist);
		System.out.println("pidList:"+pidList);
		map.put("exist", timeSpanExist);
		map.put("occupied", timtSpanOccupied);
		map.put("id", pidList);
		map.put("expname", expName);
		return map;
	}
	
	public Map<String, Object>expDateARG(String classInfo){
		Map<String, Object>map = new HashMap<String, Object>();
		List<String> dateValue = new ArrayList<String>();
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		List<Date>Pdatelist = new ArrayList<Date>();
		PeriodarrangeDAO pdao = new PeriodarrangeDAO();
		String hql = "select distinct tp.periodarrangeStartDate "+
					" from Periodarrange as tp "+
					"where tp.periodarrangeClassId = "+classInfo;
		try {
			Pdatelist = pdao.getListByHql(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("!!!!PdateList:"+Pdatelist);
		if(Pdatelist.size() == 0) System.out.println("Error fetch Periodarrange list");
		else {
			for(int i = 0;i<Pdatelist.size();i++){
				String dateStr = fmt.format(Pdatelist.get(i));
				dateValue.add(dateStr);
			}
		}
		map.put("date", dateValue);
		return map;
	}
	
	public ExpDAO geteDAO() {
		return eDAO;
	}
	public void seteDAO(ExpDAO eDAO) {
		this.eDAO = eDAO;
	}
	public CourseDAO getcDAO() {
		return cDAO;
	}
	public void setcDAO(CourseDAO cDAO) {
		this.cDAO = cDAO;
	}
	public CourseexpDAO getCeDAO() {
		return ceDAO;
	}
	public void setCeDAO(CourseexpDAO ceDAO) {
		this.ceDAO = ceDAO;
	}
	public ClassDAO getClDAO() {
		return clDAO;
	}
	public void setClDAO(ClassDAO clDAO) {
		this.clDAO = clDAO;
	}
	
	
}
