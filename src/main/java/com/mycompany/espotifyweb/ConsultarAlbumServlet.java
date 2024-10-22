package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.controladores.ControladorAlbum;
import logica.controladores.ControladorArtista;
import logica.controladores.ControladorGenero;
import logica.controladores.ControladorListaParticular;
import logica.controladores.ControladorTema;
import logica.dt.DataAlbum;
import logica.dt.DataArtista;
import logica.dt.DataTema;

public class ConsultarAlbumServlet extends HttpServlet {

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
        System.out.println("\n-----Start Consultar Album Servlet GET-----");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        
        if ("cargarArtistas".equals(action)) {
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorArtista persistence = new ControladorArtista();
            Collection<DataArtista> artistas = persistence.mostrarArtistas();

            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataArtista artista : artistas) {

                jsonResponse.append("{\"nombre\":\"").append(artista.getNickname()).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if ("cargarGeneros".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorGenero persistence = new ControladorGenero();
            Collection<String> generos = persistence.mostrarGeneros();

            StringBuilder jsonResponse = new StringBuilder("[");
            for (String genero : generos) {

                jsonResponse.append("{\"nombre\":\"").append(genero).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if ("cargarAlbumsArtistas".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorAlbum persistence = new ControladorAlbum();
            String artistaName = request.getParameter("artistaName");
            Collection<String> albumes = persistence.retornarAlbumsDelArtista(artistaName);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (String album : albumes) {

                jsonResponse.append("{\"nombre\":\"").append(album).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if ("cargarAlbumsGeneros".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorAlbum persistence = new ControladorAlbum();
            String generoName = request.getParameter("generoName");
            Collection<String> albumes = persistence.retornarAlbumsDelGenero(generoName);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (String album : albumes) {

                jsonResponse.append("{\"nombre\":\"").append(album).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if("devolverTemasAlbum".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorTema persistence = new ControladorTema();
            String albumName = request.getParameter("albumName");
            Collection<DataTema> temas = persistence.retornarTemasDeAlbum(albumName);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataTema tema : temas) {
                jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                        .append("\"duracion\":\"").append(tema.getDuracion().toString()).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if("devolverInformacionAlbum".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorAlbum persistence = new ControladorAlbum();
            String albumName = request.getParameter("albumName");
            DataAlbum album_buscado = persistence.retornarInfoAlbum(albumName);

            StringBuilder jsonResponse = new StringBuilder("[");
                jsonResponse.append("{\"nombre\":\"").append(album_buscado.getNombre()).append("\",")
                        .append("\"anio\":\"").append(album_buscado.getAnioCreacion()).append("\",")
                        .append("\"creador\":\"").append(album_buscado.getAnioCreacion()).append("\"},");

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }
        System.out.println("\n-----End Consultar Album Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("\n-----Start Agregar Tema A Lista Servlet POST-----");

        response.setContentType("application/json");
        
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        // Obtener parametros del formulario
        String albumTema = request.getParameter("albumTema");
        String nombreLista = request.getParameter("nombreLista");
        String nombreTema = request.getParameter("nombreTema");
        System.out.println(albumTema);
        ControladorListaParticular ctrlLista = new ControladorListaParticular();
        ControladorTema ctrlTema = new ControladorTema();
        DataTema temazo = ctrlTema.retornarTema(nombreTema, albumTema);
        ctrlLista.agregarTema(nickname, nombreLista, temazo);
        System.out.println("\n-----End Agregar Tema a Lista Servlet POST-----");
    }

}
