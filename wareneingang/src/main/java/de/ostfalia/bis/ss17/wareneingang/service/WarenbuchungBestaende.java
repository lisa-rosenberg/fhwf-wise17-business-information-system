package de.ostfalia.bis.ss17.wareneingang.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarenbuchungBestaende implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(WarenbuchungBestaende.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Buche Warenbuchung (Bestände)");

        // DB
        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);
        
        // Prozessdaten einlesen
        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");
        final Integer menge = (Integer) delegateExecution.getVariable("menge");
        final Integer fachnummer = (Integer) delegateExecution.getVariable("fachnummer");

        // Eintragen:
        // UPDATE bis.lagerfach SET TEIL_TNR = 7001, BESTAND_STUECK = IFNULL(BESTAND_STUECK, 0) + 20 WHERE FACHNR = 7;
        final PreparedStatement stmt = conn.prepareStatement("UPDATE bis.lagerfach SET TEIL_TNR = ?, BESTAND_STUECK = IFNULL(BESTAND_STUECK, 0) + ? WHERE FACHNR = ?");

        stmt.setInt(1, teilId);
        stmt.setInt(2, menge);
        stmt.setInt(3, fachnummer);
        stmt.executeUpdate();
        conn.commit();

        stmt.close();
        conn.close();
    }
}
