package com.acidjobs.acidjobs.core.api.user.personalDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class PersonalDetailsService {
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	public PersonalDetails save(PersonalDetails personalDetails) throws GenericException {

		try{
			PersonalDetails personalDetails1=personalDetailsRepository.findByUser(personalDetails.getUser());
			if(personalDetails1==null){
				return  personalDetailsRepository.save(personalDetails);
			}
			else{
				personalDetails1.setAddress(personalDetails.getAddress());
				personalDetails1.setCity(personalDetails.getCity());
				personalDetails1.setGender(personalDetails.getGender());
				personalDetails1.setMartialStatus(personalDetails.getMartialStatus());
				personalDetails1.setPincode(personalDetails.getPincode());
				personalDetails1.setDateOfBirth(personalDetails.getDateOfBirth());
				return  personalDetailsRepository.save(personalDetails1);
			}


		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}

	public PersonalDetails get(User user) throws GenericException {
		try{

			return personalDetailsRepository.findByUser(user);
		}
		catch (Exception exception){
			throw  new GenericException("Data not found !!");
		}
	}
}
