package de.ostfalia.bis.ss17.wareneingang.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;

public class Lagerplatzvergabe implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(Lagerplatzvergabe.class);
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Vergebe Lagerplatz");
        
        // DB
        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);
        
        // Prozessdaten einlesen
        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");

        // Fach ermitteln
        boolean foundFach = false;
        Integer fachnummer = null;
        Integer gangnummer = null;
        final PreparedStatement stmt = conn.prepareStatement("SELECT FACHNR, GANGNR FROM bis.lagerfach WHERE TEIL_TNR = ?");
        stmt.setInt(1, teilId);
        final ResultSet rs = stmt.executeQuery();

        // Es gibt schon ein Fach mit der Teilnummer?
        // SELECT * FROM bis.lagerfach WHERE TEIL_TNR = 7001;
        if (rs.next()) {
        	foundFach = true;
            // Daten auslesen
            fachnummer = rs.getInt("FACHNR");
            gangnummer = rs.getInt("GANGNR");
        }
        stmt.close();

        // Ansonsten erstes leeres Fach!
        // SELECT * FROM bis.lagerfach WHERE TEIL_TNR IS NULL LIMIT 0,1;
        if (!foundFach) {
            final PreparedStatement stmt2 = conn.prepareStatement("SELECT FACHNR, GANGNR FROM bis.lagerfach WHERE TEIL_TNR IS NULL LIMIT 0,1");
            final ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
            	foundFach = true;
                // Daten auslesen
                fachnummer = rs2.getInt("FACHNR");
                gangnummer = rs2.getInt("GANGNR");
            }
            stmt2.close();
        }
        
        conn.close();
        
        if (!foundFach) {
            throw new Exception("Kein Lagerfach gefunden!");
        }

        // Gebe Ergebnisse weiter
        delegateExecution.setVariable("fachnummer", fachnummer);
        delegateExecution.setVariable("gangnummer", gangnummer);
    }
}
