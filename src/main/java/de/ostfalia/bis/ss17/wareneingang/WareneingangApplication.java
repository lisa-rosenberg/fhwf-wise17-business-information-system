package de.ostfalia.bis.ss17.wareneingang;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;

@ProcessApplication("Normale Bestellung")
public class WareneingangApplication extends ServletProcessApplication {
}
