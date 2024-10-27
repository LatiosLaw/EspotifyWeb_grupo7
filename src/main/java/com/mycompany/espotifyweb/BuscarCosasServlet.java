package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import logica.Album;
import logica.Genero;
import logica.ListaParticular;
import logica.ListaPorDefecto;
import logica.controladores.ControladorAlbum;
import logica.controladores.ControladorListaPorDefecto;
import logica.dt.DataAlbum;
import logica.dt.DataListaPorDefecto;
import persistencia.DAO_Album;
import persistencia.DAO_Genero;
import persistencia.DAO_ListaReproduccion;

@MultipartConfig

public class BuscarCosasServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AltaDeListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AltaDeListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Buscar Cosas Servlet GET-----");
              String action = request.getParameter("action");
              String busqueda = request.getParameter("buscar");
              
              if ("MostrarListasU".equals(action)) {

                  try (PrintWriter out = response.getWriter()) {

            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaParticular> listasReproduccion = persistence.findAllListasParticularesPublicasParecidasA(busqueda);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (ListaParticular lista : listasReproduccion) {
                
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                        .append("\"creador\":\"").append(lista.getCliente().getNickname()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto())
                        .append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
              }else if("MostrarListasG".equals(action)){
try (PrintWriter out = response.getWriter()) {

            DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
            Collection<ListaPorDefecto> listasReproduccion = persistence.devolverListasPorDefectoParecidasA(busqueda);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (ListaPorDefecto lista : listasReproduccion) {
                jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                        .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto())
                        .append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
                  
              }else if("MostrarAlbumes".equals(action)){
                 try (PrintWriter out = response.getWriter()) {

            DAO_Album persistence = new DAO_Album();
            Collection<Album> albumes = persistence.findAllPorParecido(busqueda);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (Album album : albumes) {
                jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                        .append("\"artista\":\"").append(album.getCreador().getNickname()).append("\",")
                        .append("\"anio\":\"").append(album.getanioCreacion()).append("\",")
                        .append("\"imagen\":\"").append(album.getImagen())
                        .append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
                  
              }else if("VerificarSiEsGenero".equals(action)){

        // Aquí iría la lógica para verificar si el usuario existe
        boolean Existe = checkSiEsGenero(busqueda);

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        if (Existe) {
            out.print("existe");
        } else {
            out.print("no existe");
        }
        out.close();
              }else if("MostrarListasDelGeneroU".equals(action)){

                  try (PrintWriter out = response.getWriter()) {

            ControladorListaPorDefecto persistence = new ControladorListaPorDefecto();
            Collection<DataListaPorDefecto> listasReproduccion = persistence.retornarListasDelGeneroDT(busqueda);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataListaPorDefecto lista : listasReproduccion) {

                jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                        .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                        .append("\"imagen\":\"").append(lista.getFoto())
                        .append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
              }else if("MostrarAlbumesDelGeneroU".equals(action)){
try (PrintWriter out = response.getWriter()) {

            ControladorAlbum persistence = new ControladorAlbum();
            Collection<DataAlbum> albumes = persistence.retornarAlbumsDelGeneroDT(busqueda);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (DataAlbum album : albumes) {
                jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                        .append("\"artista\":\"").append(album.getCreador().getNickname()).append("\",")
                        .append("\"anio\":\"").append(album.getAnioCreacion()).append("\",")
                        .append("\"imagen\":\"").append(album.getImagen())
                        .append("\"},");
            }

            if (jsonResponse.length() > 1) {
                jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
            }

            jsonResponse.append("]");

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        }
                  
              }
              
    }
    
    private boolean checkSiEsGenero(String nom_genero) {
        // Aquí deberías realizar la consulta a la base de datos.
        // Por ejemplo:
        // return !userDao.isUsernameTaken(username);

        // Simulación de base de datos (ejemplo)
        DAO_Genero gen = new DAO_Genero();
        Genero genero = gen.find(nom_genero);
            if (genero!=null) {
                return true;
            }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
