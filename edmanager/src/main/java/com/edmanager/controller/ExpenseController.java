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
    public Response addExpense(@RequestParam("date") long date, @RequestParam("amount") double amount, @RequestParam("category") String categoryName, 
    		@RequestParam("subCategory") String subCategoryName, @RequestParam("account") String accountName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			Category category = null;
			SubCategory subCategory = null;
			Expense expense = new Expense();
			if(user != null) {
				
				category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				expense.setCategory(category);
				expense.setAmount(amount);
				expense.setDate((new Date()).getTime());
				
				if(!accountName.isEmpty()) {
					account = accountRepository.findByNameAndUser_Id(accountName, userId);
					expense.setAccountId(account.getId());
				}
				else
					expense.setAccountId(-1);
				if(!subCategoryName.isEmpty()) {
					subCategory = subCategoryRepository.findByNameAndCategory_IdAndUser_Id(subCategoryName, category.getId(), userId);
					expense.setSubCategoryId(subCategory.getId());
				}
				else
					expense.setSubCategoryId(-1);
				
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

	@ResponseBody
	@PostMapping("/edit")
    public Response editExpense(@RequestParam("date") long date, @RequestParam("amount") double amount, @RequestParam("category") String categoryName,
    		@RequestParam("newDate") long newDate, @RequestParam("newAmount") double newAmount, @RequestParam("newCategory") String newCategoryName,
    		@RequestParam("subCategory") String subCategoryName, @RequestParam("account") String accountName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			SubCategory subCategory = null;
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				Expense expense = expenseRepository.findByAmountAndDateCategory_idAndUser_Id(amount, date, category.getId(), userId);
				
				category = categoryRepository.findByNameAndUser_Id(newCategoryName, userId);
				expense.setCategory(category);
				expense.setAmount(newAmount);
				expense.setDate((new Date()).getTime());
				
				if(!accountName.isEmpty()) {
					account = accountRepository.findByNameAndUser_Id(accountName, userId);
					expense.setAccountId(account.getId());
				}
				else
					expense.setAccountId(-1);
				if(!subCategoryName.isEmpty()) {
					subCategory = subCategoryRepository.findByNameAndCategory_IdAndUser_Id(subCategoryName, category.getId(), userId);
					expense.setSubCategoryId(subCategory.getId());
				}
				else
					expense.setSubCategoryId(-1);
				
				expenseRepository.save(expense);
				
				return new Response(Constants.SUCCESS,"Expense has been updated.",null);
			}
			else
				return null;
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return null;
		}
    }
	
	@ResponseBody
	@PostMapping("/delete")
    public Response deleteExpense(@RequestParam("date") long date, @RequestParam("amount") double amount, 
    		@RequestParam("category") String categoryName,@RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				Expense expense = expenseRepository.findByAmountAndDateCategory_idAndUser_Id(amount, date, category.getId(), userId);
				expenseRepository.delete(expense);
				return new Response(Constants.SUCCESS,"Expense has been deleted.",null);
			}
			else
				return null;
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return null;
		}
    }
	
	@ResponseBody
	@PostMapping("/find")
    public Response findExpense(@RequestParam("date") long date, @RequestParam("amount") double amount, 
    		@RequestParam("category") String categoryName,@RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			if(user != null) {
				Category category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				return new Response(Constants.SUCCESS,"Expense found.", expenseRepository.findByAmountAndDateCategory_idAndUser_Id(amount, date, category.getId(), userId));
			}
			else
				return null;
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return null;
		}
    }
}
