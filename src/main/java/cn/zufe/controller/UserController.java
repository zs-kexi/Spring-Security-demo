package cn.zufe.controller;

import cn.zufe.entity.LoginUser;
import cn.zufe.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@EnableMethodSecurity
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('test')")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/login")
//    @PreAuthorize("hasAuthority('test')")
    public String login(@RequestBody LoginUser user){
        return userService.login(user);
    }
}
