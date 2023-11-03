package com.anusikh.upload.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.anusikh.upload.model.FileDetails;
import com.anusikh.upload.model.FileUploadResponse;

public interface FileUploadService {
    public FileUploadResponse uploadFile(MultipartFile file, String uploaderName) throws Exception;

    public Resource fetchFileAsResource(String fileName) throws Exception;

    public List<FileDetails> getAllFiles();
}
