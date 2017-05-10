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
        logger.info("Senden von Bestellung Start");

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("bike", delegateExecution.getVariable("bike"));
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.correlateMessage("Neue Bestellung", messageContent);
    }
}
