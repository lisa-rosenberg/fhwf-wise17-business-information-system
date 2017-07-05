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
        final Integer menge = (Integer) delegateExecution.getVariable("menge");

        Boolean teilExistiert = false;
        Boolean lieferantExistiert = false;
        Boolean angabenIo = false;

        /* Angaben prüfen */

        // Anderes Teil existiert
        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM teil WHERE TNR = ?");

        stmt.setInt(1, anderesTeilId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            teilExistiert = true;
            teilId = anderesTeilId;
        } else {
            // Teil existiert (Korrekte Angabe eines anderen Teils ist stärker als Drop-Down-Auswahl)
            stmt.setInt(1, teilId);
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                teilExistiert = true;
            }
        }

        // Lieferant existiert
        stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM lieferant WHERE id_lieferant = ?");

        stmt.setInt(1, lieferantId);
        rs = stmt.executeQuery();


        if (rs.next() && rs.getInt(1) > 0) {
            lieferantExistiert = true;
        }

        // Angaben in Ordnung

        if (lieferantExistiert && teilExistiert) {
            angabenIo = true;
        }

        /* Bestellung speichern (für Demonstrationszwecke) */

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
                "INSERT INTO beschaffungsauftrag(id_auftrag,tnr,menge,id_lieferant,status) VALUES(?,?,?,?,?)");

        stmt.setInt(1, auftragId);
        stmt.setInt(2, teilId);
        stmt.setInt(3, menge);
        stmt.setInt(4, lieferantId);
        stmt.setString(5, "offen");
        stmt.executeUpdate();
        conn.commit();

        rs.close();
        stmt.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("angabenIo", angabenIo);
    }
}
