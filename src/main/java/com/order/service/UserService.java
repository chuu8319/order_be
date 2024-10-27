package com.order.service;

import com.order.dto.JoinDto;
import com.order.entity.User;
import com.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public long joinUser(JoinDto joinDto) {
        if(userRepository.existsByUserId(joinDto.getUserId())) {
            return -1;
        }
        User user =new User();
        user.setUserId(joinDto.getUserId());
        user.setUserPassword(bCryptPasswordEncoder.encode(joinDto.getUserPassword()));
        user.setUserName(joinDto.getUserName());
        user.setUserPhone(joinDto.getUserPhone());
        user.setUserEmail(joinDto.getUserEmail());
        user.setUserType(joinDto.getUserType());
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
