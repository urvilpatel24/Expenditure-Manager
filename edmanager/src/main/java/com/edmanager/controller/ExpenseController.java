package com.edmanager.controller;

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
				expense.setDate(date);
				
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
				expense.setUser(user);
				expenseRepository.save(expense);
				
				return new Response(Constants.SUCCESS,"Expense has been added.",null);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in adding the expense.",null);
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
				Expense expense = expenseRepository.findByAmountAndDateAndCategory_IdAndUser_Id(amount, date, category.getId(), userId);
				
				category = categoryRepository.findByNameAndUser_Id(newCategoryName, userId);
				expense.setCategory(category);
				expense.setAmount(newAmount);
				expense.setDate(date);
				
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
				expense.setUser(user);
				expenseRepository.save(expense);
				
				return new Response(Constants.SUCCESS,"Expense has been updated.",null);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in updating the expense.",null);
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
				Expense expense = expenseRepository.findByAmountAndDateAndCategory_IdAndUser_Id(amount, date, category.getId(), userId);
				expenseRepository.delete(expense);
				return new Response(Constants.SUCCESS,"Expense has been deleted.",null);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in deleting the expense.",null);
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
				Expense expense = expenseRepository.findByAmountAndDateAndCategory_IdAndUser_Id(amount, date, category.getId(), userId);
				Account account = accountRepository.getById(expense.getAccountId());
				return new Response(Constants.SUCCESS,"Expense found with Amount "+expense.getAmount()+" CAD, Category "+expense.getCategory().getName()+", Account "+account.getName()+". So please provide new details for this expense.", null);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
		}catch(Exception e) {
			logger.error("Exception in findExpense API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in finding the expense.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/split")
    public Response splitExpense(@RequestParam("date") long date, @RequestParam("amount") double amount, @RequestParam("category") String categoryName, 
    			@RequestParam("account") String accountName, @RequestParam("nop") int nop, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			Category category = null;
			Expense expense = new Expense();
			if(user != null) {
				
				category = categoryRepository.findByNameAndUser_Id(categoryName, userId);
				expense.setCategory(category);
				expense.setAmount(amount);
				expense.setDate(date);
				
				if(!accountName.isEmpty()) {
					account = accountRepository.findByNameAndUser_Id(accountName, userId);
					expense.setAccountId(account.getId());
				}
				else
					expense.setAccountId(-1);
					
				expense.setSubCategoryId(subCategoryRepository.findByNameAndCategory_IdAndUser_Id("Other", category.getId(), userId).getId());
				expense.setUser(user);
				expenseRepository.save(expense);
				
				return new Response(Constants.SUCCESS,"Expense has been added and your "+nop+" friends have to pay you"+(expense.getAmount()/(nop+1)+" dollars."),null);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
		}catch(Exception e) {
			logger.error("Exception in addExpense API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in adding the expense.",null);
		}
    }

}
