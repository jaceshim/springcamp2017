package jace.shim.springcamp2017.member.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 4. 15..
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	private ApplicationContextProvider(){}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public  static <T> T getBean(String name,Class<T> aClass){
		return context.getBean(name,aClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
}
