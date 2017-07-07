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
        final Float rahmenPreis = ((Double) delegateExecution.getVariable("rahmenPreis")).floatValue();
        final Float gabelPreis = ((Double) delegateExecution.getVariable("gabelPreis")).floatValue();
        final Float farbePreis = ((Double) delegateExecution.getVariable("farbePreis")).floatValue();
        final Float motorPreis = ((Double) delegateExecution.getVariable("motorPreis")).floatValue();
        final Float akkuPreis = ((Double) delegateExecution.getVariable("akkuPreis")).floatValue();

        final Float lackPersonalMaterial = ((Double) delegateExecution.getVariable("lackPersonalMaterial")).floatValue();
        final Float lackRuestLackierung = ((Double) delegateExecution.getVariable("lackRuestLackierung")).floatValue();
        final Float lackMaschinenLackierung = ((Double) delegateExecution.getVariable("lackMaschinenLackierung")).floatValue();

        final Float rahmenPersonalMaterial = ((Double) delegateExecution.getVariable("rahmenPersonalMaterial")).floatValue();
        final Float rahmenPersonalMontage = ((Double) delegateExecution.getVariable("rahmenPersonalMontage")).floatValue();

        final Float motorPersonalMaterial = ((Double) delegateExecution.getVariable("motorPersonalMaterial")).floatValue();
        final Float motorPersonalMontage = ((Double) delegateExecution.getVariable("motorPersonalMontage")).floatValue();

        final Float raederPersonalMaterial = ((Double) delegateExecution.getVariable("raederPersonalMaterial")).floatValue();
        final Float raederPersonalMontage = ((Double) delegateExecution.getVariable("raederPersonalMontage")).floatValue();

        final Float qualiPersonalEndkontrolle = ((Double) delegateExecution.getVariable("qualiPersonalEndkontrolle")).floatValue();

        final Float raederGesamtpreis;
        final Float rahmenGesamtpreis;
        final Float gabelGesamtpreis;
        final Float farbeGesamtpreis;
        final Float motorGesamtpreis;
        final Float akkuGesamtpreis;

        final String kleinteileBez = "Kleinteile";
        Double kleinteilePreis = null;
        Double kleinteileGesamtpreis;

        final Integer menge = (Integer) delegateExecution.getVariable("menge");

        final Float personalkosten = ((Double) delegateExecution.getVariable("personalkosten")).floatValue();
        final Float ruestkosten = ((Double) delegateExecution.getVariable("ruestkosten")).floatValue();
        final Float maschinenkosten = ((Double) delegateExecution.getVariable("maschinenkosten")).floatValue();

        final Float personalkostenEinzeln;
        final Float ruestkostenEinzeln;
        final Float maschinenkostenEinzeln;
        final Float produktionskostenEinzeln;

        final Float personalzeitEinzeln;
        final Float ruestzeitEinzeln;
        final Float maschinenzeitEinzeln;

        final Float personalkostenGesamt;
        final Float ruestkostenGesamt;
        final Float maschinenkostenGesamt;
        final Float produktionskostenGesamt;

        final Float personalzeitGesamt;
        final Float ruestzeitGesamt;
        final Float maschinenzeitGesamt;

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

        /* Berechne Zeiten */

        // Personalzeit für ein E-Bike
        personalzeitEinzeln = lackPersonalMaterial +
                rahmenPersonalMaterial + rahmenPersonalMontage +
                motorPersonalMaterial + motorPersonalMontage +
                raederPersonalMaterial + raederPersonalMontage +
                qualiPersonalEndkontrolle;

        // Personalzeit für alle E-Bikes
        personalzeitGesamt = menge * personalzeitEinzeln;

        // Rüstzeit für ein E-Bike
        ruestzeitEinzeln = lackRuestLackierung;

        // Rüstzeit für alle E-Bikes
        ruestzeitGesamt = menge * ruestzeitEinzeln;

        // Maschinenzeit für ein E-Bike
        maschinenzeitEinzeln = lackMaschinenLackierung;

        // Maschinenzeit für alle E-Bikes
        maschinenzeitGesamt = menge * maschinenzeitEinzeln;

        /* Führe Produktkalkulation durch */

        // Materialkosten
        raederGesamtpreis = menge * raederPreis;
        rahmenGesamtpreis = menge * rahmenPreis;
        gabelGesamtpreis = menge * gabelPreis;
        farbeGesamtpreis = menge * farbePreis;
        motorGesamtpreis = menge * motorPreis;
        akkuGesamtpreis = menge * akkuPreis;
        kleinteileGesamtpreis = menge * kleinteilePreis;

        // Personalkosten
        personalkostenEinzeln = personalkosten * personalzeitEinzeln;
        personalkostenGesamt = personalkosten * personalzeitGesamt;

        // Rüstkosten
        ruestkostenEinzeln = ruestkosten * ruestzeitEinzeln;
        ruestkostenGesamt = ruestkosten * ruestzeitGesamt;

        // Maschinenkosten
        maschinenkostenEinzeln = maschinenkosten * maschinenzeitEinzeln;
        maschinenkostenGesamt = maschinenkosten * maschinenzeitGesamt;

        // Produktionskosten
        produktionskostenEinzeln = ruestkostenEinzeln + maschinenkostenEinzeln;
        produktionskostenGesamt = ruestkostenGesamt + maschinenkostenGesamt;

        // Preisberechnung
        preisEinzeln = raederPreis + rahmenPreis + gabelPreis + farbePreis + motorPreis +
                akkuPreis + kleinteilePreis + personalkostenEinzeln + produktionskostenEinzeln;
        preisZwischen = raederGesamtpreis + rahmenGesamtpreis + gabelGesamtpreis + farbeGesamtpreis +
                motorGesamtpreis + akkuGesamtpreis + kleinteileGesamtpreis + personalkostenGesamt + produktionskostenGesamt;
        preisMwSt = preisZwischen * 0.19;
        preisGesamt = preisZwischen + preisMwSt;

        rs.close();
        stmtSelect.close();
        conn.close();

        /* Gebe Ergebnisse weiter */

        delegateExecution.setVariable("personalkostenEinzeln", personalkostenEinzeln);
        delegateExecution.setVariable("produktionskostenEinzeln", produktionskostenEinzeln);
        delegateExecution.setVariable("kleinteileBez", kleinteileBez);
        delegateExecution.setVariable("kleinteilePreis", kleinteilePreis);
        delegateExecution.setVariable("preisEinzeln", preisEinzeln);
        delegateExecution.setVariable("preisZwischen", preisZwischen);
        delegateExecution.setVariable("preisMwSt", preisMwSt);
        delegateExecution.setVariable("preisGesamt", preisGesamt);
    }
}
