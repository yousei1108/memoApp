package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.MemoUser;
import com.example.demo.repository.MemoUserRepository;

@SpringBootTest
@Transactional
class UserDetailsServiceImplTest {

	@Autowired
	MemoUserRepository userRepository;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Test
	@DisplayName("ユーザーが存在する場合、ユーザー詳細を返すことを期待します")
	void whenUserExists_expectToGetUserDetails() {
		
		MemoUser user = new MemoUser();
		user.setUserName("test_user");
		user.setPassword("password");
		userRepository.save(user);
		
		UserDetails actual = userDetailsService.loadUserByUsername("test_user");
		
		assertEquals(user.getUserName() , actual.getUsername());
		
	}
	
	@Test
	@DisplayName("ユーザー名が存在しない場合、例外を返すことを期待します")
	void whenUserNotExists_expectToThrowEXception() {
		
		assertThrows(UsernameNotFoundException.class , () -> userDetailsService.loadUserByUsername("test_not_exists_user"));
		
	}

}
