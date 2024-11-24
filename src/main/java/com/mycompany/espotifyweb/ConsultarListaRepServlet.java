package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
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
import servicios.RegistroTema;
import servicios.DataListaParticular;
import servicios.DataListaPorDefecto;
import servicios.DataTema;
import servicios.IPublicador;

public class ConsultarListaRepServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        if (action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El parametro 'action' es requerido");
            return;
        }

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        System.out.println("Accion recibida: " + action);

        switch (action) {
            case "getTemasPorLista" -> {
                String nombreLista = request.getParameter("listaNombre");

                System.out.println("Nombre de la lista recibido: " + nombreLista);

                String tipo = request.getParameter("tipo");

                System.out.println("Tipo de la lista recibido: " + tipo);

                if (tipo.equals("2")) {
                    nombreLista = nombreLista.split("/")[0];
                }

                if (nombreLista == null || nombreLista.isEmpty() || tipo == null || tipo.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
                    return;
                }

                obtenerTemasPorLista(nombreLista, tipo, nickname, out, response);
            }
            case "devolverInformacionLista" -> {
                try {
                    String nombreLista = request.getParameter("listaNombre");
                    String tipo = request.getParameter("tipo");

                    if (tipo.equals("2")) {
                        nombreLista = nombreLista.split("/")[0];
                    }

                    StringBuilder jsonResponse = new StringBuilder("[");
                    List<String> listasCole = nickname != null ? publicador.obtenerNombreListasFavCliente(nickname) : new ArrayList<>();

                    if (tipo.equals("1")) {
                        // Para listas por defecto
                        String tieneLaik = nickname != null ? publicador.corroborarListaEnFav(nombreLista, "Por Defecto", listasCole) : "noLaik";

                        DataListaPorDefecto lista = publicador.retornarDataListaPorDefecto2(nombreLista);
                        if (lista != null) {
                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                                    .append("\"imagen\":\"").append(lista.getFoto()).append("\",")
                                    .append("\"tipo\":\"").append("1").append("\",")
                                    .append("\"fav\":\"").append(tieneLaik).append("\",")
                                    .append("\"adicional\":\"").append(lista.getGenero().getNombre()).append("\"},");
                        } else {
                            System.out.println("No se encontró la lista por defecto: " + nombreLista);
                        }

                    } else {
                        // Para listas particulares
                        String usuario = request.getParameter("usuario");
                        if (usuario == null && nickname == null) {
                            usuario = "visitante"; // Manejo para visitantes
                        }
                        String tieneLaik = publicador.corroborarListaEnFav(nombreLista, usuario, listasCole);
                        DataListaParticular lista = publicador.retornarDataListaParticular(nombreLista, nickname); // Usar nickname aquí
                        if (lista != null) {
                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                                    .append("\"imagen\":\"").append(lista.getFoto()).append("\",")
                                    .append("\"tipo\":\"").append("2").append("\",")
                                    .append("\"fav\":\"").append(tieneLaik).append("\",")
                                    .append("\"adicional\":\"").append(lista.getCreador().getNickname()).append("\"},");
                        } else {
                            System.out.println("No se encontró la lista particular: " + nombreLista);
                        }
                    }

                    jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                    jsonResponse.append("]");
                    out.print(jsonResponse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            case "devolverInformacionTema" -> {
                String nombreTema = request.getParameter("nombreTema");
                System.out.println(nombreTema);
                String albumName = request.getParameter("nombreAlbum");
                System.out.println(albumName);

                RegistroTema info = publicador.devolverRegistroTema(nombreTema, albumName);
                try {
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
            default ->
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }

        out.flush();
    }

    private void obtenerTemasPorLista(String nombreLista, String tipo, String nickname, PrintWriter out, HttpServletResponse response) throws IOException {

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if (nombreLista == null || nombreLista.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
            return;
        }

        try {
            List<DataTema> temas = publicador.obtenerTemasDeListas(nombreLista, Integer.valueOf(tipo));

            if (temas.isEmpty()) {
                out.print("[]");
                return;
            }

            List<String> cole = publicador.obtenerNombreTemasFavCliente(nickname);

            if (cole == null) {
                cole = new ArrayList<>();
            }

            StringBuilder jsonResponse = new StringBuilder("[");

            for (DataTema tema : temas) {
                String tieneLaik = "false";

                if (!cole.isEmpty()) {
                    tieneLaik = publicador.corroborarTemaEnFav(tema.getNickname(), cole);
                }

                System.out.println("Tiene laik: " + tieneLaik);
                System.out.println(tema.getArchivo());

                jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                        .append("\"album\":\"").append(tema.getNomAlb()).append("\",")
                        .append("\"duracion\":\"").append(tema.getDuracion()).append("\",")
                        .append("\"archivo\":\"").append(tema.getArchivo()).append("\",")
                        .append("\"fav\":\"").append(tieneLaik).append("\",")
                        .append("\"identificador_archivo\":\"").append(tema.getArchivo()).append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1);
            }
            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los temas");
        }
    }

}
