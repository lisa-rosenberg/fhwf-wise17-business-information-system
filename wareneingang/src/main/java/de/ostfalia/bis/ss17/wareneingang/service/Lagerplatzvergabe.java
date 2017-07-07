package de.ostfalia.bis.ss17.wareneingang.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;

@SuppressWarnings({"Duplicates", "ConstantConditions"})
public class Lagerplatzvergabe implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(Lagerplatzvergabe.class);
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Vergebe Lagerplatz");

        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");
        Integer menge = (Integer) delegateExecution.getVariable("mengeAngenommen");

        Integer palettenGroesse = null;
        StringBuilder faecher = new StringBuilder();

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Palettengröße für einzulagerndes Teil holen */

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT stueck_pro_pal FROM teil WHERE TNR = ?");
        stmt.setInt(1, teilId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            palettenGroesse = rs.getInt(1);
        }

        /* Fächerbelegung planen */

        // Zuerst Fächer wählen, in denen bereits das Teil liegt
        stmt = conn.prepareStatement(
                "SELECT FACHNR, GANGNR, LAGERORT_LAGERORTNR, BESTAND_STUECK FROM bis.lagerfach WHERE TEIL_TNR = ? AND BESTAND_STUECK < ?");
        stmt.setInt(1, teilId);
        stmt.setInt(2, palettenGroesse);
        rs = stmt.executeQuery();

        while (rs.next()) {
            int fachnummer = rs.getInt(1);
            int gangnummer = rs.getInt(2);
            int lagerort = rs.getInt(3);
            int bestandStueckGelagert = rs.getInt(4);

            int bestandStueckFrei = palettenGroesse - bestandStueckGelagert;
            int bestandStueckZuLagern;

            if (menge >= bestandStueckFrei) {
                bestandStueckZuLagern = bestandStueckFrei;
            } else {
                bestandStueckZuLagern = menge;
            }

             menge = menge - bestandStueckFrei;

            faecher.append("[L")
                    .append(lagerort)
                    .append("-");
            faecher.append("G")
                    .append(gangnummer)
                    .append("-");
            faecher.append("F")
                    .append(fachnummer)
                    .append("-");
            faecher.append("M")
                    .append(bestandStueckZuLagern)
                    .append("]");

            if (menge <= 0) {
                break;
            } else {
                faecher.append(", ");
            }
        }

        // Wenn Fächer mit dem Teil bereits voll sind oder nicht existieren, suche alle freien Fächer
        if (menge > 0) {
            stmt = conn.prepareStatement(
                    "SELECT FACHNR, GANGNR, LAGERORT_LAGERORTNR FROM bis.lagerfach WHERE TEIL_TNR IS NULL");
            rs = stmt.executeQuery();

            while (rs.next()) {
                int fachnummer = rs.getInt(1);
                int gangnummer = rs.getInt(2);
                int lagerort = rs.getInt(3);

                int bestandStueckZuLagern;

                if (menge >= palettenGroesse) {
                    bestandStueckZuLagern = palettenGroesse;
                } else {
                    bestandStueckZuLagern = menge;
                }

                menge = menge - palettenGroesse;

                faecher.append("[L")
                        .append(lagerort)
                        .append("-");
                faecher.append("G")
                        .append(gangnummer)
                        .append("-");
                faecher.append("F")
                        .append(fachnummer)
                        .append("-");
                faecher.append("M")
                        .append(bestandStueckZuLagern)
                        .append("]");

                if (menge <= 0) {
                    break;
                } else if (!rs.isLast()) {
                    faecher.append(", ");
                }
            }
        }

        rs.close();
        stmt.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        if (menge < 0) {
            menge = 0;
        }

        delegateExecution.setVariable("mengeUebrig", menge);
        delegateExecution.setVariable("faecher", faecher.toString());
    }
}
