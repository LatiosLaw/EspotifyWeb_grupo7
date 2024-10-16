package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.controladores.ControladorCliente;
import logica.dt.DataErrorBundle;

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
            out.print("{\"success\": true}");
            String nicknameCliente = request.getParameter("nickname"); // Obtener el valor del campo nickname
            Cookie userCookie = new Cookie("nickname", nicknameCliente); //Guardarlo como una cookie
            userCookie.setMaxAge(60 * 60 * 24); // Duracion de la cookie (Un dia)
            userCookie.setPath("/"); // Hace que la cookie sea accesible para toda la web
            response.addCookie(userCookie);
        } else {
            out.print("{\"success\": false, \"errorCode\": " + resultado.getNumero() + "}");
        }

        out.flush();

        System.out.println("----------End Login Servlet----------");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
