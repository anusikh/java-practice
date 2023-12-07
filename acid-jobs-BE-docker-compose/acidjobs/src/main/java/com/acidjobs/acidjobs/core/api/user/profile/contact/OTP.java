package com.acidjobs.acidjobs.core.api.user.profile.contact;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "mobile" }) })
public class OTP {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int otp;
	private Instant expiryDate;
    private  String mobile;
	@OneToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;
}
