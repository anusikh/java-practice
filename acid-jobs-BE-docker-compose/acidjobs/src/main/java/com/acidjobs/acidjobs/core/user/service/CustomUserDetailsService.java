package com.acidjobs.acidjobs.core.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.CustomUser;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.repository.UserRepository;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user=userRepository.findByEmail(username);
		if(user==null){
			throw  new UsernameNotFoundException(username);
		}
//		boolean is_enable=!user.isAccountVerified();

		CustomUser customUser=new CustomUser(user);

		return customUser;
	}
}
