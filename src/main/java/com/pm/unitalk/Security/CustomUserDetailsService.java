package com.pm.unitalk.Security;

import com.pm.unitalk.DTOs.UserDTO;
import com.pm.unitalk.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServices userServices;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userServices.findUserByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        // Create a SimpleGrantedAuthority with the role prefixed with "ROLE_" as required by Spring Security
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        
        // Return a User object (Spring Security's implementation of UserDetails)
        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(authority)
        );
    }
}