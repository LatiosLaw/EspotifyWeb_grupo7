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
import logica.Album;
import logica.ListaParticular;
import logica.ListaPorDefecto;
import logica.controladores.ControladorCliente;
import logica.dt.DT_IdTema;
import persistencia.DAO_Album;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Usuario;
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

        ControladorCliente controladorC = new ControladorCliente();

        Collection<String> nombreSeguidores = controladorC.obtenerSeguidoresUsuario(nickname);

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

        ControladorCliente controladorC = new ControladorCliente();

        Collection<String> nombreSeguidos = controladorC.obtenerSeguidosUsuario(nickname);

        String jsonSeguidos = convertToJson(nombreSeguidos);

        out.println(jsonSeguidos);
    }

    private void cargarListas(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        DAO_ListaReproduccion daoLista = new DAO_ListaReproduccion();

        Collection<ListaParticular> listas = daoLista.findListaPorCliente(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (ListaParticular lista : listas) {

            jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista() + "/" + lista.getNombreCliente()).append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
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

        DAO_Album daoAlbum = new DAO_Album();

        Collection<Album> albumes = daoAlbum.findAllPorArtista(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (Album album : albumes) {

            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                    .append("\"imagen\":\"").append(album.getImagen()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
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

        DAO_Album daoAlbum = new DAO_Album();

        Collection<Album> albumes = daoAlbum.findAllPorArtista(nickname);

        Collection<String> nombreAlbum = new ArrayList<>();

        for (Album album : albumes) {
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

        DAO_Usuario daoUsuario = new DAO_Usuario();

        Collection<ListaPorDefecto> listasD = daoUsuario.obtenerListasFavPorDefectoCliente2(nickname);
        Collection<ListaParticular> listasP = daoUsuario.obtenerListasParticularesFavCliente2(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");

        for (ListaPorDefecto lista : listasD) {

            jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista() + "-").append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        for (ListaParticular lista : listasP) {
            String nombrelista = lista.getNombreLista() + "/" + lista.getNombreCliente();
            jsonResponse.append("{\"nombre\":\"").append(nombrelista).append("\",")
                    .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
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

        DAO_Usuario daoUsuario = new DAO_Usuario();

        Collection<Album> albumes = daoUsuario.obtenerAlbumFavCliente2(nickname);

        StringBuilder jsonResponse = new StringBuilder("[");
        for (Album album : albumes) {

            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                    .append("\"imagen\":\"").append(album.getImagen()).append("\"},");
        }

        if (jsonResponse.length() > 1) {
            jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
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

        DAO_Usuario daoUsuario = new DAO_Usuario();
        Collection<DT_IdTema> temas = daoUsuario.obtenerTemaFavCliente(nickname);

        // Crear una lista para almacenar los temas como JSON
        List<Map<String, String>> temasJson = new ArrayList<>();

        for (DT_IdTema tema : temas) {
            Map<String, String> temaMap = new HashMap<>();
            temaMap.put("nombreTema", tema.getNombreTema());
            temaMap.put("nombreAlbumTema", tema.getNombreAlbumTema());
            temasJson.add(temaMap);
        }

        // Convertir la lista de mapas a JSON
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
