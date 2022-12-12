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
