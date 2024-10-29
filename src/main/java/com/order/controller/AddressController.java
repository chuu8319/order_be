package com.order.controller;

import com.order.dto.AddressResponseDto;
import com.order.service.GeoCodingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final GeoCodingService geoCodingService;

    @PostMapping("/{id}")
    public ResponseEntity<?> getCoordinates(@PathVariable(value = "id") Long id) {
        String address =  geoCodingService.getCoordinates(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<?> getAllAddress() {
        List<AddressResponseDto> addressResponseDtoList = geoCodingService.getAllAddress();
        return ResponseEntity.ok(addressResponseDtoList);
    }
}
