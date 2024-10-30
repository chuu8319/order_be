package com.order.jwt;

import com.order.common.AuthUser;
import com.order.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserResolver implements HandlerMethodArgumentResolver {

	private final JWTUtil jwtUtil;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
		boolean isUserEntityType = User.class.isAssignableFrom(parameter.getParameterType());
		
		return hasAnnotation && isUserEntityType;
	}

	@Override // JwtFilter에서 모두 검증하므로, 검증 로직은 추가하지 않음
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    	HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    	String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    	
    	if(authorizationHeader == null)
    		return null;
    	
    	String jwtToken = authorizationHeader.substring(7);
    	User user = jwtUtil.getUser(jwtToken);

        return user;
    }

}
