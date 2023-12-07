package com.acidjobs.acidjobs.core.user.jpa.data;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
	@Id
	@GeneratedValue
	private Long id;
	private String token;
	private Instant expiryDate;

	@OneToOne
	@JoinColumn(name="user_id",referencedColumnName = "id")
    private  User user;
}
