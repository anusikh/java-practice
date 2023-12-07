package com.acidjobs.acidjobs.core.api.user.education.graduation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;
import com.acidjobs.acidjobs.utility.MarkSheetUploader;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class GraduationController {

	@Autowired
	private GraduationService graduationService;
	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private MarkSheetUploader markSheetUploader;

	@PostMapping("/save-graduation")
	public List<Graduation> save(
			@RequestParam("markSheet") MultipartFile file,
			@RequestParam("qualification") String qualification,
			@RequestParam("highestQualification") boolean highestQualification,
			@RequestParam("passOutMonth") String passOutMonth,
			@RequestParam("passOutYear") int passOutYear,
			@RequestParam("marksType") String marksType,
			@RequestParam("marks") double marks,
			@RequestParam("branch") String branch,
			@RequestParam("course") String course,
			@RequestParam("institute") String institute,
			@RequestParam("university") String university,
			@RequestParam("state") String state,
			HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		if(!file.isEmpty() && file.getContentType().startsWith("application/pdf")){
			boolean is_uploaded=markSheetUploader.upload(file,user.getId(),qualification);
			if(is_uploaded){
				String url= ServletUriComponentsBuilder.fromCurrentContextPath()
													   .path("/mark-sheet/" + user.getId() + "/" + qualification + "/" + file.getOriginalFilename())
													   .toUriString();

				Graduation graduation=new Graduation(null,qualification,highestQualification,passOutMonth,passOutYear,
						marks,marksType,file.getOriginalFilename(),url,branch,course,state,institute,university,user);

				return graduationService.save(graduation);


			}
			else{
				throw new GenericException("Marksheet  failed to upload !!");
			}
		}
		else{
			throw new GenericException("Marksheet file should pdf and not empty !!");
		}
	}

	@GetMapping("/graduation")
	public List<Graduation> getResume(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		return graduationService.get(user);
	}




	@PostMapping("/delete-graduation")
	public List<Graduation> update(@RequestParam("qualification") String qualification,HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return  graduationService.delete(user,qualification);


	}
	@PostMapping("/update-graduation")
	public List<Graduation> update(
			@RequestParam("markSheet") MultipartFile file,
			@RequestParam("qualification") String qualification,
			@RequestParam("highestQualification") boolean highestQualification,
			@RequestParam("passOutMonth") String passOutMonth,
			@RequestParam("passOutYear") int passOutYear,
			@RequestParam("marksType") String marksType,
			@RequestParam("marks") double marks,
			@RequestParam("branch") String branch,
			@RequestParam("course") String course,
			@RequestParam("institute") String institute,
			@RequestParam("university") String university,
			@RequestParam("state") String state,
			HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		if(!file.isEmpty() && file.getContentType().startsWith("application/pdf")){
			boolean is_deleted= markSheetUploader.delete(user.getId(),qualification);
			if(is_deleted){
				boolean is_uploaded=markSheetUploader.upload(file,user.getId(),qualification);
				if(is_uploaded){
					String url= ServletUriComponentsBuilder.fromCurrentContextPath()
														   .path("/mark-sheet/" + user.getId() + "/" + qualification + "/" + file.getOriginalFilename())
														   .toUriString();

					Graduation graduation=new Graduation(null,qualification,highestQualification,passOutMonth,passOutYear,
							marks,marksType,file.getOriginalFilename(),url,branch,course,state,institute,university,user);

					return graduationService.update(graduation);


				}
				else{
					throw new GenericException("Marksheet if failed to upload !!");
				}
			}
			else{
				throw new GenericException("Graduation is not updated !!");
			}
		}
		else{
			throw new GenericException("Marksheet file should pdf and not empty !!");
		}
	}
}
