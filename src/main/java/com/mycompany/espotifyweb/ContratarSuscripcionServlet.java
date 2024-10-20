package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.controladores.ControladorSuscripcion;
import logica.dt.DataErrorBundle;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            case "Semanal":
                monto = "5";
                break;
            case "Mensual":
                monto = "15";
                break;
            case "Anual":
                monto = "150";
                break;
            default:
                break;
        }

        DataErrorBundle resultado = null;

        if (!tipoSuscripcion.isEmpty()) {
            LocalDate fechaActual = LocalDate.now();

            ControladorSuscripcion sus = new ControladorSuscripcion();
            resultado = sus.agregarSus(nickname, "Pendiente", fechaActual, tipoSuscripcion);

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

        if (resultado != null && resultado.getValor()) {
            out.print("{\"success\": true}");
            // Guardar la suscripcion como cookie
            Cookie userCookie = new Cookie("suscrito", "true");
            userCookie.setMaxAge(60 * 60 * 24); // Duración de la cookie (Un día)
            userCookie.setPath("/"); // Hace que la cookie sea accesible para toda la web
            response.addCookie(userCookie);
        } else {
            out.println("{\"success\": false, \"errorCode\": " + (resultado != null ? resultado.getNumero() : "desconocido") + "}");
        }

        out.flush();

        System.out.println("\n---------End Contratar Suscripcion Servlet----------");
    }

}
