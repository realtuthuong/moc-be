package com.example.MocBE.service.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.MocBE.service.AuthService;
import com.example.MocBE.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String getImageUrlAfterUpload(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder
            ));
            logger.info("Upload image to Cloudinary thành công: {}", uploadResult.get("url"));
            return (String) uploadResult.get("url");

        } catch (IOException e) {
            logger.error("Upload image lên Cloudinary thất bại: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi upload ảnh: " + e.getMessage());
        }
    }

    public String getVideoUrlAfterUpload(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", folder
            ));
            logger.info("Upload video thành công: {}", uploadResult.get("url"));
            return (String) uploadResult.get("url");

        } catch (IOException e) {
            logger.error("Upload video thất bại: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi upload video: " + e.getMessage());
        }
    }


}