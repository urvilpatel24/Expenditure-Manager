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
	
	@Query("select e from Expense e where e.date between ?1 and ?2 and e.user.id = ?3 order by e.date")
	public List<Expense> findAllByDatesAndUser_Id(long startDate, long endDate, long userId);
	
	@Query("select e from Expense e where e.date between ?1 and ?2 and e.category.id = ?3 and e.user.id = ?4 order by e.date")
	public List<Expense> findAllByDatesAndCategory_IdAndUser_Id(long startDate, long endDate, long categoryId, long userId);
	
	public Expense findByAmountAndDateAndCategory_IdAndUser_Id(double amount, long date, long categoryId, long userId);
}
