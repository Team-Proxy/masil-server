package com.beyond.masilbe.security.resolver;

import com.beyond.masilbe.domain.auth.exception.AuthException;
import com.beyond.masilbe.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(CurrentUserContext.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw AuthException.unauthorized();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails user)) {
            throw AuthException.unauthorized();
        }

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // IP / UserAgent
        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        String ip = resolveClientIp(req);
        String ua = req != null ? req.getHeader("User-Agent") : null;

        return CurrentUserContext.of(user.getUserId(), user.getEmail(), ip, ua);
    }

    private String resolveClientIp(final HttpServletRequest request) {
        if (request == null) return null;
        String xf = request.getHeader("X-Forwarded-For");
        return (xf != null) ? xf.split(",")[0].trim() : request.getRemoteAddr();
    }
}
