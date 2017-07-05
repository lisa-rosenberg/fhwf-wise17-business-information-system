package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RetourenscheinErstellen implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(RetourenscheinErstellen.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Erstelle Retourenschein");

        final Integer lieferantId = (Integer) delegateExecution.getVariable("lieferantId");
        String lieferantName = null;
        String lieferantAdresse = null;
        String lieferantTel = null;

        /* Suche Lieferanten */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM lieferant WHERE id_lieferant = ?");

        stmt.setInt(1, lieferantId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            lieferantName = rs.getString("name");
            lieferantAdresse = rs.getString("adresse");
            lieferantTel = rs.getString("telefon");
        }

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("lieferantName", lieferantName);
        delegateExecution.setVariable("lieferantAdresse", lieferantAdresse);
        delegateExecution.setVariable("lieferantTel", lieferantTel);
    }
}
