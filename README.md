# Bitter-s-Project
Versione con parziale utilizzo del database, inserisce gli utenti che aprono l'applicazione. \
Utilizza coordinate nel database per settare marker sulla nave de vero e bound sulla mappa intorno all'italia come swipe singolo ai bordi dello schermo. \
Inserisce i seguenti dati nel database:\
-Segnalazioni\
-Utenti acceduti all'app\
-Capacità corrente del centro commerciale\ 
-Utenti acceduti al centro commerciale

L'activity che contiene le tabs dei negozi cambia dinamicamente in base alla lista di nomi del centro commerciale nel database real time. \
Controlla in entrambe le mappe la presenza dei sensori richiesti, quali internet, GPS e bluetooth, altrimenti killa con un bruteforce l'applicazione
 
# MANCA:
-Gestione della coda online\
-Marker segnalazioni OPPURE sezione nella tab activity (molto più probabile/semplice)\
-Gestione bluetooth per vicinanza
