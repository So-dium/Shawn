package virnet.management.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class VirnetInterceptor extends MethodFilterInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8894256054507220925L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> session = invocation.getInvocationContext().getSession();  
		
		
        if (session.get("username") != null) {  
            return invocation.invoke();  
        } 
        else{
        	if(this.isAjaxRequest(request)){
        		HttpServletResponse response = ServletActionContext.getResponse(); 
        		response.setHeader("sessionstatus", "timeout");
        	}
            return "login";  
        } 	
	}


    private boolean isAjaxRequest(HttpServletRequest request) { 
        String header = request.getHeader("X-Requested-With"); 
        if (header != null && "XMLHttpRequest".equals(header)) 
            return true; 
        else 
            return false; 
    }  
}
