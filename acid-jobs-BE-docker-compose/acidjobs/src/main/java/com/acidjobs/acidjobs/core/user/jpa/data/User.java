package com.acidjobs.acidjobs.core.user.jpa.data;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String email;
	private String password;
	private String token;
	private boolean accountVerified;
	private int failedLoginAttempts;
	private boolean loginDisabled;
	private String Role;


	@OneToMany(mappedBy = "user")
	private Set<SecureToken> tokens;

}
