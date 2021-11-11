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
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	
	        Slot actionSlot = slots.get("action");
	        Slot moduleSlot = slots.get("module");
	        String speechText = "";
	
	        if(moduleSlot != null && moduleSlot.getResolutions() != null && moduleSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH") 
	        		&& actionSlot != null && actionSlot.getResolutions() != null && actionSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        {
	        	String module = moduleSlot.getValue();
	        	String action = actionSlot.getValue();
	        	
	        	Map<String, Object> map = new HashMap<String, Object>();
    			map.put("CURRENT_MODULE", module);
    			map.put("CURRENT_ACTION", action);
    			input.getAttributesManager().setSessionAttributes(map);
    			
	        	if(module.equalsIgnoreCase("account") || module.equalsIgnoreCase("payment method")) 
	        	{
	        		if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("insert")) {
	        			/*JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name=abcdefg");
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();*/
	        			speechText = "Please tell me the name of the account you want to create.";
	        		}
	        		else if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("change")) {
	        			speechText = "Please tell me the name of the account followd by the new name.";
	        		}
	        		else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the name of the account you want to delete.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for account.";
	        	}
	        	else if(module.equalsIgnoreCase("category") || module.equalsIgnoreCase("primary category")) 
	        	{
	        		if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("insert")) {
	        			/*JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name=abcdefg");
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();*/
	        			speechText = "Please tell me the category name you want to create.";
	        		}
	        		else if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("change")) {
	        			speechText = "Please tell me the category name followed by the new name.";
	        		}
	        		else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the category name you want to delete.";
	        		}
	        		else
	        			speechText = "Please provide appropriate action for category.";
	        	}
	        	else if(module.equalsIgnoreCase("sub-category") || module.equalsIgnoreCase("sub category") || module.equalsIgnoreCase("subcategory")) 
	        	{
	        		if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("insert")) {
	        			/*JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name=abcdefg");
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();*/
	        			speechText = "Please tell me the category name you want to create.";
	        		}
	        		else if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("change")) {
	        			speechText = "Please tell me the category name followd by the new name.";
	        		}
	        		else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("delete")) {
	        			speechText = "Please tell me the category name you want to delete.";
	        		}
	        		speechText = "Please provide appropriate action for sub-category.";
	        	}
	        	else if(module.equalsIgnoreCase("expense")) 
	        	{
	        		if(action.equalsIgnoreCase("add") || action.equalsIgnoreCase("insert")) {
	        			/*JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name=abcdefg");
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();*/
	        			speechText = "Please tell me the date, amount, category, sub-category and account for expense.";
	        		}
	        		else if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("change")) {
	        			speechText = "Sorry, edit not vailable for now.";
	        		}
	        		else if(action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("delete")) {
	        			speechText = "Sorry, delete not vailable for now.";
	        		}
	        		else if(action.equalsIgnoreCase("analyze")) {
	        			speechText = "Sorry, analyze not vailable for now.";
	        		}
	        		speechText = "Please provide appropriate action for expense.";
	        	}
	        	else
	        		speechText = "Please provide appropriate module like account or category or sub-category or expense.";
	        }
	        return input.getResponseBuilder()
	                .withSimpleCard("Expenditure Manager", speechText)
	                .withSpeech(speechText)
	                .withReprompt(Constants.REPROMPT)
	                .withShouldEndSession(false)
	                .build();
		}
		catch(Exception e) {
			return input.getResponseBuilder()
	                .withSimpleCard("Expenditure Manager", Constants.ERR_MSG)
	                .withSpeech(Constants.ERR_MSG)
	                .withReprompt(Constants.ERR_MSG)
	                .withShouldEndSession(false)
	                .build();
		}
	}
}
