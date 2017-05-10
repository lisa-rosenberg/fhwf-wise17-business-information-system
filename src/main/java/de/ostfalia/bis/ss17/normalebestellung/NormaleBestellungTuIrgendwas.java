package de.ostfalia.bis.ss17.normalebestellung;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormaleBestellungTuIrgendwas implements JavaDelegate {

  private final static Logger logger = LoggerFactory.getLogger(NormaleBestellungTuIrgendwas.class);

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    logger.info("TU IRGENDWAS");
  }
}
