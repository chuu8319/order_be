package com.order.service;

import com.order.entity.Cart;
import com.order.entity.Menu;
import com.order.entity.Restaurant;
import com.order.entity.User;
import com.order.repository.CartRepository;
import com.order.repository.MenuRepository;
import com.order.repository.RestaurantRepository;
import com.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public long addTOCart(Long userId, Long restaurantId, Long menuId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 음식점 ID입니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));

        List<Cart> userCartItems = cartRepository.findByUserId(userId);

        if (!userCartItems.isEmpty() && !userCartItems.get(0).getRestaurant().getId().equals(restaurantId)) {
            return -1;
        }

        Cart existingCartItem = cartRepository.findByUserIdAndMenuId(userId, menuId);
        long menuPrise = Long.parseLong(menu.getMenuPrice().replace(",", ""));

        if (existingCartItem != null) {
            existingCartItem.setCount(existingCartItem.getCount() + 1);
            existingCartItem.setPrice(menuPrise * existingCartItem.getCount());

            Cart savedCart = cartRepository.save(existingCartItem);
            return savedCart.getId();
        }
        // 새 항목을 장바구니에 추가
        Cart cartItem = Cart.builder()
                .user(user)
                .restaurant(restaurant)
                .menu(menu)
                .price(menuPrise)
                .count(1)
                .build();
        Cart savedCart = cartRepository.save(cartItem);

        return savedCart.getId();
    }

    public long addCart(long userId, long menuId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));

        Cart existingCartItem = cartRepository.findByUserIdAndMenuId(userId, menuId);
        long menuPrise = Long.parseLong(menu.getMenuPrice().replace(",", ""));

        if (existingCartItem == null) {
            return -1;
        }

        existingCartItem.setCount(existingCartItem.getCount() + 1);
        existingCartItem.setPrice(menuPrise * existingCartItem.getCount());
        Cart savedCart = cartRepository.save(existingCartItem);
        return savedCart.getId();
    }

    public long removeCart(long userId, long menuId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));

        Cart existingCartItem = cartRepository.findByUserIdAndMenuId(userId, menuId);
        long menuPrise = Long.parseLong(menu.getMenuPrice().replace(",", ""));

        if (existingCartItem == null) {
            return -1;
        } else if (existingCartItem.getCount() == 1) {
            cartRepository.deleteById(existingCartItem.getId());
            return existingCartItem.getId();
        }

        existingCartItem.setCount(existingCartItem.getCount() - 1);
        existingCartItem.setPrice(menuPrise * existingCartItem.getCount());
        Cart savedCart = cartRepository.save(existingCartItem);
        return savedCart.getId();
    }

    public List<Cart> getAllCart(Long id) {
        return cartRepository.findByUserId(id);
    }

    public long removeFromCart(Long userId, Long menuId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));

        Cart existingCartItem = cartRepository.findByUserIdAndMenuId(userId, menuId);
        if (existingCartItem == null) {
            return -1;
        }
        cartRepository.deleteById(existingCartItem.getId());
        return existingCartItem.getId();
    }
}
