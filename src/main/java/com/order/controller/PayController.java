package com.order.controller;

import com.order.common.AuthUser;
import com.order.entity.User;
import com.order.service.PayService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pay")
@AllArgsConstructor
@Slf4j
public class PayController {
    private final PayService payService;
    @PostMapping("/verify/{impUid}")
    public IamportResponse<Payment> verifyPayment(@AuthUser User user, @PathVariable String impUid) throws IamportResponseException, IOException {
        return payService.verifyAndSavePayment(user, impUid);
    }
}
