package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPay {
    private String userName;
    private String timeStamp;
    private String amount;
    private List<OrderMenuDto> menuList;
}
