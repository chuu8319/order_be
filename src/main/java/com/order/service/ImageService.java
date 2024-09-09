package com.order.service;

import com.order.common.FileUtils;
import com.order.entity.Image;
import com.order.entity.Restaurant;
import com.order.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final FileUtils fileUtils;
    private final ImageRepository imageRepository;

    public long addImage(Restaurant restaurant, MultipartFile file) {
        Image image;
        try {
            image = fileUtils.parseImageInfo(restaurant, file);
        } catch (Exception e) {
            return -1;
        }

        if(image != null) {
            Image createdImage = imageRepository.save(image);
            return createdImage.getId();
        } else return -1;
    }
}
