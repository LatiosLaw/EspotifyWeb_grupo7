package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataAlbum;
import servicios.DataListaPorDefecto;
import servicios.IPublicador;

@MultipartConfig

public class TodoLoDeUnGeneroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Todo Lo De Un Genero Servlet GET-----");

        String action = request.getParameter("action");

        String busqueda = request.getParameter("buscar");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if ("MostrarListasDelGeneroU".equals(action)) {

            try (PrintWriter out = response.getWriter()) {

                Collection<DataListaPorDefecto> listasReproduccion = publicador.obtenerDataListasPorGenero(busqueda);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataListaPorDefecto lista : listasReproduccion) {

                    jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                            .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                            .append("\"imagen\":\"").append(lista.getFoto())
                            .append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("MostrarAlbumesDelGeneroU".equals(action)) {
            try (PrintWriter out = response.getWriter()) {

                Collection<DataAlbum> albumes = publicador.obtenerDataAlbumesPorGenero(busqueda);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataAlbum album : albumes) {
                    jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                            .append("\"artista\":\"").append(album.getCreador().getNickname()).append("\",")
                            .append("\"anio\":\"").append(album.getAnioCreacion()).append("\",")
                            .append("\"imagen\":\"").append(album.getImagen())
                            .append("\"},");
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

    }

}
