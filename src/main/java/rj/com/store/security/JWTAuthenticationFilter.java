package rj.com.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger= LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String requestHeader= request.getHeader("Authorization");
       logger.info("requestHeader:{}",requestHeader);
       String username=null;
       String token=null;
       if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
           token = requestHeader.substring(7);
          try {
              username=jwtHelper.getUsernameFromToken(token);
          }catch (IllegalArgumentException e) {
              logger.error(e.getMessage());
          }
          catch (ExpiredJwtException e) {
              logger.error(e.getMessage());
          }
          catch(MalformedJwtException e) {
              logger.error("Some changed has done token");
          }
          catch(Exception e) {
              logger.error(e.getMessage());
          }
       }else {
           logger.info("Header in valid:{}",requestHeader);
       }
       if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
           UserDetails userDetails = userDetailsService.loadUserByUsername(username);
           //validate token
           if (username.equals(userDetails.getUsername())&&!jwtHelper.isTokenExpired(token)) {
               UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(auth);
           }
       }
       filterChain.doFilter(request, response);
    }
}
