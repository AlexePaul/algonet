package com.algonet.algonetapi.config;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.exceptions.UnauthorizedException;
import com.algonet.algonetapi.models.entities.User;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class GetAuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Check if the parameter is annotated with @GetAuthUser and is of type User
        return parameter.hasParameterAnnotation(GetAuthUser.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    @NonNull
    public Object resolveArgument(@NonNull MethodParameter parameter,@NonNull ModelAndViewContainer mavContainer,@NonNull NativeWebRequest webRequest,@NonNull WebDataBinderFactory binderFactory){
        // Retrieve the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If authentication is not available, throw UnauthorizedException
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }

        // Return the authenticated user
        return authentication.getPrincipal();
    }
}
