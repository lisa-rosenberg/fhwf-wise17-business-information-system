package de.ostfalia.bis.ss17.normalebestellung;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NormaleBestellungValidieren implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(NormaleBestellungValidieren.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Validiering von Bestellung Start");

        final Integer bike = (Integer) delegateExecution.getVariable("bike");
        final Integer kunde = (Integer) delegateExecution.getVariable("kunde");

        Class.forName("com.mysql.jdbc.Driver");
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/test", "root", "root");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM teil WHERE TNR = ?");
        preparedStatement.setInt(1, bike);
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean valide = false;
        if (resultSet.next()) {
            valide = resultSet.getLong(1) == 1;
        }

        resultSet.close();
        preparedStatement.close();

        if (valide) {
            preparedStatement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM kunde WHERE ID_Kunde = ?");
            preparedStatement.setInt(1, kunde);
            resultSet = preparedStatement.executeQuery();

            valide = false;
            if (resultSet.next()) {
                valide = resultSet.getLong(1) == 1;
            }

            resultSet.close();
            preparedStatement.close();
        }

        connection.close();

        delegateExecution.setVariable("valide", valide);
    }
}
