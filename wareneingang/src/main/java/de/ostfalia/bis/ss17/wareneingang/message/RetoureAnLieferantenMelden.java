package de.ostfalia.bis.ss17.wareneingang.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class RetoureAnLieferantenMelden implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(RetoureAnLieferantenMelden.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Retourenmeldung an Lieferant");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("lieferantId", delegateExecution.getVariable("lieferantId"));
        messageContent.put("teilId", delegateExecution.getVariable("teilId"));
        messageContent.put("mengeBestellt", delegateExecution.getVariable("mengeBestellt"));
        messageContent.put("mengeGeliefert", delegateExecution.getVariable("mengeGeliefert"));
        messageContent.put("mengeFehlend", delegateExecution.getVariable("mengeFehlend"));
        
        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neue Retourenmeldung")
                .setVariables(messageContent)
                .correlateAllWithResult();
    }
}
