package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;

import virnet.management.dao.AppointmentDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.dao.OrdermemberDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.dao.expArrangeDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.entity.Appointment;
import virnet.management.entity.Exp;
import virnet.management.entity.Groupmember;
import virnet.management.entity.Class;
import virnet.management.entity.Classgroup;
import virnet.management.util.DateUtil;
import virnet.management.util.ViewUtil;
import virnet.management.combinedao.CourseInfoCDAO;
import virnet.management.entity.Order;
import virnet.management.entity.Ordermember;
import virnet.management.entity.Periodarrange;
import virnet.management.information.service.ClassDetail;

public class OrderCDAO {
	private static String EditTittle = "预约信息";

	private AppointmentDAO aDAO = new AppointmentDAO();
	private CourseInfoCDAO cDAO = new CourseInfoCDAO();
	private ClassDAO classDAO = new ClassDAO();
	private OrderDAO oDAO = new OrderDAO();
	private OrdermemberDAO omDAO = new OrdermemberDAO();
	
	private ViewUtil vutil = new ViewUtil();

	@SuppressWarnings("unchecked")
	public Map<String, Object> Add(String user, String id, Integer ClassId, Integer ExpId,Integer ExpArrangeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取系统当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String[] systemTime = df.format(new Date()).split(" ");// new Date()为获取当前系统时间
		String currentDate = systemTime[0];
		String currentTime = systemTime[1];
		
		System.out.println(currentDate + " " + currentTime);
		
		//首先查询符合要求的 班级-实验对 （仅对学生预约有效，管理员预约不需要预约时间限制)
		//要求： 1.符合预约时段  2.未曾预约   3.该学生选课了
		String hql = "select distinct t1.expArrangeId ,t2.periodarrangeClassId , t4.classarrangeCaseExpId"
				+ " from expArrange as t1 ,Periodarrange as t2 , Classarrange as t3, ClassarrangeCase as t4 "
				+ "where t1.appointmentEndDate != NULL and "
				+ "(t1.appointmentStartDate < " + "'" + currentDate + "'"
				+ " or (t1.appointmentStartDate = " + "'" + currentDate + "'"
				+ " and t1.appointmentStartTime <= " + "'" + currentTime + "'" + "))"
				+ " and (t1.appointmentEndDate > " + "'" + currentDate + "'"
				+ " or (t1.appointmentEndDate = " + "'" + currentDate + "'"
				+ " and t1.appointmentEndTime >= " + "'" +  currentTime + "'" + "))"
				+ " and t2.periodarrangeId = t3.classarrangePeriodArrangeId "
				+ " and t3.classarrangeExpArrangeId = t1.expArrangeId "
				+ " and t4.classarrangeCaseExpArrangeId = t1.expArrangeId "
				+ " and t1.expArrangeId not in ( "
				+ " select t5.orderExpArrangeId "
				+ " from Order as t5, Ordermember as t6 "
				+ " where t6.ordermemberUserId = " + user 
				+ " and t5.orderId = t6.ordermemberOrderId )"
				+ " and t2.periodarrangeClassId in ( "
				+ " select t2.periodarrangeClassId "
				+ " from StuClass as t7, t2"
				+ " where t7.stuClassUserId = " + user 
				+ " and t7.stuClassClassId = t2.periodarrangeClassId )";
		
		System.out.println(hql);
		
		expArrangeDAO eaDAO = new expArrangeDAO();
		List<Object> candidate = eaDAO.getListByHql(hql);
		
		System.out.println("符合预约时间的记录数：" + candidate.size());
		
		if(candidate.size()== 0){
			map.put("isSuccess", false);
			return map;
		}
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();

		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> 新增预约");
		
		List<Class> clist = new ArrayList<Class>();
		
		if(id.equals("my-exp")){
			StudentInfoCDAO stuInfoCDAO = new StudentInfoCDAO();
			clist = stuInfoCDAO.getMyClass(Integer.parseInt(user));// 根据当前学生登录的学号选择他所在的所有班级名称
		}
		else {
			clist = classDAO.getList();
		}
		
		//插入班级
		List<Map<String, Object>> classlist = new ArrayList<Map<String, Object>>();
		int cs = clist.size();
		for (int i = 0; i < cs; i++) {
			
			if(id.equals("my-exp")){
				//如果为学生预约，应该判断是否处于预约时间
				if(!isClassInAppointmentPeriod(clist.get(i).getClassId(),candidate))
					continue;
			}
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("name", clist.get(i).getClassName());
			emap.put("value", clist.get(i).getClassId());

			classlist.add(emap);
		}

		List<Map<String, Object>> aclass = this.vutil.createList("预约班级名称", "", "", classlist, "singleselect", "",
				"orderClassName",(Integer)classlist.get(0).get("value"));

		if(ClassId == -1)
			ClassId = (Integer) classlist.get(0).get("value");
		
		// 根据班级返回课程号
		ClassDAO cDAO = new ClassDAO();
		Integer courseId = ((Class) cDAO.getUniqueByProperty("classId", ClassId)).getClassCourseId();

		// 插入课程实验
		List<Exp> elist = this.cDAO.getCourseExpName(courseId);// 目前是所有已添加实验都可以选择，之后会做筛选，只能选择当前要进行的实验
		List<Map<String, Object>> explist = new ArrayList<Map<String, Object>>();
		int es = elist.size();
		for (int i = 0; i < es; i++) {
			
			if(id.equals("my-exp")){
				if(!isExpInAppointmentPeriod(ClassId,elist.get(i).getExpId(),candidate))
					continue;
			}
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("name", elist.get(i).getExpName());
			emap.put("value", elist.get(i).getExpId());

			explist.add(emap);
		}

		List<Map<String, Object>> cexp = this.vutil.createList("预约实验", "", "", explist, "singleselect", "",
				"orderExpName",(Integer)explist.get(0).get("value"));
		
		if(ExpId == -1)
			ExpId = (Integer) explist.get(0).get("value"); 
		
		
		//根据班级、实验的值，获得所有在预约时段期间的实验活动，并显示他们的时间,并且学生不能参与同样的实验活动
		hql = "select distinct t1.expArrangeId "
				+ " from expArrange as t1 ,Periodarrange as t2 , Classarrange as t3, ClassarrangeCase as t4 "
				+ "where t1.appointmentEndDate != NULL and "
				+ "(t1.appointmentStartDate < " + "'" + currentDate + "'"
				+ " or (t1.appointmentStartDate = " + "'" + currentDate + "'"
				+ " and t1.appointmentStartTime <= " + "'" + currentTime + "'" + "))"
				+ " and (t1.appointmentEndDate > " + "'" + currentDate + "'"
				+ " or (t1.appointmentEndDate = " + "'" + currentDate + "'"
				+ " and t1.appointmentEndTime >= " + "'" +  currentTime + "'" + ")) "
				+ " and t2.periodarrangeClassId = " + ClassId 
				+ " and t4.classarrangeCaseExpId = " + ExpId
				+ " and t2.periodarrangeId = t3.classarrangePeriodArrangeId "
				+ " and t3.classarrangeExpArrangeId = t1.expArrangeId "
				+ " and t4.classarrangeCaseExpArrangeId = t1.expArrangeId "
				+ " and t1.expArrangeId not in ( "
				+ " select t5.orderExpArrangeId "
				+ " from Order as t5, Ordermember as t6 "
				+ " where t6.ordermemberUserId = " + user 
				+ " and t5.orderId = t6.ordermemberOrderId )";
		
		System.out.println(hql);
		
		PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
		List<Object> expArrange = pDAO.getListByHql(hql);
		DateUtil dateutil = new DateUtil();

		List<Map<String, Object>> timelist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < expArrange.size(); i++) {
			
//			//只显示有足够机柜的实验活动
//			if(!checkCabinetResource((Integer)expArrange.get(i),false))
//				continue;
			
			System.out.println("本次实验活动为" + expArrange.get(i));
			
			Map<String, Object> timemap = new HashMap<String,Object>();
			
			hql = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
				+ " from Periodarrange as t2 , Classarrange as t3 "
				+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
				+ " and t3.classarrangeExpArrangeId = " + expArrange.get(i);
			
			//获取本次实验活动的时段集合
			List<Object> periodList = pDAO.getListByHql(hql);
			
			timemap.put("name",dateutil.TimeFormat(periodList));
			timemap.put("value", expArrange.get(i));                    //保存实验排课Id到value域
			timelist.add(timemap);
		}
		List<Map<String, Object>> atime = this.vutil.createList("实验时间", "", "", timelist, "singleselect", "",
				"orderExpArrangeId",(Integer) timelist.get(0).get("value"));
		
