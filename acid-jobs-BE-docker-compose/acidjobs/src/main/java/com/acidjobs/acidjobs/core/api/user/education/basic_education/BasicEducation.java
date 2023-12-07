package com.acidjobs.acidjobs.core.api.user.education.basic_education;

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
public class BasicEducation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String qualification;
	private boolean highestQualification;
	private String	passOutMonth;
	private int passOutYear;
	private String marksType;
	private double	marks;
	private String	school;
    private String	board;
    private String markSheet;
	private String markSheetUrl;


	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="user_id",referencedColumnName = "id")
	private User user;
}