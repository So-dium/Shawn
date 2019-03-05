package virnet.management.information.service;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.entity.Class;
import virnet.management.entity.Exp;
import virnet.management.entity.Order;
import virnet.management.entity.Ordermember;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.dao.OrdermemberDAO;
import virnet.management.dao.StuClassDAO;
import virnet.management.entity.StuClass;
import virnet.management.entity.User;
import virnet.management.util.DateUtil;
import virnet.management.util.UserInfoProcessUtil;


import virnet.management.combinedao.orderConfirmTimeLimit;


public class MyExp implements InformationQuery{

	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private UserInfoProcessUtil usercheck = new UserInfoProcessUtil();
	private StuClassDAO stucDAO = new StuClassDAO();
	private ClassDAO classDAO = new ClassDAO();
	/*
	 * @param
	 * page --- required page in database
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
		try{
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		
		int stuid = this.usercheck.checkUsername(user);

		// 返回该学生所在的班级列表

		List<StuClass> sclist = this.stucDAO.getListByProperty("stuClassUserId", stuid);

		List<Class> clist = new ArrayList<Class>();
		int scSize = sclist.size();
		for (int i = 0; i < scSize; i++) {
			clist.add(this.getClass(sclist.get(i).getStuClassClassId()));
		}

		int size = clist.size();
		List<Object> selectlist = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			Map<String, Object> cmap = new HashMap<String, Object>();

			int classid = clist.get(i).getClassId();
			cmap.put("id", classid);

			cmap.put("class", this.cDAO.getClassName(classid));
			selectlist.add(cmap);
		}

		// get the request class id
		int s = selectlist.size();
		int classid;
		if (s == 0) {
			// list is null
			classid = -1;
		} else {
			classid = (int) ((Map<String, Object>) selectlist.get(0)).get("id");
		}

		for (int i = 0; i < s; i++) {
			if (select.equals(((Map<String, Object>) selectlist.get(i)).get("class"))) {
				classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
			}
		}
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> exp_name = new HashMap<String, Object>();
		exp_name.put("name", "实验名称");
		exp_name.put("class", "");
		head.add(exp_name);
		
		Map<String, Object> member= new HashMap<String, Object>();
		member.put("name", "成员");
		member.put("class", "");
		head.add(member);
		
		Map<String, Object> exp_create_time = new HashMap<String, Object>();
		exp_create_time.put("name", "预约创建时间");
		exp_create_time.put("class", "");
		head.add(exp_create_time);
		
		Map<String, Object> exp_time_id = new HashMap<String, Object>();
		exp_time_id.put("name", "实验进行时间");
		exp_time_id.put("class", "");
		head.add(exp_time_id);
		
		Map<String, Object> status= new HashMap<String, Object>();
		status.put("name", "状态");
		status.put("class", "");
		head.add(status);
		
		//添加预约确认信息
		//查看用户有没有未确认的预约信息
		String	hql = "select t1.orderId" + " from Order as t1, Ordermember as t2 "
					+ "where t1.orderId = t2.ordermemberOrderId " + "and t2.ordermemberUserId = " + user
					+ " and t1.orderStatus = " + "'等待确认'" + " and t2.ordermemberConfirmStatus = -1";

		OrderDAO oDAO = new OrderDAO();
		List<Object> confirmOrderList = (List<Object>)  oDAO.getListByHql(hql);

		if (confirmOrderList.size()!=0) {

				Map<String, Object> retailTime = new HashMap<String, Object>();
				retailTime.put("name", "剩余确认时间");
				retailTime.put("class", "");
				head.add(retailTime);

				Map<String, Object> confirm = new HashMap<String, Object>();
				confirm.put("name", "");
				confirm.put("class", "");

				head.add(confirm);

				Map<String, Object> refuse = new HashMap<String, Object>();
				refuse.put("name", "");
				refuse.put("class", "");
				head.add(refuse);		
		}
		list.add(head);

		hql = "select distinct t1.orderId, t1.orderSetUpDate, t1.orderSetUpTime, t1.orderExpId, t1.orderStatus ,t1.orderExpArrangeId "
				   + " from Order as t1, Ordermember as t2 ,Periodarrange as t3, Classarrange as t4 "
				   + " where t2.ordermemberUserId = " + stuid
				   + " and t3.periodarrangeClassId = " + classid
				   + " and t2.ordermemberOrderId = t1.orderId "
				   + " and t1.orderExpArrangeId = t4.classarrangeExpArrangeId "
				   + " and t4.classarrangePeriodArrangeId = t3.periodarrangeId ";
		
		System.out.println(hql);
		List<Object> olist = oDAO.getListByHql(hql);
		for(int i = 0; i < olist.size(); i++){
			List<Map<String, Object>> appInfo = new ArrayList<Map<String, Object>>();
			
			Object[] orderInfo = (Object[]) olist.get(i);
			
			//实验名称
			Integer expId = (Integer) orderInfo[3];
			System.out.println("实验Id"+expId);
			ExpDAO eDAO = new ExpDAO();
			String expName = ((Exp)eDAO.getUniqueByProperty("expId", expId)).getExpName();
				
			Map<String, Object> map_name = new HashMap<String, Object>();
			map_name.put("name", expName);
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + expName + "', 'my-exp' , '" + orderInfo[0] + "');");//experiment detail
			appInfo.add(map_name);
			
			//成员
			Map<String, Object> map_member = new HashMap<String, Object>();
			OrdermemberDAO omDAO = new OrdermemberDAO();
			List<Ordermember> member1 = omDAO.getListByProperty("ordermemberOrderId", orderInfo[0]);
			List<Object> memberlist = new ArrayList<Object>();
			for(int j = 0; j < member1.size(); j++){
				User u = usercheck.getUser(member1.get(j).getOrdermemberUserId());
				if(u != null){
					Map<String, Object> mapmember = new HashMap<String, Object>();
					mapmember.put("name", u.getUserNickname());
					mapmember.put("class", "");
					memberlist.add(mapmember);
				}
			}
			map_member.put("name", memberlist);
			map_member.put("class", "collapse");
			appInfo.add(map_member);
			
			
			//预约创建时间
			Map<String, Object> map_create_time = new HashMap<String, Object>();
			map_create_time.put("name", orderInfo[1] + " " + orderInfo[2]);
			map_create_time.put("class", "");
			appInfo.add(map_create_time);
			
			//实验进行时间
			hql = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
					+ " from Periodarrange as t2 , Classarrange as t3 "
					+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
					+ " and t3.classarrangeExpArrangeId = " + orderInfo[5];
				
			//获取本次实验活动的时段集合
			List<Object> periodList = oDAO.getListByHql(hql);
			DateUtil dateutil = new DateUtil();
			
			Map<String, Object> map_time = new HashMap<String, Object>();
			map_time.put("name", dateutil.TimeFormat(periodList));
			map_time.put("class", "");
			appInfo.add(map_time);
			
			Map<String, Object> map_status = new HashMap<String, Object>();
			map_status.put("name", (String) orderInfo[4]);
			map_status.put("class", "");
			appInfo.add(map_status);
				
			
			
			//预约状态说明    0代表无意义    1代表已确认  -1代表待确认
			
			//添加预约确认信息
			//查看用户有没有没有确认的预约信息
			hql = "select t1.orderId"
					+ " from Order as t1, Ordermember as t2 "
					+ "where t1.orderId = t2.ordermemberOrderId "
					+ "and t2.ordermemberUserId = " + user
					+ " and t1.orderStatus = " + "'等待确认'"
					+ " and t2.ordermemberConfirmStatus = -1"
					+ " and t1.orderId = " + orderInfo[0];
			
			Integer  confirmOrder = (Integer) oDAO.getUniqueByHql(hql);
			if(confirmOrder != null){
				
				Order o = (Order) oDAO.getUniqueByProperty("orderId", confirmOrder);
				String date = o.getOrderSetUpDate();
				String time = o.getOrderSetUpTime();
				
				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				Date orderDate = sdf.parse(date + " " + time);
				Date current = new Date();

				DateUtil du = new DateUtil();
				String timeDifference = du.TimeDifference(current, new Date(orderDate.getTime() + 5 * 60 * 1000));
				
				Map<String, Object> map_retailTime = new HashMap<String, Object>();
				map_retailTime.put("name", timeDifference);
				map_retailTime.put("class", "retailTime");
				appInfo.add(map_retailTime);
				
				Map<String, Object> map_confirm = new HashMap<String, Object>();
				map_confirm.put("name", "同意");
				map_confirm.put("class", "btn btn-new");
				map_confirm.put("onclick", "confirmThisOrder('" + user + "','" + confirmOrder+ "', '1')");
				appInfo.add(map_confirm);
				
				Map<String, Object> map_refuse = new HashMap<String, Object>();
				map_refuse.put("name", "拒绝");
				map_refuse.put("class", "btn btn-new");
				map_refuse.put("onclick", "confirmThisOrder('" + user + "','" + confirmOrder+ "', '0')");
				appInfo.add(map_refuse);
			}
			//暂时禁用删除预约功能	
			//查看用户是否为固定预约，固定预约不可删除，自由预约可以删除
//			hql = "select t1.orderId"
//					+ " from Order as t1, Ordermember as t2 "
//					+ "where t1.orderId = t2.ordermemberOrderId "
//					+ "and t2.ordermemberUserId = " + user
//					+ " and t1.orderStatus = " + "'固定预约完成'"
//					+ " and t2.ordermemberConfirmStatus = 1"
//					+ " and t1.orderId = " + orderInfo[0];
//			
//			Integer  defaultOrder = (Integer) oDAO.getUniqueByHql(hql);
//
//			if(defaultOrder == null){
//				Map<String, Object> map_delete = new HashMap<String, Object>();
//				map_delete.put("name", "删除");
//				map_delete.put("class", "btn btn-new");
//				map_delete.put("onclick", "deleteThisOrder('" + orderInfo[0] + "')");
//				appInfo.add(map_delete);
//			}
			
			list.add(appInfo);
		}
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增预约");
		button.put("class", "btn button-new");
		button.put("click", "addOrderRecord('my-exp',-1,-1,-1);");
		
		map.put("select", selectlist);
		map.put("button_new", button);
		map.put("data", list);
		
		System.out.println(list);
		}catch(Exception e){
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 根据班级编号得到班级实体类
	 * 
	 * @param classid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getClass(int classid) {
		List<Class> clist = this.classDAO.getListByProperty("classId", classid);

		if (clist.isEmpty() || clist.size() > 1) {
			return null;
		} else {
			return clist.get(0);
		}
	}
	
	
}
