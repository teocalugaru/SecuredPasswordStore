# SecuredPasswordStore
## Descriere
Aplicatia permite stocarea securizata a parolelor in fisierul Shared Preferences.
## Scenariu
Utilizatorul se va inregistra in sistem (moment in care se va retine perechea utilizator-parola in Shared Preferences - parola criptata).<br>
Ulterior, la logare se va verifica daca parola introdusa de utilizator coincide cu parola criptata din Shared Preferences.<br>
Utilizatorul isi poate selecta propria parola sau o poate genera cu ajutorul unui PasswordManager care asigura gradul de aleatorism al acesteia.
## Modul de criptare a parolelor
### Cheia AES
In momentul initializarii aplicatiei se genereaza o pereche de chei RSA (publica&privata) care vor fi retinute in KeyStore-ul Android. </br>
De asemenea, este generata si o cheie AES care este retinuta criptat in fisierul SharedPrefences (este criptata cu cheia publica RSA) si un IV public retinut tot in SharedPrefences.
### Criptare/Decriptare
La inregistrare parola utilizatorului va fi criptata cu cheia AES (care se decripteaza cu cheia privata RSA) si va fi retinuta in fisierul SharedPrefences.<br>
La logare se va prelua si decripta parola din SharedPrefences si se va compara cu cea introdusa de utilizator.

## Functionalitati adaugate
### Generare parola random
La inregistrare utilizatorul poate sa isi stabileasca singur parola sau poate sa utilizeze o parola generata random.
### Marcare complexitate parole
Dupa logarea in sistem, se va afisa o lista cu toate conturile utilizatorilor care s-au inregistrat pe device-ul curent: se vor afisa numele de utilizator si parola pentru fiecare cont (doar parola utilizatorului logat va fi afisata in clar, restul fiind afisate criptat) si se vor marca fiecare dintre parole din punct de vedere al complexitatii lor (parola slaba, medie, puternica).
### Autentificare multifactor cu amprenta si parola
S-a adaugat functionalitatea de autentificare multi-factor (cu amprenta si parola), astfel ca in momentul apasarii butonului de LOGIN va trebui ca utilizatorul sa foloseasca amprenta sau faceID-ul telefonului, dupa care, in cazul in care acestea sunt corecte, vor fi verificate si credentialele (username si parola) - multifactor authentication -- something you are (fingerprint) and something you know (username+password).
### Posibilitatea recuperarii contului
S-a adaugat posibilitatea de recuperare a contului: in pagina de login avem butonul "Forgot password" prin a carui selectie utilizatorul va putea sa introduca adresa de e-mail unde va primi parola pentru contul sau ( trebuie sa cunoasca numele de utilizator si sa foloseasca amprenta pentru ca mail-ul sa fie trimis - un factor de autentificare este pierdut, are nevoie de celalalt pentru a-l recupera)...Functionalitatea va fi completa doar in momentul in care aplicatia va avea permisiunea server-ului de mail de a trimite un e-mail in background (momentan gmail si yahoo nu permit third parties access pentru alte aplicatii - Solutie probabila: crearea unui server de mail local sau folosirea altui tip de e-mail).
