package com.acidjobs.acidjobs.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.chat.model.Message;
import com.acidjobs.acidjobs.chat.model.Status;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findBySenderAndReceiverOrSenderAndReceiver(User sender,User receiver,User receiver1 ,User sender2, Sort sort);
	List<Message> findBySenderAndReceiverAndStatus(User sender,User receiver, Status status);
}
