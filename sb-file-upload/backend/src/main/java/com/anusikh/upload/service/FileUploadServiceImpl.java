package com.anusikh.upload.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.anusikh.upload.model.FileDetails;
import com.anusikh.upload.model.FileUploadResponse;
import com.anusikh.upload.repository.FileDetailsRepository;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileDetailsRepository fileDetailsRepository;

    private static Path UPLOAD_PATH;

    static {
        try {
            UPLOAD_PATH = Paths.get(new ClassPathResource("").getFile().getAbsolutePath()
                    + File.separator + "static"
                    + File.separator + "image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String uploaderName) throws Exception {

        if (!Files.exists(UPLOAD_PATH)) {
            Files.createDirectories(UPLOAD_PATH);
        }

        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new Exception("only .jpeg and .png files are supported");
        }

        String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy").format(new Date()) + "_"
                + file.getOriginalFilename();

        Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
        Files.copy(file.getInputStream(), filePath);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/").path(timeStampedFileName).toUriString();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/").path(timeStampedFileName).toUriString();

        FileDetails fileDetails = new FileDetails(file.getOriginalFilename(), fileUri, fileDownloadUri,
                file.getSize(),
                uploaderName);

        this.fileDetailsRepository.save(fileDetails);

        FileUploadResponse fileUploadResponse = new FileUploadResponse(fileDetails.getId(),
                file.getOriginalFilename(), fileUri, fileDownloadUri,
                file.getSize(),
                uploaderName);

        return fileUploadResponse;

    }

    @Override
    public Resource fetchFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = UPLOAD_PATH.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("file not found " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("file not found " + fileName);
        }
    }

    @Override
    public List<FileDetails> getAllFiles() {
        return this.fileDetailsRepository.findAll();
    }

}
