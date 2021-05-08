package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Memo;
import com.example.demo.model.MemoUser;

public interface MemoRepository extends JpaRepository<Memo, Long> {

	public Page<Memo> findByMemoUser(MemoUser memoUser , Pageable pageable);

	@Query("SELECT m FROM Memo m WHERE m.memoUser = :memoUser AND (m.title LIKE :word OR m.text LIKE :word)")
	public Page<Memo> searchMemo(MemoUser memoUser , String word, Pageable pageable);

}
