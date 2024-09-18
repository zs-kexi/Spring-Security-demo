package cn.zufe.config;

import cn.zufe.filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //采用BCrypt加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //关闭crsf机制
        http.csrf(csrf -> csrf.disable());
        //配置请求拦截
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/login").permitAll()
                .anyRequest().authenticated());

        //将自定义的jwt令牌装配到认证过滤器中
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 登录时需要调用AuthenticationManager.authenticate执行一次校验
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


}
