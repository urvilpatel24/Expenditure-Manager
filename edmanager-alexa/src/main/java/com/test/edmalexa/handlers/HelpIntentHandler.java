package com.test.edmalexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.gson.JsonArray;
import com.test.edmalexa.util.Constants;
import com.test.edmalexa.util.RestUtil;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = "You can choose any of the available actions to continue, currently we support actions like Add Expense, Add Category, Add Account, Analyze Expense, Email Report.";
        String repromptText = "Please choose the action by saying I want to x y z.";
        return input.getResponseBuilder()
                .withSimpleCard("Expenditure Mnager", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(false)
                .build();
    }
}
