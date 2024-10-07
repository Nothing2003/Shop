package rj.com.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import rj.com.store.helper.AppCon;
import rj.com.store.security.JWTAuthenticationFilter;
import rj.com.store.security.JwtAuthenticationEntryPoint;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    private final JWTAuthenticationFilter authenticationFilter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    public SecurityConfig(JWTAuthenticationFilter authenticationFilter, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationFilter = authenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer ->
                httpSecurityCorsConfigurer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    config.setMaxAge(3600L);
                    return config;
                })
                );
        //Configuration
        http.authorizeHttpRequests(
                //configuration URL
                requests ->{
                    requests.requestMatchers(
                            "/swagger-ui/index.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "v2/api-docs/**",
                            "/swagger-resources/**",
                            "webjars/**"
                    ).permitAll();
                    //User URL Configuration
                    requests.requestMatchers(HttpMethod.DELETE,"/users/**").hasAnyRole(AppCon.ROLE_ADMIN,AppCon.ROLE_NORMAL)
                            .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole(AppCon.ROLE_ADMIN,AppCon.ROLE_NORMAL)
                            .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/users/**").permitAll();
                    //Production URL Configuration
                    requests.requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                            .requestMatchers("/products/**").hasRole(AppCon.ROLE_ADMIN);
                    //Categories URL Configuration
                    requests.requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                            .requestMatchers("/categories/**").hasRole(AppCon.ROLE_ADMIN);
                    //Authentication URL Configuration
                    requests.requestMatchers("/auth/**").permitAll();


                }
        );
        //Type of Security used
//        http.httpBasic(Customizer.withDefaults());
        //entry point

        http.exceptionHandling(exception ->
            exception.authenticationEntryPoint(authenticationEntryPoint)
        );
        //session creation policy
        http.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //main
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }


}