		if(ExpArrangeId == -1)
			ExpArrangeId = (Integer) timelist.get(0).get("value");
		
		hql = "select count(*) "
				+ " from Order as t1 , Ordermember as t2 "
				+ "where t1.orderExpArrangeId = " + ExpArrangeId 
				+ " and t1.orderId = t2.ordermemberOrderId "
				+ " and t2.ordermemberUserId = 0";
		Object result = this.oDAO.getUniqueByHql(hql);
		Integer retail = Integer.parseInt(String.valueOf(result));

		List<Map<String, Object>> retailInfo = this.vutil.createList("剩余预约名额", "", "", retail, "", "",
				"retail",0);
		
		List<Map<String, Object>> way = new ArrayList<Map<String, Object>>();

		Map<String, Object> waymap1 = new HashMap<String, Object>();
		waymap1.put("name", "单人预约");
		waymap1.put("value", 0);
		way.add(waymap1);
		
		Map<String, Object> waymap2 = new HashMap<String, Object>();
		waymap2.put("name", "组队预约");
		waymap2.put("value", 1);
		way.add(waymap2);

		List<Map<String, Object>> away = this.vutil.createList("预约方式", "", "", way, "singleselect", "",
				"orderWay",0);

		list.add(aclass);
		list.add(cexp);
		list.add(atime);
		list.add(retailInfo);
		list.add(away);

		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存预约");
		button.put("class", "btn button-new");
		button.put("click", "submitOrderDetail();");

		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);

		return map;
	}
	

	public Map<String, Object> Delete(String user, String id, String list) {
		Map<String, Object> r = new HashMap<String, Object>();// 判断更新或新建是否成功的域
		String[] array_list = list.split(",");
		int sign = 0;
		for (int i = 0; i < array_list.length; i++) {
			Appointment a = (Appointment) this.aDAO.getUniqueByProperty("appointmentId", array_list[i]);
			this.aDAO.delete(a);
			sign++;
		}
		if (sign == array_list.length) {
			// System.out.println("###########################");
			r.put("isSuccess", true);
			r.put("key", "appointment");
		} else {
			r.put("isSuccess", false);
		}
		return r;

	}

	public String time_show(Integer time_id) {
		String time_quantum = null;
		switch (time_id) {
		case 1:
			time_quantum = "6:00~8:00";
			break;
		case 2:
			time_quantum = "8:00~10:00";
			break;
		case 3:
			time_quantum = "10:00~12:00";
			break;
		case 4:
			time_quantum = "12:00~14:00";
			break;
		case 5:
			time_quantum = "14:00~16:00";
			break;
		case 6:
			time_quantum = "16:00~18:00";
			break;
		case 7:
			time_quantum = "18:00~20:00";
			break;
		case 8:
			time_quantum = "20:00~22:00";
			break;
		case 9:
			time_quantum = "22:00~24:00";
			break;
		}
		return time_quantum;
	}

	public Integer time_change(String time_quantum)// 将时间段字符串转换为数字，以便存入数据库
	{
		Integer time_id = null;
		switch (time_quantum) {
		case "6:00~8:00":
			time_id = 1;
			break;
		case "8:00~10:00":
			time_id = 2;
			break;
		case "10:00~12:00":
			time_id = 3;
			break;
		case "12:00~14:00":
			time_id = 4;
			break;
		case "14:00~16:00":
			time_id = 5;
			break;
		case "16:00~18:00":
			time_id = 6;
			break;
		case "18:00~20:00":
			time_id = 7;
			break;
		case "20:00~22:00":
			time_id = 8;
			break;
		case "22:00~24:00":
			time_id = 9;
			break;
		}
		return time_id;
	}

	
	//保存预约信息到Order表  
	//动态预约
	/*
	 * 方式一：单人预约 
	 *        采用一条队列的方式，首先寻找不足3人的小组，如果没有，则另开一组，本过程线程互斥
	 * 方式二：组队预约
	 *        必须一次性输入3个组员，若组员已在别的小组，则不能完成此次预约操作
	 */
	
	
	
	//注意增加预约时间
	public synchronized Map<String, Object> dynamicSave(String user, Map<String, Object> map) {
		
		Map<String, Object> r = null;
		try {
			boolean single = true;
			Integer classId = null;
			
			if(map.get("orderWay").equals("0"))
				single = true;
			else
				single = false;
			
			System.out.println("本次预约是否为个人" + single);
			
			r = new HashMap<String, Object>();

			Set<String> key = map.keySet();
			
			Iterator<String> keylist = key.iterator();
			Integer expArrangeId = null;
			Integer expId = null;
			List<String> orderMember = new ArrayList<String>();
			
			while (keylist.hasNext()) {
				String k = keylist.next();
				switch (k) {
				case "orderExpArrangeId":
					expArrangeId = Integer.parseInt((String)map.get(k));
					System.out.println("实验活动："+Integer.parseInt((String)map.get(k)));
					break;
				case "orderClassName":
					classId  = Integer.parseInt((String)map.get(k));
					break;
					
				case "orderExpName":
					expId  = Integer.parseInt((String)map.get(k));
					break;
				
				case "member1":
					orderMember.add((String) map.get(k));
					break;
				
				case "member2":
					orderMember.add((String) map.get(k));
					break;
//20180901蒋家盛修改预约组队人数为3人
//				case "member3":
//					orderMember.add((String) map.get(k));
//					break;
				}
			}
			ClassInfoCDAO ciCDAO = new ClassInfoCDAO();
			r.put("className", ciCDAO.getClassName(classId));
			
			if(single){
				try {
					//判断是否有足够的资源提供给个人
					String hql = " select t2.ordermemberOrderId "
							   + " from Order as t1,Ordermember as t2 "
							   + " where t1.orderExpArrangeId = " + expArrangeId 
							   + " and t2.ordermemberOrderId = t1.orderId "
							   + " and t2.ordermemberUserId = 0 "
							   + " group by t2.ordermemberOrderId ";
					
					List<Object> result = this.omDAO.getListByHql(hql);
					if (result.size() == 0) {
						r.put("isSuccess", false);
						r.put("returnData", "资源不足");
						return r;
					}

					// 判断是否已预约
					hql = " select t2.ordermemberOrderId " 
							+ " from Order as t1,Ordermember as t2 "
							+ " where t1.orderExpArrangeId = " + expArrangeId 
							+ " and t2.ordermemberOrderId = t1.orderId "
							+ " and t2.ordermemberUserId = " + user;

					result = this.oDAO.getListByHql(hql);
					if (result.size() != 0) {
						r.put("isSuccess", false);
						r.put("returnData", user + " 已经预约了");
						return r;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				//判断是否是本班级的学生
				ClassDetail check = new ClassDetail();
				for(String student:orderMember){
					if(!check.isThisClassStu(classId, Integer.parseInt(student))){
						r.put("isSuccess", false);
						r.put("returnData", "学生号"+student+"不合法");
						return r;
					}
				}
				//判断是否有足够资源提供给一个小组(必须为完整的一个空的小组)
				String hql = " select t1.orderId "
						   + " from Order as t1,Ordermember as t2 "
						   + " where t1.orderExpArrangeId = " + expArrangeId 
						   + " and t2.ordermemberOrderId = t1.orderId "
						   + " and t2.ordermemberUserId = 0 "
						   + " group by t2.ordermemberOrderId "
						   + " having count(t2.ordermemberUserId) = 3";//20180901蒋家盛修改预约组队人数为3人
				
				List<Object> result = this.omDAO.getListByHql(hql);
				System.out.println(result);
				if(result.size()==0){
					r.put("isSuccess", false);
					r.put("returnData", "小组实验资源不足,请单独预约");
					return r;
				}
				//判断3人是否已有预约
				for(String student:orderMember){
					
					hql = " select t2.ordermemberOrderId "
						+ " from Order as t1,Ordermember as t2 "
						+ " where t1.orderExpArrangeId = " + expArrangeId 
						+ " and t2.ordermemberOrderId = t1.orderId "
					    + " and t2.ordermemberUserId = " + student;
					
					result = this.oDAO.getListByHql(hql);
					if(result.size()!=0){
						r.put("isSuccess", false);
						r.put("returnData", student + "已经预约了");
						return r;
					}
				}
			}
			
			//单独预约 填入数据库
			if(single){
				try {
					String hql = " select t2.ordermemberOrderId "
							   + " from Order as t1,Ordermember as t2 "
							   + " where t1.orderExpArrangeId = " + expArrangeId 
							   + " and t2.ordermemberOrderId = t1.orderId "
							   + " and t2.ordermemberUserId != 0 "
							   + " group by t2.ordermemberOrderId "
							   + " having count(t2.ordermemberUserId) < 4 and count(t2.ordermemberUserId) > 0";
					
					List<Object> result = this.oDAO.getListByHql(hql);
					
					Integer orderId = null;
					
					if(result.size() != 0){
						//存在未满组
						orderId = (Integer)result.get(0);
						String[] para = {"ordermemberOrderId",orderId+"","ordermemberUserId","0"};

						
						@SuppressWarnings("unchecked")
						List<Ordermember> omlist = (List<Ordermember>)this.omDAO.getListByNProperty(para);
						Ordermember om = omlist.get(0);
						om.setOrdermemberUserId(Integer.parseInt(user));
						this.omDAO.update(om);
						
					}
					else{
						//需要另开一组
						hql = "select t1.orderId "
							+ " from Order as t1 "
							+ " where t1.orderExpArrangeId = " + expArrangeId
							+ " and t1.orderId not in ("
							+ " select distinct t2.orderId "
							+ " from Order as t2, Ordermember as t3 "
							+ " where t3.ordermemberUserId != 0 "
							+ " and t3.ordermemberOrderId = t2.orderId)";
						
						@SuppressWarnings("unchecked")
						List<Object> olist = this.oDAO.getListByHql(hql);
						orderId = (Integer)olist.get(0);
						
						@SuppressWarnings("unchecked")
						List<Ordermember> newGroup = this.omDAO.getListByProperty("ordermemberOrderId", orderId);
						Ordermember newMember = newGroup.get(0);
						newMember.setOrdermemberUserId(Integer.parseInt(user));
						this.omDAO.update(newMember);
					}
					
					SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String[] current = currentTime.format(new Date()).split(" ");
					Order o = (Order) oDAO.getUniqueByProperty("orderId", orderId);
					o.setOrderSetUpDate(current[0]);
					o.setOrderSetUpTime(current[1]);
					
					if(this.omDAO.getListByProperty("ordermemberOrderId", orderId).size() == 3) {
						o.setOrderStatus("预约成功");
						o.setOrderExpId(expId);
					}
					else
						o.setOrderStatus("等待加入");
					
					this.omDAO.update(o);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
			
			//组队预约 填入数据库
			else{
				String hql = " select t1.orderId "
						   + " from Order as t1,Ordermember as t2 "
						   + " where t1.orderExpArrangeId = " + expArrangeId 
						   + " and t2.ordermemberOrderId = t1.orderId "
						   + " and t2.ordermemberUserId = 0 "
						   + " group by t2.ordermemberOrderId "
						   + " having count(t2.ordermemberUserId) = 3";//20180901蒋家盛修改预约组队人数为3人
				
				List<Object> result = this.oDAO.getListByHql(hql);
				
				Integer orderId = (Integer) result.get(0);
				
				List<Ordermember> newMember = omDAO.getListByProperty("ordermemberOrderId", orderId);
				for(int i=0;i<3;i++){//20180901蒋家盛修改预约组队人数为3人
					if(i!=2){//20180901蒋家盛修改预约组队人数为3人
						newMember.get(i).setOrdermemberUserId(Integer.parseInt(orderMember.get(i)));
					}
						
					else{
						newMember.get(i).setOrdermemberUserId(Integer.parseInt(user));
					}
					newMember.get(i).setOrdermemberConfirmStatus(-1);
					omDAO.update(newMember.get(i));
				}
				
				SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] current = currentTime.format(new Date()).split(" ");
				Order o = (Order) oDAO.getUniqueByProperty("orderId", orderId);
				o.setOrderSetUpDate(current[0]);
				o.setOrderSetUpTime(current[1]);
				o.setOrderStatus("等待确认");
				o.setOrderSetUpUserId(Integer.parseInt(user));
				o.setOrderExpId(expId);
				this.omDAO.update(o);	
				
				orderConfirmTimeLimit thread = new orderConfirmTimeLimit(orderId);
				thread.start();
			}
			
			r.put("isSuccess", true);
			r.put("returnData", "预约信息已提交");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
	
	public synchronized Map<String, Object> save(String user, Map<String, Object> map  ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		
		Integer expArrangeId = null;
		
		while (keylist.hasNext()) {
			String k = keylist.next();

			switch (k) {

			case "orderExpArrangeId":
				expArrangeId = Integer.parseInt((String)map.get(k));
				System.out.println("实验活动："+Integer.parseInt((String)map.get(k)));
				break;
			}
		}


		String hql = " select t2.ordermemberOrderId "
					  + " from Order as t1,Ordermember as t2 "
					  + " where t1.orderExpArrangeId = " + expArrangeId
					  + " and t2.ordermemberOrderId = t1.orderId "
					  + " and t2.ordermemberUserId != 0 "
					  + " group by t2.ordermemberOrderId "
					  + " having count(t2.ordermemberUserId) < 3 ";//20180901蒋家盛修改预约组队人数为3人
		
		List<Object> result = this.oDAO.getListByHql(hql);
		
		if(result.size() != 0){
			//存在未满组
			try {
				Integer orderId = (Integer)result.get(0);
				String[] para = {"ordermemberOrderId",orderId+"","ordermemberUserId","0"};
				@SuppressWarnings("unchecked")
				List<Ordermember> omlist = (List<Ordermember>) this.omDAO.getListByNProperty(para);
				Ordermember om = omlist.get(0);
				om.setOrdermemberUserId(Integer.parseInt(user));
				this.omDAO.update(om);
				
				hql = "select ordermemberUserId "
						+ "from Ordermember as t1 "
						+ " where t1.ordermemberOrderId = " + orderId 
						+ " and t1.ordermemberUserId != 0";
				
				omlist = this.omDAO.getListByHql(hql);
				if(omlist.size() == 3){//20180901蒋家盛修改预约组队人数为3人
					
					Order o = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
					o.setOrderStatus("预约完成");
					this.oDAO.update(o);
				}
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			//需要另开一组
			hql = "select t1.orderId "
				+ " from Order as t1 "
				+ " where t1.orderExpArrangeId = " + expArrangeId
				+ " and t1.orderId not in ("
				+ " select distinct t2.orderId "
				+ " from Order as t2, Ordermember as t3 "
				+ " where t3.ordermemberUserId != 0 "
				+ " and t3.ordermemberOrderId = t2.orderId)";
			
			@SuppressWarnings("unchecked")
			List<Object> omlist = this.omDAO.getListByHql(hql);
			Integer insertOrderId = (Integer)omlist.get(0);
			
			@SuppressWarnings("unchecked")
			List<Ordermember> newGroup = this.omDAO.getListByProperty("ordermemberOrderId", insertOrderId);
			Ordermember newMember = newGroup.get(0);
			newMember.setOrdermemberUserId(Integer.parseInt(user));
			this.omDAO.update(newMember);
			
			/*修改资源数量*/
		}
		
		
		

		r.put("isSuccess", false);
		return r;
	}
	
	//固定预约   只需要map改成分开的参数就可以了
	//保存预约信息到Order表
		public Map<String, Object> defaultSave(String user, Map<String, Object> map) {
			Map<String, Object> r = new HashMap<String, Object>();

			Order c = new Order();

			Set<String> key = map.keySet();
			Iterator<String> keylist = key.iterator();
			
			Integer classId = null;
			
			while (keylist.hasNext()) {
				String k = keylist.next();

				switch (k) {
				case "orderClassName":
					classId = Integer.parseInt((String)map.get(k));
					System.out.println("班级："+Integer.parseInt((String)map.get(k)));
					break;
				case "orderExpName":
					c.setOrderExpId(Integer.parseInt((String)map.get(k)));
					System.out.println("实验："+Integer.parseInt((String)map.get(k)));
					System.out.println("123456789");
					break;
				case "orderExpArrangeId":
					c.setOrderExpArrangeId(Integer.parseInt((String)map.get(k)));
					System.out.println("实验活动："+Integer.parseInt((String)map.get(k)));
					break;
				}
			}
			
			c.setOrderSetUpUserId(Integer.parseInt(user));
			
			SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] current = currentTime.format(new Date()).split(" ");
			c.setOrderSetUpDate(current[0]);
			c.setOrderSetUpTime(current[1]);
			c.setOrderStatus("预约完成");
			
			//检查是否有足够的机柜
			boolean pass = checkCabinetResource( c.getOrderExpArrangeId(),true);
			
			if(pass){
				
				//判断是否已经有其他组员预约了
				String hql = "select t5.orderId "
						+ " from Order as t5, Ordermember as t6 "
						+ " where t6.ordermemberUserId = " + user
						+ " and t5.orderExpArrangeId = " + c.getOrderExpArrangeId()
						+ " and t5.orderId = t6.ordermemberOrderId ";
				
				System.out.println(hql);
				
				Object check = this.oDAO.getUniqueByHql(hql);
				if(check != null){
					r.put("isSuccess", false);
					return r;
				}
				// 修改预约表
				this.oDAO.add(c);

				// 根据班级找组员
				hql = "select t2.classgroupmemberUserId "
					+ " from Groupmember as t2 "
					+ " where t2.classgroupmemberGroupId = "
						+ "(select t3.classgroupmemberGroupId " 
						+ " from Classgroup as t1, Groupmember as t3 "
						+ " where t1.classgroupClassId = " + classId 
						+ " and t3.classgroupmemberGroupId = t1.classgroupId"
						+ " and t3.classgroupmemberUserId = " + user + ")";

				System.out.println(hql);

				@SuppressWarnings("unchecked")
				List<Object> allmember = oDAO.getListByHql(hql);

				String[] para = { "orderSetUpUserId", user, "orderExpArrangeId", c.getOrderExpArrangeId() + "" };
				Order newRow = (Order) this.oDAO.getByNProperty(para);
				Integer orderId = newRow.getOrderId();

				System.out.println(orderId);

				// 修改预约成员表
				for (Object member : allmember) {

					Integer UserId = (Integer) member;
					Ordermember om = new Ordermember();
					om.setOrdermemberOrderId(orderId);
					om.setOrdermemberUserId(UserId);
					this.omDAO.add(om);
				}
				
			}
			r.put("isSuccess", pass);
			return r;
		}
	
	public boolean isClassInAppointmentPeriod(Integer classId, List<Object> candidate){
		//判断本课程是否存在满足预约时段的实验
		for(Object record:candidate){
			Object[] records = (Object[])record;
			System.out.println(records[1] + " " + classId);
			if(records[1] == classId)
				return true;
		}
		return false;
	}
	
	public boolean isExpInAppointmentPeriod(Integer classId, Integer expId, List<Object> candidate){
		//判断某课程的某个实验是否存在满足预约时段
		for(Object record:candidate){
			Object[] records = (Object[])record;
			System.out.println(records[1] + " " + classId + " " + records[2] + " " + expId);
			if(records[1] == classId && records[2] == expId)
				return true;
		}
		return false;
	}
	
	public synchronized boolean checkCabinetResource( Integer expArrangeId , boolean isSave){
		//通过实验排课Id判断本时段时候还有空余的机柜资源   isSave判断是不是处于保存预约操作状态
		
		String hql = "select t1.periodarrangeId ,t1.periodarrangeCabinetNum "
					+ " from Periodarrange as t1 , Classarrange as t2 "
					+ " where t2.classarrangeExpArrangeId = " + expArrangeId
					+ " and t2.classarrangePeriodArrangeId = t1.periodarrangeId";
		
		PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
		@SuppressWarnings("unchecked")
		List<Object> result = pDAO.getListByHql(hql);
		
		//查看是否可用
		for(Object period:result){
			Object[] periodType = (Object[]) period;
			if((Integer)periodType[1] == 0){
				return false;
			}
		}
		
		if(isSave){
			//若可用则占用
			for(Object period:result){
				Object[] periodType = (Object[]) period;
				
				Periodarrange newPeriod = (Periodarrange) pDAO.getUniqueByProperty("periodarrangeId", periodType[0]);
				newPeriod.setPeriodarrangeCabinetNum((Integer) periodType[1] - 1);
				pDAO.update(newPeriod);
			}
		}
		return true;
		
	}
	
	//等待确认  -1   拒绝 0   同意 1
	
	@SuppressWarnings("unchecked")
	public synchronized Map<String, Object> confirmOrder(Integer user,Integer orderId,Integer confirmOrNot) {
		
		//拒绝，直接解散整个小组
		if(confirmOrNot == 0){
			List<Ordermember> omlist = this.omDAO.getListByProperty("ordermemberOrderId", orderId);
			for(Ordermember member:omlist){
				member.setOrdermemberUserId(0);
				member.setOrdermemberConfirmStatus(0);
				this.omDAO.update(member);
			}
			
			Order order = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
			order.setOrderSetUpDate("无");
			order.setOrderSetUpTime("无");
			order.setOrderSetUpUserId(0);
			order.setOrderStatus("无");
			this.oDAO.update(order);
			System.out.println("成功释放本预约资源");
		}
		
		//同意加入小组
		else if(confirmOrNot == 1){
			String[] para = {"ordermemberOrderId",orderId+"","ordermemberUserId",user+"",
					"ordermemberConfirmStatus",-1+""};
			Ordermember om = (Ordermember) this.omDAO.getByNProperty(para);
			om.setOrdermemberConfirmStatus(1);
			this.oDAO.update(om);
			
			//所有人都同意了
			String hql = "select ordermemberUserId "
					+ "from Ordermember as t1 "
					+ " where t1.ordermemberOrderId = " + orderId 
					+ " and t1.ordermemberConfirmStatus = 1";
			
			List<Object> omlist = this.omDAO.getListByHql(hql);
			if(omlist.size() == 3){//20180901蒋家盛修改预约组队人数为3人
				
				Order o = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
				o.setOrderStatus("预约完成");
				this.oDAO.update(o);
			}
		}
		Map<String , Object> r = new HashMap<String, Object>();
		
		r.put("isSuccess", true);
 		return r;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Map<String, Object> deleteOrder(Integer orderId) {
		List<Ordermember> omlist = this.omDAO.getListByProperty("ordermemberOrderId", orderId);
		for(Ordermember member:omlist){
			member.setOrdermemberUserId(0);
			member.setOrdermemberConfirmStatus(0);
			this.omDAO.update(member);
		}
			
		Order order = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
		order.setOrderSetUpDate("无");
		order.setOrderSetUpTime("无");
		order.setOrderSetUpUserId(0);
		order.setOrderStatus("无");
		this.oDAO.update(order);
		System.out.println("成功释放本预约资源");
		
		Map<String , Object> r = new HashMap<String, Object>();

		r.put("isSuccess", true);
 		return r;
	}
	
	//动态方式分配实验资源，但未预约
		//保存空的预约信息到Order表
		public boolean dynamicDistribution(String user ,List<Integer> expArrangeIdList, List<String> stuNum,Integer classId) {
			
				//目前默认都是3个人一组
				
				for(int i=0;i<expArrangeIdList.size();i++){
					
					double groupNumber = Math.ceil(Double.parseDouble(stuNum.get(i))/3);//20180901蒋家盛修改预约组队人数为3人
					for(double j=0;j<groupNumber;j++){
						
						
						Date date = new Date();
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
						
						Order order = new Order();
						order.setOrderExpArrangeId(expArrangeIdList.get(i));
						order.setOrderSetUpDate(sdf1.format(date));
						order.setOrderSetUpTime(sdf2.format(date));
						order.setOrderSetUpUserId(Integer.parseInt(user));
						order.setOrderStatus("无");
					
						//创建预约新纪录并且填充默认组员
						Integer OrderId = this.oDAO.add(order);
						System.out.println("新的行号：" + OrderId);

						if(OrderId != 0 ){
							
							for(int k=0;k<3;k++){//20180901蒋家盛修改预约组队人数为3人
							
								Ordermember newOrdermember = new Ordermember();
								newOrdermember.setOrdermemberOrderId(OrderId);
								newOrdermember.setOrdermemberUserId(0);
								newOrdermember.setOrdermemberConfirmStatus(0);
							
								//填充成员
								if(!this.omDAO.add(newOrdermember))
									return false;
							}
						}
						else
							return false;
					}	
				}
			return true;	
		}
		
		
		//固定预约
			//保存预约信息到Order表
			public boolean defaultSave(String user ,List<Integer> expArrangeIdList, Integer classId, Integer expNum) {
				
				ClassgroupDAO  cgDAO = new ClassgroupDAO();
				List<Classgroup>  group = cgDAO.getListByProperty("classgroupClassId", classId);
				Integer each = (int) Math.ceil((double) group.size()/expArrangeIdList.size());
				
				
				int groupIterator = 0,j = 0,expArrangeIdListIterator = 0;
				for(;groupIterator<group.size();groupIterator++){
					
					//分配到下一个实验活动
					if(j==each){
						j=0;
						expArrangeIdListIterator++;
					}
					
					Date date = new Date();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
					
					Order order = new Order();
					order.setOrderExpArrangeId(expArrangeIdList.get(expArrangeIdListIterator));
					order.setOrderSetUpDate(sdf1.format(date));
					order.setOrderSetUpTime(sdf2.format(date));
					order.setOrderSetUpUserId(Integer.parseInt(user));
					order.setOrderExpId(expNum);
					order.setOrderStatus("固定预约完成");
					
					//创建预约新纪录并且填充默认组员
					Integer OrderId = this.oDAO.add(order);
				
					if(OrderId != 0){
						
						GroupmemberDAO gmDAO = new GroupmemberDAO();
						List<Groupmember> member = gmDAO.getListByProperty("classgroupmemberGroupId", group.get(groupIterator).getClassgroupId());
						for(int i=0;i<member.size();i++){
							
							Ordermember newOrdermember = new Ordermember();
							newOrdermember.setOrdermemberOrderId(OrderId);
							newOrdermember.setOrdermemberUserId(member.get(i).getClassgroupmemberUserId());
							newOrdermember.setOrdermemberConfirmStatus(1);
							
							//填充成员
							if(!this.omDAO.add(newOrdermember)){
								return false;
							}
						}
					}
					else
						return false;
					
					j++;
				}	
				return true;	
			}
			public void changeOrderStatus(Integer orderId,String orderStatus){
				
				Order o = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
				o.setOrderStatus(orderStatus);
				this.oDAO.update(o);

			}
	
}

