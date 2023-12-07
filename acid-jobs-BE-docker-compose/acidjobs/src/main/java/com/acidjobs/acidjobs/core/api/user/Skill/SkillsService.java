package com.acidjobs.acidjobs.core.api.user.Skill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;

@Service
public class SkillsService {
	@Autowired
	private SkillsRepository skillsRepository;

	public List<Skills> save(Skills skills) throws GenericException {
		Skills skills1= skillsRepository.findByUserAndSkillName(skills.getUser(), skills.getSkillName());


			if(skills1==null){
				skillsRepository.save(skills);
				return skillsRepository.findByUser(skills.getUser());
			}
			else{
				throw new GenericException("This Data Already is already exista");
			}

	}

	public List<Skills> update(Skills skills) throws GenericException {

		try{
			Skills skills1= skillsRepository.findByUserAndSkillName(skills.getUser(), skills.getSkillName());
			skills1.setExperience(skills.getExperience());
			skills1.setSkillName(skills.getSkillName());
			skills1.setVersion(skills.getVersion());
			skills1.setLastUsed(skills.getLastUsed());
			skillsRepository.save(skills1);
			return skillsRepository.findByUser(skills.getUser());
		}
		catch (Exception ex){
			throw new GenericException("Data failed to update!! ");
		}
	}

	public List<Skills> delete(String skillName, User user) throws GenericException {
		try{
			Skills skills= skillsRepository.findByUserAndSkillName(user, skillName);
		     skillsRepository.delete(skills);
			 return skillsRepository.findByUser(skills.getUser());
		}
		catch (Exception ex){
			throw new GenericException("Data failed to delete!! ");
		}
	}

	public List<Skills> get(User user) throws GenericException {
		try{
			return skillsRepository.findByUser(user);
		}
		catch (Exception ex){
			throw new GenericException("Data failed to get!! ");
		}

	}
}
