package com.edmanager.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.edmanager.model.Category;
import com.edmanager.model.Expense;
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
    public Response byCategoryAndDate(@RequestParam("startDate") long startDate, @RequestParam("category") String category, 
    		@RequestParam("endDate") long endDate, @RequestParam("userId") long userId){
		try {
			logger.info("analyse expenses by Category and date : : "+startDate+" : "+endDate);
			Category cat = categoryRepository.findByNameAndUser_Id(category, userId);
			List<Expense> list = expenseRepository.findAllByDatesAndCategory_IdAndUser_Id(startDate, endDate, cat.getId(), userId);
			String res = "You have "+list.size()+" expenses for category "+cat.getName()+". ";
			double amt = 0;
			for(Expense e: list) {
				res = res.concat("You spent "+e.getAmount()+"  CAD in "+e.getCategory().getName()+" on "+formatDate(e.getDate())+" .");
				amt += e.getAmount();
			}
			res = res.concat("In total you spent "+amt+" CAD.");
			return new Response(Constants.SUCCESS,res,null);
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
			List<Expense> list = expenseRepository.findAllByDatesAndUser_Id(startDate, endDate, userId);
			String res = "You have "+list.size()+" expenses for November 2021. \n\n";
			double amt = 0;
			for(Expense e: list) {
				res.concat("You spent "+e.getAmount()+"  CAD in "+e.getCategory().getName()+" on "+formatDate(e.getDate())+" .\n");
				amt += e.getAmount();
			}
			res.concat("\n\nIn total you spent "+amt+" CAD.\n");
			MailUtil.send(Constants.EMAIL, "Expenditure Manager - Expense report", res, "Expenditure Manager");
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
	
	private String formatDate(long epoch) {
        try
        {
        	logger.info(" >>> epoch="+epoch);
            
        	Date date = new Date(epoch);
            logger.info("Date="+date);
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = formatter.format(date);
            logger.info("formattedDate="+formattedDate);
            
            return formattedDate;
        }
        catch (Exception e)
        {
            logger.error("Exception at formatDateDDMMMYYYY >>> ",e);
            e.printStackTrace();
            return "";
        }
    }
}
