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
		return input.matches(intentName("ModuleValueIntent"));
	}

	public Optional<Response> handle(HandlerInput input) 
	{
		try 
		{
			System.out.println("Inside ModuleValueIntent ...");
			
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	        
	        System.out.println("Inside ModuleValueIntent 1...");
	        String speechText = "";
	        Slot valueSlot = slots.get("value");
	        
	        if(valueSlot != null && valueSlot.getValue() != null)
	        {
	        	System.out.println("Inside If ...");
	        	
	        	String value = valueSlot.getValue();
		        Slot newValueSlot = slots.get("newValue");
		        Slot parentCategorySlot = slots.get("parentCategory");
	        	String newValue = "";
	        	String parentCategory = "";
	        	
	        	if(newValueSlot != null && newValueSlot.getValue() != null)
	        		newValue = newValueSlot.getValue();
	        	if(parentCategorySlot != null && parentCategorySlot.getValue() != null)
	        		parentCategory = parentCategorySlot.getValue();
	        	
    			Map<String, Object> map =input.getAttributesManager().getSessionAttributes();
    			String currentModule = String.valueOf(map.get("CURRENT_MODULE"));
    			String currentAction = String.valueOf(map.get("CURRENT_ACTION"));

    			System.out.println("value = "+value+", newValue = "+newValue+", parentCategory = "+parentCategory+", currentModule="+currentModule+", currentAction="+currentAction);
    			
	        	if(currentModule.equalsIgnoreCase("account"))
	        	{
	        		System.out.println("Inside account ...");
	        		if(currentAction.equalsIgnoreCase("add")) {
	        			System.out.println("Inside account add ...");
	        			speechText =  currentModule+" "+value+" has been created successfully.";
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/add?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit")) {
	        			System.out.println("Inside account edit ...");
	        			speechText =  currentModule+" "+value+" has been updated successfully.";
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/edit?userId=1&oldName="+value+"&newName="+newValue);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("delete")) {
	        			System.out.println("Inside account delete ...");
	        			speechText =  currentModule+" "+value+" has been deleted successfully.";
	        			JsonObject res = RestUtil.call(Constants.POST, "/account/delete?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else
	        			speechText = "Please provide appropriate action for account.";
	        	}
	        	else if(currentModule.equalsIgnoreCase("category")) 
	        	{
	        		System.out.println("Inside category ...");
	        		if(currentAction.equalsIgnoreCase("add")) {
	        			System.out.println("Inside category add ...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/add?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit")) {
	        			System.out.println("Inside category edit ...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/edit?userId=1&oldName="+value+"&newName="+newValue);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("delete")) {
	        			System.out.println("Inside category delete ...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/category/delete?userId=1&name="+value);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else
	        			speechText = "Please provide appropriate action for category.";
	        	}
	        	else if(currentModule.equalsIgnoreCase("subcategory")) 
	        	{
	        		System.out.println("Inside subcategory ...");
	        		if(currentAction.equalsIgnoreCase("add")) {
	        			System.out.println("Inside subcategory add...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/add?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit")) {
	        			System.out.println("Inside subcategory edit...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/edit?userId=1&oldName="+value+"&newName="+newValue+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else if(currentAction.equalsIgnoreCase("delete")) {
	        			System.out.println("Inside subcategory delete...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/subCategory/delete?userId=1&name="+value+"&parentName="+parentCategory);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        		}
	        		else
	        			speechText = "Please provide appropriate action for sub-category.";
	        	}
	        	else
	        		speechText = "Please provide appropriate module like account or category or sub-category or expense.";
	        
	        	map.put("CURRENT_MODULE", "");
    			map.put("CURRENT_ACTION", "");
    			input.getAttributesManager().setSessionAttributes(map);
	        }
	        System.out.println("sending response to alexa ..."+speechText);
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
