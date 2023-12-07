package com.acidjobs.acidjobs.core.api.hr.post_jobs;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualification;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkill;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Jobs {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotNull
  private String title;
  private String description;
  private String tags;
  private String jobsType;
  private String experience;
  private long salary;
  private String city;
  private String location;
  private Instant date;
  private String file;
  private String file_url;
  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="company",referencedColumnName = "id")
  private User user;



}
