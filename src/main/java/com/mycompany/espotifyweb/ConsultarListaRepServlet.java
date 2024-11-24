package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.Registro_tema;
import logica.controladores.ControladorAdicionalTema;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorListaParticular;
import logica.controladores.ControladorListaPorDefecto;
import logica.controladores.ControladorTema;
import logica.controladores.IControladorAdicionalTema;
import logica.dt.DataListaParticular;
import logica.dt.DataListaPorDefecto;
import logica.dt.DataTema;

public class ConsultarListaRepServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CrearListaRepServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CrearListaRepServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
          // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        if (action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El parámetro 'action' es requerido");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        System.out.println("Acción recibida: " + action);

        if ("getTemasPorLista".equals(action)) {
            String nombreLista = request.getParameter("listaNombre");
            System.out.println("Nombre de la lista recibido: " + nombreLista);
            String tipo = request.getParameter("tipo");
            System.out.println("Tipo de la lista recibido: " + tipo);
            if(tipo.equals("2")){
                nombreLista = nombreLista.split("/")[0];
            }
            if (nombreLista == null || nombreLista.isEmpty() || tipo == null || tipo.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
                return;
            }
            obtenerTemasPorLista(nombreLista, tipo, nickname, out, response);
        }else if ("devolverInformacionLista".equals(action)) {
            try (PrintWriter out2 = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                String nombreLista = request.getParameter("listaNombre");
            System.out.println("Nombre de la lista recibido: " + nombreLista);
            String tipo = request.getParameter("tipo");
            System.out.println("Tipo de la lista recibido: " + tipo);
            if(tipo.equals("2")){
                nombreLista = nombreLista.split("/")[0];
            }
            StringBuilder jsonResponse = new StringBuilder("[");
            ControladorCliente controlCli = new ControladorCliente();
            Collection<String> listasCole = controlCli.obtenerListasFavCliente(nickname);
            

            if(tipo.equals("1")){
                String tieneLaik = controlCli.corroborarListaEnFav(nombreLista, "Por Defecto", listasCole);
                

                ControladorListaPorDefecto ctrl = new ControladorListaPorDefecto();
                DataListaPorDefecto lista = ctrl.devolverInformacionChu(nombreLista);
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto()).append("\",")
                        .append("\"tipo\":\"").append("1").append("\",")
                        .append("\"fav\":\"").append(tieneLaik).append("\",");
                jsonResponse.append("\"adicional\":\"").append(lista.getGenero().getNombre()).append("\"},");
            }else{
                
                
                
                ControladorListaParticular ctrl = new ControladorListaParticular();
                String usuario = request.getParameter("usuario");
                String tieneLaik = controlCli.corroborarListaEnFav(nombreLista, usuario, listasCole);
            System.out.println("Usuario recibido: " + usuario);
                DataListaParticular lista = ctrl.devolverInformacion(nombreLista, usuario);
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto()).append("\",")
                        .append("\"tipo\":\"").append("2").append("\",")
                        .append("\"fav\":\"").append(tieneLaik).append("\",");
                jsonResponse.append("\"adicional\":\"").append(lista.getDataCliente().getNickname()).append("\"},");
            }

                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        }else if("devolverInformacionTema".equals(action)){
            IControladorAdicionalTema registros = new ControladorAdicionalTema();
            String nombreTema = request.getParameter("nombreTema");
            System.out.println(nombreTema);
            String albumName = request.getParameter("nombreAlbum");
            System.out.println(albumName);
         //   Registro_tema info = registros.devolverRegistroTema(nombreTema, albumName);
            Registro_tema info = null;
            
            try (PrintWriter out2 = response.getWriter()) {
                
                // Usa Gson para convertir la colección a JSON
            Gson gson = new Gson();
            String json = gson.toJson(info);

            // Establece el contenido de la respuesta
            response.setContentType("application/json;charset=UTF-8");
            out.print(json);

            // Log para verificar el JSON generado
            System.out.println("JSON generado: " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }

        out.flush();
    }

    private void obtenerTemasPorLista(String nombreLista, String tipo,String nickname, PrintWriter out, HttpServletResponse response) throws IOException {

        
        
        if (nombreLista == null || nombreLista.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
            return;
        }

        try {
            ControladorTema ctrlTema = new ControladorTema();
            Collection<DataTema> temas = ctrlTema.retornarTemasDeLaLista(nombreLista, Integer.valueOf(tipo));

            if (temas.isEmpty()) {
                out.print("[]");
                return;
            }
            ControladorCliente controlCli = new ControladorCliente();
            Collection <String> cole = controlCli.obtenerTemaFavCliente(nickname);
            
            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataTema tema : temas) {
                
                String tieneLaik = controlCli.corroborarTemaEnFav(tema.getNickname(), cole);
                System.out.println("Tiene laik: " + tieneLaik);
                System.out.println(tema.getArchivo());
                jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                        .append("\"album\":\"").append(tema.getNomAlb()).append("\",")
                        .append("\"duracion\":\"").append(tema.getDuracion()).append("\",")
                        .append("\"archivo\":\"").append(tema.getArchivo()).append("\",")
                        .append("\"fav\":\"").append(tieneLaik).append("\",")
                        .append("\"identificador_archivo\":\"").append(tema.getArchivo()).append("\"},");
            }

            jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            jsonResponse.append("]");
            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los temas");
        }
    }


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
