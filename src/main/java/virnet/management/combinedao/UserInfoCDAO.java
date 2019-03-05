package virnet.management.combinedao;

import java.util.List;

import virnet.management.dao.UserDAO;
import virnet.management.entity.User;

public class UserInfoCDAO {
	private UserDAO uDAO = new UserDAO();
	
	@SuppressWarnings("unchecked")
	public String getUserName(int userid){
		List<User> ulist = this.uDAO.getListByProperty("userId", userid);
		
		if(ulist.isEmpty() || ulist.size() > 1){
			//no such user
			return null;
		}
		else{
			return ulist.get(0).getUserNickname();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public User getUser(int userid){
		List<User> ulist = this.uDAO.getListByProperty("userId", userid);
		
		if(ulist.isEmpty() || ulist.size() > 1){
			//no such user
			return null;
		}
		else{
			return ulist.get(0);
		}

	}
}
