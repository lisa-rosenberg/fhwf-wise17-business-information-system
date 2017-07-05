package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AngabenPruefen
 *
 * @author lisa-rosenberg
 * @since 17/07/05
 */
public class AngabenPruefen implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(AngabenPruefen.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

    }
}
