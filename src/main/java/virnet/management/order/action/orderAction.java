package virnet.management.order.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;
import virnet.management.combinedao.OrderCDAO;

public class orderAction extends ActionSupport implements ServletRequestAware{
	
	private static final long serialVersionUID = -8115107054048074153L;
	
	private HttpServletRequest request;
	private Map<String, Object> result = new HashMap<String, Object>();
	private OrderCDAO ocDAO = new OrderCDAO();
	
	public String addOrder(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String ClassId = this.request.getParameter("ClassId");
		String ExpId = this.request.getParameter("ExpId");
		String ExpArrangeId = this.request.getParameter("ExpArrangeId");
		
		this.setResult(ocDAO.Add(user, id, Integer.parseInt(ClassId), Integer.parseInt(ExpId),Integer.parseInt(ExpArrangeId)));
		
		return SUCCESS;
	}
	
	public String submitOrder(){
		String user = this.request.getParameter("user");
		Map<String, String[]> data = this.request.getParameterMap();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Set<String> k = data.keySet();
		Iterator<String> i = k.iterator();
		while(i.hasNext()){
			String s = i.next();
			if(s.contains("data[")){
				if(s.contains("[]")){				
					map.put(s.substring(5, s.indexOf("[]") - 1), data.get(s));
				}
				else{
					String key = s.substring(5, s.length() - 1);
					map.put(key, data.get(s)[0]);
				}	
			}
		}
		
//		Set<String> test = map.keySet();
//		Iterator<String> testt = test.iterator();
//		while(testt.hasNext()){
//			String testtt = testt.next();
//			System.out.println(map.get(testtt));
//		}
		
		this.setResult(ocDAO.dynamicSave(user, map));
		return SUCCESS;
	}
	
	public String confirmOrder(){
		String user = this.request.getParameter("user");
		String orderId = this.request.getParameter("orderId");
		String confirmOrNot = this.request.getParameter("confirmOrNot");
		
		try {
			this.setResult(ocDAO.confirmOrder(Integer.parseInt(user),Integer.parseInt(orderId),Integer.parseInt(confirmOrNot)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String deleteOrder(){
		String orderId = this.request.getParameter("orderId");
		
		try {
			this.setResult(ocDAO.deleteOrder(Integer.parseInt(orderId)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> results) {
		this.result = results;
	}
}
