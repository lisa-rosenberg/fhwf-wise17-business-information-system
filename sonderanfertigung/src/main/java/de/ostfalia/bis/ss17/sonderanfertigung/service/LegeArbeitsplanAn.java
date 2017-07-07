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

        final Integer raederId = (Integer) delegateExecution.getVariable("raederId");
        final Integer rahmenId = (Integer) delegateExecution.getVariable("rahmenId");
        final Integer gabelId = (Integer) delegateExecution.getVariable("gabelId");
        final Integer farbeId = (Integer) delegateExecution.getVariable("farbeId");
        final Integer motorId = (Integer) delegateExecution.getVariable("motorId");
        final Integer akkuId = (Integer) delegateExecution.getVariable("akkuId");

        final Float lackPersonalMaterial = ((Double) delegateExecution.getVariable("lackPersonalMaterial")).floatValue();
        final Float lackRuestLackierung = ((Double) delegateExecution.getVariable("lackRuestLackierung")).floatValue();
        final Float lackMaschinenLackierung = ((Double) delegateExecution.getVariable("lackMaschinenLackierung")).floatValue();

        final Float rahmenPersonalMaterial = ((Double) delegateExecution.getVariable("rahmenPersonalMaterial")).floatValue();
        final Float rahmenPersonalMontage = ((Double) delegateExecution.getVariable("rahmenPersonalMontage")).floatValue();

        final Float motorPersonalMaterial = ((Double) delegateExecution.getVariable("motorPersonalMaterial")).floatValue();
        final Float motorPersonalMontage = ((Double) delegateExecution.getVariable("motorPersonalMontage")).floatValue();

        final Float raederPersonalMaterial = ((Double) delegateExecution.getVariable("raederPersonalMaterial")).floatValue();
        final Float raederPersonalMontage = ((Double) delegateExecution.getVariable("raederPersonalMontage")).floatValue();

        final Float qualiPersonalEndkontrolle = ((Double) delegateExecution.getVariable("qualiPersonalEndkontrolle")).floatValue();

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

        /* Lege Stückliste für Teil an */

        stmt = conn.prepareStatement(
                "INSERT INTO stueckliste(TEIL_PRODUKT,TEIL_KOMPONENTE,PRODUKTIONSKOEFFIZIENT,einbaurate) VALUES(?,?,?,?)");

        // Räder
        stmt.setInt(1, teilId);
        stmt.setInt(2, raederId);
        stmt.setInt(3, 2);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        // Rahmen
        stmt.setInt(1, teilId);
        stmt.setInt(2, rahmenId);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        // Gabel
        stmt.setInt(1, teilId);
        stmt.setInt(2, gabelId);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        // Farbe
        stmt.setInt(1, teilId);
        stmt.setInt(2, farbeId);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        // Motor
        stmt.setInt(1, teilId);
        stmt.setInt(2, motorId);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        conn.commit();

        // Akku
        stmt.setInt(1, teilId);
        stmt.setInt(2, akkuId);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);
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

        // Lackierung
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

        // Rahmenmontage
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

        // Motorenmontage
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

        // Rädermontage
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

        // Qualitätskontrolle
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1003);
        stmt.setInt(3, 1004);
        stmt.setInt(4, i);
        stmt.executeUpdate();
        conn.commit();

        /* Lege Bezugsgrößenzuordnung zum Arbeitsplan an */

        stmt = conn.prepareStatement(
                "INSERT INTO bezugsgroessenzuordnung(APLNR,AGANR,KSTNR,BZGRNR,INANSPRUCHNAHME_H) VALUES(?,?,?,?,?)");

        // Lackierung
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 2001);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, lackPersonalMaterial);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1004);
        stmt.setInt(3, 2001);
        stmt.setInt(4, 1422);
        stmt.setFloat(5, lackRuestLackierung);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1004);
        stmt.setInt(3, 2001);
        stmt.setInt(4, 1420);
        stmt.setFloat(5, lackMaschinenLackierung);
        stmt.executeUpdate();
        conn.commit();

        // Rahmenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1001);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, rahmenPersonalMaterial);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1001);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, rahmenPersonalMontage);
        stmt.executeUpdate();
        conn.commit();

        // Motorenmontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1002);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, motorPersonalMaterial);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1002);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, motorPersonalMontage);
        stmt.executeUpdate();
        conn.commit();

        // Rädermontage
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1001);
        stmt.setInt(3, 1003);
        stmt.setFloat(5, raederPersonalMaterial);
        stmt.executeUpdate();
        conn.commit();

        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1002);
        stmt.setInt(3, 1003);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, raederPersonalMontage);
        stmt.executeUpdate();
        conn.commit();

        // Qualitätskontrolle
        stmt.setInt(1, arbeitsplanId);
        stmt.setInt(2, 1003);
        stmt.setInt(3, 1004);
        stmt.setInt(4, 1421);
        stmt.setFloat(5, qualiPersonalEndkontrolle);
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
