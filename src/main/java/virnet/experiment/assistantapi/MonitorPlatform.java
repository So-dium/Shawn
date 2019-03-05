package virnet.experiment.assistantapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.entity.Class;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.util.ViewUtil;

public class MonitorPlatform {
	
	public MonitorPlatform(){
		
	}
	
	public void show(JSONObject jsonString,
				ArrayList<String> groupExisted,
				WebSocketSession wss,
				ConcurrentHashMap<String, String> MapGroupEndTime,
				ConcurrentHashMap<String, String> MapExpId,
				ConcurrentHashMap<String, String> MapGroupProblem,
				ConcurrentHashMap<String, String> MapEquipmentIp
				) throws IOException{
		if (jsonString.getString("type").equals("load")) {

			List<Map<String, Object>> course = null;
			List<Map<String, Object>> Class = null;

			// 课程列表
			@SuppressWarnings("unchecked")
			List<Course> courseList = new CourseDAO().getList();
			List<Map<String, Object>> selectList = new ArrayList<Map<String, Object>>();
			Map<String, Object> allMap = new HashMap<String, Object>();
			allMap.put("name", "全部");
			allMap.put("selected", "selected");
			selectList.add(allMap);
			int size = courseList.size();
			for (int i = 0; i < size; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", courseList.get(i).getCourseName());
				map.put("value", courseList.get(i).getCourseId());
				selectList.add(map);
			}
			ViewUtil vutil = new ViewUtil();
			course = vutil.createList("课程", "", "", selectList, "mutiselect", "", "Course",0);

			// 班级列表
			@SuppressWarnings("unchecked")
			List<Class> classList = new ClassDAO().getList();
			List<Map<String, Object>> selectList2 = new ArrayList<Map<String, Object>>();
			selectList2.add(allMap);
			size = classList.size();
			for (int i = 0; i < size; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", classList.get(i).getClassName());
				map.put("value", classList.get(i).getClassId());
				selectList2.add(map);
			}
			Class = vutil.createList("班级", "", "", selectList2, "mutiselect", "", "Class",0);

//			groupExisted.add("5");
//			MapExpId.put("5", "1");
//			groupExisted.add("6");
//			MapExpId.put("6", "2");
//			groupExisted.add("1");
//			MapExpId.put("1", "3");
//
//			MapGroupEndTime.put("5", "2017-06-30 18:00:00");
//			MapGroupEndTime.put("6", "2017-06-30 19:00:00");
//			MapGroupEndTime.put("1", "2017-06-30 19:15:00");

			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			// 遍历所有正在做实验的小组
			for (String groupId : groupExisted) {
				
				//！！！！！！！！！！！！！！！ 特别注意  ！！！！！！！！！！！！！！！
				//groupid 对应数据表 实质是 预约Id  ！！！！！！！！！！！！
				//这里要改一下hql语句
				//！！！！！！！！！！！！！！！！！！！
				
				Map<String, Object> map = new HashMap<String, Object>();

				// 小组号
				map.put("groupId", groupId);
				
				try {
					String hql = "select distinct t6.courseName, t4.className "
							+ " from expArrange as t1 ,Periodarrange as t2 , Classarrange as t3, Order as t5 ,"
							+ " Class as t4, Course as t6 "
							+ "where t5.orderId = " + groupId
							+ " and t2.periodarrangeId = t3.classarrangePeriodArrangeId "
							+ " and t3.classarrangeExpArrangeId = t1.expArrangeId "
							+ " and t1.expArrangeId  = t5.orderExpArrangeId "
							+ " and t2.periodarrangeClassId =  t4.classId "
							+ " and t4.classCourseId = t6.courseId ";

					// 课程名班级名
					OrderDAO oDAO = new OrderDAO();
					Object result = oDAO.getUniqueByHql(hql);
					if(result == null){//实验员无班级与课程
						map.put("courseName", "实验员");
						map.put("className", "实验员");
					}
					else{
						Object[] results = (Object[]) result;
						map.put("courseName", results[0]);
						map.put("className", results[1]);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 实验名称
				String expId = MapExpId.get(groupId);
				ExpDAO eDAO = new ExpDAO();
				String expName = ((Exp) eDAO.getUniqueByProperty("expId", expId)).getExpName();
				map.put("expName", expName);

				// 实验Id
				map.put("expId", expId);
				
				//机柜ip
				map.put("ip",MapEquipmentIp.get(groupId));

				// 结束时间
				map.put("endTime", MapGroupEndTime.get(groupId));
				System.out.println("剩余时间！！！！！" + MapGroupEndTime.get(groupId));

				 List<String> results = null;
				try {
					// 小组成员
					 String hql = "SELECT t1.userNickname " 
					 + "FROM User as t1, Order as t2 ,Ordermember as t3 "
					 + "WHERE t2.orderId = " + groupId
					 + " and t2.orderId = t3.ordermemberOrderId "
					 + "and t3.ordermemberUserId = t1.userId";
					 System.out.println(hql);
					
					 @SuppressWarnings("unchecked")
					 OrderDAO oDAO = new OrderDAO();
					 results = oDAO.getListByHql(hql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 String member = "";
				 for (String memberName : results) {
				 member = member + memberName + "#";
				 }
				 System.out.println("小组成员是:" + member);
				 map.put("groupMember", member);

				 //是否求助
				 if(MapGroupProblem.containsKey(groupId)){
					 map.put("problemList", MapGroupProblem.get(groupId));
				 }

				info.add(map);

			}
			Map<String, List<Map<String, Object>>> monitorInfo = new HashMap<String, List<Map<String, Object>>>();
			monitorInfo.put("Course", course);
			monitorInfo.put("Class", Class);
			monitorInfo.put("list", info);
			jsonString.put("monitorInfo", monitorInfo);
			String mess = jsonString.toString();
			wss.sendMessage(new TextMessage(mess));
		}
	}
}
