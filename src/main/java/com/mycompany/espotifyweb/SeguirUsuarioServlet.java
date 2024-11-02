package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.controladores.ControladorCliente;
import org.eclipse.persistence.exceptions.JSONException;
import persistencia.DAO_Usuario;
import org.json.JSONObject;

public class SeguirUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SeguirUsuarioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SeguirUsuarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String escapeJson(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("\\", "\\\\") // Escapa el caracter de barra invertida
                .replace("\"", "\\\"") // Escapa las comillas dobles
                .replace("\b", "\\b") // Escapa la retroceso
                .replace("\f", "\\f") // Escapa el avance de página
                .replace("\n", "\\n") // Escapa la nueva línea
                .replace("\r", "\\r") // Escapa el retorno de carro
                .replace("\t", "\\t");    // Escapa la tabulacion
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("\n-----Seguir Usuario Servlet GET-----");

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    // Obtener la sesión
    HttpSession session = request.getSession();

    // Leer el nickname desde la sesión
    String nickname = (String) session.getAttribute("nickname");
    String nickToCheck = request.getParameter("id"); // Usuario a verificar

    if (nickname == null) {
        out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
        return;
    }

    DAO_Usuario daoUsuario = new DAO_Usuario();
    Collection<String> usuariosSeguidos;

    try {
        // Obtener la lista de usuarios seguidos
        usuariosSeguidos = daoUsuario.obtenerSeguidosDeUsuario(nickname);
    } catch (Exception e) {
        out.println("{\"success\": false, \"error\": \"Error al obtener usuarios: " + e.getMessage() + "\"}");
        return;
    }

    // Verificar si el usuario a seguir está en la lista
    boolean isFollowed = false;
    if (usuariosSeguidos != null) {
        for (String usuario : usuariosSeguidos) {
            if (usuario.equals(nickToCheck)) {
                isFollowed = true;
                break;
            }
        }
    }

    // Construir respuesta JSON
    out.println("{\"isFollowed\": " + isFollowed + "}");
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
            return; // Salir si no se encuentra el usuario
        }

        // Obtener el nickname del usuario a seguir desde el cuerpo de la solicitud
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        String nickToFollow = null;

        try {
            JSONObject jsonObject = new JSONObject(body);
            nickToFollow = jsonObject.getString("id"); // Suponiendo que "id" es el campo que contiene el nickname del usuario a seguir
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }

        ControladorCliente controladorCliente = new ControladorCliente();

        // Llama al método para seguir al usuario
        boolean success = controladorCliente.seguirUsuarioWeb(nickname, nickToFollow);

        // Construir respuesta JSON
        if (success) {
            out.println("{\"success\": true}");
            System.out.println(nickname + " ahora sigue a " + nickToFollow);
        } else {
            out.println("{\"success\": false, \"error\": \"Error al seguir al usuario.\"}");
            System.out.println("Error al seguir a " + nickToFollow);
        }

        out.flush();
    }

}
