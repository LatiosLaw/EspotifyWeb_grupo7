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
import servicios.DataCliente;
import servicios.IPublicador;

public class TodosLosClientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Todos Los Clientes Servlet GET-----");

        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if ("devolverClientes".equals(action)) {
            try (PrintWriter out = response.getWriter()) {

                List<DataCliente> clientesList = publicador.obtenerDataClientes();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataCliente cliente : clientesList) {

                    jsonResponse.append("{\"nombre\":\"").append(cliente.getNickname()).append("\",")
                            .append("\"imagen\":\"").append(cliente.getFoto())
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
        System.out.println("\n-----End Todos Los Clientes Servlet GET-----");
    }

}
