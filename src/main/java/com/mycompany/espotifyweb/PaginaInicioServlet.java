package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import logica.Album;
import logica.ListaPorDefecto;
import logica.controladores.ControladorArtista;
import logica.controladores.ControladorCliente;
import logica.dt.DataArtista;
import logica.dt.DataCliente;
import persistencia.DAO_Album;
import persistencia.DAO_ListaReproduccion;

@MultipartConfig

public class PaginaInicioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AltaDeListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AltaDeListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Pagina Inicio Servlet GET-----");
              String action = request.getParameter("action");
       if ("buscarArtistas".equals(action)) {
      try (PrintWriter out = response.getWriter()) {

            // Obtener todas las listas de reproducción del cliente
            ControladorArtista persistence = new ControladorArtista();
            Collection<DataArtista> artistas = persistence.mostrarArtistas();
            List<DataArtista> artistasList = new ArrayList<>(artistas);
            Collections.shuffle(artistasList);

            StringBuilder jsonResponse = new StringBuilder("[");
            int count = 0;
for (DataArtista artista : artistasList) {

     jsonResponse.append("{\"nombre\":\"").append(artista.getNickname()).append("\",")
                        .append("\"imagen\":\"").append(artista.getFoto())
                        .append("\"},");
    
    count++;
    if (count == 10) break; // Detener el bucle después de 10 elementos
}

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
       }else if ("buscarAlbumes".equals(action)) {
     
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            DAO_Album persistence = new DAO_Album();
            Collection<Album> albumes = persistence.findAll();
            List<Album> albumesList = new ArrayList<>(albumes);
            Collections.shuffle(albumesList);
            
            StringBuilder jsonResponse = new StringBuilder("[");
            int count = 0;
            for (Album album : albumesList) {

                jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                        .append("\"imagen\":\"").append(album.getImagen())
                        .append("\"},");
    
    count++;
    if (count == 10) break; // Detener el bucle después de 10 elementos
}

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
       }else if ("buscarListas".equals(action)) {
       
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaPorDefecto> listas = persistence.devolverListasPorDefecto();
            List<ListaPorDefecto> ListasList = new ArrayList<>(listas);
            Collections.shuffle(ListasList);
            
            StringBuilder jsonResponse = new StringBuilder("[");
            int count = 0;
            for (ListaPorDefecto lista : ListasList) {

                jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto())
                        .append("\"},");
    
    count++;
    if (count == 10) break; // Detener el bucle después de 10 elementos
}

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
       }else if ("buscarClientes".equals(action)) {
 try (PrintWriter out = response.getWriter()) {

            // Obtener todas las listas de reproducción del cliente
            ControladorCliente persistence = new ControladorCliente();
            Collection<DataCliente> clientes = persistence.mostrarClientes();
            List<DataCliente> clientesList = new ArrayList<>(clientes);
            Collections.shuffle(clientesList);

            StringBuilder jsonResponse = new StringBuilder("[");
            int count = 0;
for (DataCliente cliente : clientesList) {

     jsonResponse.append("{\"nombre\":\"").append(cliente.getNickname()).append("\",")
                        .append("\"imagen\":\"").append(cliente.getFoto())
                        .append("\"},");
    
    count++;
    if (count == 10) break; // Detener el bucle después de 10 elementos
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
