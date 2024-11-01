package com.mycompany.espotifyweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import logica.Usuario;
import logica.controladores.ControladorArtista;
import logica.controladores.ControladorCliente;
import logica.dt.DataErrorBundle;
import persistencia.DAO_Usuario;

@MultipartConfig

public class AgregarUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AgregarUsuarioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgregarUsuarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Usuario Servlet-----");
        String action = request.getParameter("action");

        if ("verificarNickname".equals(action)) {
            String username = request.getParameter("Nickname");

            // Aquí iría la lógica para verificar si el usuario existe
            boolean isAvailable = checkNicknameAvailability(username);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            if (isAvailable) {
                out.print("Nickname is available");
            } else {
                out.print("Nickname is already taken");
            }
            out.close();
        } else if ("verificarCorreo".equals(action)) {
            String username = request.getParameter("correoName");

            // Aquí iría la lógica para verificar si el usuario existe
            boolean isAvailable = checkUCorreoAvailability(username);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            if (isAvailable) {
                out.print("Mail is available");
            } else {
                out.print("Mail is already taken");
            }
            out.close();
        }

    }

    private boolean checkNicknameAvailability(String username) {
        // Aquí deberías realizar la consulta a la base de datos.
        // Por ejemplo:
        // return !userDao.isUsernameTaken(username);

        // Simulación de base de datos (ejemplo)
        DAO_Usuario usr = new DAO_Usuario();
        Usuario usuario = usr.findUsuarioByNick(username);
        if (usuario != null) {
            return false;
        }
        return true;
    }

    private boolean checkUCorreoAvailability(String correo) {
        // Aquí deberías realizar la consulta a la base de datos.
        // Por ejemplo:
        // return !userDao.isUsernameTaken(username);

        // Simulación de base de datos (ejemplo)
        DAO_Usuario usr = new DAO_Usuario();
        Usuario usuario = usr.findUsuarioByMail(correo);
        if (usuario != null) {
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Agregar Usuario Servlet-----");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Obtener parametros del formulario
        String tipoUsuario = request.getParameter("tipoUsuario");
        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String mail = request.getParameter("mail");
        String pass = request.getParameter("pass");

        String fechaNacStr = request.getParameter("fechaNac");
        LocalDate fechaNac;

        if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
            fechaNac = LocalDate.parse(fechaNacStr); // convierte el String a LocalDate
        } else {
            fechaNac = LocalDate.now();
        }
        Part filePart = request.getPart("foto");
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
            String targetDir = "C:\\Users\\Law\\Documents\\GitHub\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\usuarios\\"; // Ajusta esta ruta
            //String targetDir = "D:\\Github Proyectos\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\usuarios";

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
        // Lógica para agregar usuario
        DataErrorBundle result;

        if ("cliente".equals(tipoUsuario)) {
            ControladorCliente controladorCliente = new ControladorCliente();
            result = controladorCliente.agregarCliente(nickname, nombre, apellido, pass, mail, fileName, fechaNac);

        } else if ("artista".equals(tipoUsuario)) {
            // Obtener parámetros adicionales para artista
            String biografia = request.getParameter("biografia");
            String dirWeb = request.getParameter("dirWeb");

            ControladorArtista controladorArtista = new ControladorArtista();
            result = controladorArtista.agregarArtista(nickname, nombre, apellido, pass, mail, fileName, fechaNac, biografia, dirWeb);

        } else {
            result = new DataErrorBundle(false, 1);
        }

        if (result != null) {
            if (result.getValor()) {
                out.print("{\"status\": \"success\", \"message\": \"Usuario agregado exitosamente.\"}");
            } else {
                out.print("{\"status\": \"error\", \"message\": \"" + result.getNumero() + "\"}"); // Mensaje de error específico
            }
        } else {
            out.print("{\"status\": \"error\", \"message\": \"Error desconocido.\"}");
        }
        out.flush(); //enviar la respuesta
    }

}
