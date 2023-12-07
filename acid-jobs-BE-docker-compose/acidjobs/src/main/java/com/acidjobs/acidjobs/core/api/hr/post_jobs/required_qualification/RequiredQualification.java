package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequiredQualification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String course;
	private double percentage;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "job_id",referencedColumnName = "id")
	private Jobs jobs;


}
