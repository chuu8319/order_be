package com.order.service;

import com.order.dto.JoinDto;
import com.order.dto.OrderDto;
import com.order.dto.OrderMenuDto;
import com.order.dto.OrderPay;
import com.order.entity.*;
import com.order.exception.ResourceNotFoundException;
import com.order.repository.*;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final PayMenuRepository payMenuRepository;
    private final ChatRepository chatRepository;
    private final PayRepository payRepository;

    public long joinUser(JoinDto joinDto) {
        if (userRepository.existsByUserId(joinDto.getUserId())) {
            return -1;
        }
        User user = new User();
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
        if (!userRepository.existsByUserId(user.getUserId())) {
            return -1;
        }
        user.setUserPassword(bCryptPasswordEncoder.encode(joinDto.getUserPassword()));
        user.setUserEmail(joinDto.getUserEmail());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public long deleteUser(User user) {
        if (!userRepository.existsByUserId(user.getUserId())) {
            return -1;
        }
        userRepository.deleteById(user.getId());
        return user.getId();
    }

    public List<?> searchUser(User user) {
        if (Objects.equals(user.getUserType(), "owner")) {
            return restaurantRepository.findByUser(user);
        } else {
            return null;
        }
    }

    public List<OrderDto> getOwnerOrder(User user) {
        if (user.getUserType().equals("customer")) {
            throw new ResourceNotFoundException("owner만 조회할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        List<Restaurant> restaurants = restaurantRepository.findByUser(user);
        List<OrderDto> orderDtoList = new ArrayList<>();

        restaurants.stream().forEach(restaurant -> {
            OrderDto orderDto = new OrderDto();
            List<OrderMenuDto> orderMenuDtoList = new ArrayList<>();
            orderDto.setRestaurant(restaurant.getRestaurantName());

            List<PayMenu> payMenus = payMenuRepository.findByRestaurant(restaurant);

            // 겹치는 user, amount, timestamp는 한 번만 설정
            if (!payMenus.isEmpty()) {
                PayMenu firstPayMenu = payMenus.get(0); // 첫 번째 PayMenu에서 값 설정
                orderDto.setUser(firstPayMenu.getPay().getUser().getUserName());
                orderDto.setAmount(firstPayMenu.getPay().getAmount());
                orderDto.setTimeStamp(firstPayMenu.getPay().getCreatedAt());
                orderDto.setChat((chatRepository.findByRestaurantAndCustomer(restaurant, firstPayMenu.getPay().getUser())).getId());
            }

            // 메뉴별로 그룹화
            payMenus.forEach(payMenu -> {
                OrderMenuDto orderMenuDto = new OrderMenuDto();
                orderMenuDto.setMenu(payMenu.getMenu().getMenuName());  // 메뉴 이름 설정

                // payMenu에서 count 값을 직접 가져와서 totalCount에 합산
                int totalCount = payMenu.getCount();
                orderMenuDto.setCount(totalCount);  // 메뉴의 총 수량 설정

                // orderDto에 추가
                orderMenuDtoList.add(orderMenuDto);
            });

            orderDto.setMenuList(orderMenuDtoList);
            orderDtoList.add(orderDto);
        });

        return orderDtoList;
    }

    public List<OrderDto> getCustomerOrder(User user) {
        if (user.getUserType().equals("owner")) {
            throw new ResourceNotFoundException("customer만 조회할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        List<Pay> payList = payRepository.findByUser(user);
        List<OrderDto> orderDtoList = new ArrayList<>();
        payList.forEach(pay -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setUser(user.getUserId());
            orderDto.setAmount(pay.getAmount());
            orderDto.setTimeStamp(pay.getCreatedAt());
            List<PayMenu> payMenuList = payMenuRepository.findByPay(pay);
            List<OrderMenuDto> orderMenuDtoList = new ArrayList<>();
            payMenuList.forEach(payMenu -> {
                OrderMenuDto orderMenuDto = new OrderMenuDto();
                if(orderDto.getRestaurant() == null || orderDto.getChat() == null) {
                    orderDto.setRestaurant(payMenu.getRestaurant().getRestaurantName());
                    orderDto.setChat(chatRepository.findByRestaurantAndCustomer(payMenu.getRestaurant(), user).getId());
                }
                orderMenuDto.setMenu(payMenu.getMenu().getMenuName());
                orderMenuDto.setCount(payMenu.getCount());
               orderMenuDtoList.add(orderMenuDto);
            });
            orderDto.setMenuList(orderMenuDtoList);
            orderDtoList.add(orderDto);
        });
        return orderDtoList;
    }
}
