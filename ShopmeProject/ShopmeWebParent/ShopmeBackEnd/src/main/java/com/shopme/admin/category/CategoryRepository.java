package com.shopme.admin.category;

import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{
	
}