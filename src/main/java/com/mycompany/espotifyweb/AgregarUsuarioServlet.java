package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.controladores.ControladorArtista;
import logica.controladores.ControladorCliente;
import logica.dt.DataErrorBundle;

public class AgregarUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AgregarUsuarioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgregarUsuarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Usuario Servlet-----");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener parametros del formulario
        String tipoUsuario = request.getParameter("tipoUsuario");
        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String mail = request.getParameter("mail");
        String foto = request.getParameter("foto");
        String pass = request.getParameter("pass");
        LocalDate fechaNac = LocalDate.parse(request.getParameter("fechaNac"));
        
        DataErrorBundle resultado;

        if ("cliente".equals(tipoUsuario)) {
            ControladorCliente controladorCliente = new ControladorCliente();
            resultado = controladorCliente.agregarCliente(nickname, nombre, apellido, pass, mail, foto, fechaNac);
            
        } else if ("artista".equals(tipoUsuario)) {
            // Obtener parametros adicionales para artista
            String biografia = request.getParameter("biografia");
            String dirWeb = request.getParameter("dirWeb");

            ControladorArtista controladorArtista = new ControladorArtista();
            resultado = controladorArtista.agregarArtista(nickname, nombre, apellido, pass, mail, foto, fechaNac, biografia, dirWeb);
            
        } else {
            out.println("{\"success\": false, \"errorCode\": 400}"); // Error por tipo de usuario no válido
            return;
        }

        if (resultado.getValor()) {
            out.println("{\"success\": true}");
            System.out.println("Usuario agregado exitosamente.");
        } else {
            out.println("{\"success\": false, \"errorCode\": " + resultado.getNumero() + "}");
            System.out.println("Error al agregar usuario: " + resultado.getNumero());
        }
        
        System.out.println("----------End Agregar Usuario Servlet----------");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
