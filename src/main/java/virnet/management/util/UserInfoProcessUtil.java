package virnet.management.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import virnet.management.dao.UserDAO;
import virnet.management.entity.User;

public class UserInfoProcessUtil {
	public int checkUsername(String user){
		Matcher matcher = Pattern.compile("^[0-9]+$").matcher(user);  
        if(matcher.find()){
        	//if user name is 0, then will be supposed to be wrong
        	try{
        		System.out.println("user name is : " + user + " and it's an integer, as " + Integer.parseInt(user));
        		return Integer.parseInt(user);
        	}catch(Exception e){
        		System.out.println("user name is : " + user + " and it's not an integer");
        		return 0;
        	}	
        }
        else{
        	System.out.println("user name is : " + user + " and it's not an integer");
        	return 0;
        }
	}
	
	public boolean checkPassword(String password, int keyHash){
		System.out.println("password is : " + password + "; and the hashcode is : " + password.hashCode());
		if(password.hashCode() == keyHash){
			return true;
		}
		else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public User getUser(int userid){
		UserDAO uDAO = new UserDAO();
		List<User> ulist = uDAO.getListByProperty("userId", userid);
		if(ulist.isEmpty() || ulist.size() > 1){
			return null;
		}
		else{
			return ulist.get(0);
		}
	}
}
