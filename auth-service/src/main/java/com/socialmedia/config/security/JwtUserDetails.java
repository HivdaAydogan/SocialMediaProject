package com.socialmedia.config.security;

import com.socialmedia.repository.entity.Auth;
import com.socialmedia.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {

    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUserId(Long id){
        Optional<Auth> auth  = authService.findById(id);
        if (auth.isPresent()){
            // Bir yetkilendirme mekanizmasını ve kullanıcının rollerini temsil eder.
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority(auth.get().getRole().toString()));
            /**
             * Rolleri normalde liste şeklinde tutarız ama bu uygulamada tek bir rolü varmış gibi tuttuk.
             *
             */
            return User.builder()
                    .username(auth.get().getUsername())
                    .password("")
                    .accountExpired(false)
                    .accountLocked(false)
                    .authorities(authorityList).build();
        }
        return null;
    }


}
