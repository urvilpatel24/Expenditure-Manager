package com.edmanager.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmanager.model.SubCategory;

@Repository
@Transactional(rollbackOn = Throwable.class)
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
	public SubCategory findByNameAndCategory_IdAndUser_Id(String name, long categoryId, long userId);
	public List<SubCategory> findAllByCategory_IdAndUser_Id(long categoryId, long userId); 
}
