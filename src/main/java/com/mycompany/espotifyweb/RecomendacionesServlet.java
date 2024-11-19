package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.Registro_tema;
import persistencia.DAO_RegistroTema;

public class RecomendacionesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AgregarTemaAListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgregarTemaAListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Recomendaciones GET-----");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        
        if ("devolverTemazos".equals(action)) {
            try (PrintWriter out = response.getWriter()) {
        DAO_RegistroTema persistence = new DAO_RegistroTema();
        Collection<Registro_tema> temas = persistence.buscarLos100MasPopulares();

        // Usa Gson para convertir la colección a JSON
        Gson gson = new Gson();
        String json = gson.toJson(temas);

        // Establece el contenido de la respuesta
        response.setContentType("application/json;charset=UTF-8");
        out.print(json);

        // Log para verificar el JSON generado
        System.out.println("JSON generado: " + json);
    } catch (Exception e) {
        e.printStackTrace();
    }
        }
        System.out.println("\n-----Recomendaciones GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("\n-----Recomendaciones POST-----");

        System.out.println("\n-----Recomendaciones POST-----");
    }

}
