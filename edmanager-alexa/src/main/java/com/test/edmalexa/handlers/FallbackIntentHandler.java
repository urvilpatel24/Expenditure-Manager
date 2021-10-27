package com.test.edmalexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Sorry, I don't know that. You can try again or you can say help!";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Expenditure Manager", speechText)
                .withReprompt(speechText)
                .build();
    }

}
