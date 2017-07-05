package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;

/**
 * LegeArbeitsplanAn
 *
 * @author lisa-rosenberg
 * @since 17/07/04
 */
public class LegeArbeitsplanAn implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(LegeAuftragAn.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Lege Arbeitsplan an");

        final String sonderanfertigungBez = (String) delegateExecution.getVariable("sonderanfertigungBez");
        final Boolean nachbearbeitung = (Boolean) delegateExecution.getVariable("nachbearbeitung");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Lege neues Teil an */

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT MAX(TNR) FROM teil");
        ResultSet rs = stmt.executeQuery();

        Integer teilId;
        if (rs.next() && rs.getInt(1)>10000) {
            teilId = rs.getInt(1) + 1;
        } else {
            teilId = 10001;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO teil(TNR,ART,BEZEICHNUNG,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                        "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?)");

        stmt.setInt(1, teilId);
        stmt.setString(2, "Sonderanfertigung");
        stmt.setString(3, sonderanfertigungBez);
        stmt.setString(4, "ST");
        stmt.setInt(5, 1);
        stmt.setInt(6, 1);
        stmt.setInt(7, 1);
        stmt.setInt(8, 2);
        stmt.executeUpdate();
        conn.commit();

        /* Lege Arbeitsplan an */

        stmt = conn.prepareStatement(
                "SELECT MAX(APLNR) FROM arbeitsplan");
        rs = stmt.executeQuery();

        Integer arbeitsplanId;
        if (rs.next()) {
            arbeitsplanId = rs.getInt(1);
            arbeitsplanId++;
        } else {
            arbeitsplanId = 1;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO arbeitsplan(APLNR,ANGELEGT_USER,ANGELEGT_DATUM,TEIL_TNR) VALUES(?,?,?,?)");

        stmt.setInt(1, arbeitsplanId);
        stmt.setString(2, "Demo");
        stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        stmt.setInt(4, teilId);
        stmt.executeUpdate();
        conn.commit();

        /* Lege Arbeitsgänge und Kostenstellen zum Arbeitsplan an */

        stmt = conn.prepareStatement(
                "INSERT INTO arbeitsplan_ag_ks(APLNR,AGANR,KSTNR,REIHENFOLGEINDEX) VALUES(?,?,?,?)");

        int i = 1;

        //Lackierung
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 2001);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1004);
        stmt.setInt(3, 2001);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        //Rahmenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1001);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1001);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        //Motorenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1002);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1002);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        //Radmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1003);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1003);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();
        i++;

        //Nachbearbeitung
        if (nachbearbeitung) {
            stmt.setInt(1, arbeitsplanId);
            stmt.setInt(2, 1001);
            stmt.setInt(3, 1005);
            stmt.setInt(4, i);
            stmt.executeUpdate();
            conn.commit();
            i++;

            stmt.setInt(1, arbeitsplanId);
            stmt.setInt(2, 1004);
            stmt.setInt(3, 1005);
            stmt.setInt(4, i);
            stmt.executeUpdate();
            conn.commit();
            i++;
        }

        //Qualitätskontrolle
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1003);
        stmt.setInt(3, 1004);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("teilId", teilId);
        delegateExecution.setVariable("arbeitsplanId", arbeitsplanId);
    }

}
