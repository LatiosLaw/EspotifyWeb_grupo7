package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import logica.Genero;
import logica.controladores.ControladorAlbum;
import logica.controladores.ControladorGenero;
import logica.controladores.ControladorTema;
import logica.dt.DataAlbum;
import logica.dt.DataErrorBundle;
import logica.dt.DataGenero;
import logica.dt.DataTema;
import persistencia.DAO_Genero;

@MultipartConfig

public class AltaDeAlbumServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AltaDeAlbumServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AltaDeAlbumServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("cargarGeneros".equals(action)) {

            DAO_Genero persistence = new DAO_Genero();
            Collection<Genero> generosObjeto = persistence.findAll();
            ArrayList<String> generosString = new ArrayList<>();

            for (Genero g : generosObjeto) {
                generosString.add(g.getNombre());
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convertir la lista a JSON
            Gson gson = new Gson();
            String json = gson.toJson(generosString);

            response.getWriter().write(json);
        } else if ("verificarAlbum".equals(action)) {

            String album = request.getParameter("albumName");

            // Aquí iría la lógica para verificar si el usuario existe
            boolean isAvailable = checkAlbumAvailability(album);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            if (isAvailable) {
                out.print("Album name is available");
            } else {
                out.print("Album name is already taken");
            }
            out.close();
        }
    }

    private boolean checkAlbumAvailability(String album_name) {
        // Aquí deberías realizar la consulta a la base de datos.
        // Por ejemplo:
        // return !userDao.isUsernameTaken(username);

        // Simulación de base de datos (ejemplo)
        ControladorAlbum alb = new ControladorAlbum();
        DataAlbum album = alb.retornarInfoAlbum(album_name);
        if (!album.getNombre().equals("ALBUM NO EXISTE")) {
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String validacion = request.getParameter("Valido");

        if (validacion.equals("true")) {
            // Obtener la sesión
            HttpSession session = request.getSession();

            // Leer el nickname desde la sesión
            String nickname = (String) session.getAttribute("nickname");

            String nombreAlbum = request.getParameter("nombreAlbum");
            int anioCreacion = Integer.parseInt(request.getParameter("anioCreacion"));

            Part filePart = request.getPart("imagenAlbum");
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
                String targetDir = "C:\\Users\\Law\\Documents\\GitHub\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\albumes\\"; // Ajusta esta ruta

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
            DataErrorBundle result = null;

            ControladorGenero GeneroPersistence = new ControladorGenero();

            String generosSeleccionados = request.getParameter("generosSeleccionados");
            if (generosSeleccionados != null) {
                String[] generosArray = generosSeleccionados.split(", ");

                Collection<DataTema> temas = new ArrayList<>();

                String[] nombresTemas = request.getParameterValues("nombreTema[]");
                String[] duracionesTemas = request.getParameterValues("duracionTema[]");
                String[] ubicacionesTemas = request.getParameterValues("ubicacionTema[]");
                Part[] archivosMusica = request.getParts().stream().filter(part -> "archivo_MP3[]".equals(part.getName())).toArray(Part[]::new);
                String[] direccionesWeb = request.getParameterValues("dirWeb[]");
                String[] tiposTemas = request.getParameterValues("tipoTema[]");

                ControladorTema TemaPersistence = new ControladorTema();
                if (nombresTemas != null) {
                    for (int i = 0; i < nombresTemas.length; i++) {
                        String nombreTema = nombresTemas[i];

                        String duracionTema = duracionesTemas[i];
                        String[] partesDuracion = duracionTema.split(":");
                        int minutos = Integer.parseInt(partesDuracion[0]);
                        int segundos = Integer.parseInt(partesDuracion[1]);

                        int duracionEnSegundos = minutos * 60 + segundos;

                        int ubicacionTema = Integer.parseInt(ubicacionesTemas[i]);
                        Part archivoMusica = archivosMusica[i];
                        String direccionWeb = direccionesWeb[i];
                        String ti_tema = tiposTemas[i];

                        if (ti_tema.equals("archivo_mp3")) {
                            String nombreArchivo = archivoMusica.getSubmittedFileName().toString();

                            // COMENTAR ESTA RUTA Y COLOCAR LA SUYA PROPIA, RUTA DONDE GUARDAR EL TEMAZO /////////////////////////////////////////////////////// 
                            String targetDir = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/temas/";

                            // Extraer el nombre del archivo
                            String nombretemazo = Paths.get(archivoMusica.getSubmittedFileName()).getFileName().toString(); // Obtener solo el nombre

                            // Crear el archivo destino
                            File file = new File(targetDir + nombretemazo);

                            // Asegurarse de que el directorio exista
                            File directory = new File(targetDir);
                            if (!directory.exists()) {
                                directory.mkdirs(); // Crear el directorio si no existe
                            }

                            // Guardar el archivo
                            try (InputStream fileContent = archivoMusica.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {

                                byte[] buffer = new byte[1024];
                                int bytesRead;

                                while ((bytesRead = fileContent.read(buffer)) != -1) {
                                    fos.write(buffer, 0, bytesRead);
                                }

                                System.out.println("Archivo guardado: " + nombretemazo);
                            } catch (IOException e) {
                                e.printStackTrace(); // Manejo de errores
                            }
                            TemaPersistence.crearTemaCasiCompleto(nombreTema, nombreAlbum, duracionEnSegundos, null, nombreArchivo, ubicacionTema);
                        } else if (ti_tema.equals("direccionWeb")) {
                            TemaPersistence.crearTemaCasiCompleto(nombreTema, nombreAlbum, duracionEnSegundos, direccionWeb, null, ubicacionTema);
                        }
                        DataTema tema = TemaPersistence.retornarTema(nombreTema, nombreAlbum);
                        temas.add(tema);
                    }
                }

                ControladorAlbum albumPersistence = new ControladorAlbum();
                DataAlbum album = albumPersistence.agregarAlbum(nickname, nombreAlbum, fileName, anioCreacion, temas);

                if (nombresTemas != null) {
                    for (int i = 0; i < nombresTemas.length; i++) {
                        String nombreTema = nombresTemas[i];
                        DataTema temazo = TemaPersistence.retornarTema(nombreTema, nombreAlbum);
                        TemaPersistence.actualizarTema(temazo, album);
                    }
                }

                for (String genero : generosArray) {
                    Collection<String> albumes = albumPersistence.retornarAlbumsDelGenero(genero);
                    GeneroPersistence.actualizarGenero(new DataGenero(genero), albumes, album);
                }

                result = new DataErrorBundle(true, 0);

            }

            if (result != null) {
                if (result.getValor()) {
                    out.print("{\"status\": \"success\", \"message\": \"Album registrado exitosamente.\"}");
                } else {
                    out.print("{\"status\": \"error\", \"message\": \"" + result.getNumero() + "\"}"); // Mensaje de error específico
                }
            } else {
                out.print("{\"status\": \"error\", \"message\": \"Error desconocido.\"}");
            }
            out.flush(); //enviar la respuesta

        } else {
            request.setAttribute("errorMessage", "EL NOMBRE DEL ALBUM ESTA REPETIDO.");
            request.getRequestDispatcher("AltaDeAlbum.jsp").forward(request, response);
        }
    }
}
