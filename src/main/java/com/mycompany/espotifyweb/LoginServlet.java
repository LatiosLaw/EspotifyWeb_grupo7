package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorSuscripcion;
import logica.dt.DataErrorBundle;
import persistencia.DAO_Suscripcion;
import persistencia.DAO_Usuario;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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

        System.out.println("\n---------Login Servlet----------");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String nickname = request.getParameter("nickname");
        String pass = request.getParameter("pass");

        ControladorCliente controlador = new ControladorCliente();
        DataErrorBundle resultado = controlador.iniciarSesion(nickname, pass);

        if (resultado.getValor()) {
            DAO_Usuario persistence = new DAO_Usuario();
            ControladorSuscripcion controladorSus = new ControladorSuscripcion();
            String userType = persistence.findUsuarioByNick(nickname).getDTYPE();
            Boolean suscrito = controladorSus.isVigente(nickname);

            // Guardar el tipo de usuario en la sesión
            HttpSession session = request.getSession();

            session.setAttribute("nickname", nickname);
            session.setAttribute("userType", userType);
            session.setAttribute("suscrito", suscrito);

            out.print("{\"success\": true}");
        } else {
            out.print("{\"success\": false, \"errorCode\": " + resultado.getNumero() + "}");
        }

        out.flush();

        System.out.println("----------End Login Servlet----------");
    }

}
