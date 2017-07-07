package de.ostfalia.bis.ss17.wareneingang.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * AngabenPruefen
 *
 * @author lisa-rosenberg
 * @since 17/07/05
 */
public class AngabenPruefen implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(AngabenPruefen.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        logger.info("Prüfe Angaben");

        final Integer lieferantId = (Integer) delegateExecution.getVariable("lieferantId");
        Integer teilId = (Integer) delegateExecution.getVariable("teilId");
        final Integer anderesTeilId = (Integer) delegateExecution.getVariable("anderesTeilId");
        final Integer mengeBestellt = (Integer) delegateExecution.getVariable("mengeBestellt");
        String teilBez;

        /* Angaben prüfen */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM teil WHERE TNR = ?");

        // Existiert das angegebene Nicht-Standard-Teil?
        if (anderesTeilId != null) {
            stmt.setInt(1, anderesTeilId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                teilId = anderesTeilId;
            } else {
                throw new Exception("Teil existiert nicht in der Datenbank.");
            }
        }

        // Hole Daten des Teils
        stmt = conn.prepareStatement(
                "SELECT BEZEICHNUNG FROM teil WHERE TNR = ?");

        stmt.setInt(1, teilId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            teilBez = rs.getString(1);
        } else {
            throw new Exception("Teil existiert nicht in der Datenbank.");
        }

        // Existiert der angegebene Lieferant?
        stmt = conn.prepareStatement(
                "SELECT * FROM lieferant WHERE id_lieferant = ?");

        stmt.setInt(1, lieferantId);
        rs = stmt.executeQuery();

        if (!rs.next()) {
            throw new Exception("Lieferant existiert nicht in der Datenbank.");
        }

        // Bietet der Lieferant das Teil an?
        stmt = conn.prepareStatement(
                "SELECT * FROM lieferant_teile WHERE id_lieferant = ? AND tnr = ?");

        stmt.setInt(1, lieferantId);
        stmt.setInt(2, teilId);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new Exception("Lieferant bietet Teil nicht an.");
        }

        // Passt Mindestbestellmenge?
        stmt = conn.prepareStatement(
                "SELECT * FROM lieferant_teile WHERE id_lieferant = ? AND tnr = ? AND mindest_bestellmenge_pal <= ?");

        stmt.setInt(1, lieferantId);
        stmt.setInt(2, teilId);
        stmt.setInt(3, mengeBestellt);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new Exception("Mindestbestellmenge passt nicht.");
        }

        /* Bestellung speichern (für Demonstrationszwecke, eigentlich wäre sie zu dem Zeitpunkt bereits vorhanden) */

        // Neuen Beschaffungsauftrag anlegen
        stmt = conn.prepareStatement(
                "SELECT MAX(id_auftrag) FROM beschaffungsauftrag");
        rs = stmt.executeQuery();

        Integer auftragId;
        if (rs.next()) {
            auftragId = rs.getInt(1);
            auftragId++;
        } else {
            auftragId = 1;
        }

        stmt = conn.prepareStatement(
                "INSERT INTO beschaffungsauftrag(id_auftrag,tnr,menge,id_lieferant,status,fehlmenge) VALUES(?,?,?,?,?,?)");

        stmt.setInt(1, auftragId);
        stmt.setInt(2, teilId);
        stmt.setInt(3, mengeBestellt);
        stmt.setInt(4, lieferantId);
        stmt.setString(5, "offen");
        stmt.setInt(6, mengeBestellt);
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("auftragId", auftragId);
        delegateExecution.setVariable("teilBez", teilBez);
    }
}
