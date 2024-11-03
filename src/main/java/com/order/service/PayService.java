package com.order.service;

import com.order.entity.Cart;
import com.order.entity.Pay;
import com.order.entity.PayMenu;
import com.order.entity.User;
import com.order.repository.CartRepository;
import com.order.repository.PayMenuRepository;
import com.order.repository.PayRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PayService {
    private final IamportClient iamportClient;
    private final PayRepository payRepository;
    private final CartRepository cartRepository;
    private final PayMenuRepository payMenuRepository;

    public PayService(@Value("${iamport.api.key}") String apiKey,
                      @Value("${iamport.api.secret}") String apiSecret,
                      PayRepository payRepository, CartRepository cartRepository, PayMenuRepository payMenuRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.payRepository = payRepository;
        this.cartRepository = cartRepository;
        this.payMenuRepository = payMenuRepository;
    }

    // 결제 검증 및 DB 저장 메서드 추가
    public IamportResponse<Payment> verifyAndSavePayment(User user, String impUid) throws IamportResponseException, IOException {
        log.info("userId:" + user.getId());
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

        List<Cart> userCartItems = cartRepository.findByUserId(user.getId());
        Payment payment = response.getResponse();
        Pay payEntity = new Pay();
        if (payment == null) {
            return null;
        } else {
            payEntity.setImpUid(payment.getImpUid());
            payEntity.setMerchantUid(payment.getMerchantUid());
            payEntity.setAmount(String.valueOf(payment.getAmount()));
            payEntity.setStatus(payment.getStatus());
            payEntity.setCreatedAt(payment.getPaidAt().toString());
            payEntity.setUser(user);
            payRepository.save(payEntity);

            for (Cart cartItem : userCartItems) {
                PayMenu payMenu = new PayMenu();
                payMenu.setMenu(cartItem.getMenu());
                payMenu.setCount(cartItem.getCount());
                payMenu.setPay(payEntity);
                payMenuRepository.save(payMenu);
                cartRepository.delete(cartItem);
            }
        }
        return response;
    }
}