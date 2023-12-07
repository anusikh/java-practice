package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.mapping.ToOne;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequiredSkill {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String skill;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "job_id",referencedColumnName = "id")
	private Jobs jobs;

}
