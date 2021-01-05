# Bitter-s-Project
Versione con totale utilizzo di firebase database al link: https://console.firebase.google.com/u/0/project/bitter-298116/database/bitter-298116-default-rtdb/data.\
Utilizza coordinate nel database per settare marker sulla nave de vero e bound sulla mappa intorno all'italia come swipe singolo ai bordi dello schermo.\
Può presentarsi un piccolo bug che all'avvio dell'applicazione ti metta nelle coordinate 0,0 all'equatore causa mancanza di internet o primo avvio.\
Feature applicazione:\
-Accesso ad un centro commerciale scaglionato in base ad una capienza massima e gestione coda relativa\
-Accesso ad un negozio scaglionato in base ad una capienza massima e gestione di coda relativa\
-Inserimento segnalazioni\
-Annullamento delle code relative\
-Segnalazione di vicinanza ad altri dispositivi per mantenere il distanziamento sociale

L'activity che contiene le tabs dei negozi cambia dinamicamente in base alla lista di nomi del centro commerciale nel database real time.\
Controlla in entrambe le mappe la presenza dei sensori richiesti, quali internet, GPS e bluetooth, altrimenti killa con un bruteforce l'applicazione
 
# MANCA:
-Gestione della coda di utenti da inserire nel database e utilizzare in locale come "coda" effettiva
(controllo che l'user sia il primo della coda, altrimenti aspetta, politica FIFO)
-BUG CHE SE ANNULLO LA CODA AD UN NEGOZIO MI INSERISCE COMUNQUE ALL'INTERNO, SOLO VISIVAMENTE NELL'ACTIVITY
