package com.acidjobs.acidjobs.core.api.user.profile.basic_information;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RequestMapping("/user")
@CrossOrigin
@RestController
public class BasicInformationController {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private BasicInformationService basicInformationService;

	@PostMapping("/save-basic-information")
	public GenericResponse saveBasicInformation(@RequestBody BasicInformation basicInformation, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		basicInformation.setUser(user);
		try{
			BasicInformation basicInformation1=basicInformationService.saveOrUpdate(basicInformation);
			return new GenericResponse("success","Information is added successfully",basicInformation1);
		}
		catch(Exception ex){
			return new GenericResponse("error","Failed add or update the information !!",null);
		}

	}

	@GetMapping("/get-basic-information")
	public GenericResponse getBasicInformation(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization=httpServletRequest.getHeader("Authorization");
		User user =jwtUtility.getUserFromToken(authorization);
		try{
			BasicInformation basicInformation1=basicInformationService.get(user);
			return new GenericResponse("success","basic information",basicInformation1);
		}
		catch(Exception ex){
			return new GenericResponse("error","Failed to load !!",null);
		}

	}
	@PostMapping("/delete-basic-information")
	public GenericResponse deleteBasicInformation(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization=httpServletRequest.getHeader("Authorization");
		User user =jwtUtility.getUserFromToken(authorization);

			boolean is_delete=basicInformationService.delete(user);
			if(is_delete) {
				return new GenericResponse("success", "basic information deleted successfully", null);
			}
			return new GenericResponse("error","Failed to delete basic information !!",null);

	}
}
