package com.acidjobs.acidjobs.chat.pojo;

import java.util.List;

import com.acidjobs.acidjobs.chat.model.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
	private CandidateReceiver receiver;
	private List<Message>messages;

}
