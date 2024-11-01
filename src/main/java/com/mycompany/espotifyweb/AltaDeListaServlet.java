package com.mycompany.espotifyweb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import logica.Cliente;
import logica.ListaParticular;
import logica.ListaReproduccion;
import logica.controladores.ControladorCliente;
import logica.controladores.ControladorListaParticular;
import logica.dt.DataListaParticular;
import persistencia.DAO_ListaReproduccion;
import persistencia.DAO_Usuario;

@MultipartConfig

public class AltaDeListaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AltaDeListaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AltaDeListaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO_ListaReproduccion persistence = new DAO_ListaReproduccion();

        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");

        String listaName = request.getParameter("listaName");
        ListaReproduccion lista = persistence.findListaReproduccionPorNombre(listaName, nickname);

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

            if (filePart == null || !(filePart.getSubmittedFileName().toString().endsWith("png") || filePart.getSubmittedFileName().toString().endsWith("jpg"))) {
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
            if (fileName != "default") {
                // Obtén el nombre del archivo y su tipo de contenido

                // COMENTAR ESTA RUTA Y COLOCAR LA SUYA PROPIA, RUTA DONDE GUARDAR LA FOTO DEL ALBUM /////////////////////////////////////////////////////// 
                String targetDir = "C:\\Users\\Law\\Documents\\GitHub\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\listas\\"; // Ajusta esta ruta

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

            /////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            ControladorListaParticular listControl = new ControladorListaParticular();

            ControladorCliente usrControl = new ControladorCliente();

            DAO_Usuario persistenceUsr = new DAO_Usuario();

            DAO_ListaReproduccion persistenceLista = new DAO_ListaReproduccion();

            ListaParticular dataErrorTrucho = new ListaParticular();

            dataErrorTrucho = persistenceLista.findListaPorNicks(nickname, nombreLista);

            if (dataErrorTrucho == null) {

                listControl.crearLista(nombreLista, usrControl.consultarPerfilCliente(nickname));

                DataListaParticular list = listControl.retornarlista(nombreLista, nickname);

                list.setFoto(fileName);

                Cliente cliente_lista = (Cliente) persistenceUsr.findUsuarioByNick(list.getCreadorNickname().getNickname());

                DAO_ListaReproduccion persistenceList = new DAO_ListaReproduccion();

                persistenceList.update(new ListaParticular(list.getNombre(), list.getVisibilidad(), cliente_lista, list.getFoto()));

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
