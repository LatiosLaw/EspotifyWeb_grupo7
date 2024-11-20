package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataErrorBundle;
import servicios.IPublicador;

public class ContratarSuscripcionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ContratarSuscripcionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ContratarSuscripcionServlet at " + request.getContextPath() + "</h1>");
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

        System.out.println("\n---------Contratar Suscripcion Servlet----------");

        response.setContentType("text/html;charset=UTF-8");

        String tipoSuscripcion = request.getParameter("tipoSuscripcion");

        String monto = "";

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        switch (tipoSuscripcion) {
            case "Semanal" -> monto = "5";
            case "Mensual" -> monto = "15";
            case "Anual" -> monto = "150";
            default -> {
            }
        }

        DataErrorBundle resultado = null;

        if (!tipoSuscripcion.isEmpty()) {
            
            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");

            // Crear el servicio
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);
            
            
            
            resultado = publicador.agregarSuscripcion(nickname, "Pendiente", tipoSuscripcion);

        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head>");
                out.println("<script type='text/javascript'>");
                out.println("alert('No se seleccionó ningún tipo de suscripción.');");
                out.println("window.location.href='ContratarSuscripcion.jsp';");
                out.println("</script></head><body></body></html>");
            }
        }

        PrintWriter out = response.getWriter();

        if (resultado != null && resultado.isValor()) {
            out.print("{\"success\": true}");
            session.setAttribute("suscrito", true);
        } else {
            out.println("{\"success\": false, \"errorCode\": " + (resultado != null ? resultado.getNumero() : "desconocido") + "}");
        }

        out.flush();

        System.out.println("\n---------End Contratar Suscripcion Servlet----------");
    }

}
