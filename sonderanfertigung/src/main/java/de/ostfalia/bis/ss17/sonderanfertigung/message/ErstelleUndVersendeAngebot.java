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
        logger.info("Sende Angebot an Kunde (MessageIntermediateCatchEvent)");

        /* Angebotserstellung */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT MAX(ANGEBOTSNR) FROM angebot");
        ResultSet resultSet = preparedStatement.executeQuery();
        final Integer maxAngebotId;
        if (resultSet.next()) {
            maxAngebotId = resultSet.getInt(1);
        } else {
            maxAngebotId = null;
        }

        final int angebotId;
        if (maxAngebotId == null) {
            angebotId = 1;
        } else {
            angebotId = maxAngebotId + 1;
        }

        resultSet.close();
        preparedStatement.close();

        preparedStatement = connection.prepareStatement(
                "INSERT INTO angebot(ANGEBOTSNR,VERTRIEBSBEREICHNR,STATUS,DATUM) VALUES(?,?,?,?)");
        preparedStatement.setInt(1, angebotId);
        preparedStatement.setInt(2, 1);
        preparedStatement.setString(3, "offen");
        preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        preparedStatement.executeUpdate();
        preparedStatement.close();

        /* Angebotsversand */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("kunde", delegateExecution.getVariable("kunde"));
        messageContent.put("angebot", angebotId);
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
        messageContent.put("preisGesamt", delegateExecution.getVariable("PreisGesamt"));

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Neues Angebot")
                .processInstanceId((String) delegateExecution.getVariable("prozess"))
                .setVariables(messageContent)
                .correlateAllWithResult();

    }
}
