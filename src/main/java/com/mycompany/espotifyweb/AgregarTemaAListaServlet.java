package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.ListaParticular;
import persistencia.DAO_ListaReproduccion;

public class AgregarTemaAListaServlet extends HttpServlet {

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
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        
        if ("cargarListas".equals(action)) {

        try (PrintWriter out = response.getWriter()) {
            // Comprobar si se obtuvo el nickname
            if (nickname == null) {
                out.print("{\"error\": \"No se encontró el nickname.\"}");
                return;
            }
            // Obtener todas las listas de reproducción del cliente
            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaParticular> listasReproduccion = persistence.findListaPorCliente(nickname);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (ListaParticular lista : listasReproduccion) {
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if ("cargarListasParticular".equals(action)){
            System.out.println("\nEntramo a lista particular");
             try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaParticular> listasReproduccion = persistence.findAllListasParticulares();

            StringBuilder jsonResponse = new StringBuilder("[");
            for (ListaParticular lista : listasReproduccion) {
                System.out.println("\nEntramo a las listas en cuestion");
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                        .append("\"nombrecreador\":").append(lista.getCliente().getNickname()).append("},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if ("cargarListasDefault".equals(action)){
            
        }else if ("cargarAlbumes".equals(action)){
            
        }
        System.out.println("\n-----End Agregar Tema a Lista Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Usuario Servlet-----");

    }

}
