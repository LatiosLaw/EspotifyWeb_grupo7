package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CerrarSesionServlet", urlPatterns = {"/CerrarSesionServlet"})
public class CerrarSesionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CerrarSesionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CerrarSesionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("\n---------Logout Servlet----------");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Invalida la sesión actual
        HttpSession session = request.getSession(false); // No crea una nueva sesión si no existe
        if (session != null) {
            session.invalidate(); // Invalida la sesión
        }

        // Respond with success message
        out.print("{\"success\": true, \"message\": \"Sesion cerrada exitosamente.\"}");
        out.flush();

        System.out.println("\n---------End Logout Servlet----------");
    }

}
