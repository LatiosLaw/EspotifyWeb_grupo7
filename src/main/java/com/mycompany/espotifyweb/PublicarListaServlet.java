package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataListaParticular;
import servicios.IPublicador;

@WebServlet(name = "PublicarListaServlet", urlPatterns = {"/PublicarListaServlet"})

public class PublicarListaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PublicarListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PublicarListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("\n----------Publicar Lista Servlet GET----------");

        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        try (PrintWriter out = response.getWriter()) {
            // Comprobar si se obtuvo el nickname
            if (nickname == null) {
                out.print("{\"error\": \"No se encontró el nickname.\"}");
                return;
            }

            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");

            // Crear el servicio
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);

            Collection<DataListaParticular> listasReproduccion = publicador.retornarListasParticularesDeCliente(nickname);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataListaParticular lista : listasReproduccion) {
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                        .append("\"visibilidad\":").append(lista.isVisibilidad()).append("},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        System.out.println("----------End Publicar Lista Servlet GET----------");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("\n----------Publicar Lista Servlet POST----------");

        String listaNombre = request.getParameter("primerCampo");
        System.out.println(listaNombre);
        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nicknameCliente = (String) session.getAttribute("nickname");

        // Comprobar si se obtuvo el nickname
        if (nicknameCliente == null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"errorCode\": \"No se encontró el nickname.\"}");
            return;
        }
        
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        boolean success = publicador.publicarLista(nicknameCliente, listaNombre); // Llama a tu método para publicar la lista

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (success) {
            out.print("{\"success\": true}");
        } else {
            out.print("{\"success\": false, \"errorCode\": \"La lista no pudo ser publicada.\"}");
        }
        System.out.println("----------End Publicar Lista Servlet POST----------");
    }

}
