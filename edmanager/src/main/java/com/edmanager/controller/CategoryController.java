package com.edmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.edmanager.model.Category;
import com.edmanager.model.Response;
import com.edmanager.model.Users;
import com.edmanager.repository.CategoryRepository;
import com.edmanager.repository.UsersRepository;
import com.edmanager.util.Constants;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
    private CategoryRepository categoryRepository;

	@Autowired
    private UsersRepository usersRepository;
	
	@ResponseBody
	@GetMapping("/getAll")
    public Response getAll(@RequestParam("userId") long userId){
		try {
			logger.info("Get All categories for current user.");
			return new Response(Constants.SUCCESS,"",categoryRepository.findAllByUser_Id(userId));
		}catch(Exception e) {
			logger.error("Exception in getAll API", e);
			return null;
		}
    }
	
	@ResponseBody
	@GetMapping("/add")
    public Response addCategory(@RequestParam("name") String name, @RequestParam("userId") long userId){
		try {
			Users user = usersRepository.getById(userId);
			Category category = null;
			if(user != null) {
				category = new Category();
				category.setName(name);
				category.setUser(user);
				category = categoryRepository.save(category);
				logger.info("New category has been created for user="+user.getName()+", category-name = "+category.getName());
			}
			return new Response(Constants.SUCCESS,category.getName()+" has been added.",null);
		}catch(Exception e) {
			logger.error("Exception in addCategory API", e);
			return null;
		}
    }
	
	@ResponseBody
	@PostMapping("/edit")
    public Response editCategory(@RequestParam("oldName") String oldName,@RequestParam("newName") String newName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Category category = null;
			if(user != null) {
				category = categoryRepository.findByNameAndUser_Id(oldName, userId);
				category.setName(newName);
				category = categoryRepository.save(category);
				logger.info("Category has been edited for user="+user.getName()+",category-name="+category.getName());
			}
			return new Response(Constants.SUCCESS,"Category name has been changed to "+category.getName(),null);
		}catch(Exception e) {
			logger.error("Exception in editCategory API", e);
			return null;
		}
    }

	@ResponseBody
	@PostMapping("/delete")
    public Response deleteCategory(@RequestParam("name") String name, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Category category = null;
			if(user != null) {
				category = categoryRepository.findByNameAndUser_Id(name, userId);
				categoryRepository.delete(category);
				logger.info("Category has been deleted for user="+user.getName()+", act-name="+name);
			}
			return new Response(Constants.SUCCESS,category.getName()+" has been deleted.",null);
		}catch(Exception e) {
			logger.error("Exception in deleteCategory API", e);
			return null;
		}
    }
}
