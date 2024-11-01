/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import logica.controladores.ControladorAlbum;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorSuscripcion;
import logica.controladores.ControladorTema;
import logica.dt.DataAlbum;
import logica.dt.DataCliente;
import logica.dt.DataSus;
import logica.dt.DataTema;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Urbina
 */
@WebServlet(name = "SacarAlgoFavServlet", urlPatterns = {"/SacarAlgoFavServlet"})
public class SacarAlgoFavServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SacarAlgoFavServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SacarAlgoFavServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("\n-----Sacar Algo a Fav Servlet GET-----");
        
       
        System.out.println("\n-----Sacar Algo a Fav Servlet GET-----");

    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n-----Sacar Algo a Fav Servlet POST-----");
        
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
                    if("fav".equals(tieneFav)){
                        controlCli.eliminarAlbum(cliente, dtAlbum); 
                        out.println("{\"success\": true}");
                    }else{
                        out.println("{\"success\": false, \"error\": \"El album no esta en Favoritos.\"}");
                    }
                }
                
                
            }else{
                Collection<String> coleTemas = controlCli.obtenerTemaFavCliente(nickname);
                String estaEnFav = controlCli.corroborarTemaEnFav(idCoso, coleTemas);
                if("fav".equals(estaEnFav)){
                    
                    DataTema dtTema = controlTema.retornarTema2LaSecuela(idCoso, idAlbum);
                    if(dtTema != null){
                        controlCli.eliminarTema(cliente, dtTema);
                        out.println("{\"success\": true}");
                    }else{
                         out.println("{\"success\": false, \"error\": \"El tema no esta.\"}");
                    }
                    
                }else{
                    out.println("{\"success\": false, \"error\": \"El tema no esta en Favoritos.\"}");
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
