package com.order.service;

import com.order.dto.JoinDto;
import com.order.entity.Pay;
import com.order.entity.PayMenu;
import com.order.entity.Restaurant;
import com.order.entity.User;
import com.order.repository.PayRepository;
import com.order.repository.RestaurantRepository;
import com.order.repository.UserRepository;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestaurantRepository restaurantRepository;

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

    public long updateUser(User user, JoinDto joinDto) {
        if(!userRepository.existsByUserId(user.getUserId())) {
            return -1;
        }
        user.setUserPassword(joinDto.getUserPassword());
        user.setUserEmail(joinDto.getUserEmail());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public long deleteUser(User user) {
        if(!userRepository.existsByUserId(user.getUserId())) {
            return -1;
        }
        userRepository.deleteById(user.getId());
        return user.getId();
    }

    public List<?> searchUser(User user) {
        if(Objects.equals(user.getUserType(), "owner")) {
            return restaurantRepository.findByUser(user);
        }
        else {
            return null;
        }
    }
}
