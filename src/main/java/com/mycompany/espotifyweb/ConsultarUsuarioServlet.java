package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DtIdTema;
import servicios.DataAlbum;
import servicios.DataListaParticular;
import servicios.DataListaPorDefecto;
import servicios.DataUsuario;
import servicios.IPublicador;

public class ConsultarUsuarioServlet extends HttpServlet {

    private URL url;
    private QName qname;
    private Service servicio;
    private IPublicador publicador;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        url = new URL("http://localhost:9128/publicador?wsdl");
        qname = new QName("http://servicios/", "PublicadorService");
        servicio = Service.create(url, qname);
        publicador = servicio.getPort(IPublicador.class);

        switch (action) {
            case "cargarPerfil" ->
                cargarPerfil(request, out);
            case "cargarSeguidores" ->
                cargarSeguidores(request, out);
            case "cargarSeguidos" ->
                cargarSeguidos(request, out);
            case "cargarListas" ->
                cargarListas(request, out);
            case "cargarAlbumes" ->
                cargarAlbumes(request, out);
            case "cargarFavoritos" ->
                cargarFavoritos(request, out);
            case "cargarListasFavoritas" ->
                cargarListasFavoritas(request, out);
            case "cargarAlbumesFavoritos" ->
                cargarAlbumesFavoritos(request, out);
            case "cargarTemasFavoritos" ->
                cargarTemasFavoritos(request, out);
            default ->
                out.println("{\"error\": \"Acción no válida\"}");
        }
    }

    private void cargarPerfil(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        DataUsuario user = publicador.retornarUsuario(nickname);

        if (user != null) {
            String jsonPerfil = String.format(
                    "{\"nickname\": \"%s\", \"correo\": \"%s\", \"nombre\": \"%s\", \"apellido\": \"%s\", \"fechaNacimiento\": \"%s\", \"imagen\": \"%s\", \"esArtista\": %b}",
                    user.getNickname(),
                    user.getCorreo(),
                    user.getNombre(),
                    user.getApellido(),
                    user.getFechaNac(),
                    user.getFoto() != null ? user.getFoto() : "imagenes/usuarios/defaultUser.png",
                    user.getDtype()
            );
            out.println(jsonPerfil);
        } else {
            out.println("{\"error\": \"Usuario no encontrado\"}");
        }
    }

    private void cargarSeguidores(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<String> nombreSeguidores = publicador.obtenerSeguidoresDeUsuario(nickname);

        String jsonSeguidores = convertToJson(nombreSeguidores);

        out.println(jsonSeguidores);
    }

    private String convertToJson(Collection<String> seguidores) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        for (String seguidor : seguidores) {
            jsonBuilder.append("\"").append(seguidor).append("\",");
        }

        // Eliminar la última coma y cerrar el JSON
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 1); // Eliminar la última coma
        }
        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }

    private void cargarSeguidos(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<String> nombreSeguidos = publicador.obtenerSeguidosDeUsuario(nickname);

        String jsonSeguidos = convertToJson(nombreSeguidos);

        out.println(jsonSeguidos);
    }

    private void cargarListas(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<DataListaParticular> listas = publicador.obtenerDataListasDeClientes(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (DataListaParticular lista : listas) {

            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("/").append(lista.getCreador().getNickname()).append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
        }

        jsonResponse.append("]");

        out.print(jsonResponse.toString());
    }

    private void cargarAlbumes(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesDeArtista(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (DataAlbum album : albumes) {

            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                    .append("\"imagen\":\"").append(album.getImagen()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
        }

        jsonResponse.append("]");

        out.print(jsonResponse.toString());
    }

    private void cargarFavoritos(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesFavoritos(nickname);

        Collection<String> nombreAlbum = new ArrayList<>();

        for (DataAlbum album : albumes) {
            nombreAlbum.add(album.getNombre());
        }

        String jsonAlbumes = convertToJson(nombreAlbum);

        out.println(jsonAlbumes);
    }

    private void cargarListasFavoritas(HttpServletRequest request, PrintWriter out) {
        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<DataListaPorDefecto> listasD = publicador.obtenerDataListasPorDefectoFavoritas(nickname);
        Collection<DataListaParticular> listasP = publicador.obtenerDataListasParticularesFavoritas(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");

        for (DataListaPorDefecto lista : listasD) {

            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("-").append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        for (DataListaParticular lista : listasP) {
            String nombrelista = lista.getNombre() + "/" + lista.getCreador().getNickname();
            jsonResponse.append("{\"nombre\":\"").append(nombrelista).append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
        }

        jsonResponse.append("]");

        out.print(jsonResponse.toString());
    }

    private void cargarAlbumesFavoritos(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesFavoritos(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (DataAlbum album : albumes) {

            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                    .append("\"imagen\":\"").append(album.getImagen()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
        }

        jsonResponse.append("]");

        out.print(jsonResponse.toString());
    }

    private void cargarTemasFavoritos(HttpServletRequest request, PrintWriter out) {
        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        List<DtIdTema> temas = publicador.obtenerDataIdTemasFavoritos(nickname);

        List<Map<String, String>> temasJson = new ArrayList<>();

        for (DtIdTema tema : temas) {
            Map<String, String> temaMap = new HashMap<>();
            temaMap.put("nombreTema", tema.getNombreTema());
            temaMap.put("nombreAlbumTema", tema.getNombreAlbumTema());
            temasJson.add(temaMap);
        }

        String jsonTemas = new Gson().toJson(temasJson);

        out.println(jsonTemas);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
