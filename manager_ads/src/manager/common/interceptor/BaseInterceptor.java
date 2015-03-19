package manager.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class BaseInterceptor implements Interceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2173413651364995580L;

	@Override
	public void destroy() {
		System.out.println("Destroy Interceptor");
	}
	
	@Override
	public void init() {
		System.out.println("Init Interceptor");
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		System.out.println(request.getRequestURL());
		return invocation.invoke();
	}
}
