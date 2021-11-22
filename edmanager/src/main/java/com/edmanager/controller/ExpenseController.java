package com.edmanager.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.edmanager.model.Account;
import com.edmanager.model.Category;
import com.edmanager.model.Expense;
import com.edmanager.model.Response;
import com.edmanager.model.SubCategory;
import com.edmanager.model.Users;
import com.edmanager.repository.AccountRepository;
import com.edmanager.repository.CategoryRepository;
import com.edmanager.repository.ExpenseRepository;
import com.edmanager.repository.SubCategoryRepository;
import com.edmanager.repository.UsersRepository;
import com.edmanager.util.Constants;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

	private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private CategoryRepository categoryRepository;
	
	@Autowired
    private SubCategoryRepository subCategoryRepository;

	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
    private UsersRepository usersRepository;
	
	@ResponseBody
	@PostMapping("/add")
    public Response addExpense(@RequestParam("date") String date, @RequestParam("amount") double amount, @RequestParam("category") String categoryName, 
    		@RequestParam("subCategory") String subCategoryName, @RequestParam("account") String accountName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			Category category = null;
			SubCategory subCategory = null;
			Expense expense = new Expense();
			if(user != null) { 
				account = accountRepository.findByNameAndUser_Id(accountName, userId);
				category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				subCategory = subCategoryRepository.findByNameAndCategory_IdAndUser_Id(subCategoryName, category.getId(), userId);
				
				expense.setAccountId(account.getId());
				expense.setCategory(category);
				expense.setSubCategoryId(subCategory.getId());
				expense.setAmount(amount);
				expense.setDate((new Date()).getTime());
				
				expenseRepository.save(expense);
				
				return new Response(Constants.SUCCESS,"Expense has been added.",null);
			}
			else
				return null;
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return null;
		}
    }

}
