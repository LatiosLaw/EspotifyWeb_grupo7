package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import logica.Album;
import logica.ListaParticular;
import logica.ListaPorDefecto;
import logica.controladores.ControladorListaParticular;
import logica.controladores.ControladorTema;
import logica.dt.DataTema;
import logica.tema;
import persistencia.DAO_Album;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Tema;

public class AgregarTemaAListaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AgregarTemaAListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgregarTemaAListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");

        // Obtener la sesión
        HttpSession session = request.getSession();

        // Leer el nickname desde la sesión
        String nickname = (String) session.getAttribute("nickname");

        if ("cargarListas".equals(action)) {

            try (PrintWriter out = response.getWriter()) {
                // Comprobar si se obtuvo el nickname
                if (nickname == null) {
                    out.print("{\"error\": \"No se encontró el nickname.\"}");
                    return;
                }
                // Obtener todas las listas de reproducción del cliente
                DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
                Collection<ListaParticular> listasReproduccion = persistence.findListaPorCliente(nickname);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (ListaParticular lista : listasReproduccion) {
                    jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("cargarListasParticular".equals(action)) {

            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
                Collection<ListaParticular> listasReproduccion = persistence.findAllListasParticularesPublicas();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (ListaParticular lista : listasReproduccion) {

                    jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                            .append("\"nombrecreador\":\"").append(lista.getCliente().getNickname()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("cargarListasDefault".equals(action)) {

            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();
                Collection<ListaPorDefecto> listasReproduccion = persistence.devolverListasPorDefecto();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (ListaPorDefecto lista : listasReproduccion) {

                    jsonResponse.append("{\"nombre\":\"").append(lista.getNombreLista()).append("\",")
                            .append("\"genero\":\"").append(lista.getGenero().getNombre()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("cargarAlbumes".equals(action)) {
            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                DAO_Album persistence = new DAO_Album();
                Collection<Album> albumes = persistence.findAll();

                StringBuilder jsonResponse = new StringBuilder("[");
                for (Album album : albumes) {

                    jsonResponse.append("{\"nombre\":\"").append(album.getNombre()).append("\",")
                            .append("\"nombrecreador\":\"").append(album.getCreador().getNickname()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("buscarTemasListaParticular".equals(action)) {
            String nombreLista = request.getParameter("listaName");
            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                ControladorTema ctrlTema = new ControladorTema();
                Collection<DataTema> temas = ctrlTema.retornarTemasDeLaLista(nombreLista, 2);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataTema tema : temas) {

                    jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                            .append("\"album\":\"").append(tema.getNomAlb()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("buscarTemasListaDefecto".equals(action)) {
            String nombreLista = request.getParameter("listaName");
            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                ControladorTema ctrlTema = new ControladorTema();
                Collection<DataTema> temas = ctrlTema.retornarTemasDeLaLista(nombreLista, 1);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataTema tema : temas) {

                    jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                            .append("\"album\":\"").append(tema.getNomAlb()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
            }
        } else if ("buscarTemasAlbum".equals(action)) {
            String nombreAlbum = request.getParameter("albumName");
            try (PrintWriter out = response.getWriter()) {
                // Obtener todas las listas de reproducción del cliente
                ControladorTema ctrlTema = new ControladorTema();
                Collection<DataTema> temas = ctrlTema.retornarTemasDeAlbum(nombreAlbum);

                StringBuilder jsonResponse = new StringBuilder("[");
                for (DataTema tema : temas) {

                    jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                            .append("\"album\":\"").append(tema.getNomAlb()).append("\"},");
                }

                if (jsonResponse.length() > 1) {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
                }

                jsonResponse.append("]");

                out.print(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Para depuración
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

        ControladorListaParticular ctrlLista = new ControladorListaParticular();
        ControladorTema ctrlTema = new ControladorTema();
        DAO_Tema persistence = new DAO_Tema();

        tema teMartin = persistence.find(nombreTema, albumTema);

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
                DataTema temazo = ctrlTema.retornarTema(nombreTema, albumTema);
                ctrlLista.agregarTema(nickname, nombreLista, temazo);
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
