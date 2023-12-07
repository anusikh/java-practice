package com.acidjobs.acidjobs.core.api.user.profile.profile_image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class ProfileImageService {

	@Autowired
	private ProfileImageRepository profileImageRepository;

	public ProfileImage save(ProfileImage profileImage) {
		ProfileImage profileImage1=profileImageRepository.findByUser(profileImage.getUser());
		if(profileImage1==null){
			return profileImageRepository.save(profileImage);
		}
		else{
			profileImage1.setFileName(profileImage.getFileName());
			profileImage1.setUrl(profileImage.getUrl());
			return profileImageRepository.save(profileImage1);
		}

	}

	public ProfileImage get(User user)  {

			return profileImageRepository.findByUser(user);

	}
}

