package com.acidjobs.acidjobs.chat.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String message;
	private Instant time;



	@OneToOne
	@JoinColumn(name = "sender", referencedColumnName = "id")
	private User sender;

	@OneToOne
	@JoinColumn(name = "receiver", referencedColumnName = "id")
	private User receiver;
	private Status status;
	public String getSender() {
		return sender.getEmail();
	}

	public String getReceiver() {
		return receiver.getEmail();
	}

}
