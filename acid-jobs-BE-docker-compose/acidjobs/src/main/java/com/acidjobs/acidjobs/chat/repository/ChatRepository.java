package com.acidjobs.acidjobs.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.chat.model.Chat;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
	Page<Chat> findBySender(User user, Pageable pageable);
    Page<Chat> findByReceiver(User user,Pageable pageable);
	Chat findBySenderAndReceiver(User sender, User receiver);
}
