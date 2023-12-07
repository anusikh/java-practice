package com.acidjobs.acidjobs.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
	private String message;
	private String receiver;
	private String username;

}
