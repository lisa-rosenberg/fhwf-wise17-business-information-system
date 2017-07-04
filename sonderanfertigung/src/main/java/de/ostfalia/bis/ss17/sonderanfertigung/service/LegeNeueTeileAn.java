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

        final String raeder = (String) delegateExecution.getVariable("raeder");
        final Integer raederTNR = (Integer) delegateExecution.getVariable("raederTNR");
        final Float raederPreis = ((Double) delegateExecution.getVariable("raederPreis")).floatValue();
        final Integer raederDS = (Integer) delegateExecution.getVariable("raederDS");
        final Integer raederSPP = (Integer) delegateExecution.getVariable("raederSPP");

        final String rahmen = (String) delegateExecution.getVariable("rahmen");
        final Integer rahmenTNR = (Integer) delegateExecution.getVariable("rahmenTNR");
        final Float rahmenPreis = ((Double) delegateExecution.getVariable("rahmenPreis")).floatValue();
        final Integer rahmenDS = (Integer) delegateExecution.getVariable("rahmenDS");
        final Integer rahmenSPP = (Integer) delegateExecution.getVariable("rahmenDS");

        final String gabel = (String) delegateExecution.getVariable("gabel");
        final Integer gabelTNR = (Integer) delegateExecution.getVariable("gabelTNR");
        final Float gabelPreis = ((Double) delegateExecution.getVariable("gabelPreis")).floatValue();
        final Integer gabelDS = (Integer) delegateExecution.getVariable("gabelDS");
        final Integer gabelSPP = (Integer) delegateExecution.getVariable("gabelDS");

        final String farbe = (String) delegateExecution.getVariable("farbe");
        final Integer farbeTNR = (Integer) delegateExecution.getVariable("farbeTNR");
        final Float farbePreis = ((Double) delegateExecution.getVariable("farbePreis")).floatValue();
        final Integer farbeDS = (Integer) delegateExecution.getVariable("farbeDS");
        final Integer farbeSPP = (Integer) delegateExecution.getVariable("farbeSPP");

        final String motor = (String) delegateExecution.getVariable("motor");
        final Integer motorTNR = (Integer) delegateExecution.getVariable("motorTNR");
        final Float motorPreis = ((Double) delegateExecution.getVariable("motorPreis")).floatValue();
        final Integer motorDS = (Integer) delegateExecution.getVariable("motorDS");
        final Integer motorSPP = (Integer) delegateExecution.getVariable("motorSPP");

        final String akku = (String) delegateExecution.getVariable("akku");
        final Integer akkuTNR = (Integer) delegateExecution.getVariable("akkuTNR");
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
        stmtSelect.setInt(1, raederTNR);
        ResultSet rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, raederTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, raeder);
            stmtInsert.setFloat(4, raederPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, raederDS);
            stmtInsert.setInt(9, raederSPP);
            stmtInsert.executeUpdate();
        }

        // Rahmen
        stmtSelect.setInt(1, rahmenTNR);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, rahmenTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, rahmen);
            stmtInsert.setFloat(4, rahmenPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, rahmenDS);
            stmtInsert.setInt(9, rahmenSPP);
            stmtInsert.executeUpdate();
        }

        // Gabel
        stmtSelect.setInt(1, gabelTNR);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, gabelTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, gabel);
            stmtInsert.setFloat(4, gabelPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, gabelDS);
            stmtInsert.setInt(9, gabelSPP);
            stmtInsert.executeUpdate();
        }

        // Farbe
        stmtSelect.setInt(1, farbeTNR);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, farbeTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, farbe);
            stmtInsert.setFloat(4, farbePreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, farbeDS);
            stmtInsert.setInt(9, farbeSPP);
            stmtInsert.executeUpdate();
        }

        // Motor
        stmtSelect.setInt(1, motorTNR);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, motorTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, motor);
            stmtInsert.setFloat(4, motorPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, motorDS);
            stmtInsert.setInt(9, motorSPP);
            stmtInsert.executeUpdate();
        }

        // Akku
        stmtSelect.setInt(1, akkuTNR);
        rs = stmtSelect.executeQuery();

        if (rs.next() && rs.getLong(1) == 0) {
            stmtInsert.setInt(1, akkuTNR);
            stmtInsert.setString(2, "Rohstoff");
            stmtInsert.setString(3, akku);
            stmtInsert.setFloat(4, akkuPreis);
            stmtInsert.setString(5, "ST");
            stmtInsert.setInt(6, 1);
            stmtInsert.setInt(7, 1);
            stmtInsert.setInt(8, akkuDS);
            stmtInsert.setInt(9, akkuSPP);
            stmtInsert.executeUpdate();
        }

        rs.close();
        stmtSelect.close();
        stmtInsert.close();
        conn.close();

    }
}
