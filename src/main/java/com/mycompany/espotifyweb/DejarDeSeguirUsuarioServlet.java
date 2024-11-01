package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import logica.Usuario;
import logica.controladores.ControladorCliente;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import persistencia.DAO_Usuario;

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

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return;
        }

        DAO_Usuario daoUsuario = new DAO_Usuario();
        Collection<Usuario> usuariosSeguidos;

        try {
            usuariosSeguidos = daoUsuario.obtenerSeguidosDeUsuarioObjetos(nickname);
        } catch (Exception e) {
            out.println("{\"success\": false, \"error\": \"Error al obtener usuarios: " + e.getMessage() + "\"}");
            return; // Salir si hay un error al obtener usuarios
        }

        // Construir respuesta JSON
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{\"usuarios\": [");

        if (usuariosSeguidos != null && !usuariosSeguidos.isEmpty()) {
            for (int i = 0; i < usuariosSeguidos.size(); i++) {
                Usuario usuario = usuariosSeguidos.toArray(new Usuario[0])[i];
                String tipo = usuario.getDTYPE(); // Asegúrate de tener un método para obtener el tipo
                jsonResponse
                        .append("{\"nickname\": \"").append(escapeJson(usuario.getNickname()))
                        .append("\", \"tipo\": \"").append(escapeJson(tipo))
                        .append("\"}");
                if (i < usuariosSeguidos.size() - 1) {
                    jsonResponse.append(","); // Agregar coma solo si no es el último elemento
                }
            }
        }

        jsonResponse.append("]}");

        System.out.println("JSON Response: " + jsonResponse.toString()); // Log para depuración

        out.print(jsonResponse.toString());
        out.flush();

        System.out.println("\n-----End Seguir Usuario Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener la sesión
        HttpSession session = request.getSession();
        System.out.println("Sesión obtenida: " + session.getId());

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        System.out.println("Nickname del usuario en sesión: " + nickname);

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            System.out.println("Error: Usuario no autenticado.");
            return; // Salir si no se encuentra el usuario
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

        ControladorCliente controladorCliente = new ControladorCliente();
        System.out.println("Llamando a dejarDeSeguirUsuarioWeb con " + nickname + " y " + nickToFollow);

        // Llama al método para dejar de seguir al usuario
        boolean success = controladorCliente.dejarDeSeguirUsuarioWeb(nickname, nickToFollow);

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
