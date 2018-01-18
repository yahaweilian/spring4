package com.habuma.restful;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/**
 * 检查某个bean profile是否可用
 * @author Administrator
 *
 */
public class ProfileCondition implements Condition {

	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		if (context.getEnvironment() != null) {
			MultiValueMap<String, Object> attrs = 
					metadata.getAllAnnotationAttributes(Profile.class.getName());
			if(attrs != null) {
				for (Object value : attrs.get("value")) {
					if(context.getEnvironment().acceptsProfiles((String[]) value)){
						return true;
					}
				}
				return false;
			}

		}
		return true;
	}

}
