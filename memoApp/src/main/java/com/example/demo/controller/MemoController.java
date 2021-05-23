package com.example.demo.controller;

import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Memo;
import com.example.demo.model.MemoUser;
import com.example.demo.repository.MemoRepository;
import com.example.demo.repository.MemoUserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemoController {

	private final MemoRepository memoRepository;
	private final MemoUserRepository userRepository;
	private static final int onePageSize = 30;
	private final BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String defaultPage() {
		
		return "redirect:/memo/list/1";
		
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "login";
		
	}
	
	@GetMapping("/register")
	public String register(@ModelAttribute("user") MemoUser user , Model model) {
		
		return "register";
		
	}
	
	@PostMapping("/register")
	public String process(@Validated @ModelAttribute("user") MemoUser user , BindingResult result ,
			 @RequestParam(name="re-password" , required = false) String rePassword) {
		
		if( !(user.getPassword().equals(rePassword)) ) {
			FieldError fieldError = new FieldError(result.getObjectName() , "password" , "パスワードが異なります");
			result.addError(fieldError);
		}
		
		if(result.hasErrors()) {
			
			return "register";
			
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		
		return "redirect:/result?register";
		
	}
	
	@GetMapping("/result")
	public String result() {
		
		return "result";
		
	}
	
	@GetMapping("/memo/list/{pageNumber}")
	public String showMemoList(@RequestParam(name = "word", required = false) String word,
			@PathVariable(required = false) int pageNumber, @ModelAttribute Memo memo, Model model ,
			Authentication loginUser) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("sdf", sdf);
		model.addAttribute("url", "/memo/list/");
		MemoUser user = userRepository.findByUserName(loginUser.getName());

		if (word == null || word.isEmpty()) {

			Page<Memo> memoPage = memoRepository
					.findByMemoUser( user , PageRequest.of(pageNumber - 1, onePageSize, Sort.by("memoId").descending()));
			model.addAttribute("memoList", memoPage.getContent());
			model.addAttribute("memoPage", memoPage);

		} else {

			Page<Memo> memoPage = memoRepository.searchMemo( user , "%" + word + "%",
					PageRequest.of(pageNumber - 1, onePageSize, Sort.by("memoId").descending()));
			model.addAttribute("memoList", memoPage.getContent());
			model.addAttribute("memoPage", memoPage);
			model.addAttribute("word", word);

		}

		return "memoList";

	}

	@PostMapping("/memo/save")
	public String saveMemo(@Validated @ModelAttribute Memo memo, BindingResult result , Authentication loginUser) {

		if (result.hasErrors()) {

		} else {

			MemoUser user = userRepository.findByUserName(loginUser.getName());
			memo.setMemoUser(user);
			memoRepository.save(memo);

		}

		return "redirect:/memo/list/1";

	}

	@GetMapping("/memo/delete/{id}")
	public String deleteMemo(@PathVariable Long id) {

		memoRepository.deleteById(id);
		return "redirect:/memo/list/1";

	}
	
	@GetMapping("/account/delete")
	public String deleteAccount() {
		
		return "accountDelete";
		
	}
	
	@PostMapping("/account/delete")
	public String accountDeleteProccess(Authentication loginUser , HttpServletRequest request) {
		
		try {
			request.logout();
		} catch (ServletException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		MemoUser user = userRepository.findByUserName(loginUser.getName());
		memoRepository.deleteByMemoUser(user);
		userRepository.delete(user);
		
		return "redirect:/";
		
	}
	
}
