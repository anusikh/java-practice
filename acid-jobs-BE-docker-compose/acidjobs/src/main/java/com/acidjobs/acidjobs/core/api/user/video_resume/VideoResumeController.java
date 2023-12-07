package com.acidjobs.acidjobs.core.api.user.video_resume;

import java.time.Instant;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;
import com.acidjobs.acidjobs.utility.VideoResumeUploader;

@RestController
@CrossOrigin
public class VideoResumeController {

	@Autowired
	private VideoResumeService videoResumeService;

	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private VideoResumeUploader videoResumeUploader;


	@PostMapping("/user/upload-video")
	public GenericResponse uploadVideoResume(@RequestParam("video")MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		if(!file.isEmpty() && file.getContentType().startsWith("video")){
		    boolean is_upload= videoResumeUploader.upload(file,user.getId());
			if(is_upload){
				String url= ServletUriComponentsBuilder.fromCurrentContextPath().path("video/"+user.getId()+"/"+file.getOriginalFilename()).toUriString();
				VideoResume videoResume=new VideoResume(null,url, file.getOriginalFilename(), Instant.now(),user);
				VideoResume videoResume1=videoResumeService.save(videoResume);
				if(videoResume1!=null){
					return new GenericResponse("success","video is uploaded successfully!!",videoResume1);
				}
				else{
					return new GenericResponse("error","video is not uploaded please try later!!",null);
				}
			}
			else{
				return new GenericResponse("error","video is not uploaded please try later!!",null);
			}
		}
		else{
			return new GenericResponse("error","file should not empty and  non video!!",null);
		}

	}


	@GetMapping("/video/{id}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable("id") Long id,@PathVariable("filename") String filname) throws GenericException {

		Resource resource=videoResumeUploader.getVideo(filname,id);
		return ResponseEntity.ok()
							 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
	}
}
