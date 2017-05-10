package de.ostfalia.bis.ss17.sonderanfertigung;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;

@ProcessApplication("Normale Bestellung")
public class SonderanfertigungApplication extends ServletProcessApplication {
}
