package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataUsuario;
import servicios.IPublicador;

public class ImagenDeUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        switch (action) {
            case "cargarImagenUser" ->
                cargarImagenUser(request, out);
            default ->
                out.println("{\"error\": \"Acción no válida\"}");
        }
    }

    private void cargarImagenUser(HttpServletRequest request, PrintWriter out) throws MalformedURLException {
        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        DataUsuario user = publicador.retornarUsuario(nickname);

        if (user != null) {
            StringBuilder jsonResponse = new StringBuilder("[");

            jsonResponse.append("{\"nickname\":\"").append(user.getNickname()).append("\",")
                    .append("\"imagen\":\"").append(user.getFoto()).append("\"},");

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1);
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } else {
            out.println("{\"error\": \"Usuario no encontrado\"}");
        }
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
