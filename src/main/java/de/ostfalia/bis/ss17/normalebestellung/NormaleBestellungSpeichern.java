package de.ostfalia.bis.ss17.normalebestellung;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NormaleBestellungSpeichern implements JavaDelegate {

  private final static Logger logger = LoggerFactory.getLogger(NormaleBestellungSpeichern.class);

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    logger.info("Speichern von Bestellung Start");

    final Integer bike = (Integer) delegateExecution.getVariable("bike");
    final Integer kunde = (Integer) delegateExecution.getVariable("kunde");

    Class.forName("com.mysql.jdbc.Driver");
    final Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost/test", "root", "root");
    connection.setAutoCommit(false);

    PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT MAX(BESTELLNR) FROM bestellung_vertrieb");
    ResultSet resultSet = preparedStatement.executeQuery();
    final Integer maxBestellId;
    if (resultSet.next()) {
      maxBestellId = resultSet.getInt(1);
    } else {
      maxBestellId = null;
    }

    final int bestellId;
    if (maxBestellId == null) {
      bestellId = 1;
    } else {
      bestellId = maxBestellId + 1;
    }

    resultSet.close();
    preparedStatement.close();

    preparedStatement = connection.prepareStatement(
        "INSERT INTO bestellung_vertrieb(BESTELLNR,STATUS,VERTRIEBSBEREICHNR, id_kunde) VALUES(?,?,?, ?)");
    preparedStatement.setInt(1, bestellId);
    preparedStatement.setString(2, "offen");
    preparedStatement.setInt(3, 1);
    preparedStatement.setInt(4, kunde);
    preparedStatement.executeUpdate();
    preparedStatement.close();

    preparedStatement = connection.prepareStatement(
        "INSERT INTO bestellposition(BESTELLNR, BESTELLPOSNR, MENGE_STUECK, TNR) VALUES(?,?,?,?)");
    preparedStatement.setInt(1, bestellId);
    preparedStatement.setInt(2, 1);
    preparedStatement.setInt(3, 1);
    preparedStatement.setInt(4, bike);
    preparedStatement.executeUpdate();
    preparedStatement.close();

    connection.commit();
    connection.close();
  }
}
