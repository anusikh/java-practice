package com.acidjobs.acidjobs.chat.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acidjobs.acidjobs.chat.model.Chat;
import com.acidjobs.acidjobs.chat.model.Message;
import com.acidjobs.acidjobs.chat.model.Status;
import com.acidjobs.acidjobs.chat.pojo.CandidateReceiver;
import com.acidjobs.acidjobs.chat.pojo.MessageRequest;
import com.acidjobs.acidjobs.chat.service.ChatServices;
import com.acidjobs.acidjobs.chat.service.MessageServices;
import com.acidjobs.acidjobs.core.api.user.applied_job.AppliedJobRepository;
import com.acidjobs.acidjobs.core.api.user.profile.profile_image.ProfileImage;
import com.acidjobs.acidjobs.core.api.user.profile.profile_image.ProfileImageService;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.UserService;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
public class ChatController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private UserService userService;

	@Autowired
	private AppliedJobRepository appliedJobRepository;

	@Autowired
	private ProfileImageService profileImageService;
	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private ChatServices chatServices;

	@Autowired
	private MessageServices messageServices;

	@MessageMapping("/message")//app/message
	@SendTo("/user")
	public Message receivedPublicMessage(@Payload MessageRequest messageRequest)  {
		System.err.println(messageRequest.getMessage()+' '+messageRequest.getReceiver()+' '+messageRequest.getUsername());

		User user =userService.getUserByUsername(messageRequest.getUsername());
		User receiver=userService.getUserByUsername(messageRequest.getReceiver());
		Message message1=new Message(null, messageRequest.getMessage(), Instant.now(),user,receiver, Status.SENT);
        Message message= messageServices.save(message1);
		try{
			Thread.sleep(2000);
			simpMessagingTemplate.convertAndSendToUser(messageRequest.getReceiver(), "/" +messageRequest.getUsername()+"/private",message);//user/receiver/private

		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		return message;

	}
    @PostMapping("/chat/start_new_chat")
	public GenericResponse startNewChat(@RequestParam("receiver") String email,HttpServletRequest httpServletRequest) throws GenericException {

		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		if (!email.isEmpty()) {
			User receiver = userService.getUserByUsername(email);
			if (receiver != null) {
				Chat chat1 = chatServices.getChatBySenderAndReceiver(user, receiver);
				if (chat1 == null) {
					Chat chat = new Chat(null, Instant.now(), user, receiver);
					Chat chat2=chatServices.save(chat);
					return new GenericResponse("success","",chat2);
				}
				else{
					List<Message> unreadMessages=messageServices.findBySenderAndReceiverAndStatus(receiver,user,Status.SENT);
					unreadMessages.forEach(message -> {
						message.setStatus(Status.SEEN);
						messageServices.updateMessage(message);
					});

					ProfileImage profileImage = profileImageService.get(receiver);
					List<Message>messages=messageServices.getMessageBySenderAndReceiverOrReceiverAndSender(user,receiver,Sort.by("time").descending());
					Collections.reverse(messages);
					CandidateReceiver candidateReceiver = new CandidateReceiver(receiver.getFirstName() + " " + receiver.getLastName(), receiver.getEmail(), profileImage.getUrl(), false,0,messages);

					return new GenericResponse("success", "", candidateReceiver);
				}
			}

			return new GenericResponse("success","This is user is not found",null);
		}

			return new GenericResponse("error","Email not found",null);
	}

	@PostMapping("/chat/company")
	public GenericResponse getCandidateList( HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);

		Pageable tenChat = PageRequest.of(0, 10, Sort.by("date")
													 .descending());
		Page<Chat> chats = chatServices.getChatBySender(user, tenChat);
		List<CandidateReceiver> receivers = new ArrayList<>();
		chats.getContent()
			 .forEach(chat -> {
				 List<Message> unreadMessages=messageServices.findBySenderAndReceiverAndStatus(chat.getReceiver(),chat.getSender(),Status.SENT);
				 List<Message>messages=messageServices.getMessageBySenderAndReceiverOrReceiverAndSender(chat.getSender(),chat.getReceiver(),Sort.by("time").descending());
				 Collections.reverse(messages);
				 ProfileImage profileImage = profileImageService.get(chat.getReceiver());
				 CandidateReceiver candidateReceiver = new CandidateReceiver(chat.getReceiver()
																				 .getFirstName() + " " + chat.getReceiver()
																											 .getLastName(), chat.getReceiver()
																																 .getEmail(), profileImage.getUrl(), false,unreadMessages.size(), messages);
				 receivers.add(candidateReceiver);
			 });
		return new GenericResponse("success", "", receivers);
	}

	@PostMapping("/chat/company/receiver")
	public GenericResponse getCandidateMessage(@RequestParam("receiver") String email,HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User sender = jwtUtility.getUserFromToken(authorization);
		User receiver = userService.getUserByUsername(email);

		if (receiver != null) {
			List<Message> unreadMessages=messageServices.findBySenderAndReceiverAndStatus(receiver,sender,Status.SENT);
			unreadMessages.forEach(message -> {
				message.setStatus(Status.SEEN);
				messageServices.updateMessage(message);
			});

			ProfileImage profileImage = profileImageService.get(receiver);
			List<Message>messages=messageServices.getMessageBySenderAndReceiverOrReceiverAndSender(sender,receiver,Sort.by("time").descending());
			Collections.reverse(messages);
			CandidateReceiver candidateReceiver = new CandidateReceiver(receiver.getFirstName() + " " + receiver.getLastName(), receiver.getEmail(), profileImage.getUrl(), false,0,messages);

			return new GenericResponse("success", "", candidateReceiver);

		}
		return new GenericResponse("error", "Receiver not found", null);
	}

	@PostMapping("/chat/user")
	public GenericResponse getCompanyUserList(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		Pageable tenChat = PageRequest.of(0, 10, Sort.by("date")
													 .descending());
		Page<Chat> chats = chatServices.getChatByReceiver(user, tenChat);
		List<CandidateReceiver> receivers = new ArrayList<>();
		chats.getContent()
			 .forEach(chat -> {
				 List<Message> unreadMessages=messageServices.findBySenderAndReceiverAndStatus(chat.getSender(),chat.getReceiver(),Status.SENT);
				 ProfileImage profileImage = profileImageService.get(chat.getSender());
				 List<Message>messages=messageServices.getMessageBySenderAndReceiverOrReceiverAndSender(chat.getSender(),user,Sort.by("time").descending());
				 Collections.reverse(messages);
				 CandidateReceiver candidateReceiver = new CandidateReceiver(chat.getSender()
																				 .getFirstName() + " " + chat.getSender()
																											 .getLastName(), chat.getSender()
																																 .getEmail(),"#", false, unreadMessages.size(),messages);
				 receivers.add(candidateReceiver);
			 });

		return new GenericResponse("success", "", receivers);
	}
	@PostMapping("/chat/user/receiver")
	public GenericResponse getCompanyList(@RequestParam("receiver") String email,HttpServletRequest httpServletRequest) throws GenericException {

		String authorization = httpServletRequest.getHeader("Authorization");
		User sender = jwtUtility.getUserFromToken(authorization);
		User receiver = userService.getUserByUsername(email);

		if (receiver != null) {
			List<Message> unreadMessages=messageServices.findBySenderAndReceiverAndStatus(receiver,sender,Status.SENT);
			unreadMessages.forEach(message -> {
				message.setStatus(Status.SEEN);
				messageServices.updateMessage(message);
			});

			//			ProfileImage profileImage = profileImageService.get(receiver);
			List<Message>messages=messageServices.getMessageBySenderAndReceiverOrReceiverAndSender(sender,receiver,Sort.by("time").descending());
			Collections.reverse(messages);
			CandidateReceiver candidateReceiver = new CandidateReceiver(receiver.getFirstName() + " " + receiver.getLastName(), receiver.getEmail(), "#", false,0,messages);

			return new GenericResponse("success", "", candidateReceiver);

		}
		return new GenericResponse("error", "Receiver not found", null);
	}

}
