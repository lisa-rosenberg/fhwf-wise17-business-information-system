package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuehreProduktkalkulationAus implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(FuehreProduktkalkulationAus.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Führe Produktkalkulation aus");
    }
}
