package com.acidjobs.acidjobs.core.api.user.personalDetails;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String gender;
	private String address;
	private String city;
	private String pincode;
	private String martialStatus;

	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateOfBirth;

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;
}
