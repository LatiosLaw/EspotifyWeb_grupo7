package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.Album;
import logica.ListaParticular;
import logica.Usuario;
import logica.controladores.ControladorCliente;
import logica.dt.DT_IdTema;
import persistencia.DAO_Album;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Usuario;

public class ConsultarUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        switch (action) {
            case "cargarPerfil":
                cargarPerfil(request, out);
                break;
            case "cargarSeguidores":
                cargarSeguidores(request, out);
                break;
            case "cargarSeguidos":
                cargarSeguidos(request, out);
                break;
            case "cargarListas":
                cargarListas(request, out);
                break;
            case "cargarAlbumes":
                cargarAlbumes(request, out);
                break;
            case "cargarFavoritos":
                cargarFavoritos(request, out);
                break;
            case "cargarListasFavoritas":
                cargarListasFavoritas(request, out);
                break;
            case "cargarAlbumesFavoritos":
                cargarAlbumesFavoritos(request, out);
                break;
            case "cargarTemasFavoritos":
                cargarTemasFavoritos(request, out);
                break;
            default:
                out.println("{\"error\": \"Acción no válida\"}");
                break;
        }
    }

    private void cargarPerfil(HttpServletRequest request, PrintWriter out) {

        String nickname = request.getParameter("nickname");

        DAO_Usuario daoUser = new DAO_Usuario();
        Usuario user = daoUser.findUsuarioByNick(nickname);

        if (user != null) {
            String jsonPerfil = String.format(
                    "{\"nickname\": \"%s\", \"correo\": \"%s\", \"nombre\": \"%s\", \"apellido\": \"%s\", \"fechaNacimiento\": \"%s\", \"imagen\": \"%s\", \"esArtista\": %b}",
                    user.getNickname(),
                    user.getEmail(),
                    user.getNombre(),
                    user.getApellido(),
                    user.getNacimiento(),
                    user.getFoto() != null ? user.getFoto() : "imagenes/usuarios/defaultUser.png",
                    user.getDTYPE()
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

        Collection<String> nombreListaParticular = new ArrayList<>();

        for (ListaParticular lista : listas) {
            nombreListaParticular.add(lista.getNombreLista()+"/"+lista.getNombreCliente());
        }

        String jsonListas = convertToJson(nombreListaParticular);

        out.println(jsonListas);
    }

    private void cargarAlbumes(HttpServletRequest request, PrintWriter out) {

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

        Collection<String> listasD = daoUsuario.obtenerListasFavPorDefectoCliente(nickname);
        Collection<ListaParticular> listasP = daoUsuario.obtenerListasParticularesFavCliente2(nickname);

        Collection<String> listas = new ArrayList<>();

        for (String listad : listasD) {
            listas.add(listad + "‎-");
        }

        for (ListaParticular listap : listasP) {
            listas.add(listap.getNombreLista()+"/"+listap.getNombreCliente());
        }

        String jsonListas = convertToJson(listas);

        out.println(jsonListas);
    }

    private void cargarAlbumesFavoritos(HttpServletRequest request, PrintWriter out) {

       String nickname = request.getParameter("nickname");

        if (nickname == null) {
            out.println("{\"error\": \"Nickname no encontrado en la sesión\"}");
            return;
        }

        DAO_Usuario daoUsuario = new DAO_Usuario();

        Collection<String> albumes = daoUsuario.obtenerAlbumFavCliente(nickname);

        String jsonAlbumes = convertToJson(albumes);

        out.println(jsonAlbumes);
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
