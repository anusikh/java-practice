package com.acidjobs.acidjobs.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.acidjobs.acidjobs.exception.GenericException;



@Component
public class ImageUploader {
	public final String UPLOAD_DIR="src/main/resources";

	public ImageUploader() throws IOException {

	}
	public boolean upload(MultipartFile file, Long id) {

		try{
			Path path = Paths.get(UPLOAD_DIR +"/static/image/"+id);
            if(Files.exists(path)){
				delete(id);
			}
			if(!Files.exists(path)){
				Files.createDirectories(path);
			}

			Files.copy(file.getInputStream(), Paths.get(path+"/"+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	public Resource get(String filename,Long id) throws GenericException {

		try{
			Path path = Paths.get(UPLOAD_DIR +"/static/image/" +id+"/"+filename);
			Resource resource = new UrlResource(path.toUri());
			return resource;

		}
		catch (Exception ex){
			throw new GenericException("File is not found !!");
		}
	}

	boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public boolean delete(Long id) throws GenericException {
		try{
			Path TEMP_DIRECTORY = Paths.get(UPLOAD_DIR+"/static/image");
			Path pathToBeDeleted = TEMP_DIRECTORY.resolve(String.valueOf(id));
			return  deleteDirectory(pathToBeDeleted.toFile());
		}
		catch (Exception ex){
			throw new GenericException("File is not found !!");
		}
	}
}
