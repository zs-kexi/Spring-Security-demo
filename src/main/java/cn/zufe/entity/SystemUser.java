package cn.zufe.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户
 */
@Data
@NoArgsConstructor
public class SystemUser implements UserDetails {
    //用户信息
    private User user;

    //权限
    List<SimpleGrantedAuthority> authorities;

    public SystemUser(List<String> authoritiesList, User user){
        authorities = new ArrayList<>();
        for(String authority : authoritiesList){
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
