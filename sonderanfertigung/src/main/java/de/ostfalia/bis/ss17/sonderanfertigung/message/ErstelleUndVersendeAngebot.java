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

        /* Angebotserstellung */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        conn.setAutoCommit(false);

        PreparedStatement stmtSelect = conn.prepareStatement(
                "SELECT MAX(ANGEBOTSNR) FROM angebot");
        ResultSet rs = stmtSelect.executeQuery();

        Integer angebot;
        if (rs.next()) {
            angebot = rs.getInt(1);
            angebot++;
        } else {
            angebot = 1;
        }

        PreparedStatement stmtInsert = conn.prepareStatement(
                "INSERT INTO angebot(ANGEBOTSNR,VERTRIEBSBEREICHNR,STATUS,DATUM) VALUES(?,?,?,?)");
        stmtInsert.setInt(1, angebot);
        stmtInsert.setInt(2, 1);
        stmtInsert.setString(3, "offen");
        stmtInsert.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        stmtInsert.executeUpdate();

        conn.commit();
        rs.close();
        stmtSelect.close();
        stmtInsert.close();
        conn.close();

        /* Angebotsversand */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("refVertrieb", delegateExecution.getProcessInstanceId());
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));
        messageContent.put("angebot", angebot);
        messageContent.put("raeder", delegateExecution.getVariable("raeder"));
        messageContent.put("raederTNR", delegateExecution.getVariable("raederTNR"));
        messageContent.put("raederPreis", delegateExecution.getVariable("raederPreis"));
        messageContent.put("raederGesamtpreis", delegateExecution.getVariable("raederGesamtpreis"));
        messageContent.put("rahmen", delegateExecution.getVariable("rahmen"));
        messageContent.put("rahmenTNR", delegateExecution.getVariable("rahmenTNR"));
        messageContent.put("rahmenPreis", delegateExecution.getVariable("rahmenPreis"));
        messageContent.put("rahmenGesamtpreis", delegateExecution.getVariable("rahmenGesamtpreis"));
        messageContent.put("gabel", delegateExecution.getVariable("gabel"));
        messageContent.put("gabelTNR", delegateExecution.getVariable("gabelTNR"));
        messageContent.put("gabelPreis", delegateExecution.getVariable("gabelPreis"));
        messageContent.put("gabelGesamtpreis", delegateExecution.getVariable("gabelGesamtpreis"));
        messageContent.put("farbe", delegateExecution.getVariable("farbe"));
        messageContent.put("farbeTNR", delegateExecution.getVariable("farbeTNR"));
        messageContent.put("farbePreis", delegateExecution.getVariable("farbePreis"));
        messageContent.put("farbeGesamtpreis", delegateExecution.getVariable("farbeGesamtpreis"));
        messageContent.put("motor", delegateExecution.getVariable("motor"));
        messageContent.put("motorTNR", delegateExecution.getVariable("motorTNR"));
        messageContent.put("motorPreis", delegateExecution.getVariable("motorPreis"));
        messageContent.put("motorGesamtpreis", delegateExecution.getVariable("motorGesamtpreis"));
        messageContent.put("akku", delegateExecution.getVariable("akku"));
        messageContent.put("akkuTNR", delegateExecution.getVariable("akkuTNR"));
        messageContent.put("akkuPreis", delegateExecution.getVariable("akkuPreis"));
        messageContent.put("akkuGesamtpreis", delegateExecution.getVariable("akkuGesamtpreis"));
        messageContent.put("kleinteile", delegateExecution.getVariable("kleinteile"));
        messageContent.put("kleinteileTNR", delegateExecution.getVariable("kleinteileTNR"));
        messageContent.put("kleinteilePreis", delegateExecution.getVariable("kleinteilePreis"));
        messageContent.put("kleinteileGesamtpreis", delegateExecution.getVariable("kleinteileGesamtpreis"));
        messageContent.put("menge", delegateExecution.getVariable("menge"));
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
