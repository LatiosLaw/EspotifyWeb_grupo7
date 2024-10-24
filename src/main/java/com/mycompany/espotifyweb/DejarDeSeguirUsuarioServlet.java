package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return;
        }

        // Obtener el nickname del usuario a seguir desde el cuerpo de la solicitud
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        // Parsear el JSON para obtener el nickname del usuario a seguir
        String nickToFollow = null;

        try {
            JSONObject jsonObject = new JSONObject(body);
            nickToFollow = jsonObject.getString("id"); // Suponiendo que "id" es el campo que contiene el nickname del usuario a seguir
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }

        // Llamar al método seguirUsuario de ControladorCliente
        ControladorCliente controladorCliente = new ControladorCliente();

        boolean success = controladorCliente.dejarDeSeguirUsuarioWeb(nickname, nickToFollow); // Captura el resultado de la operación

        // Construir respuesta JSON
        if (success) {
            out.println("{\"success\": true}");
            System.out.println(nickname + " ahora sigue a " + nickToFollow);
        } else {
            out.println("{\"success\": false, \"error\": \"Error al dejar de seguir al usuario.\"}");
            System.out.println("Error al dejar de seguir a " + nickToFollow);
        }

        out.flush();
    }

}
