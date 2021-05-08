package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Memo;
import com.example.demo.model.MemoUser;
import com.example.demo.repository.MemoRepository;
import com.example.demo.repository.MemoUserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class MemoControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	MemoUserRepository userRepository;
	
	@Autowired
	MemoRepository memoRepository;
	
	@Test
	@DisplayName("ユーザー登録に誤りがある場合、エラーを返すことを期待します")
	void whenUserRegisterError_expectToReturnError() throws Exception{
		
		mockMvc.perform(
						post("/register")
						.flashAttr("user", new MemoUser())
						.with(csrf())
				)
				.andExpect(view().name("register"))
				.andExpect(model().hasErrors());
	}
	
	@Test
	@DisplayName("メモ一覧にアクセスした時、ログインしたユーザーが登録したメモが表示されることを期待します")
	@WithMockUser(username = "test_user")
	void whenAccessMemoList_expectToDisplayLoginUserMemo() throws Exception{
		
		MemoUser user = new MemoUser();
		Memo memo = new Memo();
		
		user.setUserName("test_user");
		user.setPassword("password");
		userRepository.save(user);
		user = userRepository.findByUserName(user.getUserName());
		
		memo.setTitle("test_title");
		memo.setText("test_text");
		memo.setMemoUser(user);
		memoRepository.save(memo);		
		
		MvcResult result = mockMvc.perform(get("/memo/list/1")).andReturn();
		ModelAndView mv = result.getModelAndView();
		List<Memo> memoList = (List<Memo>) mv.getModel().get("memoList");
		assertEquals(user.getId() , memoList.get(0).getMemoUser().getId());
		
		
	}
	
	

}
