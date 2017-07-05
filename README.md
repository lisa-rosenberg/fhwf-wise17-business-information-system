Business IS - Gruppe 4
======================

Reference
---------

#### Aufgabe
- Thema:        `Kundenerfassungsprozess` + `Wareneingang`
- Abgabe:       `07. Juli 2017`
- Präsentation: `06. Juli 2017, 16:00 Uhr`

#### Rahmenbedingungen
- Tools:    `Java SE 8, Maven 3, Camunda Modeler, Camunda BPMN 2.0`


Progress
--------

#### Wareneingang
- [x] BPMN anpassen
    - [x] Pool-Einstellungen setzen
    - [x] Java-Klassen bei Service- und Message-Tasks/-Throw Events hinterlegen
    - [x] Namen bei Abzweigungen nach allen Gateways wählen
    - [x] Expressions/Variablen bei Abzweigungen nach Exclusive Gateways wählen
    - [x] Assignees bei User Tasks wählen
        - [ ] (optional) Group Assignees
    - [x] Form Keys (html-Links) bei User Tasks eingeben
    - [x] Messages bei Message-Tasks/-Throw Events und ggf. Message Start Events wählen
- [x] HTML anpassen
    - [x] (optional) Java-Script einbetten
- [x] Java-Implementation
    - [x] Logger-Infos anpassen
    - [x] messageContent und startProcessInstanceByMessage bei User-Task-Klassen anpassen


#### Kundenerfassungsprozess
- [x] BPMN anpassen
    - [x] Pool-Einstellungen setzen
    - [x] Java-Klassen bei Service- und Message-Tasks/-Throw Events hinterlegen
    - [x] Namen bei Abzweigungen nach Gateways wählen
    - [x] Expressions/Variablen bei Abzweigungen nach Exclusive Gateways wählen
    - [x] Assignees bei User Tasks wählen
        - [ ] (optional) Group Assignees
    - [x] Form Keys (html-Links) bei User Tasks eingeben
    - [x] Messages bei Message-Tasks/-Throw Events und ggf. Message Start Events wählen
    - [x] (optional) BPMN weiter anpassen, passende Tasks zwischenschalten
- [x] HTML anpassen
    - [ ] (optional) JavaScript einbetten
- [x] Java-Implementation
    - [x] Logger-Infos anpassen
    - [x] messageContent und startProcessInstanceByMessage bei User-Task-Klassen anpassen
    - [x] (optional) Bei Task "Anfrage für eine Sonderanfertigung senden" DB-Conn und Teileinfos heraussuchen


### Cheat Sheet
- Message Start Event:

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.startProcessInstanceByMessage("Neue Bestellung", messageContent);

- Message Throw Event / Message Send Task:

        final RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.correlateMessage(xxx);

- Mehrere Process Instances:

        delegateExecution.getProcessInstanceId();
        
- Message Start Event:
    - Mit leerer Message starten, wenn keine Verbindung zu einem Message Throw Event oder Message Send Task besteht.
        
        
### Questions

- [x] Prozess_Sonderanfertigung.bpmn: Welche Zeiteinheit soll 200 sein? Bis Klärung als Stunden formatiert.
- [x] erstelle_arbeitsplan_fuer_die_sonderanfertigung.html: Was soll hier genau passieren?
- [ ] Kleinteile pro Fahrrad - ein oder zwei Mal? Stückliste-PDF suggeriert doppelte Einberechnung.