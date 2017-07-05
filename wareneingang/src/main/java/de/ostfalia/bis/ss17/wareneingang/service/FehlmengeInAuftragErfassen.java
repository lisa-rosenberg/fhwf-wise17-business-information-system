package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FehlmengeInAuftragErfassen implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(FehlmengeInAuftragErfassen.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Erfasse Fehlmenge im Auftrag");

        final Integer auftragId = (Integer) delegateExecution.getVariable("auftragId");
        final Integer mengeBestellt = (Integer) delegateExecution.getVariable("mengeBestellt");
        final Integer mengeGeliefert = (Integer) delegateExecution.getVariable("mengeGeliefert");

        Integer mengeFehlend = mengeBestellt - mengeGeliefert;

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE beschaffungsauftrag SET fehlmenge = ? WHERE id_auftrag = ?");

        stmt.setInt(1, mengeFehlend);
        stmt.setInt(2, auftragId);
        conn.commit();

        stmt.close();
        conn.close();
    }
}
