package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * LegeNeueTeileAn
 *
 * @author lisa-rosenberg
 * @since 17/07/04
 */
@SuppressWarnings("Duplicates")
public class LegeNeueTeileAn implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(LegeNeueTeileAn.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        logger.info("Führe Produktkalkulation aus");

        final Integer raederId = (Integer) delegateExecution.getVariable("raederId");
        final String raederBez = (String) delegateExecution.getVariable("raederBez");
        final Float raederPreis = ((Double) delegateExecution.getVariable("raederPreis")).floatValue();
        final Integer raederDS = (Integer) delegateExecution.getVariable("raederDS");
        final Integer raederSPP = (Integer) delegateExecution.getVariable("raederSPP");

        final Integer rahmenId = (Integer) delegateExecution.getVariable("rahmenId");
        final String rahmenBez = (String) delegateExecution.getVariable("rahmenBez");
        final Float rahmenPreis = ((Double) delegateExecution.getVariable("rahmenPreis")).floatValue();
        final Integer rahmenDS = (Integer) delegateExecution.getVariable("rahmenDS");
        final Integer rahmenSPP = (Integer) delegateExecution.getVariable("rahmenDS");

        final Integer gabelId = (Integer) delegateExecution.getVariable("gabelId");
        final String gabelBez = (String) delegateExecution.getVariable("gabelBez");
        final Float gabelPreis = ((Double) delegateExecution.getVariable("gabelPreis")).floatValue();
        final Integer gabelDS = (Integer) delegateExecution.getVariable("gabelDS");
        final Integer gabelSPP = (Integer) delegateExecution.getVariable("gabelDS");

        final Integer farbeId = (Integer) delegateExecution.getVariable("farbeId");
        final String farbeBez = (String) delegateExecution.getVariable("farbeBez");
        final Float farbePreis = ((Double) delegateExecution.getVariable("farbePreis")).floatValue();
        final Integer farbeDS = (Integer) delegateExecution.getVariable("farbeDS");
        final Integer farbeSPP = (Integer) delegateExecution.getVariable("farbeSPP");

        final Integer motorId = (Integer) delegateExecution.getVariable("motorId");
        final String motorBez = (String) delegateExecution.getVariable("motorBez");
        final Float motorPreis = ((Double) delegateExecution.getVariable("motorPreis")).floatValue();
        final Integer motorDS = (Integer) delegateExecution.getVariable("motorDS");
        final Integer motorSPP = (Integer) delegateExecution.getVariable("motorSPP");

        final Integer akkuId = (Integer) delegateExecution.getVariable("akkuId");
        final String akkuBez = (String) delegateExecution.getVariable("akkuBez");
        final Float akkuPreis = ((Double) delegateExecution.getVariable("akkuPreis")).floatValue();
        final Integer akkuDS = (Integer) delegateExecution.getVariable("akkuDS");
        final Integer akkuSPP = (Integer) delegateExecution.getVariable("akkuSPP");

        /* Schreibe fehlende Teile in die Datenbank */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmtSelect = conn.prepareStatement(
                "SELECT COUNT(*) FROM teil WHERE TNR = ?");

        PreparedStatement stmtInsert = conn.prepareStatement(
                "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                        "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");

        // Räder
        stmtSelect.setInt(1, raederId);
        ResultSet rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, raederId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, raederBez);
            stmtInsert.setFloat(4, raederPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, raederDS);
            stmtInsert.setInt(9, raederSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        // Rahmen
        stmtSelect.setInt(1, rahmenId);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, rahmenId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, rahmenBez);
            stmtInsert.setFloat(4, rahmenPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, rahmenDS);
            stmtInsert.setInt(9, rahmenSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        // Gabel
        stmtSelect.setInt(1, gabelId);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, gabelId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, gabelBez);
            stmtInsert.setFloat(4, gabelPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, gabelDS);
            stmtInsert.setInt(9, gabelSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        // Farbe
        stmtSelect.setInt(1, farbeId);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, farbeId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, farbeBez);
            stmtInsert.setFloat(4, farbePreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, farbeDS);
            stmtInsert.setInt(9, farbeSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        // Motor
        stmtSelect.setInt(1, motorId);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, motorId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, motorBez);
            stmtInsert.setFloat(4, motorPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, motorDS);
            stmtInsert.setInt(9, motorSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        // Akku
        stmtSelect.setInt(1, akkuId);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, akkuId);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, akkuBez);
            stmtInsert.setFloat(4, akkuPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, akkuDS);
            stmtInsert.setInt(9, akkuSPP);
            stmtInsert.executeUpdate();
            conn.commit();
        }

        rs.close();
        stmtSelect.close();
        stmtInsert.close();
        conn.close();

    }
}
