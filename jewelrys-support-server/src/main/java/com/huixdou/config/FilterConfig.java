package com.huixdou.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huixdou.api.filter.SupFilter;

@Configuration
public class FilterConfig {
	
	@Bean
	public SupFilter supFilter() {
		return new SupFilter();
	}
	
	@Bean
	public FilterRegistrationBean<Filter> registSupFilter() {
		final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(supFilter());
		registrationBean.addUrlPatterns("/sup/*");
		return registrationBean;
	}

}
