package com.example.demo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Memo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memoId;

	@Column(columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Generated(value = GenerationTime.ALWAYS)
	private Timestamp update_at;

	@NotBlank
	@Size(max = 100)
	private String title;

	@NotBlank
	@Size(max = 1000)
	private String text;

	@ManyToOne
	private MemoUser memoUser;

}
