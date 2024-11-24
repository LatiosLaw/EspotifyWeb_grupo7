package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import logica.Album;
import logica.ListaParticular;
import logica.ListaPorDefecto;
import servicios.DataAlbum;
import servicios.DataListaPorDefecto;
import logica.tema;
import persistencia.DAO_Album;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Tema;
import servicios.IPublicador;

@MultipartConfig

public class BuscarCosasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Buscar Cosas Servlet GET-----");
        String action = request.getParameter("action");
        String busqueda = request.getParameter("buscar");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if (null != action) {
            switch (action) {
                case "MostrarListasU" -> {
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
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "MostrarListasG" -> {
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
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "MostrarAlbumes" -> {
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
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "MostrarTemas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        DAO_Tema persistence = new DAO_Tema();
                        
                        Collection<tema> temas = persistence.findAllPorParecido(busqueda);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (tema temazo : temas) {
                            jsonResponse.append("{\"nombre\":\"").append(temazo.getNickname()).append("\",")
                                    .append("\"album\":\"").append(temazo.getAlbum().getNombre()).append("\",")
                                    .append("\"artista\":\"").append(temazo.getAlbum().getCreador().getNickname())
                                    .append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "VerificarSiEsGenero" -> {
                    boolean Existe = publicador.checkSiEsGenero(busqueda);
                    
                    response.setContentType("text/plain");
                    try (PrintWriter out = response.getWriter()) {
                        if (Existe) {
                            out.print("existe");
                        } else {
                            out.print("no existe");
                        }
                    }
                }
                case "MostrarListasDelGeneroU" -> {
                    try (PrintWriter out = response.getWriter()) {
                        List<DataListaPorDefecto> listasReproduccion = publicador.obtenerDataListasPorGenero(busqueda);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataListaPorDefecto lista : listasReproduccion) {

                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                                    .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\",")
                                    .append("\"imagen\":\"").append(lista.getFoto())
                                    .append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "MostrarAlbumesDelGeneroU" -> {
                    try (PrintWriter out = response.getWriter()) {

                        List<DataAlbum> albumes = publicador.obtenerDataAlbumesPorGenero(busqueda);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataAlbum album : albumes) {
                            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                                    .append("\"artista\":\"").append(album.getCreador().getNickname()).append("\",")
                                    .append("\"anio\":\"").append(album.getAnioCreacion()).append("\",")
                                    .append("\"imagen\":\"").append(album.getImagen())
                                    .append("\"},");
                        }

                        if (jsonResponse.length() > 1) {
                            jsonResponse.deleteCharAt(jsonResponse.length() - 1);
                        }

                        jsonResponse.append("]");

                        out.print(jsonResponse.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                default -> {
                }
            }
        }

    }

}
