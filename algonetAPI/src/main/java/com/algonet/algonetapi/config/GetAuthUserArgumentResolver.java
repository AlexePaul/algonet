package com.algonet.algonetapi.config;

import com.algonet.algonetapi.annotations.GetAuthUser;
import com.algonet.algonetapi.models.entities.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        // Check if the parameter is annotated with @GetAuthUser and is of type User
        return parameter.hasParameterAnnotation(GetAuthUser.class) && parameter.getParameterType().equals(User.class);
    }    @Override
    @Nullable
    public User resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        // Retrieve the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If authentication is not available or user is anonymous, return null (allow nullable User)
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        // Check if the principal is a CustomUserPrincipal object
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetailsService.CustomUserPrincipal) {
            return ((CustomUserDetailsService.CustomUserPrincipal) principal).getUser();
        }
        
        // Fallback for direct User objects (should not happen with our setup)
        if (principal instanceof User) {
            return (User) principal;
        }

        // Return null if we can't resolve the user
        return null;
    }
}
