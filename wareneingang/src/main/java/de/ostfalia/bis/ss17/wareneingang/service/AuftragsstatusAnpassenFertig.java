package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AuftragsstatusAnpassenFertig implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(AuftragsstatusAnpassenFertig.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Passe Auftragsstatus an");

        final Integer auftragId = (Integer) delegateExecution.getVariable("auftragId");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE beschaffungsauftrag SET fehlmenge = ?, status = ? WHERE id_auftrag = ?");

        stmt.setInt(1, 0);
        stmt.setString(2, "fertig");
        stmt.setInt(3, auftragId);
        stmt.executeUpdate();
        conn.commit();

        stmt.close();
        conn.close();

        delegateExecution.setVariable("mengeAngenommen", delegateExecution.getVariable("mengeBestellt"));
    }
}
