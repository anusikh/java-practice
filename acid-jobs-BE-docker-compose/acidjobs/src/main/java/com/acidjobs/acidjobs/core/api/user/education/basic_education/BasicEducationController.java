package com.acidjobs.acidjobs.core.api.user.education.basic_education;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;
import com.acidjobs.acidjobs.utility.MarkSheetUploader;

@RestController
@CrossOrigin
public class BasicEducationController {
	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private BasicEducationService basicEducationService;

	@Autowired
	private MarkSheetUploader markSheetUploader;

	@PostMapping("/user/save-basic-education")
	public List<BasicEducation> save(@RequestParam("markSheet") MultipartFile file,
									@RequestParam("qualification") String qualification,
			                        @RequestParam("highestQualification") boolean highestQualification,
           							@RequestParam("passOutMonth") String passOutMonth,
          							@RequestParam("passOutYear") int passOutYear,
 									@RequestParam("marksType") String marksType,
										@RequestParam("marks") double marks,
									@RequestParam("school") String school,
								@RequestParam("board") String board, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		if(!file.isEmpty() && file.getContentType().startsWith("application/pdf")){
			boolean is_uploaded=markSheetUploader.upload(file,user.getId(),qualification);
			if(is_uploaded){
				String url= ServletUriComponentsBuilder.fromCurrentContextPath()
													   .path("/mark-sheet/" + user.getId() + "/" +qualification+"/"+ file.getOriginalFilename())
													   .toUriString();

				BasicEducation basicEducation=new BasicEducation(null,qualification,highestQualification,passOutMonth,passOutYear,marksType,marks,school,
						board,file.getOriginalFilename(),url,user);

				return basicEducationService.save(basicEducation);


			}
			else{
				throw new GenericException("Marksheet if failed to upload !!");
			}
		}
		else{
			throw new GenericException("Marksheet file should pdf and not empty !!");
		}
	}

	@GetMapping("/mark-sheet/{id}/{qualification}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable("id") Long id,@PathVariable("qualification") String qualification,@PathVariable("filename") String filname) throws GenericException {

		Resource resource = markSheetUploader.get(filname, id, qualification);
		return ResponseEntity.ok()
							 .contentType(MediaType.APPLICATION_PDF)
							 .body(resource);
	}

	@GetMapping("/user/basic-education")
	public List<BasicEducation> getResume(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		return basicEducationService.get(user);
	}


	@PostMapping("/user/update-basic-education")
	public List<BasicEducation> update(@RequestParam("markSheet") MultipartFile file,
			@RequestParam("qualification") String qualification,
			@RequestParam("highestQualification") boolean highestQualification,
			@RequestParam("passOutMonth") String passOutMonth,
			@RequestParam("passOutYear") int passOutYear,
			@RequestParam("marksType") String marksType,
			@RequestParam("marks") double marks,
			@RequestParam("school") String school,
			@RequestParam("board") String board

			, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		if(!file.isEmpty()&& file.getContentType().startsWith("application/pdf")) {
			Boolean b = markSheetUploader.delete(user.getId(),qualification);
			if (b) {
				boolean is_uploaded = markSheetUploader.upload(file, user.getId(),qualification);
				if (is_uploaded) {
					String url= ServletUriComponentsBuilder.fromCurrentContextPath()
														   .path("/mark-sheet/" + user.getId() + "/" +qualification+"/"+ file.getOriginalFilename())
														   .toUriString();

					BasicEducation basicEducation=new BasicEducation(null,qualification,highestQualification,passOutMonth,passOutYear,marksType,marks,school,
							board,file.getOriginalFilename(),url,user);

					return basicEducationService.update(basicEducation);
				} else {
					throw new GenericException("Marksheet  failed to upload !!");
				}
			} else {
				throw new GenericException("Marksheet  failed to upload !!");
			}
		}
		else{
			throw new GenericException("Marksheet file should pdf and not empty !!");
		}
	}

	@PostMapping("/user/delete-basic-education")
	public List<BasicEducation> update(@RequestParam("qualification") String qualification,HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return  basicEducationService.delete(user,qualification);
	}

}
