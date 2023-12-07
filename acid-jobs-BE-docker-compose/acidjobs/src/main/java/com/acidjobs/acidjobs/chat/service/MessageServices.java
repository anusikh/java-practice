package com.acidjobs.acidjobs.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.chat.model.Message;
import com.acidjobs.acidjobs.chat.model.Status;
import com.acidjobs.acidjobs.chat.repository.MessageRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Service
public class MessageServices {
	@Autowired
	private MessageRepository messageRepository;

	public Message save(Message message1) {
		return messageRepository.save(message1);

	}

	public List<Message> getMessageBySenderAndReceiverOrReceiverAndSender(User sender, User receiver, Sort sort) {
		List<Message> messages=messageRepository.findBySenderAndReceiverOrSenderAndReceiver(sender,receiver,receiver,sender,sort);

		return messages;
	}
	public List<Message> findBySenderAndReceiverAndStatus(User sender,User receiver,Status status){
		return messageRepository.findBySenderAndReceiverAndStatus(sender,receiver,status);
	}

	public void updateMessage(Message message) {
		messageRepository.save(message);
	}
}
