package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataListaParticular;
import servicios.DataListaReproduccion;
import servicios.IPublicador;

@MultipartConfig

public class AltaDeListaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");
        String listaName = request.getParameter("listaName");

        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        DataListaReproduccion lista = publicador.retornarDataListaParticular(listaName, nickname);

        if (lista != null) {
            response.getWriter().write("exists");
        } else {
            response.getWriter().write("available");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String validacion = request.getParameter("Valido");

        if (validacion.equals("true")) {
            HttpSession session = request.getSession();
            String nickname = (String) session.getAttribute("nickname");

            String nombreLista = request.getParameter("nombreLista");

            Part filePart = request.getPart("imagenLista");
            String fileName;

            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);

            if (filePart == null || !(filePart.getSubmittedFileName().endsWith("png") || filePart.getSubmittedFileName().endsWith("jpg"))) {
                // No se seleccionó ningún archivo
                fileName = "default";
            } else {
                // Se seleccionó un archivo
                fileName = filePart.getSubmittedFileName();
            }

            /////////////////////////////////////////////////////////////////////////////////////////////////////
            ///// COMENTAR DE ACA PARA ABAJO ASI NO SE LES ROMPA AL RESTO ////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            // Verifica que el archivo no sea nulo
            if (!"default".equals(fileName)) {
                // Obtén el nombre del archivo y su tipo de contenido

                // COMENTAR ESTA RUTA Y COLOCAR LA SUYA PROPIA, RUTA DONDE GUARDAR LA FOTO DEL ALBUM /////////////////////////////////////////////////////// 
                //    String targetDir = "C:\\Users\\Law\\Documents\\GitHub\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\listas\\"; // Ajusta esta ruta
// RUTA CURE : 
                String targetDir = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/imagenes/listas/";
                // Crear el directorio si no existe
                File uploadDir = new File(targetDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // Guardar el archivo
                File file = new File(targetDir + File.separator + fileName);
                try (FileOutputStream fos = new FileOutputStream(file); InputStream fileContent = filePart.getInputStream()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = fileContent.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }

                }
            } else {
                fileName = "default";
            }

            DataListaParticular dataErrorTrucho;

            dataErrorTrucho = publicador.retornarDataListaParticular(nombreLista, nickname);

            if (dataErrorTrucho == null) {

                publicador.crearListaParticular(nombreLista, publicador.retornarCliente(nickname));

                DataListaParticular list = publicador.retornarDataListaParticular(nombreLista, nickname);

                list.setFoto(fileName);

                publicador.actualizarListaParticular(list);

                out.print("{\"status\": \"success\", \"message\": \"Lista creada exitosamente.\"}");
            } else {
                out.print("{\"status\": \"error\", \"message\": \"" + "La lista ya existe" + "\"}");
            }

            out.flush();

        } else {
            request.setAttribute("errorMessage", "EL NOMBRE DE LA LISTA ESTA REPETIDO.");
            request.getRequestDispatcher("AltaDeLista.jsp").forward(request, response);
        }
    }
}
