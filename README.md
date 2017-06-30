Business IS - Gruppe 4
======================

Reference
---------

#### Aufgabe
- Thema:        `Kundenerfassungsprozess` + `Wareneingang`
- Abgabe        `07. Juli 2017`

#### Rahmenbedingungen
- Tools:    `Java SE 8, Maven 3, Camunda Modeler, Camunda BPMN 2.0`


Progress
--------

#### Wareneingang
- [ ] BPMN anpassen
    - [x] Pool-Einstellungen setzen
    - [x] Java-Klassen bei Service- und Message-Tasks/-Throw Events hinterlegen
    - [x] Namen bei Abzweigungen nach allen Gateways wählen
    - [ ] Expressions/Variablen bei Abzweigungen nach Exclusive Gateways wählen
    - [ ] Assignees bei User Tasks wählen
    - [x] Form Keys (html-Links) bei User Tasks eingeben
    - [ ] Messages bei Message-Tasks/-Throw Events und ggf. Message Start Events wählen
- [ ] forms anpassen
- [ ] Logger-Infos anpassen
- [ ] messageContent und startProcessInstanceByMessage bei User-Task-Klassen anpassen


#### Kundenerfassungsprozess
- [ ] BPMN anpassen
    - [x] Pool-Einstellungen setzen
    - [x] Java-Klassen bei Service- und Message-Tasks/-Throw Events hinterlegen
    - [ ] Namen bei Abzweigungen nach Gateways wählen
    - [ ] Expressions/Variablen bei Abzweigungen nach Exclusive Gateways wählen
    - [ ] Assignees bei User Tasks wählen
    - [x] Form Keys (html-Links) bei User Tasks eingeben
    - [ ] Messages bei Message-Tasks/-Throw Events und ggf. Message Start Events wählen
- [ ] forms anpassen
- [ ] Logger-Infos anpassen
- [ ] messageContent und startProcessInstanceByMessage bei User-Task-Klassen anpassen

### Bug-Tracking
- BUG-1: x
     - `x`


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
 
   
### Notes

- Process_Wareneingang:
    - bei Wareneingangskontrolle müssen zwei Felder bei der Form gesetzt werden:
        - Menge
        - IO/NIO (in Ordnung/nicht in Ordnung)
        
### Questions

- Prozess_Sonderanfertigung.bpmn: Welche Zeiteinheit soll 200 sein? Bis Klärung als Stunden formatiert.