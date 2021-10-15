package com.edmanager.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmanager.model.SubCategory;

@Repository
@Transactional(rollbackOn = Throwable.class)
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

}
