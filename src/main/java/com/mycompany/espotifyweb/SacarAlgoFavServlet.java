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
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "SacarAlgoFavServlet", urlPatterns = {"/SacarAlgoFavServlet"})
public class SacarAlgoFavServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Sacar Algo a Fav Servlet POST-----");

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
            
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("idCoso: " + idCoso + "/tipoCoso: " + tipoCoso);
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return; // Salir si hay un error al procesar el JSON
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        if (idCoso != null && !idCoso.isEmpty()) {

            DataCliente cliente = publicador.retornarCliente(nickname);
System.out.println("Cliente obtenido: " + cliente);

if ("Album".equals(tipoCoso)) {
    System.out.println("Tipo de objeto: Album");

    DataAlbum dtAlbum = publicador.obtenerDataAlbum(idCoso);
    System.out.println("Datos del álbum obtenidos: " + dtAlbum.getNombre());

    if ("ALBUM NO EXISTE".equals(dtAlbum.getNombre())) {
        out.println("{\"success\": false, \"error\": \"No existe Album.\"}");
        System.out.println("Error: No existe el álbum con ID " + idCoso);
    } else {
        List<String> coleAlbum = publicador.obtenerNombreAlbumesFavCliente(nickname);
        System.out.println("Álbumes favoritos del cliente: " + coleAlbum);

        if (coleAlbum.isEmpty()) {
            out.println("{\"success\": false, \"error\": \"La lista de álbumes favoritos está vacía.\"}");
            System.out.println("Advertencia: La lista de álbumes favoritos está vacía. No se puede corroborar.");
        } else {
            String tieneFav = publicador.corroborarAlbumEnFav(idCoso, coleAlbum);
            System.out.println("Resultado de corroborar si el álbum está en favoritos: " + tieneFav);

            if ("fav".equals(tieneFav)) {
                publicador.eliminarAlbumDeFav(cliente, dtAlbum);
                out.println("{\"success\": true}");
                System.out.println("Álbum eliminado de favoritos: " + dtAlbum.getNombre());
            } else {
                out.println("{\"success\": false, \"error\": \"El album no esta en Favoritos.\"}");
                System.out.println("Error: El álbum " + dtAlbum.getNombre() + " no está en favoritos.");
            }
        }
    }
}
 else {
                List<String> coleTemas = publicador.obtenerNombreTemasFavCliente(nickname);
                String estaEnFav = publicador.corroborarTemaEnFav(idCoso, coleTemas);
                if ("fav".equals(estaEnFav)) {

                    DataTema dtTema = publicador.retornarTema2(idCoso, idAlbum);
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
