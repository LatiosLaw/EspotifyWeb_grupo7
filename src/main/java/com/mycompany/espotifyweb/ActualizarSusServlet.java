package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataSus;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "ActualizarSusServlet", urlPatterns = {"/ActualizarSusServlet"})
public class ActualizarSusServlet extends HttpServlet {

    private String escapeJson(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("\\", "\\\\") // Escapa el carácter de barra invertida
                .replace("\"", "\\\"") // Escapa las comillas dobles
                .replace("\b", "\\b") // Escapa la retroceso
                .replace("\f", "\\f") // Escapa el avance de página
                .replace("\n", "\\n") // Escapa la nueva línea
                .replace("\r", "\\r") // Escapa el retorno de carro
                .replace("\t", "\\t");    // Escapa la tabulación
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Actualizar sus Servlet GET-----");

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return;
        }

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        Collection<DataSus> cole;

        try {
            cole = publicador.obtenerDataSuscripciones(nickname);
        } catch (Exception e) {
            out.println("{\"success\": false, \"error\": \"Error al obtener Sus: " + e.getMessage() + "\"}");
            return;
        }
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{\"sus\": [");

        if (cole != null && !cole.isEmpty()) {
            for (int i = 0; i < cole.size(); i++) {
                DataSus data = cole.toArray(DataSus[]::new)[i];

                jsonResponse.append("{\"id\": \"").append(escapeJson(String.valueOf(String.valueOf(data.getId()))))
                        .append("\", \"nick\": \"").append(escapeJson(data.getUserNick()))
                        .append("\", \"fecha\": \"").append(escapeJson(String.valueOf(data.getUltiFechaHabi())))
                        .append("\", \"tipo\": \"").append(escapeJson(data.getTipoSus()))
                        .append("\", \"estado\": \"").append(escapeJson(data.getEstado()))
                        .append("\"}");
                if (i < cole.size() - 1) {
                    jsonResponse.append(","); // Agregar coma solo si no es el último elemento
                }
            }
        }

        jsonResponse.append("]}");

        System.out.println("JSON Response: " + jsonResponse.toString()); // Log para depuración

        out.print(jsonResponse.toString());
        out.flush();

        System.out.println("\n-----Actualizar sus Servlet GET-----");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Actualizar sus Servlet POST-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return;
        }

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        String idSus = null;
        String estadoSus = null;

        try {
            JSONObject jsonObject = new JSONObject(body);
            idSus = jsonObject.getString("id");
            estadoSus = jsonObject.getString("estado");

            System.out.println("idSus: " + idSus + "/estadoSus: " + estadoSus);

        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        int idPosta;
        try {
            idPosta = Integer.parseInt(idSus);
        } catch (NumberFormatException e) {
            idPosta = 0;
        }
        if (idPosta > 0) {

            DataSus token = publicador.obtenerDataSuscripcionPorID(idPosta);
            if (token != null) {
                publicador.actualizarSuscripcion(idPosta, estadoSus);
                System.out.println("\n-----Success-----");
                out.println("{\"success\": true}");
            } else {
                System.out.println("\n-----No existe Sus con ese id.-----");
                out.println("{\"success\": false, \"error\": \"No existe Sus con ese id.\"}");
            }
        } else {
            System.out.println("\n-----Error a cargar los datos.-----");
            out.println("{\"success\": false, \"error\": \"Error a cargar los datos.\"}");
        }
        out.flush();
    }

}
