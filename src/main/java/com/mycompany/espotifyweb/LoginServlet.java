package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import logica.Usuario;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorSuscripcion;
import logica.dt.DataErrorBundle;
import persistencia.DAO_Usuario;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        
       
        if ("obtenerTipoUsuario".equals(action)) {
            try {
                getUserType(request, response);
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Error al obtener el tipo de usuario\"}");
                out.flush();
            }
        } else if ("obtenerSuscripcion".equals(action)) {
            try {
                getSuscripcion(request, response);
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Error al obtener el estado de la suscripcion del usuario\"}");
                out.flush();
            }
        } else if ("obtenerTipoUsuario2".equals(action)) {
            try {
                getUserType2(request, response);
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Error al obtener el tipo de usuario\"}");
                out.flush();
            }
        } else if ("obtenerSuscripcion2".equals(action)) {
            try {
                getSuscripcion2(request, response);
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Error al obtener el estado de la suscripcion del usuario\"}");
                out.flush();
            }
        } else {
            processRequest(request, response);
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

    public String getUserTypeFromSession2(HttpServletRequest request) {
        DAO_Usuario controladorUsr = new DAO_Usuario();
        String nickname = request.getParameter("nickname");
        String tipo_usuario = controladorUsr.findUsuarioByNick(nickname).getDTYPE();
        return tipo_usuario;
    }

    public void getSuscripcion2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Boolean estaSuscrito = getSuscripcionFromSession2(request); // Llama al método que ya tienes

        out.print("{\"suscrito\": " + (estaSuscrito != null ? estaSuscrito.toString() : "null") + "}");
        out.flush();
    }

    public Boolean getSuscripcionFromSession2(HttpServletRequest request) {
        ControladorSuscripcion controladorSus = new ControladorSuscripcion();
        String nickname = request.getParameter("nickname");
        Boolean suscrito = controladorSus.tieneSusValida(nickname);
        return suscrito;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n---------Login Servlet----------");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String nickname = request.getParameter("nicknameLogin");
        String pass = request.getParameter("passLogin");

        DAO_Usuario persistence = new DAO_Usuario();
        

        ControladorCliente controlador = new ControladorCliente();
        DataErrorBundle resultado = controlador.iniciarSesion(nickname, pass);

        if (resultado.getValor()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            Usuario usuario = persistence.findUsuarioByNick(nickname);
            
            System.out.println("El tipo de usuario es: " + usuario.getDTYPE());
            String userType = null;

            if (usuario != null) {
                userType = usuario.getDTYPE();
                if (userType == null) {
                    System.out.println("El tipo de usuario es nulo.");
                } else {
                    System.out.println("Tipo de usuario: " + userType);
                }
            } else {
                System.out.println("Usuario no encontrado.");
                out.print("{\"success\": false, \"errorCode\": 404, \"message\": \"Usuario no encontrado\"}");
                out.flush();
                return;
            }

            ControladorSuscripcion controladorSus = new ControladorSuscripcion();
            Boolean suscrito = controladorSus.tieneSusValida(nickname);
            System.out.println("Suscrito: " + suscrito);
            session = request.getSession();

            session.setAttribute("nickname", nickname);
            session.setAttribute("userType", userType);
            session.setAttribute("suscrito", suscrito);

            out.print("{\"success\": true}");
        } else {
            out.print("{\"success\": false, \"errorCode\": " + resultado.getNumero() + "}");
        }

        out.flush();
    }

}
