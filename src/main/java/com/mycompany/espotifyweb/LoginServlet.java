package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.MalformedURLException;
import javax.xml.ws.Service; // Asegúrate de que esta clase esté disponible en tu classpath
import java.net.URL;
import javax.xml.namespace.QName; // Este import sigue siendo de javax, ya que es parte de JAX-WS
import servicios.DataErrorBundle;
import servicios.IPublicador;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (null == action) {
            processRequest(request, response);
        } else {
            switch (action) {
                case "obtenerTipoUsuario" -> {
                    try {
                        getUserType(request, response);
                    } catch (IOException e) {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        PrintWriter out = response.getWriter();
                        out.print("{\"error\": \"Error al obtener el tipo de usuario\"}");
                        out.flush();
                    }
                }
                case "obtenerSuscripcion" -> {
                    try {
                        getSuscripcion(request, response);
                    } catch (IOException e) {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        PrintWriter out = response.getWriter();
                        out.print("{\"error\": \"Error al obtener el estado de la suscripcion del usuario\"}");
                        out.flush();
                    }
                }
                case "obtenerTipoUsuario2" -> {
                    try {
                        getUserType2(request, response);
                    } catch (IOException e) {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        PrintWriter out = response.getWriter();
                        out.print("{\"error\": \"Error al obtener el tipo de usuario\"}");
                        out.flush();
                    }
                }
                case "obtenerSuscripcion2" -> {
                    try {
                        getSuscripcion2(request, response);
                    } catch (IOException e) {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        PrintWriter out = response.getWriter();
                        out.print("{\"error\": \"Error al obtener el estado de la suscripcion del usuario\"}");
                        out.flush();
                    }
                }
                default ->
                    processRequest(request, response);
            }
        }
    }

    public void getUserType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String userType = getUserTypeFromSession(request); // Llama al método que ya tienes

        out.print("{\"userType\": \"" + (userType != null ? userType : "") + "\"}");
        out.flush();
    }

    public String getUserTypeFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("userType");
    }

    public void getSuscripcion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Boolean estaSuscrito = getSuscripcionFromSession(request); // Llama al método que ya tienes

        out.print("{\"suscrito\": " + (estaSuscrito != null ? estaSuscrito.toString() : "null") + "}");
        out.flush();
    }

    public Boolean getSuscripcionFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Boolean) session.getAttribute("suscrito");
    }

    public void getUserType2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String userType = getUserTypeFromSession2(request); // Llama al método que ya tienes

        out.print("{\"userType\": \"" + (userType != null ? userType : "") + "\"}");
        out.flush();
    }

    public String getUserTypeFromSession2(HttpServletRequest request) throws MalformedURLException {
        String nickname = request.getParameter("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        String resultado;

        // Llamada al método de login del servicio SOAP
        resultado = publicador.getTipoUsuario(nickname);

        return resultado;
    }

    public void getSuscripcion2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Boolean estaSuscrito = getSuscripcionFromSession2(request); // Llama al método que ya tienes

        out.print("{\"suscrito\": " + (estaSuscrito != null ? estaSuscrito.toString() : "null") + "}");
        out.flush();
    }

    public Boolean getSuscripcionFromSession2(HttpServletRequest request) throws MalformedURLException {
        String nickname = request.getParameter("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        Boolean resultado;

        resultado = publicador.getSuscripcion(nickname);

        return resultado;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n---------Login Servlet----------");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener los parámetros del formulario
        String nickname = request.getParameter("nicknameLogin");
        String pass = request.getParameter("passLogin");

        DataErrorBundle resultado;

        try {
            // URL del WSDL
            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");

            // Crear el servicio
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);

            // Llamada al método de login del servicio SOAP
            resultado = publicador.iniciarSesion(nickname, pass);

            if (resultado.isValor()) {
                // Invalidar sesión previa si existe
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                // Obtener el tipo de usuario desde el publicador
                String userType = publicador.getTipoUsuario(nickname);
                System.out.println("Servlet: El tipo de usuario es: " + userType);

                // Verificar si userType es null y buscar en la cookie
                if (userType == null) {
                    // Obtener las cookies de la solicitud
                    Cookie[] cookies = request.getCookies();
                    String tipoUsuarioDesdeCookie = null;

                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if ("tipoUsuario".equals(cookie.getName())) {
                                tipoUsuarioDesdeCookie = cookie.getValue();
                                break;
                            }
                        }
                    }

                    if (tipoUsuarioDesdeCookie != null) {
                        if ("cliente".equals(tipoUsuarioDesdeCookie)) {
                            userType = "Cliente";
                        } else if ("artista".equals(tipoUsuarioDesdeCookie)) {
                            userType = "Artista";
                        } else {
                            out.print("{\"success\": false, \"errorCode\": 400, \"message\": \"Tipo de usuario no válido\"}");
                            return;
                        }
                        System.out.println("Tipo de usuario obtenido de la cookie: " + userType);
                    } else {
                        out.print("{\"success\": false, \"errorCode\": 404, \"message\": \"Usuario no encontrado\"}");
                        return;
                    }
                }

                Boolean suscrito = publicador.getSuscripcion(nickname);

                // Crear nueva sesión
                session = request.getSession();
                session.setAttribute("nickname", nickname);
                session.setAttribute("userType", userType);
                session.setAttribute("suscrito", suscrito);

                // Respuesta exitosa
                out.print("{\"success\": true, \"userType\": \"" + userType + "\", \"suscrito\": " + suscrito + "}");
            } else {
                out.print("{\"success\": false, \"errorCode\": " + resultado.getNumero() + ", \"message\": \"" + resultado.isValor() + "\"}");
            }
        } catch (MalformedURLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"error\": \"Error en el servicio\"}");
        } finally {
            out.flush();
        }
    }

}
