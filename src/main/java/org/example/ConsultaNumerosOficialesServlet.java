package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Listado público de números oficiales, con búsqueda por número o entidad y paginación
@WebServlet("/consulta-numeros-oficiales")
public class ConsultaNumerosOficialesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String busqueda = UtilWeb.texto(request, "q");
        List<Numero_oficial> lista = NumeroOficialServlet.filtrar(new Numero_oficialDAO().listarNumOficiales(), busqueda);

        request.setAttribute("busqueda", busqueda);
        request.setAttribute("listaNumerosOficialesPublicos", Paginacion.paginar(request, lista));

        request.getRequestDispatcher(
                "/WEB-INF/vistas/usuarios/numeros-oficiales.jsp"
        ).forward(request, response);
    }
}
