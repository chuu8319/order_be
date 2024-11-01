package com.order.configuration;

import java.util.List;


import com.order.jwt.AuthUserResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final AuthUserResolver authUserResolver;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("http://localhost:3000") // 프론트엔드 요청 수락
			.allowedMethods("GET", "POST", "DELETE", "PATCH")
			.exposedHeaders("token", "username")
			.allowedHeaders("Authorization", "Content-Type");
	}

	// token 정보 Users Entity에 저장
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserResolver);
	}
}