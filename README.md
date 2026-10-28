# Employee Salary Management (Terminal)

Requirements: JDK 17+, Maven, and MySQL.

1. Run `sql/schema.sql` in MySQL Workbench.
2. If needed, set `DB_URL`, `DB_USER`, and `DB_PASSWORD` environment variables.
3. From this project folder, run:
   `mvn clean package`
   `mvn exec:java -Dexec.mainClass=com.example.salary.Main`

The program runs entirely in the terminal. Features: add, update, delete, search, list, salary calculation, and gross salary report.
