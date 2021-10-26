package com.edmanager.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmanager.model.Category;

@Repository
@Transactional(rollbackOn = Throwable.class)
public interface CategoryRepository extends JpaRepository<Category, Long> {
	public Category findByName(String name);
}
