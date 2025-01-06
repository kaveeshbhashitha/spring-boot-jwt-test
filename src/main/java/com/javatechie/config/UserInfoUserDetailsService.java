package com.javatechie.config;

import com.javatechie.entity.UserInfo;
import com.javatechie.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoRepository repository;
    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = repository.findByName(username);
        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = repository.findByName(username);
        if (user.isPresent()) {
            UserInfo userInfo = user.get();
            List<SimpleGrantedAuthority> authorities = Arrays.stream(userInfo.getRoles().split(","))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                    .toList();
            return new org.springframework.security.core.userdetails.User(
                    userInfo.getName(),
                    userInfo.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

}
