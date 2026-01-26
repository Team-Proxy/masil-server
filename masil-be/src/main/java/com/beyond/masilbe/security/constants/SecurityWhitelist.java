package com.beyond.masilbe.security.constants;

public final class SecurityWhitelist {

    // 내부 시스템 호출용 API
    public static final String[] INTERNAL = {"/internal/**"};

    // 로그인 / 리프레시 등 인증 없이 접근 가능한 API
    public static final String[] AUTH = {"/api/auth/**","/api/users"};

    public static final String[] SSE = {"/api/sse/**"};

    public static final String[] ACTUATOR = {"/", "/actuator/health", "/actuator/info"};

    public static final String[] SWAGGER = {"/swagger-ui/**", "/v3/api-docs/**"};

    // 인스턴스화 방지
    private SecurityWhitelist() {}
}
