package com.edmanager.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edmanager.model.Expense;

@Repository
@Transactional(rollbackOn = Throwable.class)
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	public List<Expense> findAllByCategory_idAndUser_Id(long categoryId, long userId);
	
	@Query("select e from Expense e where e.date between ?1 and ?2 and e.user.id = ?3")
	public List<Expense> findAllByDatesAndUser_Id(long startDate, long endDate, long userId);
	
	public Expense findByAmountAndDateCategory_idAndUser_Id(double amount, long date, long categoryId, long userId);
}
