package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lagerplatzvergabe implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(Lagerplatzvergabe.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("TU IRGENDWAS");
    }
}
