package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataAlbum;
import servicios.DataCliente;
import servicios.DataTema;
import org.json.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "AgregarAlgoFavServlet", urlPatterns = {"/AgregarAlgoFavServlet"})
public class AgregarAlgoFavServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Algo a Fav Servlet POST-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
        String idCoso = null;
        String tipoCoso = null;
        String idAlbum = null;

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        try {
            JSONObject jsonObject = new JSONObject(body);
            idCoso = jsonObject.getString("id");
            tipoCoso = jsonObject.getString("coso");
            idAlbum = jsonObject.getString("album");

            System.out.println("idCoso: " + idCoso + ", tipoCoso: " + tipoCoso);
        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Exit if there is an error processing the JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        if (idCoso != null && !idCoso.isEmpty()) {
            DataCliente cliente = publicador.retornarCliente(nickname);

            if ("Album".equals(tipoCoso)) {
                DataAlbum dtAlbum = publicador.obtenerDataAlbum(idCoso);

                if ("ALBUM NO EXISTE".equals(dtAlbum.getNombre())) {
                    out.println("{\"success\": false, \"error\": \"No existe Album.\"}");
                } else {
                    List<String> coleAlbum = publicador.obtenerNombreAlbumesFavCliente(nickname);
                    
                    // Check if coleAlbum is null or empty before corroborating
                    if (coleAlbum == null || coleAlbum.isEmpty()) {
                        publicador.agregarAlbumEnFav(cliente, dtAlbum);
                        out.println("{\"success\": true}");
                    } else {
                        String tieneFav = publicador.corroborarAlbumEnFav(idCoso, coleAlbum);
                        if (!"fav".equals(tieneFav)) {
                            publicador.agregarAlbumEnFav(cliente, dtAlbum);
                            out.println("{\"success\": true}");
                        } else {
                            out.println("{\"success\": false, \"error\": \"El album ya está en Favoritos.\"}");
                        }
                    }
                }
            } else { // Handling themes
                List<String> coleTemas = publicador.obtenerNombreTemasFavCliente(nickname);

                // Check if coleTemas is null or empty
                if (coleTemas == null || coleTemas.isEmpty()) {
                    DataTema dtTema = publicador.retornarTema2(idCoso, idAlbum);
                    if (dtTema != null) {
                        publicador.agregarTemaEnFav(cliente, dtTema);
                        out.println("{\"success\": true}");
                    } else {
                        out.println("{\"success\": false, \"error\": \"El tema no está.\"}");
                    }
                } else {
                    String estaEnFav = publicador.corroborarTemaEnFav(idCoso, coleTemas);
                    if (!"fav".equals(estaEnFav)) {
                        DataTema dtTema = publicador.retornarTema2(idCoso, idAlbum);
                        if (dtTema != null) {
                            publicador.agregarTemaEnFav(cliente, dtTema);
                            out.println("{\"success\": true}");
                        } else {
                            out.println("{\"success\": false, \"error\": \"El tema no está.\"}");
                        }
                    } else {
                        out.println("{\"success\": false, \"error\": \"El tema ya está en Favoritos.\"}");
                    }
                }
            }
        } else {
            out.println("{\"success\": false, \"error\": \"ID no válido.\"}");
        }
        out.flush();
    }
}