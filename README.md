# Library Management System

Aceasta este o aplicație desktop completă pentru gestionarea unei biblioteci, dezvoltată în Java. Aplicația permite administrarea cărților, a utilizatorilor și procesarea vânzărilor, având o arhitectură robustă și un sistem de securitate bazat pe roluri.

## 1. Rezumatul Aplicației

Scopul principal al aplicației este digitalizarea proceselor dintr-o bibliotecă, facilitând interacțiunea dintre trei tipuri de actori: Administratori, Angajați și Clienți.

**Funcționalități cheie:**
* **Autentificare Securizată:** Sistem de login cu parole criptate (hashing) și redirectare dinamică în funcție de rol.
* **Gestiunea Cărților (Inventory):** Adăugare, ștergere, afișare și actualizare stoc și preț.
* **Vânzări:** Procesarea tranzacțiilor de cumpărare (Buy), care actualizează automat stocul cărților și generează istoricul vânzărilor.
* **Raportare:** Generarea de rapoarte PDF automate (via iText) privind activitatea angajaților și veniturile generate în ultima lună.
* **Gestiunea Utilizatorilor:** Administratorul poate promova clienți la statutul de angajați sau poate șterge utilizatori.

## 2. Concepte și Arhitectură

Aplicația este construită pe o **Arhitectură Stratificată (Layered Architecture)**, respectând principiile **SOLID** și **Clean Code** pentru a asigura scalabilitatea și mentenanța codului.

**Arhitectura:**
* **Presentation Layer (View & Controller):** Realizat cu JavaFX, gestionează interfața grafică și interacțiunea cu utilizatorul.
* **Service Layer:** Conține logica de business (ex: validarea stocului înainte de vânzare, alegerea aleatoare a unui angajat pentru procesarea vânzării, calcularea rapoartelor).
* **Data Access Layer (Repository):** Comunică direct cu baza de date MySQL prin JDBC, executând interogări SQL native.
* **Model/DTO:** Obiecte pentru transferul datelor între straturi (`BookDTO`, `ReportDTO`).

**Design Patterns (Tipare de proiectare) folosite:**
* **Builder Pattern:** Folosit pentru crearea obiectelor complexe (`BookBuilder`, `UserBuilder`, `BookDTOBuilder`) într-un mod fluent.
* **Factory Pattern:** Implementat în `ComponentFactory`, `LoginComponentFactory` și `SQLTableCreationFactory` pentru instanțierea obiectelor și gestionarea dependențelor.
* **Singleton Pattern:** Asigură o instanță unică pentru anumite fabrici de componente și pentru conexiunea la baza de date.
* **Decorator Pattern:** Folosit pentru adăugarea de funcționalități extra repository-urilor (ex: caching) fără a modifica codul existent.
* **Data Transfer Object (DTO):** Separarea modelului de bază de date de modelul afișat în interfață.

**Alte concepte:**
* **Manual Dependency Injection:** Injectarea dependențelor (Repositories în Services, Services în Controllers) se face manual prin Factory-uri, demonstrând înțelegerea mecanismului din spatele framework-urilor moderne.
* **Role-Based Access Control (RBAC):** Permisiuni granulare bazate pe tabelele de legătură din baza de date (`user_role`, `role_right`).

## 3. Tehnologii Folosite

* **Limbaj:** Java 21
* **GUI Framework:** JavaFX
* **Build Tool:** Gradle
* **Bază de date:** MySQL
* **Conectivitate DB:** JDBC (Java Database Connectivity) - interogări SQL native pentru performanță, flexibilitate și control (inclusiv `JOIN`-uri complexe pentru rapoarte).
* **Securitate:** SHA-256 Hashing pentru parole.
* **Raportare:** iText PDF Library (pentru generarea rapoartelor de vânzări).
* **Controlul Versiunii:** Git

## 4. Setup și Configurare

Pentru a rula aplicația local:

1. **Cerințe preliminare:**
   * Java JDK 21 instalat.
   * MySQL Server rulând local pe portul 3306.
   * Credențialele MySQL setate corect în clasa `database.JDBConnectionWrapper` (utilizator și parolă).

2. **Inițializare Bază de Date:**
   * Proiectul include o clasă utilitară `database.Bootstrap`. Rularea metodei `main` din această clasă va "curăța" baza de date, va șterge tabelele existente și le va recrea curate (inclusiv constrângerile de tip Foreign Key), populând automat rolurile și drepturile necesare.

3. **Rulare Aplicație:**
   * Executați clasa `launcher.Main` pentru a porni interfața grafică.

4. **Credențiale de Test:**
   * Creați un cont nou direct din aplicație (Sign Up).
   * Parola va fi salvată criptat în baza de date. Administratorul poate schimba rolurile din tabelele MySQL sau direct din panoul de administrare.
