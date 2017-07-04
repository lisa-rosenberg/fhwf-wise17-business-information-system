package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class LoescheAngebot implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(LoescheAngebot.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Lösche Angebot");

        //TODO Löschen von dem angelegten Sonderanfertigungs-Teil und Arbeitsplan
        //TODO Löschen des angelegten Auftrages funktioniert noch nicht?

        final Integer angebotId = (Integer) delegateExecution.getVariable("angebotId");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM angebot WHERE ANGEBOTSNR = ?");

        stmt.setInt(1, angebotId);
        stmt.executeQuery();
        conn.commit();

        stmt.close();
        conn.close();
    }
}
