package rj.com.store.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rj.com.store.datatransferobjects.ApiResponseMessage;

import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString( ApiResponseMessage.builder()
                .success(false)
                .massage(authException.getMessage())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build());
     response.setContentType("application/json");
     response.getWriter().write(json);
    }
}
