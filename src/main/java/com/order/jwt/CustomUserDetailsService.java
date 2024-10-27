package com.order.jwt;

import com.order.entity.User;
import com.order.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username: " + username);
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("User가 없습니다.");
        }
        return new CustomUserDetails(user);
    }
}