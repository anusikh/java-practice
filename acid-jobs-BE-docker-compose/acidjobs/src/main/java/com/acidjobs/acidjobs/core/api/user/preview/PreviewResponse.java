package com.acidjobs.acidjobs.core.api.user.preview;

import java.util.List;

import com.acidjobs.acidjobs.core.api.user.Project.Project;
import com.acidjobs.acidjobs.core.api.user.Skill.Skills;
import com.acidjobs.acidjobs.core.api.user.education.basic_education.BasicEducation;
import com.acidjobs.acidjobs.core.api.user.education.graduation.Graduation;
import com.acidjobs.acidjobs.core.api.user.employment.Employment;
import com.acidjobs.acidjobs.core.api.user.personalDetails.PersonalDetails;
import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviewResponse {
	private List<Graduation> graduations;
	private List<BasicEducation> basicEducation;
	private List<Skills> skills;
	private List<Project> projects;
    private List<SocialAccount> socialAccounts;
	private List<Employment> employments;
	private PersonalDetails personalDetails;
	private Resume resume;
}
