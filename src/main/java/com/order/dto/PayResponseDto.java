package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResponseDto {
    String name;
    Long amount;
    String buyer_email;
    String buyer_name;
    String  buyer_tel;
}
