package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataAlbum;
import servicios.DataArtista;
import servicios.DataGenero;
import servicios.DataTema;
import servicios.IPublicador;
import servicios.RegistroTema;

public class ConsultarAlbumServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if (null != action) {
            switch (action) {
                case "cargarArtistas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        Collection<DataArtista> artistas = publicador.obtenerDataArtistas();

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataArtista artista : artistas) {

                            jsonResponse.append("{\"nombre\":\"").append(artista.getNickname()).append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "cargarGeneros" -> {
                    try (PrintWriter out = response.getWriter()) {

                        Collection<String> generos = publicador.obtenerGeneros();

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (String genero : generos) {

                            jsonResponse.append("{\"nombre\":\"").append(genero).append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "cargarAlbumsArtistas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        String artistaName = request.getParameter("artistaName");

                        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesDeArtista(artistaName);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataAlbum album : albumes) {

                            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "cargarAlbumsGeneros" -> {
                    try (PrintWriter out = response.getWriter()) {
                        String generoName = request.getParameter("generoName");
                        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesPorGenero(generoName);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataAlbum album : albumes) {

                            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "devolverTemasAlbum" -> {
                    try (PrintWriter out = response.getWriter()) {
                        String albumName = request.getParameter("albumName");

                        Collection<DataTema> temas = publicador.obtenerTemasDeAlbumes(albumName);

                        List<String> temasCole = publicador.obtenerNombreTemasFavCliente(nickname);

                        if (temasCole == null) {
                            temasCole = new ArrayList<>();
                        }

                        StringBuilder jsonResponse = new StringBuilder("[");

                        for (DataTema tema : temas) {
                            String tieneLaik = "noLaik";
                            if (!temasCole.isEmpty()) {
                                tieneLaik = publicador.corroborarTemaEnFav(tema.getNickname(), temasCole);
                            }

                            jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                                    .append("\"duracion\":\"").append(tema.getDuracion().toString()).append("\",")
                                    .append("\"archivo\":\"").append(tema.getArchivo()).append("\",")
                                    .append("\"link\":\"").append(tema.getAccess()).append("\",")
                                    .append("\"album\":\"").append(tema.getNomAlb()).append("\",")
                                    .append("\"fav\":\"").append(tieneLaik).append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case "devolverInformacionAlbum" -> {
                    try (PrintWriter out = response.getWriter()) {
                        String albumName = request.getParameter("albumName");

                        DataAlbum album_buscado = publicador.obtenerDataAlbum(albumName);
                        List<String> albumsFav = publicador.obtenerNombreAlbumesFavCliente(nickname);

                        if (albumsFav == null) {
                            albumsFav = new ArrayList<>();
                        }

                        String tieneLaik = "noLaik";
                        if (!albumsFav.isEmpty()) {
                            tieneLaik = publicador.corroborarAlbumEnFav(album_buscado.getNombre(), albumsFav);
                        }

                        StringBuilder jsonResponse = new StringBuilder("[");
                        jsonResponse.append("{\"nombre\":\"").append(album_buscado.getNombre()).append("\",")
                                .append("\"anio\":\"").append(album_buscado.getAnioCreacion()).append("\",")
                                .append("\"imagen\":\"").append(album_buscado.getImagen()).append("\",")
                                .append("\"fav\":\"").append(tieneLaik).append("\",");

                        jsonResponse.append("\"generos\":[");

                        Collection<DataGenero> generos_album = album_buscado.getGeneros();

                        for (DataGenero genero : generos_album) {
                            jsonResponse.append("{\"nombre\":\"").append(genero.getNombre()).append("\"},");
                        }

                        if (!generos_album.isEmpty()) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("],");
                        jsonResponse.append("\"creador\":\"").append(album_buscado.getCreador().getNickname()).append("\"}");

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case "Download" -> {

                    String TARGET_DIR = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/temas/";
                    String fileName = request.getParameter("filename");
                    String nombreTema = request.getParameter("nombreTema");
                    String nombreAlbum = request.getParameter("nombreAlbum");

                    publicador.incrementarInfoDescarga(nombreTema, nombreAlbum);

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
                    try (FileInputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
                case "devolverSubscripcion" -> {
                    if (session.getAttribute("suscrito") == null) {
                        response.getWriter().write("{\"sus\": false}");
                    } else {
                        Boolean sessionValue = (Boolean) session.getAttribute("suscrito");

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        // Enviar el valor de la sesión como JSON
                        if (sessionValue == true) {
                            response.getWriter().write("{\"sus\": \"true\"}");
                        } else {
                            response.getWriter().write("{\"sus\": \"false\"}");
                        }
                    }
                }
                case "incrementarReproduccion" -> {
                    String nombreTema = request.getParameter("nombreTema");
                    System.out.println(nombreTema);
                    String albumName = request.getParameter("nombreAlbum");
                    System.out.println(albumName);

                    publicador.incrementarInfoReproduccion(nombreTema, albumName);
                }
                case "devolverInformacionTema" -> {
                    String nombreTema = request.getParameter("nombreTema");
                    System.out.println(nombreTema);
                    String albumName = request.getParameter("nombreAlbum");
                    System.out.println(albumName);

                    RegistroTema info = publicador.devolverRegistroTema(nombreTema, albumName);

                    try (PrintWriter out = response.getWriter()) {

                        // Usa Gson para convertir la colección a JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(info);

                        // Establece el contenido de la respuesta
                        response.setContentType("application/json;charset=UTF-8");
                        out.print(json);

                        // Log para verificar el JSON generado
                        System.out.println("JSON generado: " + json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                default -> {
                }
            }
        }
        System.out.println("\n-----End Consultar Album Servlet GET-----");
    }

}
