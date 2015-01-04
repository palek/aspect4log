package org.foo.config;

import net.sf.aspect4log.aspect.LogAspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("org.foo")
@EnableAspectJAutoProxy
public class FooConfiguration {

	@Bean
	LogAspect createLogAspect(){
		return  LogAspect.aspectOf();
	}
	
}
