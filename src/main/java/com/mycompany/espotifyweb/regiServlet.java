/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.controladores.ControladorAlbum;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorSuscripcion;
import logica.controladores.ControladorTema;
import logica.dt.DataAlbum;
import logica.dt.DataCliente;
import logica.dt.DataTema;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Urbina
 */
@WebServlet(name = "regiServlet", urlPatterns = {"/regiServlet"})
public class regiServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet regiServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet regiServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n-----regiServlet GET-----");
        
       
        System.out.println("\n-----regiServlet GET-----");

    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n-----regiServlet POST-----");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        LocalDate hoy = LocalDate.now();
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
         String os = null;
         String navegador = null;
         
         
         
        try {
            JSONObject jsonObject = new JSONObject(body);
            os = jsonObject.getString("os"); 
            
            navegador = jsonObject.getString("nave");
            
           

            System.out.println("os: " + os + "/navegaDoor: " + navegador);
            
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");
 
        ControladorCliente controlCli = new ControladorCliente();
        controlCli.hiroshimaYnagasaki();
        
        controlCli.agregarRegistro(nickname,os,navegador);
           
          out.println("{\"success\": true}");
          out.flush();   

    
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Falta pasta. -Larry Capasta";
    }// </editor-fold>

}
