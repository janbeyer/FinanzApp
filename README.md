# FinanzApp
Projektarbeit Android Finanz Application 

## Einleitung
Viele Anwender wünschen sich nichts anderes als einen besseren Überblick über die eigene Finanzverwaltung. Ist jedem bewusst mit welchen Kosten ein Tag, ein Monat oder ein bzw. mehrere Jahre verbunden sind? Machen sich alle darüber Gedanken für was das eigene Geld eingenommen bzw. ausgegeben wird? Wünscht sich nicht jeder auf einer einfache und übersichtliche Art und Weise seine eigene finanzielle Zukunft zu planen? Was könnte man sich in den nächsten 10 Jahren zurücklegen? In welchem Bereich sollte man mit seinem privaten Vermö-gen sparsamer umgehen?
Um sich selber einen Überblick über die eigenen Finanzen zu machen hilft manchmal der Griff zu einem Taschenrechner. Oft aber sind die Einnahmen und Ausgaben so detailliert bzw. unüberschaubar, sodass der Griff zum Taschenrech-ner schnell zur Überforderung wird, denn man hat nicht so eben mal für das Jahr berechnet, mit welchen Einnahmen und Ausgaben kalkuliert werden kann. Hier machen einem zahlreiche Rechnungen (u.a. auch zyklische Rechnungen) einen Strich durch die händische Berechnung, sodass hier auch mal ein gesamter Tag investiert werden muss. Das ist den meisten – berechtigt – ein zu hoher Aufwand!
Um dem Anwender die eigene Finanzverwaltung angenehmer zu gestalten sind multimediale Anwendungen gesucht, die sich durch eine einfache Bedienung und Pflege leicht erlernen und nutzen lassen.
In einer immer mobiler werdenden Welt werden von Benutzern häufig mobile An-wendungen gesucht, welche einfach zu bedienen sind und ihren Anwendungsfall abdecken.
Um die Pflege und Analyse der eigenen Finanzen zu erleichtern und die finanzielle Zukunft besser planen zu können, wurde die Idee dieser Umsetzung ausgelöst.

## Ist-Zustand
Der User sammelt während seines Alltags Rechnungen und bewahrt diese auf (I1). Zu einem gegebenen Zeitpunkt werden die Rechnungen tabellarisch aufgelistet (I2). Im Anschluss können die Kosten und Einnahmen gegenübergestellt und eine manuelle Analyse der Daten durchgeführt werden.

## Soll-Konzept
Das Soll-Konzept bezeichnet die MUST-HAVE Features.

### Allgemein
Ziel ist die Implementierung einer Android-Applikation zur Analyse und Pflege der eigenen und privaten Finanzverwaltung.
In der Applikation ist es möglicher über eigene Profile (Spielstände) die eigenen Transaktionen zu pflegen, diese einer Gruppe (Kategorie) zuzuweisen und als Re-sultat eine Analyse über einen definierten Zeitraum zu erstellen.

### Voraussetzungen
Die Anwendung wird für alle Android Smartphones und Tablets mit dem Betriebs-systemversion „Oreo“ entwickelt. Für alle anderen Android-Betriebssystemversionen kann zunächst im Prototyp eine einwandfreie Funktionali-tät nicht gewährleistet werden.

### Umfang
Der User kann ein Profil anlegen, welches seine Daten aufnimmt (S1). Es können entweder die vorgegebenen Kategorien verwendet oder neue angelegt werden (S2). Über die Transaktionen kann jede Einnahme bzw. Ausgabe eingepflegt wer-den (S3). Am Monatsende (auch Wochen- oder Jahresende) kann dann eine Ana-lyse der bisherigen Daten erfolgen (S4). 

#### Profile
Über die Applikation ist es möglich unterschiedliche Profile (Spielstände) zu pflegen. Diese lassen sich in einer Ansicht betrachten. In der Profile-Ansicht ist es zusätzlich möglich neue Profile hinzuzufügen und bereits existierende Spielstände zu editieren bzw. zu entfernen. Durch die Auswahl eines jeweiligen Profils kommt der Benutzer in das eigentliche Menu der Anwendung.

#### Hauptmenü
Das Menu listet die Aktionen auf, die für ein Profil (ein Spielstand) vom Benutzer durchge-führt werden können. Über das Menu kommt der Benutzer in weitere Unterdialoge mit den folgenden Funktionen:
* Verwaltung der Transaktionsgruppen
* Verwaltung der Transaktionen
* Analyse

#### Gruppen
In diesem Menu können Transaktionsgruppen gepflegt werden. Eine Menge von Transakti-onsgruppen (z.B. Haushalt, Transportmittel, Versicherungen u.v.m.) werden automatisiert von der Applikation für jedes Profil vorgegeben. Für den Benutzer ist es dennoch möglich Transaktionsgruppen zu verwalten. In der Transaktionsgruppen-Übersicht werden die ak-tuell existierenden Transaktionsgruppen aufgelistet. Der Benutzer hat hier die Möglichkeit neue Transaktionsgruppen hinzufügen und diese zu editieren bzw. löschen.

#### Transaktionen
In diesem Menu ist es möglich unterschiedliche einkommende oder ausgehende Transakti-onen zu pflegen. Eine Transaktion bezeichnet einen Geldeingang oder Geldausgang, der zu einem festgelegten Zeitpunkt in einem festgelegten Zyklus erfolgt. In der Transaktionsan-sicht werden die Transaktionen aufgelistet. Transaktionen lassen sich hinzufügen, editieren und löschen.

#### Analyse
Im Analyse-Menu kann nach der Eingabe eines Startbetrags und eines zeitlichen Intervalls eine Finanzanalyse gestartet werden. Nach der Ausführung dieser Analyse werden dem Benutzer anhand der gepflegten Transaktionen die nach diesem zeitlichen Intervall vor-handen Werte der genutzten Transaktionsgruppen in der Analyseübersicht visuell repräsen-tiert.

#### Hilfe
In der Android-Applikation wird eine Hilfeübersicht umgesetzt. Der Popup-Dialog der Hilfe wird durch einen Klick auf ein Symbol angezeigt. Das Symbol zum Aufruf der Hilfe wird in allen Übersichten im Titelmenu zu finden sein. In dieser Übersicht hat der Benutzer die Möglichkeit, die grundlegenden Aktionen und Anwendungsfälle der jeweiligen Übersichten zu verstehen und bei Problemen oder Fragestellungen in dieser nachzulesen.

#### Internationalisierung
Abhängig der genutzten Sprache des verwendeten Smartphones / Tablets und dessen And-roid-Betriebssystem wird als Default-Sprache der Anwendung die Sprache English verwen-det. Zusätzlich wird die Applikation auch in der Sprache Deutsch angeboten. Die Sprache ist im Prototyp jedoch nicht konfigurierbar, sondern richtet sich ausschließlich nach der ge-nutzten Sprache des aktuellen Betriebssystems.
