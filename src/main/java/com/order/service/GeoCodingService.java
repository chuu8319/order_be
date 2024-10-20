package com.order.service;

import com.order.dto.AddressResponseDto;
import com.order.entity.Address;
import com.order.entity.Image;
import com.order.entity.Restaurant;
import com.order.exception.ResourceNotFoundException;
import com.order.repository.AddressRepository;
import com.order.repository.ImageRepository;
import com.order.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GeoCodingService {
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final ImageRepository imageRepository;

    @Value("${NAVER_GEOCODE_URL}")
    String NAVER_GEOCODE_URL;

    @Value("${CLIENT_ID}")
    String CLIENT_ID;

    @Value("${CLIENT_SECRET}")
    String CLIENT_SECRET;

    public String getCoordinates(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        String address = restaurant.getRestaurantAddress();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        String url = NAVER_GEOCODE_URL + "?query=" + address;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());

        System.out.println(jsonResponse);
        Double latitude = jsonResponse.getJSONArray("addresses")
                .getJSONObject(0)
                .getDouble("y");
        Double longitude = jsonResponse.getJSONArray("addresses")
                .getJSONObject(0)
                .getDouble("x");

        // 새로운 Address 생성 및 저장
        Address newAddress = new Address();
        newAddress.setX(latitude);
        newAddress.setY(longitude);
        newAddress.setRestaurant(restaurant);

        addressRepository.save(newAddress);

        return response.getBody();
    }

    public List<AddressResponseDto> getAllAddress() {
        List<Address> addressList = addressRepository.findAll();
        List<Image> images = imageRepository.findAll();

        List<AddressResponseDto> addressResponseDtoList = new ArrayList<>();

        for(Address address : addressList) {
            AddressResponseDto addressResponseDto = new AddressResponseDto();
            addressResponseDto.setId(address.getId());
            addressResponseDto.setX(address.getX());
            addressResponseDto.setY(address.getY());
            addressResponseDto.setRestaurantId(address.getRestaurant().getId());
            addressResponseDto.setRestaurantName(address.getRestaurant().getRestaurantName());
            addressResponseDto.setRestaurantCategory(address.getRestaurant().getRestaurantCategory());
            addressResponseDto.setRestaurantAddress(address.getRestaurant().getRestaurantAddress());
            addressResponseDto.setRestaurantPhone(address.getRestaurant().getRestaurantPhone());
            images.stream()
                    .filter(image -> image.getRestaurant().getId().equals(address.getRestaurant().getId()))
                    .findFirst().ifPresent(matchingImage -> addressResponseDto.setRestaurantStoredFilePath(matchingImage.getStoredFilePath()));
            addressResponseDtoList.add(addressResponseDto);
        }

        return addressResponseDtoList;
    }
}
