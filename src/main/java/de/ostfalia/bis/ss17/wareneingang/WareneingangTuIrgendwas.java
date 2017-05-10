package de.ostfalia.bis.ss17.wareneingang;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WareneingangTuIrgendwas implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(WareneingangTuIrgendwas.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("TU IRGENDWAS");
    }
}
