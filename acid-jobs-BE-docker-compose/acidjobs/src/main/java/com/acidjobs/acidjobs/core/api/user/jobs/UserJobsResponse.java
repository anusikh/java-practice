package com.acidjobs.acidjobs.core.api.user.jobs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJobsResponse {
	private List<UserJobs> jobs;
	private long totalJobs;
	private int totalPages;

}
