# 🛒 Projecte Botiga Online (JDBC)

Aplicació de consola en Java que simula la gestió d'una botiga online, implementant una arquitectura **MVC (Model-View-Controller)** amb accés a dades mitjançant **JDBC**.

Aquest projecte demostra l'ús avançat de bases de dades relacionals amb Java, centrant-se en la integritat de les dades mitjançant **Transaccions ACID** i la gestió d'errors controlada.

## 🚀 Funcionalitats Principals

* **Gestió de Clients i Productes (CRUD):** Alta, llistat i modificació de dades mestres.
* **Gestió de Comandes Transaccional:**
    * Creació de comandes amb múltiples línies de producte.
    * **Control d'Estoc:** Decrement automàtic d'estoc i *rollback* automàtic si no hi ha prou unitats.
    * **Savepoints:** Aplicació de descomptes amb punts de recuperació (si falla el descompte, la comanda es guarda amb el preu original).
* **Consultes Avançades:** Generació d'informes detallats utilitzant `JOIN` per vincular clients, comandes i productes.

## 🛠️ Tecnologies Utilitzades

* **Llenguatge:** Java (JDK 17+)
* **Base de Dades:** MySQL 8.0
* **Accés a Dades:** JDBC (Java Database Connectivity)
* **Patró de Disseny:** DAO (Data Access Object)
* **IDE:** NetBeans / IntelliJ / VS Code

## ⚙️ Instal·lació i Configuració

1.  **Base de Dades:**
    * Executa l'script `sql/schema.sql` en el teu servidor MySQL per crear la base de dades `botiga` i les taules necessàries.
    * L'script inclou dades de prova inicials.

2.  **Configuració de Connexió:**
    * Edita l'arxiu `src/com/mycompany/botiga/util/Connexio.java`.
    * Configura les constants `USER` i `PASSWORD` amb les teves credencials locals.

3.  **Execució:**
    * Compila i executa la classe principal: `com.mycompany.botiga.Botiga`.

## 📚 Conceptes Clau Implementats

* **PreparedStatement:** Per prevenir injecció SQL i millorar el rendiment.
* **ACID Transactions:** Ús de `setAutoCommit(false)`, `commit()` i `rollback()` per garantir la consistència.
* **Savepoints:** Gestió granular d'errors dins d'una transacció activa.

## 👤 Autor

* **Alex** - Desenvolupador Full Stack Junior (DAM)
