package com.edmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.edmanager.model.Category;
import com.edmanager.model.Response;
import com.edmanager.repository.CategoryRepository;
import com.edmanager.repository.ExpenseRepository;
import com.edmanager.util.Constants;
import com.edmanager.util.MailUtil;

@RestController
@RequestMapping("/analyse")
public class AnalyseController {

	private static final Logger logger = LoggerFactory.getLogger(AnalyseController.class);
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
    private CategoryRepository categoryRepository;
	
	@ResponseBody
	@GetMapping("/byCategoryAndDate")
    public Response byCategoryAndDate(@RequestParam("category") String category, @RequestParam("startDate") long startDate, 
    		@RequestParam("endDate") long endDate, @RequestParam("userId") long userId){
		try {
			logger.info("analyse expenses by Category and date : "+category+" : "+startDate+" : "+endDate);
			Category cat = categoryRepository.findByNameAndUser_Id(category, userId);
			return new Response(Constants.SUCCESS,Constants.SUCCESS,expenseRepository.findAllByDatesAndCategoryAndUser_Id(startDate, endDate, cat.getId(), userId));
		}catch(Exception e) {
			logger.error("Exception in byCategoryAndDate API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in fetching the results.",null);
		}
    }
	
	@ResponseBody
	@GetMapping("/email")
    public Response email(@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate, @RequestParam("userId") long userId){
		try {
			logger.info("email analyse expenses : "+startDate+" : "+endDate);
			MailUtil.send(Constants.EMAIL, "Expenditure Manager - Expense report", "", "Expenditure Manager");
			return new Response(Constants.SUCCESS,"I have sent an analysis report on your registerd Email.",null);
		}catch(Exception e) {
			logger.error("Exception in byCategoryAndDate API", e);
			return new Response(Constants.EXCEPTION,"There is a problem in fetching the results.",null);
		}
    }
	
	@ResponseBody
	@GetMapping("/byCategory")
    public Response byCategory(@RequestParam("category") String category, @RequestParam("userId") long userId){
		try {
			logger.info("analyse expenses by Category : ",category);
			Category cat = categoryRepository.findByNameAndUser_Id(category, userId);
			return new Response(Constants.SUCCESS,Constants.SUCCESS,expenseRepository.findAllByCategory_idAndUser_Id(cat.getId(), userId));
		}catch(Exception e) {
			logger.error("Exception in byCategory API", e);
			return null;
		}
    }
	
	@ResponseBody
	@GetMapping("/byDate")
    public Response byDate(@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate, @RequestParam("userId") long userId) {
		try {
			logger.info("analyse expenses by date : ",startDate+" : "+endDate);
			return new Response(Constants.SUCCESS,Constants.SUCCESS,expenseRepository.findAllByDatesAndUser_Id(startDate, endDate, userId));
		}catch(Exception e) {
			logger.error("Exception in byDate API", e);
			return null;
		}
    }
}