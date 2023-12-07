package com.acidjobs.acidjobs.core.api.user.profile.contact;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.ContactAlreadyExists;
import com.acidjobs.acidjobs.exception.GenericException;
import com.twilio.Twilio;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
@Service
public class UserPhoneService {
	private final String ACCOUNT_SID ="AC352545fa3efa24fee50538939aa74895";

	private final String AUTH_TOKEN = "0de68472cf68ba37d50fe4cc916e5c15";
	private final String FROM_NUMBER = "+12544015596";
    private final int OTP_EXPIRY=60*1000*30;

	@Autowired
	private UserPhoneRepository userPhoneRepository;

	@Autowired
	private OtpRepository otpRepository;


	public boolean send(SmsPojo sms, User user) throws ParseException {
		try{

			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			int min = 100000;
			int max = 999999;
			int number=(int)(Math.random()*(max-min+1)+min);
			String msg ="Your OTP - "+number+ " please verify your number using this otp and this expired in 30 mins";
			Message message = Message.creator(new PhoneNumber(sms.getPhoneNumber()), new PhoneNumber(FROM_NUMBER), msg).create();

			OTP otp=new OTP(null,number, Instant.now().plusMillis(OTP_EXPIRY),sms.getPhoneNumber(), user);
            saveOtp(otp);
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}





	}

	public OTP saveOtp(OTP otp){
		OTP otp1=otpRepository.findByUserAndMobile(otp.getUser(),otp.getMobile());
		if(otp1==null){
			return otpRepository.save(otp);
		}
		else{
			otp1.setOtp(otp.getOtp());
			otp1.setExpiryDate(otp.getExpiryDate());
			otp1.setMobile(otp.getMobile());
			return otpRepository.save(otp1);
		}
	}

	public UserContact save(UserContact userContact) throws ContactAlreadyExists {
		UserContact userContact1 = userPhoneRepository.findByUserAndPhoneNumber(userContact.getUser(), userContact.getPhoneNumber());
		if(userContact1==null){
			UserContact userContact3= userPhoneRepository.findByPhoneNumber(userContact.getPhoneNumber());
			if(userContact3!=null){
				return null;
			}
			return  userPhoneRepository.save(userContact);
		}
		userContact1.setPhoneNumber(userContact.getPhoneNumber());
		userContact1.setIsVerified(userContact.getIsVerified());
		return userPhoneRepository.save(userContact1);

	}

//	public UserPhone get(User user) throws GenericException {
//		try{
//			return userPhoneRepository.findByUser(user);
//		}
//		catch(Exception ex){
//			throw new GenericException("Mobile number does not exists");
//		}
//	}

	public OTP getOtp(User user) throws GenericException {
		UserContact userContact = userPhoneRepository.findByUser(user);

		try{
			return otpRepository.findByUserAndMobile(user,userContact.getPhoneNumber());
		}
		catch(Exception ex){
			throw new GenericException("OTP  does not exists");
		}
	}

	public void deleleOTP(OTP otp) throws GenericException {
		try{
			otpRepository.delete(otp);
		}
		catch(Exception ex){
			throw new GenericException("OTP number does not exists");
		}
	}

	public void verify(User user) {
		UserContact userContact = userPhoneRepository.findByUser(user);
		if(userContact !=null){
			userContact.setIsVerified(true);
			userPhoneRepository.save(userContact);
		}
	}

	public UserContact get(User user) throws GenericException {
		try{
			UserContact userContact= userPhoneRepository.findByUser(user);
			if(userContact!=null && userContact.getIsVerified()){
				return userContact;
			}
			return null;
		}
		catch (Exception ex){
			throw  new GenericException("Contact information is not present!!");
		}
	}
}
