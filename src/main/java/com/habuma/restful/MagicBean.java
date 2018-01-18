package com.habuma.restful;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

public class MagicBean {

	@Bean
	@Conditional(MagicExistsCondition.class)
	public MagicBean magicBean(){
		return new MagicBean();
	}
	
}
