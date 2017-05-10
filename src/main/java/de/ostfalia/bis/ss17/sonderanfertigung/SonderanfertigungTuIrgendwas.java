package de.ostfalia.bis.ss17.sonderanfertigung;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonderanfertigungTuIrgendwas implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(SonderanfertigungTuIrgendwas.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("TU IRGENDWAS");
    }
}
