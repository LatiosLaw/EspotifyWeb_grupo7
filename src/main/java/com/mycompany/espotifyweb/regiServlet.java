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
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;
import servicios.IPublicador;

@WebServlet(name = "regiServlet", urlPatterns = {"/regiServlet"})
public class regiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----regiServlet POST-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        System.out.println("Body:" + body);
        String os = null;
        String navegador = null;
        String ip = null;
        String url = null;
        
        // URL del WSDL
        URL wurl = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(wurl, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        try {
            JSONObject jsonObject = new JSONObject(body);
            os = jsonObject.getString("os");

            navegador = jsonObject.getString("nave");

            ip = jsonObject.getString("ip");

            url = jsonObject.getString("url");

            System.out.println("os: " + os + "/navegaDoor: " + navegador);

        } catch (JSONException e) {
            out.println("{\"success\": false, \"error\": \"Error al procesar la solicitud.\"}");
            return;
        }
        System.out.println("\n-----Pasado la construccion del json-----");

        publicador.hiroshimaYNagasaki();

        publicador.agregarRegistro(nickname, os, navegador, ip, url);

        out.println("{\"success\": true}");
        out.flush();
        
        System.out.println("\n-----End regiServlet POST-----");
    }

}
