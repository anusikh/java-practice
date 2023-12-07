package com.acidjobs.acidjobs.core.api.user.profile.profile_image;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
import com.acidjobs.acidjobs.utility.ImageUploader;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
public class ProfileImageController {

	@Autowired
	private ProfileImageService profileImageService;
	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private ImageUploader imageUploader;

	@PostMapping("/user/upload/profile-image")
	public ProfileImage uploadImage(@RequestParam("image")MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

			if(!file.isEmpty() && file.getContentType().startsWith("image")){
				boolean is_uploaded=imageUploader.upload(file,user.getId());
				if(is_uploaded){
					String url= ServletUriComponentsBuilder.fromCurrentContextPath()
														   .path("/image/" + user.getId() + "/" + file.getOriginalFilename())
														   .toUriString();

					ProfileImage profileImage=new ProfileImage(null, file.getOriginalFilename(),url, user);
					return profileImageService.save(profileImage);


				}
				else{
					throw new GenericException("Image is failed to upload !!");
				}
			}
			else{
				throw new GenericException("Image file should image and not empty !!");
			}


	}


	@GetMapping("/image/{id}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable("id") Long id,@PathVariable("filename") String filname) throws GenericException {

		Resource resource=imageUploader.get(filname,id);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
				.contentType(MediaType.IMAGE_PNG).body(resource);
	}

	@GetMapping("/user/profile-image")
	public ProfileImage get(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		return profileImageService.get(user);
	}
}
