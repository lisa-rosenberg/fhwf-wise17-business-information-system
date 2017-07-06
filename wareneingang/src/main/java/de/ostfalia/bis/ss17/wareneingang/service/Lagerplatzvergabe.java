package de.ostfalia.bis.ss17.wareneingang.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;

@SuppressWarnings("Duplicates")
public class Lagerplatzvergabe implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(Lagerplatzvergabe.class);
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Vergebe Lagerplatz");

        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Fach ermitteln */

        boolean foundFach = false;
        Integer fachnummer = null;
        Integer gangnummer = null;
        Integer lagerort = null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT FACHNR, GANGNR, LAGERORT_LAGERORTNR FROM bis.lagerfach WHERE TEIL_TNR = ?");
        stmt.setInt(1, teilId);
        ResultSet rs = stmt.executeQuery();

        // Gibt es schon ein Fach mit der Teilnummer?
        if (rs.next()) {
            foundFach = true;
            fachnummer = rs.getInt("FACHNR");
            gangnummer = rs.getInt("GANGNR");
            lagerort = rs.getInt("LAGERORT_LAGERORTNR");
        }

        // Ansonsten erstes leeres Fach!
        if (!foundFach) {
            stmt = conn.prepareStatement(
                    "SELECT FACHNR, GANGNR, LAGERORT_LAGERORTNR FROM bis.lagerfach WHERE TEIL_TNR IS NULL LIMIT 0,1");
            rs = stmt.executeQuery();

            if (rs.next()) {
                foundFach = true;
                fachnummer = rs.getInt("FACHNR");
                gangnummer = rs.getInt("GANGNR");
                lagerort = rs.getInt("LAGERORT_LAGERORTNR");
            }
        }

        rs.close();
        stmt.close();
        conn.close();
        
        if (!foundFach) {
            throw new Exception("Kein Lagerfach gefunden!");
        }

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("fachnummer", fachnummer);
        delegateExecution.setVariable("gangnummer", gangnummer);
        delegateExecution.setVariable("lagerort", lagerort);
    }
}
