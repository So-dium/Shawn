package virnet.management.realExpStuNum;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class realExpStuNumAction extends ActionSupport implements ServletRequestAware{
	
	/**
	 * 在学生进入实验平台之前，在session中修改实验人数
	 */
	private static final long serialVersionUID = 8225994305475618173L;

	private HttpServletRequest request;
	
	private Map<String, Object> result = new HashMap<String, Object>();
	
	public String realExpStuNum(){
		
		String stuNum = this.request.getParameter("realExpStuNum");
		
		ActionContext.getContext().getSession().put("realExpStuNum", Integer.parseInt(stuNum));

		//System.out.println(Integer.parseInt(stuNum));
		return SUCCESS;
	}
	
	public Map<String, Object> getResults(){
		return this.result;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
	}
}