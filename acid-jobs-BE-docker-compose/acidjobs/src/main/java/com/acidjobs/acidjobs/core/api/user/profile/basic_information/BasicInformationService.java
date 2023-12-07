package com.acidjobs.acidjobs.core.api.user.profile.basic_information;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Service
public class BasicInformationService {
	@Autowired
	private BasicInformationRepository basicInformationRepository;


	public BasicInformation saveOrUpdate(BasicInformation basicInformation){
		BasicInformation basicInformation1=basicInformationRepository.findByUser(basicInformation.getUser());
		if(basicInformation1==null){
			return  basicInformationRepository.save(basicInformation);
		}

			basicInformation1.setAge(basicInformation.getAge());
			basicInformation1.setTitle(basicInformation.getTitle());
			basicInformation1.setWorkingExperience(basicInformation.getWorkingExperience());
			return basicInformationRepository.save(basicInformation1);

	}

	public BasicInformation get(User user){
		return basicInformationRepository.findByUser(user);
	}

	public boolean delete(User user){
		BasicInformation basicInformation= basicInformationRepository.findByUser(user);
		if(basicInformation!=null){
			basicInformationRepository.delete(basicInformation);
			return true;
		}
		return false;
	}
}
