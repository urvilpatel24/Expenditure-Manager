package com.test.edmalexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
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
	        String speechText = "";
	
	        if(amountSlot != null && amountSlot.getResolutions() != null && amountSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        {
	        	String date = dateSlot.getValue();
	        	String amount = amountSlot.getValue();
	        	String category = categorySlot.getValue();
	        	String account = accountSlot.getValue();
	        	String subCategory = "";
	        	
	        	if(subCategorySlot != null && subCategorySlot.getResolutions() != null && subCategorySlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		subCategory = subCategorySlot.getValue();
	        	else
	        		subCategory = "Other";
	        	
	        	if(categorySlot != null && categorySlot.getResolutions() != null && categorySlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		category = categorySlot.getValue();
	        	else
	        		category = "Other";
	        	
	        	if(accountSlot != null && accountSlot.getResolutions() != null && accountSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		account = accountSlot.getValue();
	        	else
	        		account = "Cash";
	        	
	        	if(dateSlot != null && dateSlot.getResolutions() != null && dateSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		date = dateSlot.getValue();
	        	else
	        		date = (new Date()).getTime() + "";
	        	
	        	System.out.println(subCategory+":"+category+":"+account+":"+date+":"+amount);
	        	date = (new Date()).getTime() + "";
	        	
    			Map<String, Object> map =input.getAttributesManager().getSessionAttributes();
    			String currentModule = String.valueOf(map.get("CURRENT_MODULE"));
    			String currentAction = String.valueOf(map.get("CURRENT_ACTION"));
    			String currentSubAction = String.valueOf(map.get("CURRENT_SUB_ACTION"));
    			
    			System.out.println(currentModule+":"+currentAction);
    			
	        	if(currentModule.equalsIgnoreCase("expense")) 
	        	{
	        		if(currentAction.equalsIgnoreCase("add")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/add?userId=1&date="+date+"&subCategory="+subCategory+"&category="+category+"&amount="+amount+"&account="+account);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") && currentSubAction.equalsIgnoreCase("find")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/find?userId=1&date="+date+"&amount="+amount+"&category="+category);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			
	        			map.put("CURRENT_SUB_ACTION", "");
	        			map.put("CURRENT_MODULE", currentModule);
	        			map.put("CURRENT_ACTION", currentAction);
	        			map.put("DATE", date);
	        			map.put("AMOUNT", amount);
	        			map.put("CATEGORY", category);
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") && ( currentSubAction == null || currentSubAction.isEmpty()) ) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/edit?userId=1&date="+String.valueOf(map.get("DATE"))+"&amount="+String.valueOf(map.get("AMOUNT"))+"&category="+String.valueOf(map.get("CATEGORY"))+
	        					"&newDate="+date+"&newAmount="+amount+"&newCategory="+category+"&subCategory="+subCategory+"&account="+account);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			
	        			map.put("CURRENT_SUB_ACTION", "");
	        			map.put("DATE", "");
	        			map.put("AMOUNT", "");
	        			map.put("CATEGORY", "");
	        			map.put("CURRENT_MODULE", currentModule);
	        			map.put("CURRENT_ACTION", currentAction);
	        		}
	        		else if(currentAction.equalsIgnoreCase("delete")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/delete?userId=1&date="+date+"&amount="+amount+"&category="+category);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        	}
	        	else
	        		speechText = "Please provide appropriate module like account or category or expense.";
	        	
	        	input.getAttributesManager().setSessionAttributes(map);
	        }
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withReprompt(Constants.REPROMPT)
	                .withShouldEndSession(false)
	                .build();
		}
		catch(Exception e) {
			return input.getResponseBuilder()
	                .withSpeech(Constants.ERR_MSG)
	                .withReprompt(Constants.ERR_MSG)
	                .withShouldEndSession(false)
	                .build();
		}
	}

}
