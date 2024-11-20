package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataCliente;
import servicios.DataAlbum;
import servicios.DataArtista;
import servicios.DataListaPorDefecto;
import servicios.IPublicador;

@MultipartConfig

public class PaginaInicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Pagina Inicio Servlet GET-----");
        String action = request.getParameter("action");
        if (null != action) {
            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);

            switch (action) {
                case "buscarArtistas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        List<DataArtista> artistas = publicador.obtenerDataArtistas();

                        Collections.shuffle(artistas);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        int count = 0;
                        for (DataArtista artista : artistas) {

                            jsonResponse.append("{\"nombre\":\"").append(artista.getNickname()).append("\",")
                                    .append("\"imagen\":\"").append(artista.getFoto())
                                    .append("\"},");

                            count++;
                            if (count == 10) {
                                break;
                            }
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
                case "buscarAlbumes" -> {
                    try (PrintWriter out = response.getWriter()) {

                        List<DataAlbum> albumes = publicador.obtenerDataAlbumes();;

                        Collections.shuffle(albumes);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        int count = 0;
                        for (DataAlbum album : albumes) {

                            jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                                    .append("\"imagen\":\"").append(album.getImagen())
                                    .append("\"},");

                            count++;
                            if (count == 10) {
                                break;
                            }
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
                case "buscarListas" -> {
                    try (PrintWriter out = response.getWriter()) {

                        List<DataListaPorDefecto> listas = publicador.obtenerDataListaPorDefecto();

                        Collections.shuffle(listas);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        int count = 0;
                        for (DataListaPorDefecto lista : listas) {

                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\",")
                                    .append("\"imagen\":\"").append(lista.getFoto())
                                    .append("\"},");

                            count++;
                            if (count == 10) {
                                break;
                            }
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
                case "buscarClientes" -> {
                    try (PrintWriter out = response.getWriter()) {

                        List<DataCliente> clientes = publicador.obtenerDataClientes();

                        Collections.shuffle(clientes);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        int count = 0;
                        for (DataCliente cliente : clientes) {

                            jsonResponse.append("{\"nombre\":\"").append(cliente.getNickname()).append("\",")
                                    .append("\"imagen\":\"").append(cliente.getFoto())
                                    .append("\"},");

                            count++;
                            if (count == 10) {
                                break;
                            }
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
