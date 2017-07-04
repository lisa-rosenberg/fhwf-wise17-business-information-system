package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

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

        final String sonderanfertigung = (String) delegateExecution.getVariable("sonderanfertigung");
        final Boolean nachbearbeitung = (Boolean) delegateExecution.getVariable("nachbearbeitung");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Lege neues Teil an */

        //TODO Noch wird die teilId 0 gewählt.

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT MIN(t.TNR) AS NextId " +
                        "FROM teil t " +
                        "WHERE NOT EXISTS (SELECT NULL FROM teil n WHERE n.TNR=n.TNR+1 AND n.TNR>10000) " +
                        "AND t.TNR>10000");
        ResultSet rs = stmt.executeQuery();

        Integer teilId = 10000;
        if (rs.next()) {
            teilId = rs.getInt(1);
        }

        stmt = conn.prepareStatement(
                "INSERT INTO teil(TNR,ART,BEZEICHNUNG,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                        "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?)");

        stmt.setInt(1, teilId);
        stmt.setString(2, "Sonderanfertigung");
        stmt.setString(3, sonderanfertigung);
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

        //TODO noch erscheinen keine Datensätze in der Datenbank

        stmt = conn.prepareStatement(
                "INSERT INTO arbeitsplan_ag_ks(APLNR,AGANR,KSTNR,REIHENFOLGEINDEX) VALUES(?,?,?,?)");

        //Lackierung
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 2001);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1004);
        stmt.setInt(3, 2001);
        stmt.setInt(4, 2);
        stmt.executeUpdate();
        conn.commit();

        //Rahmenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1001);
        stmt.setInt(4, 3);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1001);
        stmt.setInt(4, 4);
        stmt.executeUpdate();
        conn.commit();

        //Motorenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1002);
        stmt.setInt(4, 5);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1002);
        stmt.setInt(4, 6);
        stmt.executeUpdate();
        conn.commit();

        //Radmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1003);
        stmt.setInt(4, 7);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1003);
        stmt.setInt(4, 8);
        stmt.executeUpdate();
        conn.commit();

        //Nachbearbeitung
        if (nachbearbeitung) {
            stmt.setInt(1, arbeitsplanId);
            stmt.setInt(2, 1001);
            stmt.setInt(3, 1005);
            stmt.setInt(4, 9);
            stmt.executeUpdate();
            conn.commit();

            stmt.setInt(1, arbeitsplanId);
            stmt.setInt(2, 1004);
            stmt.setInt(3, 1005);
            stmt.setInt(4, 10);
            stmt.executeUpdate();
            conn.commit();
        }

        //Qualitätskontrolle
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1003);
        stmt.setInt(3, 1004);
        stmt.setInt(4, 11);
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();
    }

}
