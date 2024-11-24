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
import servicios.DataAlbum;
import servicios.DataListaPorDefecto;
import servicios.DataListaParticular;
import servicios.DataTema;
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

                        Collection<DataListaParticular> listasReproduccion = publicador.obtenerDataListaParticularPorParecido(busqueda);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataListaParticular lista : listasReproduccion) {

                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                                    .append("\"creador\":\"").append(lista.getCreador().getNickname()).append("\",")
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

                        Collection<DataListaPorDefecto> listasReproduccion = publicador.obtenerDataListaPorDefectoPorParecido(busqueda);

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
                case "MostrarAlbumes" -> {
                    try (PrintWriter out = response.getWriter()) {

                        Collection<DataAlbum> albumes = publicador.obtenerDataAlbumPorParecido(busqueda);

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
                case "MostrarTemas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        Collection<DataTema> temas = publicador.obtenerDataTemaPorParecido(busqueda);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataTema temazo : temas) {
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
