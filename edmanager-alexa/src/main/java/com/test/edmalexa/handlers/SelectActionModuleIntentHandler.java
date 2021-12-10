package com.test.edmalexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.test.edmalexa.util.Constants;

public class SelectActionModuleIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("SelectActionModuleIntent"));
	}

	public Optional<Response> handle(HandlerInput input) 
	{
		try 
		{
			System.out.println("Inside SelectActionModuleIntent ...");
			
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	
	        System.out.println("Inside SelectActionModuleIntent 1...");
	        
	        Slot actionSlot = slots.get("action");
	        Slot moduleSlot = slots.get("module");
	        String speechText = "";
	
	        System.out.println("Inside SelectActionModuleIntent 2...");
	        
	        if(moduleSlot != null && moduleSlot.getResolutions() != null && moduleSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH") 
	        		&& actionSlot != null && actionSlot.getResolutions() != null && actionSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        {
	        	System.out.println("Inside If...");
	        	
	        	String module = moduleSlot.getValue();
	        	String action = actionSlot.getValue();
	        	
	        	Map<String, Object> map = new HashMap<String, Object>();
    			map.put("CURRENT_MODULE", module);
    			map.put("CURRENT_ACTION", action);
    			
    			System.out.println("module = "+module+", action="+action);
    			
	        	if(module.equalsIgnoreCase("account")) 
	        	{
	        		if(action.equalsIgnoreCase("add")) {
	        			speechText = "Please tell me the name of the account you want to create.";
	        		}
	        		else if(action.equalsIgnoreCase("edit")) {
	        			speechText = "Please tell me the name of the account followd by the new name.";
	        		}
	        		else if(action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the name of the account you want to delete.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for account.";
	        	}
	        	else if(module.equalsIgnoreCase("category")) 
	        	{
	        		if(action.equalsIgnoreCase("add")) {
	        			speechText = "Please tell me the category name you want to create.";
	        		}
	        		else if(action.equalsIgnoreCase("edit")) {
	        			speechText = "Please tell me the category name followed by the new name.";
	        		}
	        		else if(action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the category name you want to delete.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for category.";
	        	}
	        	else if(module.equalsIgnoreCase("subcategory")) 
	        	{
	        		if(action.equalsIgnoreCase("add")) {
	        			speechText = "Please tell me the subcategory name you want to create along with parent category name.";
	        		}
	        		else if(action.equalsIgnoreCase("edit")) {
	        			speechText = "Please tell me the subcategory name followd by the new name along with parent category name.";
	        		}
	        		else if(action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the subcategory name you want to delete along with parent category name.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for sub-category.";
	        	}
	        	else if(module.equalsIgnoreCase("expense")) 
	        	{
	        		if(action.equalsIgnoreCase("add")) {
	        			speechText = "Please tell me the date, amount, category, sub-category and account for expense.";
	        		}
	        		else if(action.equalsIgnoreCase("edit")) {
	        			map.put("CURRENT_SUB_ACTION", "find");
	        			speechText = "Please tell me the category, date and amount of expense so that I can find the expense that you want to update.";
	        		}
	        		else if(action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the category, date and amount of expense so that I can find and delete that expense for you.";
	        		}
	        		else if(action.equalsIgnoreCase("analyze")) {
	        			speechText = "Please tell me the category and time period for which you want to analyse your expenses.";
		        	}
	        		else if (action.equalsIgnoreCase("email")) {
	        			speechText = "Please tell me the time period for which you want the report of your expenses.";
	        		}
	        		else if (action.equalsIgnoreCase("split")) {
	        			speechText = "Please tell me the date, amount, category, account and no of persons included in expense.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for expense.";
	        	}
	        	else
	        		speechText = "Please provide appropriate module like account or category or sub-category or expense.";
	        	
	        	input.getAttributesManager().setSessionAttributes(map);
	        }
	        System.out.println("Sending response to alexa : "+speechText);
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withReprompt(Constants.REPROMPT)
	                .withShouldEndSession(false)
	                .build();
		}
		catch(Exception e) {
			System.out.println("Exception : "+e.getMessage());
			e.printStackTrace();
			return input.getResponseBuilder()
	                .withSpeech(Constants.ERR_MSG)
	                .withReprompt(Constants.ERR_MSG)
	                .withShouldEndSession(false)
	                .build();
		}
	}
}
