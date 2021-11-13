package com.test.edmalexa.handlers;

import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

public class LaunchRequestHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    public Optional<Response> handle(HandlerInput input) {
    	// String userEmail = input.getServiceClientFactory().getUpsService().getProfileEmail();
        String speechText = "Welcome to Expenditure Manager. How can I help you today ?";
        String repromptText = "Please choose any action like Add Expense, Add Category, Analyze Expense etc, How can I help you ?";
        return input.getResponseBuilder()
                .withSimpleCard("Expenditure Manager", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}
