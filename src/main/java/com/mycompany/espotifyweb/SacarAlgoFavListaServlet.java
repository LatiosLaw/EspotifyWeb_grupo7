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
import logica.controladores.ControladorListaParticular;
import logica.controladores.ControladorListaPorDefecto;
import logica.controladores.ControladorSuscripcion;
import logica.controladores.ControladorTema;
import logica.dt.DataAlbum;
import logica.dt.DataCliente;
import logica.dt.DataListaParticular;
import logica.dt.DataListaPorDefecto;
import logica.dt.DataListaReproduccion;
import logica.dt.DataSus;
import logica.dt.DataTema;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Urbina
 */
@WebServlet(name = "SacarAlgoFavListaServlet", urlPatterns = {"/SacarAlgoFavListaServlet"})
public class SacarAlgoFavListaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SacarAlgoFavListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SacarAlgoFavListaServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("\n-----Sacar Algo a Fav de Lista Servlet GET-----");
        
       
        System.out.println("\n-----Sacar Algo a Fav de Lista Servlet GET-----");

    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
           
        
        System.out.println("\n-----Sacar Algo a Fav de Lista Servlet POST-----");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
         String idCoso = null;
         String idCreadorAlbum = null;
         String tipoCoso = null;
         String tipo = null;
         
         
        try {
            JSONObject jsonObject = new JSONObject(body);
            idCoso = jsonObject.getString("id"); 
            
            tipoCoso = jsonObject.getString("coso");
            tipo = jsonObject.getString("tipo");
            System.out.println("Antes del if: tipo" + tipo);
            if(!"1".equals(tipo)){
                idCreadorAlbum = jsonObject.getString("creaDoorAlboom");
            }else{
                idCreadorAlbum = "none";
            }
            
            
            
            
            
             System.out.println("idCoso: " + idCoso + "/tipoCoso: " + tipoCoso);
             System.out.println("creaDoor/Alboom: " + idCreadorAlbum + "/tipo: " + tipo);
             
            
            
            
            
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");
       
        
        
        if(idCoso!=null || !idCoso.isEmpty()){
            
            ControladorListaPorDefecto controlLipo = new ControladorListaPorDefecto();
            ControladorListaParticular controlPar = new ControladorListaParticular();
            ControladorAlbum controlAlb = new ControladorAlbum();
            ControladorTema controlTema = new ControladorTema();
            ControladorCliente controlCli = new ControladorCliente();
            DataCliente cliente = controlCli.consultarPerfilCliente(nickname);
            
            if("Lista".equals(tipoCoso)){
                
                //DataAlbum dtAlbum =  controlAlb.retornarInfoAlbum(idCoso);
                    
                    if("1".equals(tipo)){
                        //por defecto
                        Collection <String> listasCole = controlCli.obtenerListasFavCliente(nickname);
                        String tieneLaik = controlCli.corroborarListaEnFav(idCoso, "Por Defecto", listasCole);
                        DataListaReproduccion dtLista = controlPar.devolverInformacionListaRepro(idCoso, idCreadorAlbum);
                        if(!"fav".equals(tieneLaik)){
                            //controlCli.agregarLista(cliente, dtLista);
                            out.println("{\"success\": false, \"error\": \"La lista no esta en Fav.\"}");
                             
                        }else{
                               controlCli.eliminarLista(cliente, dtLista); 
                               out.println("{\"success\": true}");

                        }
                    }else{
                        //particular
                        Collection <String> listasCole = controlCli.obtenerListasFavCliente(nickname);
                        String tieneLaik = controlCli.corroborarListaEnFav(idCoso, idCreadorAlbum, listasCole);
                        
                        System.out.println("Datos para el retornarLista");
                         System.out.println("IdCoso: " +idCoso);
                         System.out.println("nickname: " +idCreadorAlbum);
                        DataListaReproduccion dtLista = controlPar.devolverInformacionListaRepro(idCoso, idCreadorAlbum);
                        if("fav".equals(tieneLaik)){
                            controlCli.eliminarLista(cliente, dtLista);
                             out.println("{\"success\": true}");
                        }else{
                         out.println("{\"success\": false, \"error\": \"El album no esta en Favoritos.\"}");
                    }
                        
                    }

  
            }else{
                Collection<String> coleTemas = controlCli.obtenerTemaFavCliente(nickname);
                String estaEnFav = controlCli.corroborarTemaEnFav(idCoso, coleTemas);
                if("fav".equals(estaEnFav)){
                    System.out.println("Antes del retornar tema 2 la secuela");
                    System.out.println("idCoso: " + idCoso + "// idCreadorAlbum: " + idCreadorAlbum);
                    DataTema dtTema = controlTema.retornarTema(idCoso, idCreadorAlbum);
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
