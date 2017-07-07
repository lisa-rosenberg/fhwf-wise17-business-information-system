package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class LoescheAngebot implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(LoescheAngebot.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Lösche Angebot");

        final Integer angebotId = (Integer) delegateExecution.getVariable("angebotId");
        final Integer arbeitsplanId = (Integer) delegateExecution.getVariable("arbeitsplanId");
        final Integer teilId = (Integer) delegateExecution.getVariable("teilId");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        /* Angebot löschen */

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM angebot WHERE ANGEBOTSNR = ?");
        stmt.setInt(1, angebotId);
        stmt.executeUpdate();
        conn.commit();

        /* Arbeitsplan mit einzelnen Arbeitsgängen löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM arbeitsplan_ag_ks WHERE APLNR = ?");
        stmt.setInt(1, arbeitsplanId);
        stmt.executeUpdate();
        conn.commit();

         /* Bezugsgrößenzuordnung löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM bezugsgroessenzuordnung WHERE APLNR = ?");
        stmt.setInt(1, arbeitsplanId);
        stmt.executeUpdate();
        conn.commit();

        /* Produktkalkulation löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM kalkulation WHERE APLNR = ?");
        stmt.setInt(1, arbeitsplanId);
        stmt.executeUpdate();
        conn.commit();

        /* Arbeitsplan löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM arbeitsplan WHERE APLNR = ?");
        stmt.setInt(1, arbeitsplanId);
        stmt.executeUpdate();
        conn.commit();

        /* Stueckliste löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM stueckliste WHERE TEIL_PRODUKT = ?");
        stmt.setInt(1, teilId);
        stmt.executeUpdate();
        conn.commit();

        /* Teil für Sonderanfertigung löschen */

        stmt = conn.prepareStatement(
                "DELETE FROM teil WHERE TNR = ?");
        stmt.setInt(1, teilId);
        stmt.executeUpdate();
        conn.commit();

        stmt.close();
        conn.close();
    }
}
