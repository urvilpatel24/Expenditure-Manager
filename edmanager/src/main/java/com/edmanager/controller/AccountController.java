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

import com.edmanager.model.Account;
import com.edmanager.model.Response;
import com.edmanager.model.Users;
import com.edmanager.repository.AccountRepository;
import com.edmanager.repository.UsersRepository;
import com.edmanager.util.Constants;

@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
    private AccountRepository accountRepository;

	@Autowired
    private UsersRepository usersRepository;
	
	@ResponseBody
	@GetMapping("/getAll")
    public Response getAll(@RequestParam("userId") long userId){
		try {
			logger.info("Get All accounts for current user.");
			return new Response(Constants.SUCCESS,"",accountRepository.findAllByUser_id(userId));
		}catch(Exception e) {
			logger.error("Exception in getAll API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in fetching accounts.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/add")
    public Response addAccount(@RequestParam("name") String name, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = new Account(name, user); 
				account = accountRepository.save(account);
				logger.info("New account has been created for user="+user.getName()+", act-name="+account.getName());
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,account.getName()+" has been added.",null);
		}catch(Exception e) {
			logger.error("Exception in addAccount API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in adding account.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/edit")
    public Response editAccount(@RequestParam("oldName") String oldName,@RequestParam("newName") String newName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = accountRepository.findByNameAndUser_Id(oldName, userId);
				account.setName(newName);
				account = accountRepository.save(account);
				logger.info("Account has been edited for user="+user.getName()+", act-name="+account.getName());
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,"Account name has been changed to "+account.getName(),null);
		}catch(Exception e) {
			logger.error("Exception in editAccount API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in updating account.",null);
		}
    }
	
	@ResponseBody
	@PostMapping("/delete")
    public Response deleteAccount(@RequestParam("name") String name, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = accountRepository.findByNameAndUser_Id(name, userId);
				accountRepository.delete(account);
				logger.info("Account has been deleted for user="+user.getName()+", act-name="+name);
			}
			else
				return new Response(Constants.ERROR,"Provided data is incorrect.",null);
			return new Response(Constants.SUCCESS,account.getName()+" has been deleted.",null);
		}catch(Exception e) {
			logger.error("Exception in deleteAccount API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in deleting account.",null);
		}
    }
}
