package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
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

public class SeguirUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Seguir Usuario Servlet GET-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String nickname = (String) session.getAttribute("nickname");
        String nickToCheck = request.getParameter("id"); // Usuario a verificar

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

        Collection<String> usuariosSeguidos;
        Collection<String> usuariosProcesados;

        try {
            // Obtener la lista de usuarios seguidos
            usuariosSeguidos = publicador.obtenerSeguidosDe(nickname);

            // Procesar los usuarios seguidos para quedarnos solo con el username
            usuariosProcesados = obtenerUsuariosSinTipo(usuariosSeguidos);

        } catch (Exception e) {
            out.println("{\"success\": false, \"error\": \"Error al obtener usuarios: " + e.getMessage() + "\"}");
            return;
        }

        // Verificar si el usuario a seguir está en la lista
        boolean isFollowed = false;
        if (usuariosSeguidos != null) {
            for (String usuario : usuariosProcesados) {
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

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        Boolean suscrito = (Boolean) session.getAttribute("suscrito");

        if (nickname == null) {
            out.println("{\"success\": false, \"error\": \"Usuario no autenticado.\"}");
            return; // Salir si no se encuentra el usuario
        }

        if (suscrito != true) {
            out.println("{\"success\": false, \"error\": \"No posees una suscripcion vigente.\"}");
            return;
        }

        // Obtener el nickname del usuario a seguir desde el cuerpo de la solicitud
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        String nickToFollow = null;

        try {
            JSONObject jsonObject = new JSONObject(body);
            nickToFollow = jsonObject.getString("id");
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }

        // URL del WSDL
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        boolean success = publicador.seguirUsuario(nickname, nickToFollow);

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
