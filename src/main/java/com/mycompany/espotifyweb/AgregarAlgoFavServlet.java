/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
@WebServlet(name = "AgregarAlgoFavServlet", urlPatterns = {"/AgregarAlgoFavServlet"})
public class AgregarAlgoFavServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AgregarAlgoFavServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgregarAlgoFavServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("\n-----Agregar Algo a Fav Servlet GET-----");
        
       
        System.out.println("\n-----Agregar Algo a Fav Servlet GET-----");

    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n-----Agregar Algo a Fav Servlet POST-----");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
         String idCoso = null;
         String tipoCoso = null;
         String idAlbum = null;
         
         
        try {
            JSONObject jsonObject = new JSONObject(body);
            idCoso = jsonObject.getString("id"); 
            
            tipoCoso = jsonObject.getString("coso");
            
            idAlbum = jsonObject.getString("album");

            System.out.println("idCoso: " + idCoso + "/tipoCoso: " + tipoCoso);
            
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");
        ControladorSuscripcion controlSus = new ControladorSuscripcion();
        
        
        if(idCoso!=null || !idCoso.isEmpty()){
            
            ControladorAlbum controlAlb = new ControladorAlbum();
            ControladorTema controlTema = new ControladorTema();
            ControladorCliente controlCli = new ControladorCliente();
            DataCliente cliente = controlCli.consultarPerfilCliente(nickname);
            if("Album".equals(tipoCoso)){
                
                DataAlbum dtAlbum =  controlAlb.retornarInfoAlbum(idCoso);
                
                if("ALBUM NO EXISTE".equals(dtAlbum.getNombre())){
                        out.println("{\"success\": false, \"error\": \"No existe Album.\"}");
                }else{
                    Collection <String> coleAlbum = controlCli.obtenerAlbumFavCliente(nickname);
                    String tieneFav = controlCli.corroborarAlbumEnFav(idCoso, coleAlbum);
                    if(!"fav".equals(tieneFav)){
                        controlCli.agregarAlbum(cliente, dtAlbum); 
                        out.println("{\"success\": true}");
                    }else{
                        out.println("{\"success\": false, \"error\": \"El album ya esta en Favoritos.\"}");
                    }
                }
  
            }else{
                Collection<String> coleTemas = controlCli.obtenerTemaFavCliente(nickname);
                String estaEnFav = controlCli.corroborarTemaEnFav(idCoso, coleTemas);
                if(!"fav".equals(estaEnFav)){
                    
                    DataTema dtTema = controlTema.retornarTema2LaSecuela(idCoso, idAlbum);
                    if(dtTema != null){
                        controlCli.agregarTema(cliente, dtTema);
                        out.println("{\"success\": true}");
                    }else{
                         out.println("{\"success\": false, \"error\": \"El tema no esta.\"}");
                    }
                    
                }else{
                    out.println("{\"success\": false, \"error\": \"El tema ya esta en Favoritos.\"}");
                }
                
            }

          out.flush();   

    }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
