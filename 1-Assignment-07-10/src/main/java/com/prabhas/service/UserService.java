package com.prabhas.service;

import org.springframework.stereotype.Service;

import com.prabhas.model.User;
import com.prabhas.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	public String getUserNameById(Long id) {
		return userRepository.findById(id)
			   .map(User::getUsername)
			   .orElse("Unknown");
	}
}
