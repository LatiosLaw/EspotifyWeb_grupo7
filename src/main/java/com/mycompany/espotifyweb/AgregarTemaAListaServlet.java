package com.mycompany.espotifyweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import logica.ListaParticular;
import logica.controladores.ControladorTema;
import servicios.DataTema;
import logica.tema;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Tema;
import servicios.DataListaParticular;
import servicios.IPublicador;

public class AgregarTemaAListaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if (null != action) {
            switch (action) {
                case "cargarListas" -> {
                    try (PrintWriter out = response.getWriter()) {
                        if (nickname == null) {
                            out.print("{\"error\": \"No se encontró el nickname.\"}");
                            return;
                        }

                        List<DataListaParticular> listas = publicador.retornarListasParticularesDeCliente(nickname);

                        StringBuilder jsonResponse = new StringBuilder("[");
                        for (DataListaParticular lista : listas) {
                            jsonResponse.append("{\"nombre\":\"").append(lista.getNombre()).append("\"},");
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
        System.out.println("\n-----End Agregar Tema a Lista Servlet GET-----");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Start Agregar Tema A Lista Servlet POST-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        // Leer el cuerpo de la solicitud
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();

        while ((line = reader.readLine()) != null) {
            jsonBuffer.append(line);
        }

        // Convertir el JSON a una cadena
        String jsonData = jsonBuffer.toString();

        // Extraer los campos del JSON manualmente
        String albumTema = extractJsonValue(jsonData, "albumTema");
        String nombreLista = extractJsonValue(jsonData, "nombreLista");
        String nombreTema = extractJsonValue(jsonData, "nombreTema");

        System.out.println(albumTema);
        System.out.println(nombreLista);
        System.out.println(nombreTema);

        DataTema teMartin = publicador.retornarTema(nombreTema, albumTema);

        if (teMartin != null) {

            DAO_ListaReproduccion listPersistence = new DAO_ListaReproduccion();

            ListaParticular listaExistente = listPersistence.findListaPorNicks(nickname, nombreLista);

            boolean temaYaExiste = false;
            for (tema t : listaExistente.getTemas()) {
                if (t.getNickname().equals(teMartin.getNickname())) {
                    temaYaExiste = true;
                    break;
                }
            }

            if (temaYaExiste) {
                out.print("{\"status\": \"error\", \"message\": \"Este tema ya pertenece a tu lista.\"}");
            } else {
                DataTema temazo = publicador.retornarTema(nombreTema, albumTema);
                publicador.agregarTemaALista(nickname, nombreLista, temazo);
                out.print("{\"status\": \"success\", \"message\": \"Tema agregado exitosamente.\"}");
            }

        } else {
            out.print("{\"status\": \"error\", \"message\": \"Error al agregar el tema a la lista.\"}");
        }

        out.flush(); // enviar la respuesta
        System.out.println("\n-----End Agregar Tema a Lista Servlet POST-----");
    }

// Metodo para extraer valores del JSON
    private String extractJsonValue(String jsonData, String key) {
        String value = null;

        // Busca la clave en el JSON y extrae su valor
        int keyIndex = jsonData.indexOf(key);

        if (keyIndex != -1) {
            int startIndex = jsonData.indexOf(":", keyIndex) + 1;
            int endIndex = jsonData.indexOf(",", startIndex);

            if (endIndex == -1) {
                endIndex = jsonData.indexOf("}", startIndex); // Para el último valor
            }

            value = jsonData.substring(startIndex, endIndex).trim().replace("\"", ""); // Eliminar comillas
        }

        return value;
    }

}
