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
        logger.info("Sende Anfrage für Sonderanfertigung an EBIKE2020Vertrieb");

        String raeder = null;
        final Integer raederTNR = (Integer) delegateExecution.getVariable("raederTNR");
        Double raederPreis = null;
        Integer raederDS = null;
        Integer raederSPP = null;

        String rahmen = null;
        final Integer rahmenTNR = (Integer) delegateExecution.getVariable("rahmenTNR");
        Double rahmenPreis = null;
        Integer rahmenDS = null;
        Integer rahmenSPP = null;

        String gabel = null;
        final Integer gabelTNR = (Integer) delegateExecution.getVariable("gabelTNR");
        Double gabelPreis = null;
        Integer gabelDS = null;
        Integer gabelSPP = null;

        String farbe = null;
        final Integer farbeTNR = (Integer) delegateExecution.getVariable("farbeTNR");
        Double farbePreis = null;
        Integer farbeDS = null;
        Integer farbeSPP = null;

        String motor = null;
        final Integer motorTNR = (Integer) delegateExecution.getVariable("motorTNR");
        Double motorPreis = null;
        Integer motorDS = null;
        Integer motorSPP = null;

        String akku = null;
        final Integer akkuTNR = (Integer) delegateExecution.getVariable("akkuTNR");
        Double akkuPreis = null;
        Integer akkuDS = null;
        Integer akkuSPP = null;

        /* Teilesuche */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/BIS", "root", "mysql");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM teil WHERE TNR = ?");

        // Räder
        preparedStatement.setInt(1, raederTNR);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            raeder = resultSet.getString("BEZEICHNUNG");
            raederPreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            raederDS = resultSet.getInt("DISPOSITIONSSTUFE");
            raederSPP = resultSet.getInt("stueck_pro_pal");
        }

        // Rahmen
        preparedStatement.setInt(1, rahmenTNR);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            rahmen = resultSet.getString("BEZEICHNUNG");
            rahmenPreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            rahmenDS = resultSet.getInt("DISPOSITIONSSTUFE");
            rahmenSPP = resultSet.getInt("stueck_pro_pal");
        }

        // Gabel
        preparedStatement.setInt(1, gabelTNR);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            gabel = resultSet.getString("BEZEICHNUNG");
            gabelPreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            gabelDS = resultSet.getInt("DISPOSITIONSSTUFE");
            gabelSPP = resultSet.getInt("stueck_pro_pal");
        }

        // Farbe
        preparedStatement.setInt(1, farbeTNR);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            farbe = resultSet.getString("BEZEICHNUNG");
            farbePreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            farbeDS = resultSet.getInt("DISPOSITIONSSTUFE");
            farbeSPP = resultSet.getInt("stueck_pro_pal");
        }

        // Motor
        preparedStatement.setInt(1, motorTNR);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            motor = resultSet.getString("BEZEICHNUNG");
            motorPreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            motorDS = resultSet.getInt("DISPOSITIONSSTUFE");
            motorSPP = resultSet.getInt("stueck_pro_pal");
        }

        // Akku
        preparedStatement.setInt(1, akkuTNR);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            akku = resultSet.getString("BEZEICHNUNG");
            akkuPreis = (double) (resultSet.getFloat("STANDARDPREIS"));
            akkuDS = resultSet.getInt("DISPOSITIONSSTUFE");
            akkuSPP = resultSet.getInt("stueck_pro_pal");
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        /* Weitergabe der Informationen */

        final HashMap<String, Object> messageContent = new HashMap<>();
        messageContent.put("prozess", delegateExecution.getProcessInstanceId());
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
