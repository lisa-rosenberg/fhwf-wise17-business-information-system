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

        final Integer kundeId = (Integer) delegateExecution.getVariable("kundeId");
        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");
        final Integer arbeitsplanId = (Integer) delegateExecution.getVariable("arbeitsplanId");
        final Integer menge = (Integer) delegateExecution.getVariable("menge");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Lege Kundenbestellung an*/

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT MAX(BESTELLNR) FROM bestellung_vertrieb");
        ResultSet rs = stmt.executeQuery();

        Integer auftragId;
        if (rs.next()) {
            auftragId = rs.getInt(1);
            auftragId++;
        } else {
            auftragId = 1;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO bestellung_vertrieb(BESTELLNR,STATUS,VERTRIEBSBEREICHNR,KUNDENNR) VALUES(?,?,?,?)");
        stmt.setInt(1, auftragId);
        stmt.setString(2, "offen");
        stmt.setInt(3, 1);
        stmt.setInt(4, kundeId);
        stmt.executeUpdate();
        conn.commit();

        /* Lege Fertigungsauftrag an */

        stmt = conn.prepareStatement(
                "SELECT MAX(FAUFTRNR) FROM fertigungsauftrag");
        rs = stmt.executeQuery();

        Integer fertigungId;
        if (rs.next()) {
            fertigungId = rs.getInt(1);
            fertigungId++;
        } else {
            fertigungId = 1;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO fertigungsauftrag(FAUFTRNR,MENGE_STUECK,DATUM_SOLL,STATUS,TEIL_TNR,ARBEITSPLAN_APLNR) VALUES(?,?,?,?,?,?)");
        stmt.setInt(1, fertigungId);
        stmt.setInt(2, menge);
        stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        stmt.setString(4, "offen");
        stmt.setInt(5, teilId);
        stmt.setInt(6, arbeitsplanId);
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("auftragId", auftragId);
        delegateExecution.setVariable("fertigungId", fertigungId);
    }
}
