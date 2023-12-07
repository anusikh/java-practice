package com.acidjobs.acidjobs.core.api.user.resume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class ResumeService {
	@Autowired
	private ResumeRepository resumeRepository;
	public Resume save(Resume resume) {
		Resume resume1=resumeRepository.findByUser(resume.getUser());
		if(resume1==null){
			return resumeRepository.save(resume);
		}
		else{
			resume1.setResume(resume.getResume());
			resume1.setFileName(resume.getFileName());
			return resumeRepository.save(resume1);

		}


	}

	public Resume get(User user) throws GenericException {

		try{
			Resume resume=resumeRepository.findByUser(user);

			if(resume!=null){

				return resume;
			}
            else{
				throw  new GenericException("Resume not found by this user");
			}
		}
		catch (Exception ex){
			throw  new GenericException("Resume not found");
		}
	}
}
