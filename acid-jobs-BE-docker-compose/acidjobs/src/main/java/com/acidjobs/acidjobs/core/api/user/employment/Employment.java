package com.acidjobs.acidjobs.core.api.user.employment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String designation;
	private String organization;
	private boolean currentCompany;
	private int startYear;
	private String	startMonth;
	private int endYear;
	private String endMonth;
	private String 	description;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName = "id")
	private User user;
}
