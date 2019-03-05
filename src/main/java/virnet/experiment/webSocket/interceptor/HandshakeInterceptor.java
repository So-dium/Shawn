package virnet.experiment.webSocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Component
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("Before Handshake");
		
		if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            if (session != null) {
                //获取用户的用户名和组别,存到web socket的session中
            	System.out.println("HandshakeInterceptor session != null");
            	String userName = session.getAttribute("username").toString();
            	String workgroup = session.getAttribute("workgroup").toString();
            	String pageType = session.getAttribute("pageType").toString();
            	String expRole = session.getAttribute("expRole").toString();
            	System.out.println("pageType :" + pageType);
            	System.out.println("expRole :" + expRole);
            	attributes.put("WS_USER_Name",userName);
            	attributes.put("WS_USER_WorkGroup",workgroup);
            	attributes.put("WS_USER_pageType",pageType);
            	attributes.put("WS_USER_expRole",expRole);
            	
            	if (!expRole.equals("monitor") ){
            		System.out.println("HandshakeInterceptor pageType != null");
            		String realExpStuNum = session.getAttribute("realExpStuNum").toString();
            		attributes.put("WS_USER_RealExpStuNum",realExpStuNum);
            	}
            }
        }
		
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		System.out.println("After Handshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}

}
