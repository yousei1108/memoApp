package com.example.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.MemoUserRepository;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName , String>{

	private final MemoUserRepository repository;
	
	public UniqueUserNameValidator() {
		
		this.repository = null;
		
	}
	
	@Autowired
	public UniqueUserNameValidator(MemoUserRepository repository) {
		
		this.repository = repository;
		
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		return repository == null || repository.findByUserName(value) == null;
		
	}
	
	

}
