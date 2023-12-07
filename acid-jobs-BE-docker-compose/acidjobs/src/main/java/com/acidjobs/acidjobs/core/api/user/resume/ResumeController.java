package com.acidjobs.acidjobs.core.api.user.resume;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;
import com.acidjobs.acidjobs.utility.ResumeUploader;

@RestController
@CrossOrigin
public class ResumeController {

	@Autowired
	private ResumeService resumeService;

	@Autowired
	private ResumeUploader resumeUploader;

	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/user/save-resume")
	public Resume save(@RequestParam("resume")MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		if(!file.isEmpty() && file.getContentType().startsWith("application/pdf")){
			boolean is_uploaded=resumeUploader.upload(file,user.getId());
			if(is_uploaded){
				String url= ServletUriComponentsBuilder.fromCurrentContextPath()
													   .path("/resume/" + user.getId() + "/" + file.getOriginalFilename())
													   .toUriString();

				Resume resume=new Resume(null,url, file.getOriginalFilename(), user);
				return resumeService.save(resume);


			}
			else{
				throw new GenericException("Resume is failed to upload !!");
			}
		}
		else{
			throw new GenericException("Resume file should pdf and not empty !!");
		}
	}

	@GetMapping("/resume/{id}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable("id") Long id,@PathVariable("filename") String filname) throws GenericException {

		Resource resource=resumeUploader.getResume(filname,id);
		return ResponseEntity.ok()
							 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
	}

	@GetMapping("/user/get-resume")
	public Resume getResume(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		return resumeService.get(user);
	}


	@PostMapping("/user/update-resume")
	public Resume update(@RequestParam("resume")MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		if(!file.isEmpty()&& file.getContentType().startsWith("application/pdf")) {
			Boolean b = resumeUploader.deleteResume(user.getId());
			if (b) {
				boolean is_uploaded = resumeUploader.upload(file, user.getId());
				if (is_uploaded) {
					String url = ServletUriComponentsBuilder.fromCurrentContextPath()
															.path("/resume/" + user.getId() + "/" + file.getOriginalFilename())
															.toUriString();

					Resume resume = new Resume(null, url, file.getOriginalFilename(), user);
					return resumeService.save(resume);
				} else {
					throw new GenericException("Resume is failed to upload !!");
				}
			} else {
				throw new GenericException("Resume is failed to update !!");
			}
		}
		else{
			throw new GenericException("Resume file should pdf and not empty !!");
		}
	}


}
