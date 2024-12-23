package com.mycompany.espotifyweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
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
import logica.dt.DataGenero;
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
                        .append("\"duracion\":\"").append(tema.getDuracion().toString()).append("\",")
                        .append("\"archivo\":\"").append(tema.getArchivo()).append("\",")
                        .append("\"link\":\"").append(tema.getAccess())
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
        }else if("devolverInformacionAlbum".equals(action)){
            try (PrintWriter out = response.getWriter()) {
            // Obtener todas las listas de reproducción del cliente
            ControladorAlbum persistence = new ControladorAlbum();
            String albumName = request.getParameter("albumName");
            DataAlbum album_buscado = persistence.retornarInfoAlbum(albumName);

            StringBuilder jsonResponse = new StringBuilder("[");
                jsonResponse.append("{\"nombre\":\"").append(album_buscado.getNombre()).append("\",")
                        .append("\"anio\":\"").append(album_buscado.getAnioCreacion()).append("\",")
                        .append("\"imagen\":\"").append(album_buscado.getImagen()).append("\",");
                jsonResponse.append("\"generos\":[");

                Collection<DataGenero> generos_album = album_buscado.getGeneros();
// Iterar sobre los géneros del álbum

Iterator<DataGenero> iterador = generos_album.iterator();

        // Iterar sobre el array y asignar identificadores únicos consecutivos
        while (iterador.hasNext()) {
            DataGenero genero = iterador.next();
            System.out.println(genero.getNombre());
    jsonResponse.append("{\"nombre\":\"").append(genero.getNombre()).append("\"},");
        }

// Eliminar la última coma si se añadieron géneros
if (album_buscado.getGeneros().size() > 0) {
    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
}

jsonResponse.append("],"); // Cerrar la lista de géneros

// Agregar el creador (aquí usas el mismo método para año y creador, asumiendo un error)
jsonResponse.append("\"creador\":\"").append(album_buscado.getCreador().getNickname()).append("\"},");

    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma

jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
        }else if("Download".equals(action)){
           String TARGET_DIR = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/temas/";
           String fileName = request.getParameter("filename");
        File file = new File(TARGET_DIR + fileName);

        // Verifica si el archivo existe
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        // Configuración de la respuesta
        response.setContentType("audio/mpeg");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        response.setContentLengthLong(file.length());

        // Escribir el archivo en la respuesta
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        }else if("devolverSubscripcion".equals(action)){
            if(session.getAttribute("suscrito")==null){
                response.getWriter().write("{\"sus\": false}");
            }else{
        Boolean sessionValue = (Boolean) session.getAttribute("suscrito");
        // Configuración de la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enviar el valor de la sesión como JSON
        
        if (sessionValue==true) {
            response.getWriter().write("{\"sus\": \"true\"}");
        } else {
            response.getWriter().write("{\"sus\": \"false\"}");
        }
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
