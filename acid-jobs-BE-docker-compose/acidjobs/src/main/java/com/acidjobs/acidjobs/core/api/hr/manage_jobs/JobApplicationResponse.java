package com.acidjobs.acidjobs.core.api.hr.manage_jobs;

import java.util.List;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplicationResponse {
	List<Object> users;
	private Jobs jobs;

}
