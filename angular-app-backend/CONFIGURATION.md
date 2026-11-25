# Configuración local del backend

La aplicación requiere estas variables de entorno al arrancar:

- `DB_URL`: URL JDBC de MySQL.
- `DB_USERNAME`: usuario de MySQL.
- `DB_PASSWORD`: contraseña de MySQL.

Defina las variables en el entorno del proceso o en el gestor de secretos de su plataforma. No guarde valores reales en archivos del repositorio ni en ejemplos versionados. Spring Boot resuelve las referencias de `application.properties` al iniciar; si falta una variable requerida, el inicio debe fallar.

Para pruebas automatizadas use valores de prueba y una base de datos aislada. No ejecute pruebas de integración contra la base de datos real sin revisar antes su efecto.

La contraseña que figuraba en el repositorio público debe considerarse expuesta. Coordine su rotación con el responsable de la base de datos. Retirar el valor del archivo actual no lo elimina del historial Git.
