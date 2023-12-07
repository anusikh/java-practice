package com.acidjobs.acidjobs.core.api.user.profile.contact;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.ContactAlreadyExists;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class SmsController {

	@Autowired
	private SimpMessagingTemplate webSocket;


	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private UserPhoneService userPhoneService;

	private final String  TOPIC_DESTINATION = "/lesson/sms";


	@PostMapping("/add-phone")
	public GenericResponse sentOtp(@RequestBody SmsPojo smsPojo, HttpServletRequest httpServletRequest) throws Exception {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		UserContact userContact1 =userPhoneService.get(user);

			  if(userContact1==null){
				  boolean is_send= userPhoneService.send(smsPojo,user);
				  if(is_send){
					  UserContact userContact = new UserContact(null, smsPojo.getPhoneNumber(), false, user);
					  userPhoneService.save(userContact);
					  webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: "+smsPojo.getPhoneNumber());
				      return new GenericResponse("success","Successfully OTP send to your mobile number "+smsPojo.getPhoneNumber(),null);
				  }
				  return new GenericResponse("error","Please chack your number and try again !!",null);
			  }
			  return new GenericResponse("error","This in number is associated with another account !!",null);



	}

	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}




		@PostMapping("/verify-otp")
		public GenericResponse ValidateToken(@RequestBody TempOtp otp,HttpServletRequest httpServletRequest) throws GenericException {
			String authorization = httpServletRequest.getHeader("Authorization");
			User user= jwtUtility.getUserFromToken(authorization);


			OTP otp1=userPhoneService.getOtp(user);

			if(otp1.getOtp()==otp.getOtp()){

				userPhoneService.verify(user);
				userPhoneService.deleleOTP(otp1);
				UserContact userContact1 =userPhoneService.get(user);
				return new GenericResponse("success","Your mobile number is successfully verified",userContact1);
			}
			return new GenericResponse("error","failed verify your mobile number",null);

		}

	@GetMapping("/profile")
	public UserContact get(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return userPhoneService.get(user);
	}
}
