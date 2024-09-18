package cn.zufe.filter;

import cn.zufe.entity.SystemUser;
import cn.zufe.util.JwtUtils;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //免校验白名单
    private static List<String> whiteList;

    static {
        try {
            InputStream resource = JwtAuthenticationFilter.class.getResourceAsStream("/security-whitelist.properties");
            Properties properties = new Properties();
            properties.load(resource);
            whiteList = new ArrayList<>(properties.stringPropertyNames());
        }catch (Exception e){
            log.error("加载/security-whitelist.properties白名单出错：{}",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1、如果请求路径在白名单中直接放行，不需要校验jwt令牌
        String requestUrl = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for(String url : whiteList){
            if(pathMatcher.match(url, requestUrl)){
                filterChain.doFilter(request, response);
                return;
            }
        }
        //2、如果有jwt令牌并且合法则直接放行，如果不合法则直接报错
        String token = request.getHeader("Authorization");
        SystemUser user = null;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            String object = claims.getSubject();
            user = JSON.parseObject(object, SystemUser.class);
        }catch (Exception e){
            throw new RuntimeException("非法令牌");
        }

        //3、将用户信息存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}
