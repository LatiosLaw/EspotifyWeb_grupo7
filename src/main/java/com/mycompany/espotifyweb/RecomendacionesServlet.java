package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.IPublicador;
import servicios.RegistroTema;

public class RecomendacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Recomendaciones GET-----");
        String action = request.getParameter("action");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        response.setContentType("application/json;charset=UTF-8");

        if ("devolverTemazos".equals(action)) {
            try (PrintWriter out = response.getWriter()) {

                List<RegistroTema> temas = publicador.obtenerLos100MasPopulares();

                if (temas == null) {
                    System.out.println("Aun no hay temas en el ranking");
                } else {
                    for (RegistroTema tema : temas) {
                        System.out.println(tema.getIdentificador().getNombreTema());
                    }
                }

                Gson gson = new Gson();
                String json = gson.toJson(temas);

                response.setContentType("application/json;charset=UTF-8");
                out.print(json);

                System.out.println("JSON generado: " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n-----Recomendaciones GET-----");
    }

}
