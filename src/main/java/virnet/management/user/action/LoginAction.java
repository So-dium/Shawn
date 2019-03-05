package virnet.management.user.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import virnet.management.dao.UserDAO;
import virnet.management.user.service.LoginService;
import virnet.management.util.LoginDataUtil;
import virnet.management.util.LoginReturnData;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4931099203948953274L;

	private LoginService loginservice = new LoginService();

	private String userId = new String();
	private String password = new String();
	
	private Map<String, Object> session; 
	
	public String login(){
		String user = this.getUserId();
		String password = this.getPassword();
		
		ActionContext.getContext().getSession().put("User",user);
		
		LoginReturnData data = this.loginservice.login(user, password);
		
		this.setSession();
		
		this.session.put("state", data.getState());
		this.session.put("statement", data.getStatement());
		
		if(data.getState() == 4){//login successfully
			List<LoginDataUtil> logindata = new ArrayList<LoginDataUtil>();
			
			int size = data.getPowerlist().size();
			
			for(int i = 0; i < size; i++){
				LoginDataUtil temp = new LoginDataUtil();
				
				if(i == 0){
					temp.setClassname("sidebar-active");;
				}
				else{
					temp.setClassname("sidebar-nonactive");
				}
				temp.setPowerinfo(data.getPowerlist().get(i).getPowerInfo());
				temp.setIdname(data.getPowerlist().get(i).getPowerId());
				
				logindata.add(temp);
			}
			
			this.session.put("powerlist", logindata);
			
			UserDAO uDAO = new UserDAO(); 
			String userName = uDAO.getUniqueByProperty("userId", user).getUserNickname();
			
			
			//session此时不能为空
			if(userName == null){
				this.session.put("username", "");
			}else{
				this.session.put("username", userName);
			}
			

			this.session.put("workgroup","0");
			System.out.println("workgroup="+session.get("workgroup")+"username is "+ session.get("username"));
//			this.session.put("expId",null);
			
			return SUCCESS;
		}
		else{
			return ERROR;
		}
	}
	
	public LoginService getLoginService(){
		return this.loginservice;
	}
	
	public void setLoginService(LoginService login){
		this.loginservice = login;
	}
	
	public String getUserId() {
		 return this.userId;
	}
	 
	 public void setUserId(String userId) {
	     this.userId = userId;
	 }
	 
	 public String getPassword() {
		 return password;
	 }
	 
	 public void setPassword(String password) {
		 this.password = password;
	 }
	 
	 public void setSession(){
		 this.session = ActionContext.getContext().getSession();
	 }
//	 public void setExpId(Integer expId){
//		 this.session.put("expId", expId);
//	 }
//	 public Object getExpId(){
//		 return this.session.get("expId");
//	 }
}
