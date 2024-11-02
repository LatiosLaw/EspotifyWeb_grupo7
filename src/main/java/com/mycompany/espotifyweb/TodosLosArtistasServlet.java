package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.controladores.ControladorArtista;
import logica.dt.DataArtista;

public class TodosLosArtistasServlet extends HttpServlet {

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
        System.out.println("\n-----Start Todos Los Artistas Servlet GET-----");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");
        
        if ("devolverArtias".equals(action)) {
      try (PrintWriter out = response.getWriter()) {

            // Obtener todas las listas de reproducción del cliente
            ControladorArtista persistence = new ControladorArtista();
            Collection<DataArtista> artistas = persistence.mostrarArtistas();
            List<DataArtista> artistasList = new ArrayList<>(artistas);

            StringBuilder jsonResponse = new StringBuilder("[");
for (DataArtista artista : artistasList) {

     jsonResponse.append("{\"nombre\":\"").append(artista.getNickname()).append("\",")
                        .append("\"imagen\":\"").append(artista.getFoto())
                        .append("\"},");
}

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }
        System.out.println("\n-----End Todos Los Artistas Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("\n-----Start Todos Los Artistas Servlet POST-----");

        System.out.println("\n-----End Todos Los Artistas Servlet POST-----");
    }

}
