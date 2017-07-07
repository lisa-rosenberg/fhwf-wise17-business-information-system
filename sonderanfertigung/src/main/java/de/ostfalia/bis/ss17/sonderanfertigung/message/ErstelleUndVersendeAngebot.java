package de.ostfalia.bis.ss17.sonderanfertigung.message;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;

public class ErstelleUndVersendeAngebot implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(ErstelleUndVersendeAngebot.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Sende Angebot an Kunden");

        final Integer kundeId = (Integer) delegateExecution.getVariable("kundeId");

        /* Angebotserstellung */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmtSelect = conn.prepareStatement(
                "SELECT MAX(ANGEBOTSNR) FROM angebot");
        ResultSet rs = stmtSelect.executeQuery();

        Integer angebotId;
        if (rs.next()) {
            angebotId = rs.getInt(1);
            angebotId++;
        } else {
            angebotId = 1;
        }

        PreparedStatement stmtInsert = conn.prepareStatement(
                "INSERT INTO angebot(ANGEBOTSNR,VERTRIEBSBEREICHNR,STATUS,DATUM,KUNDENNR) VALUES(?,?,?,?,?)");
        stmtInsert.setInt(1, angebotId);
        stmtInsert.setInt(2, 1);
        stmtInsert.setString(3, "offen");
        stmtInsert.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        stmtInsert.setInt(5, kundeId);
        stmtInsert.executeUpdate();
        conn.commit();

        rs.close();
        stmtSelect.close();
        stmtInsert.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("angebotId", angebotId);

        /* Angebotsversand */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("refVertrieb", delegateExecution.getProcessInstanceId());
        messageContent.put("kundeId", delegateExecution.getVariable("kundeId"));
        messageContent.put("angebotId", angebotId);
        messageContent.put("raederId", delegateExecution.getVariable("raederId"));
        messageContent.put("raederBez", delegateExecution.getVariable("raederBez"));
        messageContent.put("raederPreis", delegateExecution.getVariable("raederPreis"));
        messageContent.put("rahmenId", delegateExecution.getVariable("rahmenId"));
        messageContent.put("rahmenBez", delegateExecution.getVariable("rahmenBez"));
        messageContent.put("rahmenPreis", delegateExecution.getVariable("rahmenPreis"));
        messageContent.put("gabelId", delegateExecution.getVariable("gabelId"));
        messageContent.put("gabelBez", delegateExecution.getVariable("gabelBez"));
        messageContent.put("gabelPreis", delegateExecution.getVariable("gabelPreis"));
        messageContent.put("farbeId", delegateExecution.getVariable("farbeId"));
        messageContent.put("farbeBez", delegateExecution.getVariable("farbeBez"));
        messageContent.put("farbePreis", delegateExecution.getVariable("farbePreis"));
        messageContent.put("motorId", delegateExecution.getVariable("motorId"));
        messageContent.put("motorBez", delegateExecution.getVariable("motorBez"));
        messageContent.put("motorPreis", delegateExecution.getVariable("motorPreis"));
        messageContent.put("akkuId", delegateExecution.getVariable("akkuId"));
        messageContent.put("akkuBez", delegateExecution.getVariable("akkuBez"));
        messageContent.put("akkuPreis", delegateExecution.getVariable("akkuPreis"));
        messageContent.put("kleinteileId", delegateExecution.getVariable("kleinteileId"));
        messageContent.put("kleinteileBez", delegateExecution.getVariable("kleinteileBez"));
        messageContent.put("kleinteilePreis", delegateExecution.getVariable("kleinteilePreis"));
        messageContent.put("menge", delegateExecution.getVariable("menge"));
        messageContent.put("personalkostenEinzeln", delegateExecution.getVariable("personalkostenEinzeln"));
        messageContent.put("produktionskostenEinzeln", delegateExecution.getVariable("produktionskostenEinzeln"));
        messageContent.put("preisEinzeln", delegateExecution.getVariable("preisEinzeln"));
        messageContent.put("preisZwischen", delegateExecution.getVariable("preisZwischen"));
        messageContent.put("preisMwSt", delegateExecution.getVariable("preisMwSt"));
        messageContent.put("preisGesamt", delegateExecution.getVariable("preisGesamt"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neues Angebot")
                .processInstanceId((String) delegateExecution.getVariable("refKunde"))
                .setVariables(messageContent)
                .correlateAllWithResult();

    }
}
