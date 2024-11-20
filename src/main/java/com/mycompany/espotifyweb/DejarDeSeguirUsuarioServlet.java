package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "DejarDeSeguirUsuarioServlet", urlPatterns = {"/DejarDeSeguirUsuarioServlet"})
public class DejarDeSeguirUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DejarDeSeguirUsuarioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DejarDeSeguirUsuarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        System.out.println("\n-----Dejar De Seguir Usuario Servlet GET-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return;
        }

        // URL del WSDL
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        Collection<String> usuariosSeguidos, usuariosProcesados;

        try {
            usuariosSeguidos = publicador.obtenerSeguidosDe(nickname);
            // Procesar los usuarios seguidos para quedarnos solo con el username y el tipo
            usuariosProcesados = obtenerUsuariosSinTipo(usuariosSeguidos);
        } catch (Exception e) {
            out.println("{\"success\": false, \"error\": \"Error al obtener usuarios: " + e.getMessage() + "\"}");
            return; // Salir si hay un error al obtener usuarios
        }

        // Construir respuesta JSON
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{\"usuarios\": [");

        if (usuariosSeguidos != null && !usuariosSeguidos.isEmpty()) {
            for (int i = 0; i < usuariosSeguidos.size(); i++) {
                String usuario = usuariosSeguidos.toArray(new String[0])[i];

                // Separar nickname y tipo
                int slashIndex = usuario.indexOf('/');
                if (slashIndex != -1) {
                    String nicknameSeguido = usuario.substring(0, slashIndex); // Obtener el nickname
                    String tipo = usuario.substring(slashIndex + 1); // Obtener el tipo (después del '/')

                    // Construir la parte del JSON
                    jsonResponse
                            .append("{\"nickname\": \"").append(escapeJson(nicknameSeguido))
                            .append("\", \"tipo\": \"").append(escapeJson(tipo))
                            .append("\"}");
                }
                if (i < usuariosSeguidos.size() - 1) {
                    jsonResponse.append(",");
                }
            }
        }

        jsonResponse.append("]}");

        System.out.println("JSON Response: " + jsonResponse.toString()); // Log para depuración

        out.print(jsonResponse.toString());
        out.flush();

        System.out.println("\n-----End Dejar De Seguir Usuario Servlet GET-----");
    }

    private Collection<String> obtenerUsuariosSinTipo(Collection<String> usuariosSeguidos) {
        Collection<String> usuariosProcesados = new ArrayList<>();

        for (String usuario : usuariosSeguidos) {
            // Buscar el índice del primer '/'
            int slashIndex = usuario.indexOf('/');

            if (slashIndex != -1) {
                // Si se encuentra '/', extraer solo la parte antes del '/'
                usuariosProcesados.add(usuario.substring(0, slashIndex));
            } else {
                // Si no se encuentra '/', agregar el nombre completo
                usuariosProcesados.add(usuario);
            }
        }

        return usuariosProcesados;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        System.out.println("Sesión obtenida: " + session.getId());
        String nickname = (String) session.getAttribute("nickname");
        System.out.println("Nickname del usuario en sesión: " + nickname);

        Boolean suscrito = (Boolean) session.getAttribute("suscrito");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            System.out.println("Error: Usuario no autenticado.");
            return; // Salir si no se encuentra el usuario
        }

        if (suscrito != true) {
            out.println("{\"success\": false, \"error\": \"No posees una suscripcion vigente.\"}");
            return;
        }

        // Obtener el nickname del usuario a dejar de seguir desde el cuerpo de la solicitud
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Cuerpo de la solicitud: " + body);

        String nickToFollow = null;

        try {
            JSONObject jsonObject = new JSONObject(body);
            nickToFollow = jsonObject.getString("id"); // Suponiendo que "id" es el campo que contiene el nickname del usuario a dejar de seguir
            System.out.println("Nickname del usuario a dejar de seguir: " + nickToFollow);
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            System.out.println("Error al procesar el JSON: " + e.getMessage());
            return; // Salir si hay un error al procesar el JSON
        }

        // URL del WSDL
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        System.out.println("Llamando a dejarDeSeguirUsuarioWeb con " + nickname + " y " + nickToFollow);

        // Llama al método para dejar de seguir al usuario
        boolean success = publicador.dejarDeSeguirUsuario(nickname, nickToFollow);

        // Construir respuesta JSON
        if (success) {
            out.println("{\"success\": true}");
            System.out.println(nickname + " ha dejado de seguir a " + nickToFollow);
        } else {
            out.println("{\"success\": false, \"error\": \"Error al dejar de seguir al usuario.\"}");
            System.out.println("Error al dejar de seguir a " + nickToFollow);
        }

        out.flush();
    }

}
