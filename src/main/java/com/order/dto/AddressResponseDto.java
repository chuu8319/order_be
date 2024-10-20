package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private Long id;
    private Double x;
    private Double y;
    private Long restaurantId;
    private String restaurantName;
    private String restaurantCategory;
    private String restaurantPhone;
    private String restaurantAddress;
    private String restaurantStoredFilePath;
}
