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

        Integer kundeId = (Integer) delegateExecution.getVariable("kundeId");

        Integer raederId = (Integer) delegateExecution.getVariable("raederId");
        String raederBez = null;
        Double raederPreis = null;
        Integer raederDS = null;
        Integer raederSPP = null;

        Integer rahmenId = (Integer) delegateExecution.getVariable("rahmenId");
        String rahmenBez = null;
        Double rahmenPreis = null;
        Integer rahmenDS = null;
        Integer rahmenSPP = null;

        Integer gabelId = (Integer) delegateExecution.getVariable("gabelId");
        String gabelBez = null;
        Double gabelPreis = null;
        Integer gabelDS = null;
        Integer gabelSPP = null;

        Integer farbeId = (Integer) delegateExecution.getVariable("farbeId");
        String farbeBez = null;
        Double farbePreis = null;
        Integer farbeDS = null;
        Integer farbeSPP = null;

        Integer motorId = (Integer) delegateExecution.getVariable("motorId");
        String motorBez = null;
        Double motorPreis = null;
        Integer motorDS = null;
        Integer motorSPP = null;

        Integer akkuId = (Integer) delegateExecution.getVariable("akkuId");
        String akkuBez = null;
        Double akkuPreis = null;
        Integer akkuDS = null;
        Integer akkuSPP = null;

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");

        /* Suche des Kunden */

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM kunde WHERE ID_Kunde = ?");

        stmt.setInt(1, kundeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new Exception("Kunde existiert nicht in der Datenbank.");
        }

        /* Suche der in der Datenbank bereits vorhandenen Teile */

        stmt = conn.prepareStatement(
                "SELECT * FROM teil WHERE TNR = ?");

        // Räder
        stmt.setInt(1, raederId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            raederBez = rs.getString("BEZEICHNUNG");
            raederPreis = (double) (rs.getFloat("STANDARDPREIS"));
            raederDS = rs.getInt("DISPOSITIONSSTUFE");
            raederSPP = rs.getInt("stueck_pro_pal");
        } else {
            raederId = null;
        }

        // Rahmen
        stmt.setInt(1, rahmenId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            rahmenBez = rs.getString("BEZEICHNUNG");
            rahmenPreis = (double) (rs.getFloat("STANDARDPREIS"));
            rahmenDS = rs.getInt("DISPOSITIONSSTUFE");
            rahmenSPP = rs.getInt("stueck_pro_pal");
        } else {
            rahmenId = null;
        }

        // Gabel
        stmt.setInt(1, gabelId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            gabelBez = rs.getString("BEZEICHNUNG");
            gabelPreis = (double) (rs.getFloat("STANDARDPREIS"));
            gabelDS = rs.getInt("DISPOSITIONSSTUFE");
            gabelSPP = rs.getInt("stueck_pro_pal");
        } else {
            gabelId = null;
        }

        // Farbe
        stmt.setInt(1, farbeId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            farbeBez = rs.getString("BEZEICHNUNG");
            farbePreis = (double) (rs.getFloat("STANDARDPREIS"));
            farbeDS = rs.getInt("DISPOSITIONSSTUFE");
            farbeSPP = rs.getInt("stueck_pro_pal");
        } else {
            farbeId = null;
        }

        // Motor
        stmt.setInt(1, motorId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            motorBez = rs.getString("BEZEICHNUNG");
            motorPreis = (double) (rs.getFloat("STANDARDPREIS"));
            motorDS = rs.getInt("DISPOSITIONSSTUFE");
            motorSPP = rs.getInt("stueck_pro_pal");
        } else {
            motorId = null;
        }

        // Akku
        stmt.setInt(1, akkuId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            akkuBez = rs.getString("BEZEICHNUNG");
            akkuPreis = (double) (rs.getFloat("STANDARDPREIS"));
            akkuDS = rs.getInt("DISPOSITIONSSTUFE");
            akkuSPP = rs.getInt("stueck_pro_pal");
        } else {
            akkuId = null;
        }

        rs.close();
        stmt.close();
        conn.close();

        /* Absenden der Sonderanfertigungsanfrage */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("refKunde", delegateExecution.getProcessInstanceId());
        messageContent.put("kundeId", delegateExecution.getVariable("kundeId"));
        messageContent.put("raederId", raederId);
        messageContent.put("raederBez", raederBez);
        messageContent.put("raederPreis", raederPreis);
        messageContent.put("raederDS", raederDS);
        messageContent.put("raederSPP", raederSPP);
        messageContent.put("rahmenId", rahmenId);
        messageContent.put("rahmenBez", rahmenBez);
        messageContent.put("rahmenPreis", rahmenPreis);
        messageContent.put("rahmenDS", rahmenDS);
        messageContent.put("rahmenSPP", rahmenSPP);
        messageContent.put("gabelId", gabelId);
        messageContent.put("gabelBez", gabelBez);
        messageContent.put("gabelPreis", gabelPreis);
        messageContent.put("gabelDS", gabelDS);
        messageContent.put("gabelSPP", gabelSPP);
        messageContent.put("farbeId", farbeId);
        messageContent.put("farbeBez", farbeBez);
        messageContent.put("farbePreis", farbePreis);
        messageContent.put("farbeDS", farbeDS);
        messageContent.put("farbeSPP", farbeSPP);
        messageContent.put("motorId", motorId);
        messageContent.put("motorBez", motorBez);
        messageContent.put("motorPreis", motorPreis);
        messageContent.put("motorDS", motorDS);
        messageContent.put("motorSPP", motorSPP);
        messageContent.put("akkuId", akkuId);
        messageContent.put("akkuBez", akkuBez);
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
