package com.dziem.popapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements Filter {
    // Limit to 200 requests per IP per minute
    private static final long MAX_REQUESTS_PER_MINUTE = 200;
    private static final long WINDOW_DURATION = TimeUnit.MINUTES.toMillis(1);

    private ConcurrentHashMap<String, UserRateLimiter> clientRequestMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = httpRequest.getRemoteAddr();  // Identify client by IP address

        UserRateLimiter rateLimiter = clientRequestMap.computeIfAbsent(clientIp, k -> new UserRateLimiter());

        if (rateLimiter.allowRequest()) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());  // 429 Too Many Requests
            httpResponse.getWriter().write("Too many requests - please slow down.");
        }
    }

    // Inner class to track rate limit per user
    private static class UserRateLimiter {
        private long requestCount = 0;
        private long windowStartTime = System.currentTimeMillis();

        public boolean allowRequest() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - windowStartTime > WINDOW_DURATION) {
                requestCount = 0;
                windowStartTime = currentTime;
            }

            if (requestCount < MAX_REQUESTS_PER_MINUTE) {
                requestCount++;
                return true;
            }

            return false;
        }
    }
}
