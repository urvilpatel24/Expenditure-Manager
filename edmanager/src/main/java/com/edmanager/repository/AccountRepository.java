package com.edmanager.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmanager.model.Account;

@Repository
@Transactional(rollbackOn = Throwable.class)
public interface AccountRepository extends JpaRepository<Account, Long> {

	public Account findByNameAndUser_Id(String name, long userId);
	public List<Account> findAllByUser_id(long userId);
}
