package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("Duplicates")
public class FuehreProduktkalkulationAus implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(FuehreProduktkalkulationAus.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Führe Produktkalkulation aus");

        // TODO Float to Double
        final String raeder = (String) delegateExecution.getVariable("raeder");
        final Integer raederTNR = (Integer) delegateExecution.getVariable("raederTNR");
        final Float raederPreis = ((Double) delegateExecution.getVariable("raederPreis")).floatValue();
        final Integer raederDS = (Integer) delegateExecution.getVariable("raederDS");
        final Integer raederSPP = (Integer) delegateExecution.getVariable("raederSPP");
        final Float raederGesamtpreis;

        final String rahmen = (String) delegateExecution.getVariable("rahmen");
        final Integer rahmenTNR = (Integer) delegateExecution.getVariable("rahmenTNR");
        final Float rahmenPreis = ((Double) delegateExecution.getVariable("rahmenPreis")).floatValue();
        final Integer rahmenDS = (Integer) delegateExecution.getVariable("rahmenDS");
        final Integer rahmenSPP = (Integer) delegateExecution.getVariable("rahmenDS");
        final Float rahmenGesamtpreis;

        final String gabel = (String) delegateExecution.getVariable("gabel");
        final Integer gabelTNR = (Integer) delegateExecution.getVariable("gabelTNR");
        final Float gabelPreis = ((Double) delegateExecution.getVariable("gabelPreis")).floatValue();
        final Integer gabelDS = (Integer) delegateExecution.getVariable("gabelDS");
        final Integer gabelSPP = (Integer) delegateExecution.getVariable("gabelDS");
        final Float gabelGesamtpreis;

        final String farbe = (String) delegateExecution.getVariable("farbe");
        final Integer farbeTNR = (Integer) delegateExecution.getVariable("farbeTNR");
        final Float farbePreis = ((Double) delegateExecution.getVariable("farbePreis")).floatValue();
        final Integer farbeDS = (Integer) delegateExecution.getVariable("farbeDS");
        final Integer farbeSPP = (Integer) delegateExecution.getVariable("farbeSPP");
        final Float farbeGesamtpreis;

        final String motor = (String) delegateExecution.getVariable("motor");
        final Integer motorTNR = (Integer) delegateExecution.getVariable("motorTNR");
        final Float motorPreis = ((Double) delegateExecution.getVariable("motorPreis")).floatValue();
        final Integer motorDS = (Integer) delegateExecution.getVariable("motorDS");
        final Integer motorSPP = (Integer) delegateExecution.getVariable("motorSPP");
        final Float motorGesamtpreis;

        final String akku = (String) delegateExecution.getVariable("akku");
        final Integer akkuTNR = (Integer) delegateExecution.getVariable("akkuTNR");
        final Float akkuPreis = ((Double) delegateExecution.getVariable("akkuPreis")).floatValue();
        final Integer akkuDS = (Integer) delegateExecution.getVariable("akkuDS");
        final Integer akkuSPP = (Integer) delegateExecution.getVariable("akkuSPP");
        final Float akkuGesamtpreis;

        final String kleinteile = "Kleinteile";
        final Float kleinteilePreis;
        final Float kleinteileGesamtpreis;

        final Integer menge = (Integer) delegateExecution.getVariable("menge");

        final Double preisEinzeln;
        final Double preisZwischen;
        final Double preisMwSt;
        final Double preisGesamt;

        /* Schreibe fehlende Teile in die Datenbank */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/BIS", "root", "mysql");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM teil WHERE TNR = ?");

        // Räder
        preparedStatement.setInt(1, raederTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, raederTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, raeder);
            preparedStatement.setFloat(4, raederPreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, raederDS);
            preparedStatement.setInt(9, raederSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

        // Rahmen
        preparedStatement.setInt(1, rahmenTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, rahmenTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, rahmen);
            preparedStatement.setFloat(4, rahmenPreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, rahmenDS);
            preparedStatement.setInt(9, rahmenSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

        // Gabel
        preparedStatement.setInt(1, gabelTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, gabelTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, gabel);
            preparedStatement.setFloat(4, gabelPreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, gabelDS);
            preparedStatement.setInt(9, gabelSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

        // Farbe
        preparedStatement.setInt(1, farbeTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, farbeTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, farbe);
            preparedStatement.setFloat(4, farbePreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, farbeDS);
            preparedStatement.setInt(9, farbeSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

        // Motor
        preparedStatement.setInt(1, motorTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, motorTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, motor);
            preparedStatement.setFloat(4, motorPreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, motorDS);
            preparedStatement.setInt(9, motorSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

        // Akku
        preparedStatement.setInt(1, akkuTNR);

        if (preparedStatement.executeQuery().getLong(1) == 0) {
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teil(TNR,ART,BEZEICHNUNG,STANDARDPREIS,BASISMENGENEINHEIT,PRODGRUPPENNR," +
                            "SPARTENR,DISPOSITIONSSTUFE,stueck_pro_pal) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, akkuTNR);
            preparedStatement.setString(2, "Rohstoff");
            preparedStatement.setString(3, akku);
            preparedStatement.setFloat(4, akkuPreis);
            preparedStatement.setString(5, "ST");
            preparedStatement.setInt(6, 1);
            preparedStatement.setInt(7, 1);
            preparedStatement.setInt(8, akkuDS);
            preparedStatement.setInt(9, akkuSPP);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
        connection.commit();

        /* Führe Produktkalkulation durch */

        raederGesamtpreis = menge * raederPreis;
        rahmenGesamtpreis = menge * rahmenPreis;
        gabelGesamtpreis = menge * gabelPreis;
        farbeGesamtpreis = menge * farbePreis;
        motorGesamtpreis = menge * motorPreis;
        akkuGesamtpreis = menge * akkuPreis;

        preparedStatement = connection.prepareStatement(
                "SELECT STANDARDPREIS FROM teil WHERE TNR = 6001");
        kleinteilePreis = preparedStatement.executeQuery().getFloat(1);
        kleinteileGesamtpreis = menge * kleinteilePreis;
        connection.close();

        preisEinzeln = (double) (raederPreis + rahmenPreis + gabelPreis + farbePreis + motorPreis + akkuPreis + kleinteilePreis);
        preisZwischen = (double) (raederGesamtpreis + rahmenGesamtpreis + gabelGesamtpreis + farbeGesamtpreis + motorGesamtpreis
                + akkuGesamtpreis + kleinteileGesamtpreis);
        preisMwSt = preisZwischen * 0.19;
        preisGesamt = preisZwischen + preisMwSt;

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("raederGesamtpreis", raederGesamtpreis);
        delegateExecution.setVariable("rahmenGesamtpreis", rahmenGesamtpreis);
        delegateExecution.setVariable("gabelGesamtpreis", gabelGesamtpreis);
        delegateExecution.setVariable("farbeGesamtpreis", farbeGesamtpreis);
        delegateExecution.setVariable("motorGesamtpreis", motorGesamtpreis);
        delegateExecution.setVariable("akkuGesamtpreis", akkuGesamtpreis);
        delegateExecution.setVariable("kleinteile", kleinteile);
        delegateExecution.setVariable("kleinteilePreis", kleinteilePreis);
        delegateExecution.setVariable("kleinteileGesamtpreis", kleinteileGesamtpreis);
        delegateExecution.setVariable("preisEinzeln", preisEinzeln);
        delegateExecution.setVariable("preisEinzeln", preisZwischen);
        delegateExecution.setVariable("preisGesamt", preisMwSt);
        delegateExecution.setVariable("preisGesamt", preisGesamt);
    }
}
