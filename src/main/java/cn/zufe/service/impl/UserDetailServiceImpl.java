package cn.zufe.service.impl;

import cn.zufe.entity.SystemUser;
import cn.zufe.entity.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 实现UserDetailsService接口
     * @param
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        //1、先查询用户数据
        User user = queryUserByUsername(username);
        //2、如果不存在则直接抛出异常
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //3、查询权限信息并封装成对象返回
        List<String> authorities = queryAuthorities(username);

        return new SystemUser(authorities, user);
    }

    /**
     * 查询用户信息
     * @param username
     * @return
     */
    public User queryUserByUsername(String username){
        //todo:由于此处为模拟，则我这边固定一个数据
        User user = new User();
        user.setUsername(username);
        //这里采用的是BCrypt加密，所以为了模拟从数据库获取数据，我这边直接临时加密
        user.setPassword(passwordEncoder.encode("123456"));
        user.setId(1);
        user.setPhone("123456789654");
        user.setEmail("123456@qq.com");
        return user;
    }

    /**
     * 根据用户数据查询权限信息
     * @param username
     * @return
     */
    public List<String> queryAuthorities(String username){
        //todo:由于此处为模拟，则我这边固定一个权限
        return List.of("test");
    }
}
