package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String restaurant;
    private String amount;
    private String timeStamp;
    private String user;
    private Long chat;
    List<OrderMenuDto> menuList;
}
