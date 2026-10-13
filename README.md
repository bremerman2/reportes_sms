# Reportes SMS

Aplicación web (Jakarta EE: Servlets + JSP + JSTL) para gestionar reportes de SMS sospechosos.

## Requisitos

- JDK 26 (el que usa el proyecto)
- Apache Tomcat 11
- MariaDB con la base `reportes_sms`

## Configuración

`src/main/java/org/example/ConexionDB.java` **no se sube a git** (está en `.gitignore`)
porque tiene el usuario y la contraseña de la base. Cada uno tiene que tener el suyo:

```java
package org.example;

import java.sql.*;

public class ConexionDB {

    private static final String url = "jdbc:mariadb://localhost:3306/reportes_sms";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    private ConexionDB(){
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url,USUARIO,PASSWORD);
    }
}
```

## Cómo correrlo (IntelliJ)

1. Abrir el proyecto y recargar Maven.
2. Run → Edit Configurations → `+` → Tomcat Server → Local, elegir la carpeta de Tomcat 11.
3. En la pestaña Deployment agregar el artifact `reportes_sms:war exploded`.
4. Ejecutar y entrar a `http://localhost:8080/reportes_sms/` (o al context path que haya quedado).
5. Iniciar sesión con el email y la contraseña de un administrador de la tabla `administrador`.

## Estructura

| Carpeta / archivo | Qué tiene |
|---|---|
| `org.example.*` (modelos y `*DAO`) | Lo mismo que tenía la versión de consola |
| `*Servlet.java` | Un servlet por sección (reemplazan los menús del viejo `Main`) |
| `FiltroSesion.java` | Manda al login a quien no inició sesión |
| `InicioAplicacion.java` | Carga el driver de MariaDB cuando arranca Tomcat |
| `UtilWeb.java` | Métodos de ayuda para leer parámetros y mostrar mensajes |
| `webapp/WEB-INF/vistas/` | Las páginas JSP (dentro de `WEB-INF` para que no se puedan abrir sin pasar por un servlet) |

| URL | Equivale en la consola a |
|---|---|
| `/administradores` | 1. Gestión de Administradores |
| `/numeros-oficiales` | 2. Gestión de Números Oficiales |
| `/prefijos` | 3. Gestión de Prefijos |
| `/reportes` | 4. Gestión de Reportes |
| `/usuarios` | 5. Gestión de Usuarios |
