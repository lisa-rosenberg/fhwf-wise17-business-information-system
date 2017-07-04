package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class LegeAuftragAn implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(LegeAuftragAn.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Lege Auftrag an");

        final Integer kundeId = (Integer) delegateExecution.getVariable("kunde");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT MAX(BESTELLNR) FROM bestellung_vertrieb");
        ResultSet rs = stmt.executeQuery();

        Integer auftrag;
        if (rs.next()) {
            auftrag = rs.getInt(1);
            auftrag++;
        } else {
            auftrag = 1;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO bestellung_vertrieb(BESTELLNR,STATUS,VERTRIEBSBEREICHNR,KUNDENNR) VALUES(?,?,?,?)");
        stmt.setInt(1, auftrag);
        stmt.setString(2, "offen");
        stmt.setInt(3, 1);
        stmt.setInt(1, kundeId);
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();
    }
}
