package com.test.edmalexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.google.gson.JsonObject;
import com.test.edmalexa.util.Constants;
import com.test.edmalexa.util.RestUtil;

public class ExpenseValueIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("ExpenseValueIntentHandler"));
	}

	public Optional<Response> handle(HandlerInput input) 
	{
		try 
		{
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	
	        Slot dateSlot = slots.get("date");
	        Slot amountSlot = slots.get("amount");
	        Slot categorySlot = slots.get("category");
	        Slot subCategorySlot = slots.get("subCategory");
	        Slot accountSlot = slots.get("account");
	        String speechText = Constants.ERR_MSG;
	
	        if(dateSlot != null && dateSlot.getResolutions() != null && dateSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH") 
	        		&& amountSlot != null && amountSlot.getResolutions() != null && amountSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
	        		&& categorySlot != null && categorySlot.getResolutions() != null && categorySlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")
					&& accountSlot != null && accountSlot.getResolutions() != null && accountSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        {
	        	String date = dateSlot.getValue();
	        	String amount = amountSlot.getValue();
	        	String category = categorySlot.getValue();
	        	String account = accountSlot.getValue();
	        	String subCategory = "";
	        	if(subCategorySlot != null && subCategorySlot.getResolutions() != null && subCategorySlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		subCategory = subCategorySlot.getValue();
	        	
    			Map<String, Object> map =input.getAttributesManager().getSessionAttributes();
    			String currentModule = String.valueOf(map.get("CURRENT_MODULE"));
    			String currentAction =String.valueOf(map.get("CURRENT_ACTION"));
    			
	        	if(currentModule.equalsIgnoreCase("expense")) 
	        	{
	        		speechText = "date is "+date+", amount is "+amount+" category is "+category+" sub is "+subCategory+ " account is "+account;
	        		/*if(currentAction.equalsIgnoreCase("add") || currentAction.equalsIgnoreCase("insert")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/add?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") || currentAction.equalsIgnoreCase("update") || currentAction.equalsIgnoreCase("change")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/edit?userId=1&oldName="+value+"&newName="+newValue+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("remove") || currentAction.equalsIgnoreCase("delete")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/delete?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}*/
	        		speechText = "Please provide appropriate action for sub-category.";
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
