package com.test.edmalexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.test.edmalexa.handlers.CancelandStopIntentHandler;
import com.test.edmalexa.handlers.FallbackIntentHandler;
import com.test.edmalexa.handlers.HelpIntentHandler;
import com.test.edmalexa.handlers.LaunchRequestHandler;
import com.test.edmalexa.handlers.ModuleValueIntentHandler;
import com.test.edmalexa.handlers.SelectActionModulecsaIntentHandler;
import com.test.edmalexa.handlers.SessionEndedRequestHandler;

public class EDMStreamHandler extends SkillStreamHandler {

    @SuppressWarnings("unchecked")
	private static Skill getSkill() {
        return Skills.standard().addRequestHandlers(
        				new SelectActionModulecsaIntentHandler(),
        				new ModuleValueIntentHandler(),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler())
        		.build();
    }

    public EDMStreamHandler() {
        super(getSkill());
    }

}
