package de.ostfalia.bis.ss17.sonderanfertigung.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SendeAbsageAnKunden implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(SendeAbsageAnKunden.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Absage an Kunden");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("kundeId", delegateExecution.getVariable("kundeId"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();

        runtimeService.createMessageCorrelation("Neue Absage")
                .processInstanceId((String) delegateExecution.getVariable("refKunde"))
                .setVariables(messageContent)
                .correlateAllWithResult();
    }
}
