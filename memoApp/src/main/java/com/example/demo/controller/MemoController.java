package com.example.demo.controller;

import java.text.SimpleDateFormat;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Memo;
import com.example.demo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemoController {

	private final MemoRepository repository;

	@GetMapping("/memo/List")
	public String showMemoList(@ModelAttribute Memo memo, Model model) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.addAttribute("sdf", sdf);
		model.addAttribute("memoList", repository.findAll(Sort.by(Sort.Direction.DESC, "memoId")));

		return "memoList";

	}

	@PostMapping("/memo/save")
	public String saveMemo(@Validated @ModelAttribute Memo memo, BindingResult result) {

		if (result.hasErrors()) {

		} else {

			repository.save(memo);

		}

		return "redirect:/memo/List";

	}

	@GetMapping("/memo/delete/{id}")
	public String deleteMemo(@PathVariable Long id) {

		repository.deleteById(id);
		return "redirect:/memo/List";

	}

}
