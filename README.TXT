PROJECT TITLE: The Legend of Informatik LK

PURPOSE OF PROJECT: 
Das Ziel dieses Projektes ist die Entwicklung eines Top-Down Spieles, mit Informatik als Thema. 
Der Spieler wird auf ein zufällig generiertes Feld mit Gegnern und Items gespawnt. 
Die Gegner sind nach typischen Herausforderungen in der Programmierung gestaltet. 
Zum Beispiel gibt es Spinnen (Bugs), Schnecken (Ineffizienz), Krebse die nur seitlich laufen können (Nebeneffekt) etc. 
Der Spieler kann Nahkampfwaffen wie einen Cursor und eine Tastatur aufheben und die „Herausforderungen“ zerstören. 
Außerdem gibt es Fernkampfwaffen, wie ein Buch und eine Maus, die der Spieler auf die Gegner werfen kann. 
Zuletzt gibt es Power-Ups wie Schuhe, die die Laufgeschwindigkeit erhöhen oder ein Herz, dass das maximale Leben erhöht. 
Man kann das gleiche Item mehrmals aufheben, wodurch sich die Effekte stapeln.

VERSION or DATE: 13.05.2019

AUTHORS: Cashen Adkins, Jakob Kleine, Cepehr Lorenzo Bromand, Ares Zühlke, Janni Marlene Röbbecke
--------------------------------------------------------------------------------------------------------------------------

BEDIENUNG: 
Um ein neues Spiel zu starten, muss die main()-Methode der Klasse Game aufgerufen werden.
Die Steuerung der Spielfigur erfolgt über die Tasten w (oben), s (unten), a (links) und d (rechts).

STAND DES PROJEKTS (Prototyp 1, 13.05.2019): 
Wir hatten uns vorgenommen, als ersten Prototypen ein Spiel programmiert zu haben, in dem der Spieler eine Figur animiert
mit Tastensteuerung durch ein Spielfeld lenken kann, dessen Hintergrund dem Hintergrund entspricht, den wir im Entwurf geplant hatten.
Außerdem war geplant, dass der Spieler nicht aus dem Spielfeld laufen kann, also mit den Wänden kollidiert, und dass es Gegner gibt, 
die sich zwar noch nicht bewegen können, die aber auf dem Spielfeld stehen und dem Spieler bei Kollision HP abziehen.

Ersteres haben wir geschafft, zu implementieren, den zweiten Punkt (also v.A. Kollision) haben wir aus Zeitgründen leider nicht geschafft.

Diese Punkte werden aber im Folgenden in Angriff genommen, wobei der (über)nächste Schritt außerdem ist, die Gegner sich auf den Spieler 
zubewegen zu lassen.