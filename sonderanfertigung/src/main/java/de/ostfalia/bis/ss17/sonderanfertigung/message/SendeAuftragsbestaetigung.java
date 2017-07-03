package de.ostfalia.bis.ss17.sonderanfertigung.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SendeAuftragsbestaetigung implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(SendeAuftragsbestaetigung.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Auftragsbestätigung an EBIKE2020-Vertrieb");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));
        messageContent.put("angebot", delegateExecution.getVariable("angebot"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neue Auftragsbestätigung")
                .processInstanceId((String) delegateExecution.getVariable("refVertrieb"))
                .setVariables(messageContent)
                .correlateAllWithResult();
    }
}
