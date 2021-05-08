package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.validator.UniqueUserName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MemoUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	@Size(max = 20 , message = "20文字以内で設定してください")
	@UniqueUserName
	private String userName;

	@NotBlank
	private String password;

	@OneToMany(mappedBy = "memoUser")
	private List<Memo> memoList;

}
