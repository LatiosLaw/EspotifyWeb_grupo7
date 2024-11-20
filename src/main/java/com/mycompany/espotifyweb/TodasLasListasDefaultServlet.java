package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.IPublicador;
import servicios.DataListaPorDefecto;

public class TodasLasListasDefaultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Todas Las Listas Servlet GET-----");
        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");

        if ("devolverListas".equals(action)) {
            try (PrintWriter out = response.getWriter()) {

                URL url = new URL("http://localhost:9128/publicador?wsdl");
                QName qname = new QName("http://servicios/", "PublicadorService");
                Service servicio = Service.create(url, qname);
                IPublicador publicador = servicio.getPort(IPublicador.class);
                Collection<DataListaPorDefecto> listas = publicador.obtenerDataListaPorDefecto();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataListaPorDefecto lista : listas) {
                    jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                            .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                            .append("\"imagen\":\"").append(lista.getFoto()).append("\"},");
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
        System.out.println("\n-----End Todas Las Listas Servlet GET-----");
    }

}
