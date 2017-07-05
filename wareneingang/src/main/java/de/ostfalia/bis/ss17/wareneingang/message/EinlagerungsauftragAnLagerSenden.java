package de.ostfalia.bis.ss17.wareneingang.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class EinlagerungsauftragAnLagerSenden implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(EinlagerungsauftragAnLagerSenden.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Einlagerungsauftrag an Lager (MessageStartEvent)");

        final Integer auftragId = (Integer) delegateExecution.getVariable("auftragId");
        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");
        final Integer mengeGeliefert = (Integer) delegateExecution.getVariable("mengeGeliefert");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("teilId", teilId);
        messageContent.put("menge", mengeGeliefert);
        
        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neue Einlagerung")
                .processInstanceId("" + auftragId)
                .setVariables(messageContent)
                .correlateAllWithResult();
    }
}
