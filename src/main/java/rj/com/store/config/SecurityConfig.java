package rj.com.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        //Configuration
        http.authorizeHttpRequests(
                //configuration URL
                requests ->{
                    //User URL Configuration
                    requests.requestMatchers(HttpMethod.DELETE,"/users/**").hasAnyRole("ADMIN","NORMAL")
                            .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("ADMIN","NORMAL")
                            .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/users/**").permitAll();
                    //Production URL Configuration
                    requests.requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                            .requestMatchers("/products/**").hasRole("ADMIN");
                    //Categories URL Configuration
                    requests.requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                            .requestMatchers("/categories/**").hasRole("ADMIN");
                }
        );
        //Type of Security used
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
