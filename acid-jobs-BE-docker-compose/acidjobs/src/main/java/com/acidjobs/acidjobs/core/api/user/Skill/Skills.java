package com.acidjobs.acidjobs.core.api.user.Skill;

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

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
public class Skills {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private  int lastUsed;
	private String level;
	private double experience;
	private String skillName;
	private float version;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName = "id")
	private User user;
}
