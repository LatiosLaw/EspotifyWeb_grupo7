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
import servicios.DataCliente;
import servicios.DataListaReproduccion;
import servicios.DataTema;
import org.json.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "SacarAlgoFavListaServlet", urlPatterns = {"/SacarAlgoFavListaServlet"})
public class SacarAlgoFavListaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("\n-----Sacar Algo a Fav de Lista Servlet POST-----");

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
            System.out.println("Antes del if: tipo" + tipo);
            if (!"1".equals(tipo)) {
                idCreadorAlbum = jsonObject.getString("creaDoorAlboom");
            } else {
                idCreadorAlbum = "none";
            }

            System.out.println("idCoso: " + idCoso + "/tipoCoso: " + tipoCoso);
            System.out.println("creaDoor/Alboom: " + idCreadorAlbum + "/tipo: " + tipo);

        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        if (idCoso != null && !idCoso.isEmpty()) {

            DataCliente cliente = publicador.retornarCliente(nickname);

            if ("Lista".equals(tipoCoso)) {

                //DataAlbum dtAlbum =  controlAlb.retornarInfoAlbum(idCoso);
                if ("1".equals(tipo)) {
                    //por defecto
                    List<String> listasCole = publicador.obtenerNombreListasFavCliente(nickname);
                    String tieneLaik = publicador.corroborarListaEnFav(idCoso, "Por Defecto", listasCole);
                    DataListaReproduccion dtLista = publicador.devolverInformacionListaRepro(idCoso, idCreadorAlbum);
                    if (!"fav".equals(tieneLaik)) {
                        //controlCli.agregarLista(cliente, dtLista);
                        out.println("{\"success\": false, \"error\": \"La lista no esta en Fav.\"}");
                    } else {
                        publicador.eliminarListaDeFav(cliente, dtLista);
                        out.println("{\"success\": true}");
                    }
                } else {
                    //particular
                    List<String> listasCole = publicador.obtenerNombreListasFavCliente(nickname);
                    String tieneLaik = publicador.corroborarListaEnFav(idCoso, idCreadorAlbum, listasCole);

                    System.out.println("Datos para el retornarLista");
                    System.out.println("IdCoso: " + idCoso);
                    System.out.println("nickname: " + idCreadorAlbum);
                    DataListaReproduccion dtLista = publicador.devolverInformacionListaRepro(idCoso, idCreadorAlbum);
                    if ("fav".equals(tieneLaik)) {
                        publicador.eliminarListaDeFav(cliente, dtLista);
                        out.println("{\"success\": true}");
                    } else {
                        out.println("{\"success\": false, \"error\": \"El album no esta en Favoritos.\"}");
                    }
                }
            } else {
                List<String> coleTemas = publicador.obtenerNombreTemasFavCliente(nickname);
                String estaEnFav = publicador.corroborarTemaEnFav(idCoso, coleTemas);
                if ("fav".equals(estaEnFav)) {
                    System.out.println("Antes del retornar tema 2 la secuela");
                    System.out.println("idCoso: " + idCoso + "// idCreadorAlbum: " + idCreadorAlbum);
                    DataTema dtTema = publicador.retornarTema(idCoso, idCreadorAlbum);
                    if (dtTema != null) {
                        publicador.eliminarTemaDeFav(cliente, dtTema);
                        out.println("{\"success\": true}");
                    } else {
                        out.println("{\"success\": false, \"error\": \"El tema no esta.\"}");
                    }

                } else {
                    out.println("{\"success\": false, \"error\": \"El tema no esta en Favoritos.\"}");
                }
            }
            out.flush();
        }
    }

}
