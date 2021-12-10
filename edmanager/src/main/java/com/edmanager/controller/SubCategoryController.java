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
import com.edmanager.model.SubCategory;
import com.edmanager.model.Users;
import com.edmanager.repository.CategoryRepository;
import com.edmanager.repository.SubCategoryRepository;
import com.edmanager.repository.UsersRepository;
import com.edmanager.util.Constants;

@RestController
@RequestMapping("/subCategory")
public class SubCategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(SubCategoryController.class);
	
	@Autowired
    private CategoryRepository categoryRepository;

	@Autowired
    private SubCategoryRepository subCategoryRepository;

	@Autowired
    private UsersRepository usersRepository;
	
	@ResponseBody
	@GetMapping("/getAll")
    public Response getAll(@RequestParam("parentName") String parentName, @RequestParam("userId") long userId){
		try {
			logger.info("Get All sub-categories of "+parentName+" for current user.");
			Category category = categoryRepository.findByNameAndUser_Id(parentName, userId);
			return new Response(Constants.SUCCESS,"",subCategoryRepository.findAllByCategory_IdAndUser_Id(category.getId(), userId));
		}catch(Exception e) {
			logger.error("Exception in getAll API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in fetching sub-categories.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/add")
    public Response addSubCategory(@RequestParam("name") String name, @RequestParam("parentName") String parentName, @RequestParam("userId") long userId){
		try {
			Users user = usersRepository.getById(userId);
			SubCategory subCategory = null;
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(parentName, userId);
				subCategory = new SubCategory();
				subCategory.setName(name);
				subCategory.setCategory(category);
				subCategory.setUser(user);
				subCategory = subCategoryRepository.save(subCategory);
				logger.info("New subCategory has been created for user="+user.getName()+", subCategory-name = "+subCategory.getName());
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,subCategory.getName()+" has been added as sub category.",null);
		}catch(Exception e) {
			logger.error("Exception in addSubCategory API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in adding sub-category.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/edit")
    public Response editSubCategory(@RequestParam("oldName") String oldName,@RequestParam("newName") String newName, @RequestParam("parentName") String parentName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			SubCategory subCategory = null;
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(parentName, userId);
				subCategory = subCategoryRepository.findByNameAndCategory_IdAndUser_Id(oldName, category.getId(), userId);
				subCategory.setName(newName);
				subCategory.setCategory(category);
				subCategory.setUser(user);
				subCategory = subCategoryRepository.save(subCategory);
				logger.info("SubCategory has been edited for user="+user.getName()+",subCategory-name="+subCategory.getName());
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,"SubCategory name has been changed to "+subCategory.getName(),null);
		}catch(Exception e) {
			logger.error("Exception in editSubCategory API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in updating sub-category.",null);
		}
    }

	@ResponseBody
	@PostMapping("/delete")
    public Response deleteSubCategory(@RequestParam("name") String name, @RequestParam("parentName") String parentName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			SubCategory subCategory = null;
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(parentName, userId);
				subCategory = subCategoryRepository.findByNameAndCategory_IdAndUser_Id(name, category.getId(), userId);
				subCategoryRepository.delete(subCategory);
				logger.info("SubCategory has been deleted for user="+user.getName()+", act-name="+name);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,subCategory.getName()+" has been deleted.",null);
		}catch(Exception e) {
			logger.error("Exception in deleteSubCategory API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in deleting sub-category.",null);
		}
    }
}
