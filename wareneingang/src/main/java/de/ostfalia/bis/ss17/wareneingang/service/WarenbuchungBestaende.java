package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarenbuchungBestaende implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(WarenbuchungBestaende.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Buche Warenbuchung (Bestände)");
    }
}
