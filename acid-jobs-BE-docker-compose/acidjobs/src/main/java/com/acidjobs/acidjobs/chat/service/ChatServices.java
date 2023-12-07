package com.acidjobs.acidjobs.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.chat.model.Chat;
import com.acidjobs.acidjobs.chat.repository.ChatRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Service
public class ChatServices {
	@Autowired
	private ChatRepository chatRepository;

	public Chat save(Chat chat) {
		return chatRepository.save(chat);
	}

	public Page<Chat> getChatBySender(User user, Pageable pageable) {
		return chatRepository.findBySender(user, pageable);
	}

	public Chat getChatBySenderAndReceiver(User user, User receiver) {
		return chatRepository.findBySenderAndReceiver(user, receiver);
	}

	public Page<Chat> getChatByReceiver(User user, Pageable pageable) {
		return  chatRepository.findByReceiver(user,pageable);

	}
}
