package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataAlbum;
import servicios.IPublicador;

public class TodosLosAlbumesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Todos Los Albumes Servlet GET-----");
        
        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");
        
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if ("devolverAlbumes".equals(action)) {
            try (PrintWriter out = response.getWriter()) {

                List<DataAlbum> albums = publicador.obtenerDataAlbumes();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataAlbum album : albums) {
                    jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                            .append("\"creador\":\"").append(album.getCreador().getNickname()).append("\",")
                            .append("\"imagen\":\"").append(album.getImagen()).append("\"},");
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
        System.out.println("\n-----End Todos Los Albumes Servlet GET-----");
    }

}
