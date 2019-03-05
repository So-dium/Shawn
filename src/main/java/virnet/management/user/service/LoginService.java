package virnet.management.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import virnet.management.dao.CharacterPowerDAO;
import virnet.management.dao.PowerDAO;
import virnet.management.dao.UserCharacterDAO;
import virnet.management.dao.UserDAO;
import virnet.management.entity.CharacterPower;
import virnet.management.entity.Power;
import virnet.management.entity.User;
import virnet.management.entity.UserCharacter;
import virnet.management.util.LoginReturnData;
import virnet.management.util.UserInfoProcessUtil;

public class LoginService {
	private UserDAO userDAO = new UserDAO();
	private UserCharacterDAO usercharacterDAO = new UserCharacterDAO();
	private CharacterPowerDAO characterpowerDAO = new CharacterPowerDAO();
	private PowerDAO powerDAO = new PowerDAO();
	
	private UserInfoProcessUtil check = new UserInfoProcessUtil();
	
	/*
	 *@param
	 *user --- user name submit when login
	 *password --- password submit when login
	 *
	 *return
	 *
	 *login state --- no such user, error password, database exception, user without character, proper state of login
	 *
	 *if correct, power list is required
	 *
	 */
	@SuppressWarnings("unchecked")
	public LoginReturnData login(String user, String password){
		//query from user to check the user information
		//query from user for password
		//then fetch the information about role and permission
		LoginReturnData data = new LoginReturnData();
		List<Power> powerlist = new ArrayList<Power>();
		//check the user

		//process the user name
		int userid = this.check.checkUsername(user);
		
		if(userid == 0){
			//wrong user name
			
			//no such user
			data.setState(0);
			
			data.setStatement("No such user!");
			
			data.setPowerlist(powerlist);
		}
		else{
			System.out.println("userId"+ userid);
			List<User> userlist= this.userDAO.getListByProperty("userId", userid);
			if(userlist.size() > 1){
				//database exception
				data.setState(2);
				
				data.setStatement("Data exception");
				
				data.setPowerlist(powerlist);
			}
			else if(userlist.isEmpty()){
				//no such user
				data.setState(0);
				
				data.setStatement("No such user!");
				
				data.setPowerlist(powerlist);
			}
			else{
				//check password
				if(this.check.checkPassword(password, userlist.get(0).getUserKeyResult())){
					//acquire user character
					List<UserCharacter> userCharacterlist = this.usercharacterDAO.getListByProperty("userCharacterUserId", userlist.get(0).getUserId());
					
					Comparator<UserCharacter> comparator = new Comparator<UserCharacter>(){
					     public int compare(UserCharacter c1, UserCharacter c2) {
					    	 return c1.getUserCharacterCharacterId()-c2.getUserCharacterCharacterId();
					     }
					 };
					 
					 Collections.sort(userCharacterlist,comparator);
					
					if(userCharacterlist.isEmpty()){
						//no character
						data.setState(3);
						
						data.setStatement("User without character");
						
						data.setPowerlist(powerlist);
					}
					else{
						//find out power of each character
						int userCharacterlistLength = userCharacterlist.size();
						
						for(int i = 0; i < userCharacterlistLength; i++){
							List<CharacterPower> characterpowerlist = this.characterpowerDAO.getListByProperty("characterPowerCharacterId", userCharacterlist.get(i).getUserCharacterCharacterId());
						
							int l = characterpowerlist.size();
							for(int j = 0; j < l; j++){
								//add power of each character to the user power list
								powerlist.addAll(this.powerDAO.getListByProperty("powerId", characterpowerlist.get(j).getCharacterPowerPowerId()));
							}
						}
						
						//return power list
						
						data.setState(4);
						
						data.setStatement("login successfully");
						
						data.setPowerlist(powerlist);
					}
				}
				else{
					//error password
					data.setState(1);
					
					data.setStatement("wrong password");
					
					data.setPowerlist(powerlist);
				}
			}
		}	
		return data;
	}
}
