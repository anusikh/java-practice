package com.anusikh.upload.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anusikh.upload.model.FileDetails;
import com.anusikh.upload.model.FileUploadResponse;
import com.anusikh.upload.service.FileUploadService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "file")
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<FileDetails> getAllFiles() {
        return this.fileUploadService.getAllFiles();
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFiles(@RequestParam("name") String name,
            @RequestParam("files") MultipartFile[] files) {
        try {
            List<FileUploadResponse> fileUploadResponses = Arrays.stream(files).map(file -> {

                try {
                    return fileUploadService.uploadFile(file, name);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }).collect(Collectors.toList());

            return new ResponseEntity<>(fileUploadResponses, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName,
            HttpServletRequest request) throws Exception {

        try {
            Resource resource = this.fileUploadService.fetchFileAsResource(fileName);
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
