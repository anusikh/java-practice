package com.acidjobs.acidjobs.chat.pojo;

import java.util.List;

import com.acidjobs.acidjobs.chat.model.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateReceiver {
	private String name;
	private String email;
	private String image;
	private boolean active;
	private int unreadMessage;
    private List<Message> messages;


}
