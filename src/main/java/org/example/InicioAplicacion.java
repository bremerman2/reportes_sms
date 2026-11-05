package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// Se ejecuta una sola vez cuando Tomcat levanta la aplicación.
// Dentro de Tomcat el driver de MariaDB no siempre se registra solo,
// por eso se carga a mano acá (es lo mismo que hace el Class.forName del ejemplo de clase).
@WebListener
public class InicioAplicacion implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("El driver de MariaDB no está disponible", e);
        }

        // Cambia en cada despliegue: se agrega al link de alertasms.css (?v=...) para que
        // el navegador no siga usando la versión vieja que tenía guardada en caché
        evento.getServletContext().setAttribute("versionEstilos", Long.toString(System.currentTimeMillis(), 36));
    }
}
