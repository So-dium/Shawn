package virnet.management.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class UserCheck {
	public Object check(ProceedingJoinPoint pjp){
		
		System.out.println("this is usercheck and the argvs are : " + pjp);
		Object[] args = pjp.getArgs();
		System.out.println("there are args :" + args[0]);
		
		Object service = null;
		try {
			service = pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("check end");
		
		return service;
	}
}