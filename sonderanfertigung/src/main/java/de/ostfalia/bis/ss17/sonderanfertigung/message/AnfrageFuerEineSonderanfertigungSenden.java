package de.ostfalia.bis.ss17.sonderanfertigung.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@SuppressWarnings("Duplicates")
public class AnfrageFuerEineSonderanfertigungSenden implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(AnfrageFuerEineSonderanfertigungSenden.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Anfrage für Sonderanfertigung an EBIKE2020-Vertrieb");

        String raeder = null;
        Integer raederTNR = (Integer) delegateExecution.getVariable("raederTNR");
        Double raederPreis = null;
        Integer raederDS = null;
        Integer raederSPP = null;

        String rahmen = null;
        Integer rahmenTNR = (Integer) delegateExecution.getVariable("rahmenTNR");
        Double rahmenPreis = null;
        Integer rahmenDS = null;
        Integer rahmenSPP = null;

        String gabel = null;
        Integer gabelTNR = (Integer) delegateExecution.getVariable("gabelTNR");
        Double gabelPreis = null;
        Integer gabelDS = null;
        Integer gabelSPP = null;

        String farbe = null;
        Integer farbeTNR = (Integer) delegateExecution.getVariable("farbeTNR");
        Double farbePreis = null;
        Integer farbeDS = null;
        Integer farbeSPP = null;

        String motor = null;
        Integer motorTNR = (Integer) delegateExecution.getVariable("motorTNR");
        Double motorPreis = null;
        Integer motorDS = null;
        Integer motorSPP = null;

        String akku = null;
        Integer akkuTNR = (Integer) delegateExecution.getVariable("akkuTNR");
        Double akkuPreis = null;
        Integer akkuDS = null;
        Integer akkuSPP = null;

        /* Suche der in der Datenbank bereits vorhandenen Teile */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM teil WHERE TNR = ?");

        // Räder
        stmt.setInt(1, raederTNR);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            raeder = rs.getString("BEZEICHNUNG");
            raederPreis = (double) (rs.getFloat("STANDARDPREIS"));
            raederDS = rs.getInt("DISPOSITIONSSTUFE");
            raederSPP = rs.getInt("stueck_pro_pal");
        } else {
            raederTNR = null;
        }

        // Rahmen
        stmt.setInt(1, rahmenTNR);
        rs = stmt.executeQuery();
        if (rs.next()) {
            rahmen = rs.getString("BEZEICHNUNG");
            rahmenPreis = (double) (rs.getFloat("STANDARDPREIS"));
            rahmenDS = rs.getInt("DISPOSITIONSSTUFE");
            rahmenSPP = rs.getInt("stueck_pro_pal");
        } else {
            rahmenTNR = null;
        }

        // Gabel
        stmt.setInt(1, gabelTNR);
        rs = stmt.executeQuery();
        if (rs.next()) {
            gabel = rs.getString("BEZEICHNUNG");
            gabelPreis = (double) (rs.getFloat("STANDARDPREIS"));
            gabelDS = rs.getInt("DISPOSITIONSSTUFE");
            gabelSPP = rs.getInt("stueck_pro_pal");
        } else {
            gabelTNR = null;
        }

        // Farbe
        stmt.setInt(1, farbeTNR);
        rs = stmt.executeQuery();
        if (rs.next()) {
            farbe = rs.getString("BEZEICHNUNG");
            farbePreis = (double) (rs.getFloat("STANDARDPREIS"));
            farbeDS = rs.getInt("DISPOSITIONSSTUFE");
            farbeSPP = rs.getInt("stueck_pro_pal");
        } else {
            farbeTNR = null;
        }

        // Motor
        stmt.setInt(1, motorTNR);
        rs = stmt.executeQuery();
        if (rs.next()) {
            motor = rs.getString("BEZEICHNUNG");
            motorPreis = (double) (rs.getFloat("STANDARDPREIS"));
            motorDS = rs.getInt("DISPOSITIONSSTUFE");
            motorSPP = rs.getInt("stueck_pro_pal");
        } else {
            motorTNR = null;
        }

        // Akku
        stmt.setInt(1, akkuTNR);
        rs = stmt.executeQuery();
        if (rs.next()) {
            akku = rs.getString("BEZEICHNUNG");
            akkuPreis = (double) (rs.getFloat("STANDARDPREIS"));
            akkuDS = rs.getInt("DISPOSITIONSSTUFE");
            akkuSPP = rs.getInt("stueck_pro_pal");
        } else {
            akkuTNR = null;
        }

        rs.close();
        stmt.close();
        conn.close();

        /* Absenden der Sonderanfertigungsanfrage */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("refKunde", delegateExecution.getProcessInstanceId());
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));
        messageContent.put("raeder", raeder);
        messageContent.put("raederTNR", delegateExecution.getVariable("raederTNR"));
        messageContent.put("raederPreis", raederPreis);
        messageContent.put("raederDS", raederDS);
        messageContent.put("raederSPP", raederSPP);
        messageContent.put("rahmen", rahmen);
        messageContent.put("rahmenTNR", delegateExecution.getVariable("rahmenTNR"));
        messageContent.put("rahmenPreis", rahmenPreis);
        messageContent.put("rahmenDS", rahmenDS);
        messageContent.put("rahmenSPP", rahmenSPP);
        messageContent.put("gabel", gabel);
        messageContent.put("gabelTNR", delegateExecution.getVariable("gabelTNR"));
        messageContent.put("gabelPreis", gabelPreis);
        messageContent.put("gabelDS", gabelDS);
        messageContent.put("gabelSPP", gabelSPP);
        messageContent.put("farbe", farbe);
        messageContent.put("farbeTNR", delegateExecution.getVariable("farbeTNR"));
        messageContent.put("farbePreis", farbePreis);
        messageContent.put("farbeDS", farbeDS);
        messageContent.put("farbeSPP", farbeSPP);
        messageContent.put("motor", motor);
        messageContent.put("motorTNR", delegateExecution.getVariable("motorTNR"));
        messageContent.put("motorPreis", motorPreis);
        messageContent.put("motorDS", motorDS);
        messageContent.put("motorSPP", motorSPP);
        messageContent.put("akku", akku);
        messageContent.put("akkuTNR", delegateExecution.getVariable("akkuTNR"));
        messageContent.put("akkuPreis", akkuPreis);
        messageContent.put("akkuDS", akkuDS);
        messageContent.put("akkuSPP", akkuSPP);
        messageContent.put("sonder", delegateExecution.getVariable("sonder"));
        messageContent.put("menge", delegateExecution.getVariable("menge"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neue Sonderanfertigungsanfrage")
                .setVariables(messageContent)
                .correlateStartMessage();
    }
}
