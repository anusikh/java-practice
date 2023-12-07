package com.acidjobs.acidjobs.core.api.hr.post_jobs;

import javax.annotation.Nullable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
	@PostMapping("/update-job")
	public String updateJob(@Nullable @RequestParam("image") MultipartFile file){

		if(file!=null){
			return file.getOriginalFilename();
		}
		return "file";
	}
}
