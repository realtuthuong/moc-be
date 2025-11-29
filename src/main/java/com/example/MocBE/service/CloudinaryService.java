package com.example.MocBE.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    public String getImageUrlAfterUpload(MultipartFile file, String folder)throws IOException;

    public String getVideoUrlAfterUpload(MultipartFile file, String folder)throws IOException;

}
