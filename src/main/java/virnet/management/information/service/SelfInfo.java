package virnet.management.information.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.management.dao.UserDAO;
import virnet.management.entity.User;
import virnet.management.util.UserInfoProcessUtil;

public class SelfInfo{
	
	private UserDAO uDAO = new UserDAO();
	
	//个人信息显示页面
	
	public Map<String, Object> showSelfInfoDetail(String id, String name) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "个人信息管理 <i class='icon-double-angle-right'></i> " + name);
		
		String returnid = id;
	    
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "修改个人信息");
		button.put("class", "btn button-new");
		button.put("click", "editContent();");
		
		Map<String, Object> passwordButton = new HashMap<String, Object>();
		passwordButton.put("content", "修改密码");
		passwordButton.put("class", "btn button-new");
		passwordButton.put("click", "changePassword();");
		
		try {
			list = selfInfoList(name,id,0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		returnid = "selfInfo";
		
		map.put("tittle", tittle);
		map.put("button", button);
		map.put("password", passwordButton);
		map.put("data", list);
		map.put("id", returnid);
		
		return map;
	}
	
	//无修改密码页面
	
	public Map<String, Object> Edit(String name) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "修改信息 <i class='icon-double-angle-right'></i> " + name);
		
		list = selfInfoList(name,"selfInfo", 1);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
	    
		map.put("button", button);
		map.put("tittle", tittle);
		map.put("data", list);
		
		return map;
	}
	
	//含修改密码页面
	
	public Map<String, Object> changePassword(String name) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
			
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "修改密码 <i class='icon-double-angle-right'></i> " + name);
			
		list = selfInfoList(name,"selfInfo", 2);
			
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		    
		map.put("button", button);
		map.put("tittle", tittle);
		map.put("data", list);
			
		return map;
	}
	
	//详细信息菜单，上述三种情况页面均会调用
	
	public List<List<Map<String, Object>>> selfInfoList(String name,String id, Integer isEdit) throws ParseException{
		
		//isEdit 0为不修改   1为修改基本信息  2为修改密码 
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		//用户不能重名
		User currentUser = this.uDAO.getUniqueByProperty("userId", name);  
		
		if(currentUser == null){
			//error  no such user
			list.clear();
			return list;
		}
		else{
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "用户ID");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", currentUser.getUserId());
			if(isEdit != 0){
				map12.put("class", "");
				map12.put("onclick", "");
				map12.put("value", "userId");
			}
			
			list1.add(map11);
			list1.add(map12);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "用户名称");
			
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", currentUser.getUserNickname());
			if(isEdit != 0){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "userName");
			}
			
			list2.add(map21);
			list2.add(map22);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "手机号码");
			
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", currentUser.getUserPhone());
			if(isEdit != 0){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "userPhone");
			}
			
			list3.add(map31);
			list3.add(map32);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "电子邮箱");
			
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", currentUser.getUserEmail());
			if(isEdit != 0){
				map42.put("class", "btn btn-link a edit");
				map42.put("onclick", "editable(this);");
				map42.put("value", "userEmail");
			}
			
			list4.add(map41);
			list4.add(map42);
			
			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			
			
			//编辑是可以修改密码
			if(isEdit == 2){
				List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
				
				Map<String, Object> map51 = new HashMap<String, Object>();
				map51.put("name", "原密码");
				
				Map<String, Object> map52 = new HashMap<String, Object>();
				map52.put("name", "");
				
				map52.put("class", "btn btn-link edit");
				map52.put("onclick", "editable_password(this);");
				map52.put("value", "oldPassword");
				
				list5.add(map51);
				list5.add(map52);
				
				Map<String, Object> map61 = new HashMap<String, Object>();
				map61.put("name", "新密码");
				
				Map<String, Object> map62 = new HashMap<String, Object>();
				map62.put("name", "");
				
				map62.put("class", "btn btn-link edit");
				map62.put("onclick", "editable_password(this);");
				map62.put("value", "newPassword");
				
				list6.add(map61);
				list6.add(map62);
				
				Map<String, Object> map71 = new HashMap<String, Object>();
				map71.put("name", "确认新密码");
				
				Map<String, Object> map72 = new HashMap<String, Object>();
				map72.put("name", "");
				
				map72.put("class", "btn btn-link edit");
				map72.put("onclick", "editable_password(this);");
				map72.put("value", "confirmNewPassword");
				
				list7.add(map71);
				list7.add(map72);
				
				list.add(list5);
				list.add(list6);
				list.add(list7);
			
			}	
			return list;
		}
	}
	public Map<String, Object> save(String user, Map<String, Object> map){
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		User currentUser = this.uDAO.getUniqueByProperty("userId", user);
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		
		String newPassword = null;
		String comfirmNewPassword = null;
		
		boolean whetherChangePassword = false;
		
		boolean flag = true;
		while(keylist.hasNext()){
			String k = keylist.next();
			System.out.println(k);			
			switch(k){
				case "userName" : String Name=(String) map.get(k);
								User queryExistsUser= this.uDAO.getUniqueByProperty("userNickname",Name);
								//合法的用户名
								if( queryExistsUser == null 
										|| queryExistsUser.getUserId() == Integer.parseInt(user) ){
									currentUser.setUserNickname(Name); 
									break;
								}else{
									r.put("isSuccess", false);
									r.put("returndata", "该用户名已被占用");
									return r;
								}
				case "userPhone" : currentUser.setUserPhone((String) map.get(k)); break;
				case "userEmail" : currentUser.setUserEmail((String) map.get(k)); break;
				
				//判断原密码是否正确
				case "oldPassword" : whetherChangePassword = true;
									 UserInfoProcessUtil check = new UserInfoProcessUtil();
									 if(!check.checkPassword((String) map.get(k), currentUser.getUserKeyResult())){
										System.out.println("原密码错误");
										r.put("isSuccess", false);
										r.put("returndata", "原密码错误");
										flag = false;
									 }
									 break;
				case "newPassword" :  newPassword = (String) map.get(k);break;
				case "confirmNewPassword" : comfirmNewPassword = (String) map.get(k);break;
			}
		}
		if(whetherChangePassword && flag){
			try {
				//确认新密码
				if(newPassword.equals(comfirmNewPassword)){
					
					//原密码正确
					System.out.println("原密码正确且新密码符合");
					currentUser.setUserKeyResult(newPassword.hashCode());
					if(this.uDAO.update(currentUser)){
						r.put("isSuccess", true);
						r.put("name", user);
						r.put("key", "selfInfo");
					}
					else{
						r.put("isSuccess", false);
					}	
				}
				else{
					r.put("isSuccess", false);
					r.put("returndata", "两次输入密码不符");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(!whetherChangePassword){
			if(this.uDAO.update(currentUser)){
				r.put("isSuccess", true);
				r.put("name", user);
				r.put("key", "selfInfo");
				System.out.println("未修改密码");
			}
			else{
				r.put("isSuccess", false);
			}
		}
		return r;
	}

}
