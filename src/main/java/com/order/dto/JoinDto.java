package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    private String userId;
    private String userPassword;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userType;
}
