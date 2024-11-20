package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataCliente;
import servicios.DataListaParticular;
import servicios.DataListaPorDefecto;
import servicios.DataTema;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "AgregarAlgoFavListaServlet", urlPatterns = {"/AgregarAlgoFavListaServlet"})
public class AgregarAlgoFavListaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Algo a Fav de Lista Servlet POST-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
        String idCoso = null;
        String idCreadorAlbum = null;
        String tipoCoso = null;
        String tipo = null;

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        try {
            JSONObject jsonObject = new JSONObject(body);
            idCoso = jsonObject.getString("id");
            tipoCoso = jsonObject.getString("coso");
            tipo = jsonObject.getString("tipo");

            if (!"1".equals(tipo)) {
                idCreadorAlbum = jsonObject.getString("creaDoorAlboom");
            } else {
                idCreadorAlbum = "none";
            }

            System.out.println("idCoso: " + idCoso + ", tipoCoso: " + tipoCoso);
            System.out.println("idCreadorAlbum: " + idCreadorAlbum + ", tipo: " + tipo);
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return;
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        if (idCoso != null && !idCoso.isEmpty()) {
            DataCliente cliente = publicador.retornarCliente(nickname);

            if ("Lista".equals(tipoCoso)) {
                if ("1".equals(tipo)) {
                    List<String> listasCole = publicador.obtenerNombreListasFavCliente(nickname);

                    if (listasCole == null || listasCole.isEmpty()) {
                        DataListaPorDefecto dtLista = publicador.retornarDataListaPorDefecto2(idCoso);
                        if (dtLista != null) {
                            publicador.agregarListaEnFav(cliente, dtLista);
                            out.println("{\"success\": true}");
                        } else {
                            out.println("{\"success\": false, \"error\": \"La lista no está disponible.\"}");
                        }
                    } else {
                        String tieneLaik = publicador.corroborarListaEnFav(idCoso, "Por Defecto", listasCole);
                        if (!"fav".equals(tieneLaik)) {
                            DataListaPorDefecto dtLista = publicador.retornarDataListaPorDefecto2(idCoso);
                            if (dtLista != null) {
                                publicador.agregarListaEnFav(cliente, dtLista);
                                out.println("{\"success\": true}");
                            } else {
                                out.println("{\"success\": false, \"error\": \"La lista no está disponible.\"}");
                            }
                        } else {
                            out.println("{\"success\": false, \"error\": \"La lista ya está en Favoritos.\"}");
                        }
                    }
                } else {
                    List<String> listasCole = publicador.obtenerNombreListasFavCliente(nickname);

                    if (listasCole == null || listasCole.isEmpty()) {
                        DataListaParticular dtLista = publicador.retornarDataListaParticular(idCoso, idCreadorAlbum);
                        if (dtLista != null) {
                            publicador.agregarListaEnFav(cliente, dtLista);
                            out.println("{\"success\": true}");
                        } else {
                            out.println("{\"success\": false, \"error\": \"La lista no está disponible.\"}");
                        }
                    } else {
                        String tieneLaik = publicador.corroborarListaEnFav(idCoso, idCreadorAlbum, listasCole);
                        if (!"fav".equals(tieneLaik)) {
                            DataListaParticular dtLista = publicador.retornarDataListaParticular(idCoso, idCreadorAlbum);
                            if (dtLista != null) {
                                publicador.agregarListaEnFav(cliente, dtLista);
                                out.println("{\"success\": true}");
                            } else {
                                out.println("{\"success\": false, \"error\": \"La lista no está disponible.\"}");
                            }
                        } else {
                            out.println("{\"success\": false, \"error\": \"La lista ya está en Favoritos.\"}");
                        }
                    }
                }
            } else {
                List<String> coleTemas = publicador.obtenerNombreTemasFavCliente(nickname);

                if (coleTemas == null || coleTemas.isEmpty()) {
                    DataTema dtTema = publicador.retornarTema(idCoso, idCreadorAlbum);
                    if (dtTema != null) {
                        publicador.agregarTemaEnFav(cliente, dtTema);
                        out.println("{\"success\": true}");
                    } else {
                        out.println("{\"success\": false, \"error\": \"El tema no está disponible.\"}");
                    }
                } else {
                    String estaEnFav = publicador.corroborarTemaEnFav(idCoso, coleTemas);
                    if (!"fav".equals(estaEnFav)) {
                        DataTema dtTema = publicador.retornarTema(idCoso, idCreadorAlbum);
                        if (dtTema != null) {
                            publicador.agregarTemaEnFav(cliente, dtTema);
                            out.println("{\"success\": true}");
                        } else {
                            out.println("{\"success\": false, \"error\": \"El tema no está disponible.\"}");
                        }
                    } else {
                        out.println("{\"success\": false, \"error\": \"El tema ya está en Favoritos.\"}");
                    }
                }
            }
            out.flush();
        }
    }
}
