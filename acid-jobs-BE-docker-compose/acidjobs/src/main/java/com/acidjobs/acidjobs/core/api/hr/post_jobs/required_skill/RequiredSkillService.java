package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;


@Service
public class RequiredSkillService {

	@Autowired
	private RequiredSkillRepository requiredSkillRepository;

	public List<RequiredSkill> add(RequiredSkill requiredSkill){
		RequiredSkill requiredSkill1=requiredSkillRepository.save(requiredSkill);
		return requiredSkillRepository.findByJobs(requiredSkill.getJobs());
	}

	public List<RequiredSkill> getByJob(Jobs jobs1) {
		return  requiredSkillRepository.findByJobs(jobs1);
	}

	public boolean delete(Jobs jobs) {

		Long noOfRecord = requiredSkillRepository.deleteByJobs(jobs);

		if (noOfRecord >= 0) {
			return true;
		}
		return false;
	}

	public void deleteSkill(RequiredSkill requiredSkill) {
		requiredSkillRepository.delete(requiredSkill);
	}

	public void addOrUpdate(RequiredSkill requiredSkill) {
		RequiredSkill requiredSkill1 = requiredSkillRepository.findByJobsAndSkill(requiredSkill.getJobs(), requiredSkill.getSkill());
		if (requiredSkill1 == null) {
			requiredSkillRepository.save(requiredSkill);
		}

	}
}
