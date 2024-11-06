package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import logica.ListaPorDefecto;
import persistencia.DAO_ListaReproduccion;

public class TodasLasListasDefaultServlet extends HttpServlet {

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
        System.out.println("\n-----Start Todas Las Listas Servlet GET-----");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");
        
        if ("devolverListas".equals(action)) {
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaPorDefecto> listas = persistence.devolverListasPorDefecto();
            
            StringBuilder jsonResponse = new StringBuilder("[");
            for (ListaPorDefecto lista : listas) {
                 jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                        .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
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
        System.out.println("\n-----End Todas Las Listas Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("\n-----Start Todas Las Listas Servlet POST-----");

        System.out.println("\n-----End Todas Las Listas Servlet POST-----");
    }

}
