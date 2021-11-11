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

public class ModuleValueIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("ModuleValueIntentHandler"));
	}

	public Optional<Response> handle(HandlerInput input) 
	{
		try 
		{
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	
	        Slot moduleSlot = slots.get("module");
	        Slot valueSlot = slots.get("value");
	        Slot newValueSlot = slots.get("newValue");
	        Slot parentCategorySlot = slots.get("parentCategory");
	        String speechText = Constants.ERR_MSG;
	
	        if(moduleSlot != null && moduleSlot.getResolutions() != null && moduleSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH") 
	        		&& valueSlot != null && valueSlot.getResolutions() != null && valueSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        {
	        	String module = moduleSlot.getValue();
	        	String value = valueSlot.getValue();
	        	String newValue = "";
	        	if(newValueSlot != null && newValueSlot.getResolutions() != null && newValueSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		newValue = newValueSlot.getValue();
	        	String parentCategory = "";
	        	if(parentCategorySlot != null && parentCategorySlot.getResolutions() != null && parentCategorySlot.getResolutions().toString().contains("ER_SUCCESS_MATCH"))
	        		parentCategory = parentCategorySlot.getValue();
	        	
    			Map<String, Object> map =input.getAttributesManager().getSessionAttributes();
    			String currentModule = String.valueOf(map.get("CURRENT_MODULE"));
    			String currentAction =String.valueOf(map.get("CURRENT_ACTION"));
    			
	        	if(currentModule.equalsIgnoreCase(module) && (currentModule.equalsIgnoreCase("account") || currentModule.equalsIgnoreCase("payment method"))) 
	        	{
	        		if(currentAction.equalsIgnoreCase("add") || currentAction.equalsIgnoreCase("insert")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") || currentAction.equalsIgnoreCase("update") || currentAction.equalsIgnoreCase("change")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/edit?userId=1&oldName="+value+"&newName="+newValue);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("remove") || currentAction.equalsIgnoreCase("delete")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/delete?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else
	        			speechText = "Please provide appropriate action for account.";
	        	}
	        	else if(currentModule.equalsIgnoreCase(module) && (currentModule.equalsIgnoreCase("category") || currentModule.equalsIgnoreCase("primary category"))) 
	        	{
	        		if(currentAction.equalsIgnoreCase("add") || currentAction.equalsIgnoreCase("insert")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/add?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") || currentAction.equalsIgnoreCase("update") || currentAction.equalsIgnoreCase("change")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/edit?userId=1&oldName="+value+"&newName="+newValue);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("remove") || currentAction.equalsIgnoreCase("delete")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/delete?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else
	        			speechText = "Please provide appropriate action for category.";
	        	}
	        	else if(currentModule.equalsIgnoreCase(module) && (currentModule.equalsIgnoreCase("sub-category") || currentModule.equalsIgnoreCase("sub category") || currentModule.equalsIgnoreCase("subcategory"))) 
	        	{
	        		if(currentAction.equalsIgnoreCase("add") || currentAction.equalsIgnoreCase("insert")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/add?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") || currentAction.equalsIgnoreCase("update") || currentAction.equalsIgnoreCase("change")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/edit?userId=1&oldName="+value+"&newName="+newValue+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
	        		else if(currentAction.equalsIgnoreCase("remove") || currentAction.equalsIgnoreCase("delete")) {
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/delete?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString().equalsIgnoreCase(Constants.SUCCESS))
	        				speechText = res.get(Constants.MESSAGE).toString();
	        		}
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
