package com.edmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edmanager.model.Account;
import com.edmanager.model.Category;
import com.edmanager.model.Users;
import com.edmanager.repository.CategoryRepository;
import com.edmanager.repository.UsersRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
    private CategoryRepository categoryRepository;

	@Autowired
    private UsersRepository usersRepository;
	
	@GetMapping("/insert")
    public ResponseEntity<?> addCategory(@RequestParam("name") String name, @RequestParam("userId") long userId){
		try {
			Users user = usersRepository.getById(userId);
			Category category = null;
			if(user != null) {
				category = new Category(name);
				category = categoryRepository.save(category);
				logger.info("New category has been created for user="+user.getName());
				//account = new Account(name, currentBalance, user); 
				//account = accountRepository.save(account);
				//logger.info("New account has been created for user="+user.getName()+", act-name="+account.getName());
			}
			return ResponseEntity.ok("Success");
		}catch(Exception e) {
			logger.error("Exception in addCategory API", e);
			return ResponseEntity.ok(null);
		}
    }
	@GetMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestParam("oldName") String oldName,@RequestParam("newName") String newName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Category category = null;
			if(user != null) {
				category = categoryRepository.findByName(oldName);
				category.setName(newName);
				category = categoryRepository.save(category);
				logger.info("Category has been edited for user="+user.getName()+",cat-name="+category.getName());
				
//				account = accountRepository.findByName(oldName);
//				account.setName(newName);
//				account = accountRepository.save(account);
//				logger.info("Account has been edited for user="+user.getName()+", act-name="+account.getName());
			}
			return ResponseEntity.ok("Success");
		}catch(Exception e) {
			logger.error("Exception in editAccount API", e);
			return ResponseEntity.ok(null);
		}
    }

}
