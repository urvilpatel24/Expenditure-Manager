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
import com.edmanager.model.Users;
import com.edmanager.repository.AccountRepository;
import com.edmanager.repository.UsersRepository;

@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
    private AccountRepository accountRepository;

	@Autowired
    private UsersRepository usersRepository;
	
	@GetMapping("/insert")
    public ResponseEntity<?> addAccount(@RequestParam("name") String name, @RequestParam("balance") double currentBalance, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = new Account(name, currentBalance, user); 
				account = accountRepository.save(account);
				logger.info("New account has been created for user="+user.getName()+", act-name="+account.getName());
			}
			return ResponseEntity.ok("Success");
		}catch(Exception e) {
			logger.error("Exception in addAccount API", e);
			return ResponseEntity.ok(null);
		}
    }
	
	@GetMapping("/edit")
    public ResponseEntity<?> editAccount(@RequestParam("oldName") String oldName,@RequestParam("newName") String newName, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = accountRepository.findByName(oldName);
				account.setName(newName);
				account = accountRepository.save(account);
				logger.info("Account has been edited for user="+user.getName()+", act-name="+account.getName());
			}
			return ResponseEntity.ok("Success");
		}catch(Exception e) {
			logger.error("Exception in editAccount API", e);
			return ResponseEntity.ok(null);
		}
    }
	
	@GetMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam("name") String name, @RequestParam("userId") long userId) {
		try {
			Users user = usersRepository.getById(userId);
			Account account = null;
			if(user != null) {
				account = accountRepository.findByName(name);
				accountRepository.delete(account);
				logger.info("Account has been deleted for user="+user.getName()+", act-name="+name);
			}
			return ResponseEntity.ok("Success");
		}catch(Exception e) {
			logger.error("Exception in deleteAccount API", e);
			return ResponseEntity.ok(null);
		}
    }
}
