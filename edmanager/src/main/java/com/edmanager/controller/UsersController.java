package com.edmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
/*	@Autowired
    private UsersRepository usersRepository;
*/

	/*@GetMapping("/helloWorld")
    public ResponseEntity<?> helloWorld() throws Exception {
    	logger.info("Demo....");
        return ResponseEntity.ok("Hello User123");
    }*/
}
