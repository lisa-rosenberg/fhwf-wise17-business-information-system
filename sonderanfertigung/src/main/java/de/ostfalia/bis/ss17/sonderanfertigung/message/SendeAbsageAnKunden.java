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
        logger.info("Sende Absage an Kunde (MessageIntermediateCatchEvent)");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.correlateMessage("Neue Absage", messageContent);
    }
}
