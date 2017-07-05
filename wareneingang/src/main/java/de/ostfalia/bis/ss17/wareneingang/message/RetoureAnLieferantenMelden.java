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
        logger.info("Sende Retourenmeldung an Lieferant (NoMessageCatchEvent)");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("teilenummer", delegateExecution.getVariable("teilenummer"));
        messageContent.put("menge", delegateExecution.getVariable("menge"));
        messageContent.put("lieferant", delegateExecution.getVariable("lieferant"));
        
        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neue Retourenmeldung")
                .processInstanceId("" + delegateExecution.getVariable("auftragId"))
                .setVariables(messageContent)
                .correlateAllWithResult();
    }
}
