package com.order.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.order.entity.Image;
import com.order.entity.Restaurant;
import com.order.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileUtils {
	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;

	@Autowired
	private ImageRepository imageRepository;

	public Image parseImageInfo(Restaurant restaurant, MultipartFile file) throws Exception {
		// 파일 없을 시 null 반환
		if (ObjectUtils.isEmpty(file)) {
			return null;
		}

		// 파일 정보를 저장할 객체를 생성: 해당 메서드에서 반환하는 값
		Image imageEntity = new Image();

		// 해당 creatorId에 해당하는 Image Entity 존재하는지 확인
		Optional<Image> storedImageOptional = imageRepository.findByRestaurantId(restaurant.getId());

		if (storedImageOptional.isPresent()) {
			// 이전 이미지 삭제
			Path filePath  = Paths.get(storedImageOptional.get().getStoredFilePath());
			Files.deleteIfExists(filePath);

			// 이미 존재하는 Image Entity 불러오기
			imageEntity = storedImageOptional.get();
		}

		// 파일을 저장할 디렉터리를 지정 (존재하지 않는 경우 생성)
		String storedDir = uploadDir + restaurant.getId();
		File dir = new File(storedDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String contentType = file.getContentType();

		// Content-Type 이미지 파일인 경우에 한하여 지정된 확장자로 저장되도록 설정
		String fileExtension = "";
		if (contentType.contains("jpeg")) {
			fileExtension = ".jpg";
		} else if (contentType.contains("png")) {
			fileExtension = ".png";
		} else if (contentType.contains("gif")) {
			fileExtension = ".gif";
		}

		// 저장에 사용할 파일 이름을 조합 (현재 시간을 파일명으로 사용)
		String storedFileName = System.nanoTime() + fileExtension;

		// 파일 정보를 리스트에 저장
		imageEntity.setStoredFilePath(storedDir + "/" + storedFileName);
		imageEntity.setRestaurant(restaurant);

		// 파일 저장
		dir = new File(storedDir + "/" + storedFileName);
		file.transferTo(dir);

		return imageEntity;
	}

}
