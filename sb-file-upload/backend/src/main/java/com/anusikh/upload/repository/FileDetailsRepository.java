package com.anusikh.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anusikh.upload.model.FileDetails;

@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {

}
