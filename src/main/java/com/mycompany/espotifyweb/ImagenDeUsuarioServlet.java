package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.Usuario;
import persistencia.DAO_Usuario;

public class ImagenDeUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        
        switch(action){
            case "cargarImagenUser":
                cargarImagenUser(request, out);
                break;
            default:
                out.println("{\"error\": \"Acción no válida\"}");
                break;
        }
    }

    private void cargarImagenUser(HttpServletRequest request, PrintWriter out){
        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        DAO_Usuario daoUser = new DAO_Usuario();
        Usuario user = daoUser.findUsuarioByNick(nickname);

        if (user != null) {
            StringBuilder jsonResponse = new StringBuilder("[");
                
                        jsonResponse.append("{\"nickname\":\"").append(user.getNickname()).append("\",")
                            .append("\"imagen\":\"").append(user.getFoto()).append("\"},");

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la Ãºltima coma
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