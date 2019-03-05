package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import virnet.management.dao.UserDAO;

public class Student implements InformationQuery {
	
	private Integer pageSize = 20;
	private UserDAO userDAO = new UserDAO();
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "学生ID");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "学生昵称");
		head_name.put("class", "");
		head.add(head_name);	
		
		Map<String, String> head_phone = new HashMap<String, String>();
		head_phone.put("name", "手机号码");
		head_phone.put("class", "");
		head.add(head_phone);
		
		Map<String, String> head_email = new HashMap<String, String>();
		head_email.put("name", "邮箱地址");
		head_email.put("class", "");
		head.add(head_email);
		
		list.add(head);
		
		//页数是从1开始算的
		if(page == 0){
			page = 1;
		}
		
		//查询是学生的记录
		List<Object> userlist = new ArrayList<Object>();
		
		String hql = "select t1.userId,t1.userNickname,t1.userPhone,t1.userEmail "
				+ "from User as t1, UserCharacter as t2 "
				+ "where t1.userId = t2.userCharacterUserId and t2.userCharacterCharacterId = 3";
		
		//多表连接查询结果为object数组组成的list的类型
		userlist = this.userDAO.getListByHql(hql);
		
		int size = userlist.size();
		System.out.println("user list size : " + size);
		
		for(int i = (page-1)*pageSize; i < (page)*pageSize && i < size; i++){
			List<Map<String, String>> studentInfo = new ArrayList<Map<String, String>>();
			
			//对查询结果数组进行处理
			Object obj = userlist.get(i);
			Object[] student = (Object[])obj;
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", String.valueOf(student[0]) );
			map_id.put("class", "");
			studentInfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", String.valueOf(student[1]));
			map_name.put("class", "");
			studentInfo.add(map_name);
			
			Map<String, String> map_phone = new HashMap<String, String>();
			map_phone.put("name", String.valueOf(student[2]));
			map_phone.put("class", "");
			studentInfo.add(map_phone);
			
			Map<String, String> map_email = new HashMap<String, String>();
			map_email.put("name", String.valueOf(student[3]));
			map_email.put("class", "");
			studentInfo.add(map_email);
			
			
			list.add(studentInfo);
		}
		
		int pageNO = size / pageSize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "导入学生信息");
		button.put("class", "btn button-new");
		button.put("click", "inputPage();");
		
		map.put("data", list);
		map.put("page", pageNO);
		map.put("button_new", button);
		
		return map;
	}

}
