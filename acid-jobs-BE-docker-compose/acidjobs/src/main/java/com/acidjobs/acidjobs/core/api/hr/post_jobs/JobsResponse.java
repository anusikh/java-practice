package com.acidjobs.acidjobs.core.api.hr.post_jobs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobsResponse {
	private List<Job> jobs;
	private long totalJobs;
	private int totalPages;

}
