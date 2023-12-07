package com.acidjobs.acidjobs.core.api.user.education.graduation;

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
public class Graduation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String qualification;
	private boolean highestQualification;
	private String	passOutMonth;
	private int passOutYear;
	private double	marks;
    private String marksType;
	private String markSheet;
	private String markSheetUrl;
	private String branch;
	private String course;
	private String state;
	private String institute;
	private  String university;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName = "id")
	private User user;
}
