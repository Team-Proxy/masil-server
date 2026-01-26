package com.beyond.masilbe.security.jwt;

import com.beyond.masilbe.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenRepository redisTokenRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    // TTL 추후 사용
    // private static final Duration PERMISSION_TTL = Duration.ofMinutes(10);

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader(JwtConstants.HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(JwtConstants.TOKEN_PREFIX_LENGTH).trim();

        if (token.isEmpty()) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request, response, new InsufficientAuthenticationException("Empty JWT token"));
            return;
        }

        // 블랙리스트 토큰이면 인증 없이 다음 필터로 넘김
        if (redisTokenRepository.isBlacklisted(token)) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request, response, new InsufficientAuthenticationException("블랙리스트 처리된 토큰입니다."));
            return;
        }

        try {
            Claims claims = jwtTokenProvider.getClaims(token);

            Long userId = Long.valueOf(claims.getSubject());
            String email = claims.get(JwtConstants.CLAIM_EMAIL, String.class);

            String role = claims.get(JwtConstants.CLAIM_ROLE, String.class);
            List<String> permissions = claims.get(JwtConstants.CLAIM_PERMISSIONS, List.class);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // role은 optional
            if (role != null && !role.isBlank()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            // permissions도 optional
            if (permissions != null) {
                permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
            }

            CustomUserDetails userDetails = new CustomUserDetails(userId, email, authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 정상 인증 → 다음 필터
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("[JwtFilter] Expired JWT token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request, response, new InsufficientAuthenticationException("Expired JWT token"));

        } catch (Exception e) {
            log.warn("[JwtFilter] Invalid JWT token", e);
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request, response, new InsufficientAuthenticationException("Invalid JWT token"));
        }
    }
}
