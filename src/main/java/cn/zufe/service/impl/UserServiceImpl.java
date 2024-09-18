package cn.zufe.service.impl;

import cn.zufe.entity.LoginUser;
import cn.zufe.entity.SystemUser;
import cn.zufe.service.UserService;
import cn.zufe.util.JwtUtils;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public String login(LoginUser user) {
        //1、将其封装为authentication对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //2、进行校验
        Authentication authenticate = authenticationManager.authenticate(authentication);

        //3、如果authentication为空
        if(authenticate == null){
            throw new RuntimeException("用户名或密码错误");
        }

        //4、将数据放入到上下文中
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticate);
        SecurityContextHolder.setContext(context);

        //4、否则则登入成功，并且颁发令牌
        SystemUser principal = (SystemUser) authenticate.getPrincipal();

        return JwtUtils.createJWT(JSON.toJSONString(principal), null);
    }
}
