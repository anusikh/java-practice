package com.acidjobs.acidjobs.core.api.user.jobs;

import java.util.List;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualification;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJobs {
	private Jobs jobs;
	private long days;
	private List<RequiredSkill>skills;
	private List<RequiredQualification> qualifications;
}
