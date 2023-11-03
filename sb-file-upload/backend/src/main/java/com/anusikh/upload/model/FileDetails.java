package com.anusikh.upload.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fileName;
    @Column
    private String fileUri;
    @Column
    private String fileDownloadUri;
    @Column
    private Long fileSize;
    @Column
    private String uploaderName;

    public FileDetails(String fileName, String fileUri, String fileDownloadUri, long fileSize, String uploaderName) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileDownloadUri = fileDownloadUri;
        this.fileSize = fileSize;
        this.uploaderName = uploaderName;
    }
}
