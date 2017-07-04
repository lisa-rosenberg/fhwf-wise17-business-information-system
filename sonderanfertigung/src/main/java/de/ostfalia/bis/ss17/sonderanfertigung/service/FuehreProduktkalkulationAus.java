package de.ostfalia.bis.ss17.sonderanfertigung.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings({"Duplicates", "ConstantConditions"})
public class FuehreProduktkalkulationAus implements JavaDelegate {

    private final static Logger logger = LoggerFactory.getLogger(FuehreProduktkalkulationAus.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Führe Produktkalkulation aus");

        final Float raederPreis = ((Double) delegateExecution.getVariable("raederPreis")).floatValue();
        final Float raederGesamtpreis;

        final Float rahmenPreis = ((Double) delegateExecution.getVariable("rahmenPreis")).floatValue();
        final Float rahmenGesamtpreis;

        final Float gabelPreis = ((Double) delegateExecution.getVariable("gabelPreis")).floatValue();
        final Float gabelGesamtpreis;

        final Float farbePreis = ((Double) delegateExecution.getVariable("farbePreis")).floatValue();
        final Float farbeGesamtpreis;

        final Float motorPreis = ((Double) delegateExecution.getVariable("motorPreis")).floatValue();
        final Float motorGesamtpreis;

        final Float akkuPreis = ((Double) delegateExecution.getVariable("akkuPreis")).floatValue();
        final Float akkuGesamtpreis;

        final String kleinteileBez = "Kleinteile";
        Double kleinteilePreis = null;
        Double kleinteileGesamtpreis;

        final Integer menge = (Integer) delegateExecution.getVariable("menge");

        Double preisEinzeln;
        Double preisZwischen;
        Double preisMwSt;
        Double preisGesamt;

        /* Hole fehlende Daten */

        Class.forName("com.mysql.jdbc.Driver");
        final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/bis", "root", "mysql");

        PreparedStatement stmtSelect = conn.prepareStatement(
                "SELECT STANDARDPREIS FROM teil WHERE TNR = 6001");
        ResultSet rs = stmtSelect.executeQuery();

        if (rs.next()) {
            kleinteilePreis = (double) (rs.getFloat(1));
        }

        /* Führe Produktkalkulation durch */

        raederGesamtpreis = menge * raederPreis;
        rahmenGesamtpreis = menge * rahmenPreis;
        gabelGesamtpreis = menge * gabelPreis;
        farbeGesamtpreis = menge * farbePreis;
        motorGesamtpreis = menge * motorPreis;
        akkuGesamtpreis = menge * akkuPreis;
        kleinteileGesamtpreis = menge * kleinteilePreis;

        preisEinzeln = (double) (raederPreis + rahmenPreis + gabelPreis + farbePreis + motorPreis + akkuPreis + kleinteilePreis);
        preisZwischen = (double) (raederGesamtpreis + rahmenGesamtpreis + gabelGesamtpreis + farbeGesamtpreis + motorGesamtpreis + akkuGesamtpreis + kleinteileGesamtpreis);
        preisMwSt = preisZwischen * 0.19;
        preisGesamt = preisZwischen + preisMwSt;

        rs.close();
        stmtSelect.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("raederGesamtpreis", raederGesamtpreis);
        delegateExecution.setVariable("rahmenGesamtpreis", rahmenGesamtpreis);
        delegateExecution.setVariable("gabelGesamtpreis", gabelGesamtpreis);
        delegateExecution.setVariable("farbeGesamtpreis", farbeGesamtpreis);
        delegateExecution.setVariable("motorGesamtpreis", motorGesamtpreis);
        delegateExecution.setVariable("akkuGesamtpreis", akkuGesamtpreis);
        delegateExecution.setVariable("kleinteileBez", kleinteileBez);
        delegateExecution.setVariable("kleinteilePreis", kleinteilePreis);
        delegateExecution.setVariable("kleinteileGesamtpreis", kleinteileGesamtpreis);
        delegateExecution.setVariable("preisEinzeln", preisEinzeln);
        delegateExecution.setVariable("preisZwischen", preisZwischen);
        delegateExecution.setVariable("preisMwSt", preisMwSt);
        delegateExecution.setVariable("preisGesamt", preisGesamt);
    }
}
