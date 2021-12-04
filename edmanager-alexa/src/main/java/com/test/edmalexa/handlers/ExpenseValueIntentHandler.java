package com.test.edmalexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.text.SimpleDateFormat;
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
		return input.matches(intentName("ExpenseValueIntent"));
	}

	public Optional<Response> handle(HandlerInput input) 
	{
		try 
		{
			System.out.println("Inside ExpenseValueIntent ...");
			
			Request request = input.getRequestEnvelope().getRequest();
	        IntentRequest intentRequest = (IntentRequest) request;
	        Intent intent = intentRequest.getIntent();
	        Map<String, Slot> slots = intent.getSlots();
	
	        System.out.println("Inside ExpenseValueIntent 1 ...");
	        String speechText = "";
	        Slot dateSlot = slots.get("date");
	        System.out.println("Inside ExpenseValueIntent 2...");
	        if(dateSlot != null && dateSlot.getValue() != null)
	        {
	        	System.out.println("Inside If ...");
	            
		        Slot categorySlot = slots.get("category");
		        Slot subCategorySlot = slots.get("subCategory");
		        Slot accountSlot = slots.get("account");
		        Slot amountSlot = slots.get("amount");
		        
	        	long date = 0L;
	        	String ds = "";
	        	String amount = "";
	        	String category = "";
	        	String account = "";
	        	String subCategory = "";
	        	
	        	if(amountSlot != null && amountSlot.getValue() != null)
	        		amount = amountSlot.getValue();
	        	else
	        		amount = "0";
	        	
	        	if(subCategorySlot != null && subCategorySlot.getValue() != null)
	        		subCategory = subCategorySlot.getValue();
	        	else
	        		subCategory = "Other";
	        	
	        	if(categorySlot != null && categorySlot.getValue() != null)
	        		category = categorySlot.getValue();
	        	else
	        		category = "Other";
	        	
	        	if(accountSlot != null && accountSlot.getValue() != null)
	        		account = accountSlot.getValue();
	        	else
	        		account = "Cash";
	        	
	        	if(dateSlot != null && dateSlot.getValue() != null) {
	        		ds = dateSlot.getValue();
	        		date = this.formatDate(ds);
	        	}
	        	else
	        		date = this.formatDate("");
	        	
	        	System.out.println("subCategory = "+subCategory+", category="+category+", account="+account+", date="+date+", amount="+amount);
	        	
    			Map<String, Object> map =input.getAttributesManager().getSessionAttributes();
    			String currentModule = String.valueOf(map.get("CURRENT_MODULE"));
    			String currentAction = String.valueOf(map.get("CURRENT_ACTION"));
    			String currentSubAction = String.valueOf(map.get("CURRENT_SUB_ACTION"));
    			
    			System.out.println(currentModule+":"+currentAction);
    			
	        	if(currentModule.equalsIgnoreCase("expense")) 
	        	{
	        		System.out.println("Inside expense ...");
	        		if(currentAction.equalsIgnoreCase("add")) {
	        			System.out.println("Inside expense add ...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/add?userId=1&date="+date+"&subCategory="+subCategory+"&category="+category+"&amount="+amount+"&account="+account);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit") && currentSubAction.equalsIgnoreCase("find")) {
	        			System.out.println("Inside expense edit-find...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/find?userId=1&date="+date+"&amount="+amount+"&category="+category);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_SUB_ACTION", "");
	        			map.put("CURRENT_MODULE", currentModule);
	        			map.put("CURRENT_ACTION", currentAction);
	        			map.put("DATE", date);
	        			map.put("AMOUNT", amount);
	        			map.put("CATEGORY", category);
	        		}
	        		else if(currentAction.equalsIgnoreCase("edit")) {
	        			System.out.println("Inside expense edit edit...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/edit?userId=1&date="+String.valueOf(map.get("DATE"))+"&amount="+String.valueOf(map.get("AMOUNT"))+"&category="+String.valueOf(map.get("CATEGORY"))+
	        					"&newDate="+date+"&newAmount="+amount+"&newCategory="+category+"&subCategory="+subCategory+"&account="+account);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_SUB_ACTION", "");
	        			map.put("DATE", "");
	        			map.put("AMOUNT", "");
	        			map.put("CATEGORY", "");
	        			map.put("CURRENT_MODULE", currentModule);
	        			map.put("CURRENT_ACTION", currentAction);
	        		}
	        		else if(currentAction.equalsIgnoreCase("delete")) {
	        			System.out.println("Inside expense delete ...");
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/delete?userId=1&date="+date+"&amount="+amount+"&category="+category);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        		else if(currentAction.equalsIgnoreCase("analyze")) {
	        			System.out.println("Inside expense analyse ...");
	        			long s = 0L;
	        			long e = 0L;
	        			if(ds.equals("2021-11")) {
	        				s = formatDate("2021-11-01");
	        				e = formatDate("2021-11-30");
	        			}
	        			else if (ds.equals("2021-W48")){
	        				s = formatDate("2021-11-29");
	        				e = formatDate("2021-12-05");
	        			}
	        			JsonObject res = RestUtil.call(Constants.GET, "/analyse/byCategoryAndDate?userId=1&startDate="+s+"&endDate="+e);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        		else if(currentAction.equalsIgnoreCase("email")) {
	        			System.out.println("Inside email analyse ...");
	        			long s = 0L;
	        			long e = 0L;
	        			if(ds.equals("2021-11")) {
	        				s = formatDate("2021-11-01");
	        				e = formatDate("2021-11-30");
	        			}
	        			else if (ds.equals("2021-W48")){
	        				s = formatDate("2021-11-29");
	        				e = formatDate("2021-12-05");
	        			}
	        			JsonObject res = RestUtil.call(Constants.GET, "/analyse/email?userId=1&startDate="+s+"&endDate="+e);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        		else if(currentAction.equalsIgnoreCase("split")) {
	        			System.out.println("Inside split expense add ...");
	        			String nop = slots.get("nop").getValue();
	        			System.out.println("nop="+nop);
	        			JsonObject res = RestUtil.call(Constants.POST, "/expense/split?userId=1&date="+date+"&subCategory="+subCategory+"&category="+category+"&amount="+amount+"&account="+account+"&nop="+nop);
	        			if(res.get(Constants.STATUS).toString() != null && !res.get(Constants.STATUS).toString().isEmpty())
	        				speechText = res.get(Constants.MESSAGE).toString();
	        			else
	        				speechText = Constants.ERR_MSG_SERVER;
	        			
	        			map.put("CURRENT_MODULE", "");
	        			map.put("CURRENT_ACTION", "");
	        		}
	        	}
	        	else
	        		speechText = "Please provide appropriate module like account or category or expense.";
	        	
	        	input.getAttributesManager().setSessionAttributes(map);
	        }
	        System.out.println("Sending response to alexa ..."+speechText);
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withReprompt(Constants.REPROMPT)
	                .withShouldEndSession(false)
	                .build();
		}
		catch(Exception e) {
			System.out.println("Exception "+e.getMessage());
			e.printStackTrace();
			return input.getResponseBuilder()
	                .withSpeech(Constants.ERR_MSG)
	                .withReprompt(Constants.ERR_MSG)
	                .withShouldEndSession(false)
	                .build();
		}
	}

	private long formatDate(String date) throws Exception {
		try {
			System.out.println("date = "+date);
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
	        System.out.println(d.toString());
	        return d.getTime();
		}
		catch(Exception e) {
			System.out.println("Setting current date ....");
			Date dt = new Date();
			String s = (dt.getYear()+1900)+"-"+(dt.getMonth()+1)+"-"+dt.getDate();
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(s);
            System.out.println(dt.toString());
            return dt.getTime();
		}
	}
	
	/*private long fmtDate(String date) throws Exception {
		try {
			if(date.equals("2021-11-29"))
				return formatDate("2021-12-05");
			else if(date.equals("2021-11-22"))
				return formatDate("2021-11-28");
			else if(date.equals("2021-11-30"))
				return formatDate("2021-11-01");
			else if(date.equals("2021-W48"))
				
			else return formatDate(date);
		}
		catch(Exception e) {
			return 0L;
		}
	}*/
}
