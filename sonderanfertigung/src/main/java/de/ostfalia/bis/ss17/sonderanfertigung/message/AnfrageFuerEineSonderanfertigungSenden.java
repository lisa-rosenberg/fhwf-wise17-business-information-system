package de.ostfalia.bis.ss17.sonderanfertigung.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class AnfrageFuerEineSonderanfertigungSenden implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(AnfrageFuerEineSonderanfertigungSenden.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Anfrage für Sonderanfertigung an EBIKE2020Vertrieb");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));
        messageContent.put("raeder", delegateExecution.getVariable("raeder"));
        messageContent.put("rahmen", delegateExecution.getVariable("rahmen"));
        messageContent.put("gabel", delegateExecution.getVariable("gabel"));
        messageContent.put("farbe", delegateExecution.getVariable("farbe"));
        messageContent.put("motor", delegateExecution.getVariable("motor"));
        messageContent.put("akku", delegateExecution.getVariable("akku"));
        messageContent.put("sonder", delegateExecution.getVariable("sonder"));
        messageContent.put("menge", delegateExecution.getVariable("menge"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.startProcessInstanceByMessage("Neue Sonderanfertigungsanfrage", messageContent);
    }
}
