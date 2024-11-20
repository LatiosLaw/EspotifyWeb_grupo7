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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import servicios.DataErrorBundle;
import servicios.DataTema;
import servicios.DataGenero;
import servicios.DataAlbum;
import servicios.IPublicador;

@MultipartConfig

public class AltaDeAlbumServlet extends HttpServlet {

    private static final String ALBUM_IMAGES_DIR = "/home/nico/Escritorio/EspotifyWeb_grupo7-segundaEntrega/src/main/webapp/imagenes/albumes";
    private static final String MUSIC_FILES_DIR = "/home/nico/Escritorio/EspotifyWeb_grupo7-segundaEntrega/src/main/webapp/temas/";
    private static final String YTDLP_PATH = "/home/nico/Escritorio/EspotifyWeb_grupo7/src/main/webapp/scripts/yt-dlp";
    
  // Cure //  private static final String ALBUM_IMAGES_DIR = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/imagenes/albumes/";
 // Cure //  private static final String MUSIC_FILES_DIR = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/temas/";
 // Cure //   private static final String YTDLP_PATH = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/scripts/";

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

        // URL del WSDL
        URL url = new URL("http://localhost:9128/publicador?wsdl");
        QName qname = new QName("http://servicios/", "PublicadorService");

        // Crear el servicio
        Service servicio = Service.create(url, qname);
        IPublicador publicador = servicio.getPort(IPublicador.class);

        if ("cargarGeneros".equals(action)) {

            Collection<String> generosString = publicador.obtenerGeneros();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Gson gson = new Gson();
            String json = gson.toJson(generosString);

            response.getWriter().write(json);
        } else if ("verificarAlbum".equals(action)) {

            String album = request.getParameter("albumName");

            boolean isAvailable = publicador.checkAlbumAvailability(album);

            response.setContentType("text/plain");
            try (PrintWriter out = response.getWriter()) {
                if (isAvailable) {
                    out.print("Album name is available");
                } else {
                    out.print("Album name is already taken");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String validacion = request.getParameter("Valido");

        if (validacion.equals("true")) {

            // URL del WSDL
            URL url = new URL("http://localhost:9128/publicador?wsdl");
            QName qname = new QName("http://servicios/", "PublicadorService");

            // Crear el servicio
            Service servicio = Service.create(url, qname);
            IPublicador publicador = servicio.getPort(IPublicador.class);

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
            if (!"default".equals(fileName)) {
                // Obtén el nombre del archivo y su tipo de contenido

                // COMENTAR ESTA RUTA Y COLOCAR LA SUYA PROPIA, RUTA DONDE GUARDAR LA FOTO DEL ALBUM /////////////////////////////////////////////////////// 
                //   String targetDir = "C:\\Users\\Law\\Documents\\GitHub\\EspotifyWeb_grupo7\\src\\main\\webapp\\imagenes\\albumes\\"; // Ajusta esta ruta
// RUTA CURE : 
                //              String targetDir = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/imagenes/albumes/";
                String targetDir = ALBUM_IMAGES_DIR;
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
            DataErrorBundle result = new DataErrorBundle();

            String generosSeleccionados = request.getParameter("generosSeleccionados");
            if (generosSeleccionados != null) {
                String[] generosArray = generosSeleccionados.split(", ");

                List<DataTema> temas = new ArrayList<>();

                String[] nombresTemas = request.getParameterValues("nombreTema[]");
                String[] duracionesTemas = request.getParameterValues("duracionTema[]");
                String[] ubicacionesTemas = request.getParameterValues("ubicacionTema[]");
                Part[] archivosMusica = request.getParts().stream().filter(part -> "archivo_MP3[]".equals(part.getName())).toArray(Part[]::new);
                String[] direccionesWeb = request.getParameterValues("dirWeb[]");
                String[] tiposTemas = request.getParameterValues("tipoTema[]");

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
                            // String targetDir = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/temas/";
// RUTA CURE : 
                            //    String targetDir = "/home/tecnologo/Escritorio/grupo7/EspotifyWeb_grupo7/src/main/webapp/temas/";
                            String targetDir = MUSIC_FILES_DIR;
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
                            publicador.crearTemaCasiCompleto(nombreTema, nombreAlbum, duracionEnSegundos, null, nombreArchivo, ubicacionTema);
                        } else if (ti_tema.equals("direccionWeb")) {

                            String outputFileName = nombreTema + ".mp3";

                            //String outputDirectory = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/temas/"+outputFileName;
                            // RUTA CURE : 
                            String outputDirectory = MUSIC_FILES_DIR + outputFileName;

                            // String projectDir = "C:/Users/Law/Documents/GitHub/EspotifyWeb_grupo7/src/main/webapp/scripts/";
                            // RUTA CURE : 
                            String projectDir = YTDLP_PATH;

                            // String executablePath = projectDir + "yt-dlp.exe";
                            // RUTA CURE 
                            String executablePath = projectDir + "yt-dlp";

                            // Comando para descargar el video usando yt-dlp
                            String downloadCommand = executablePath + " --default-search ytsearch -x --audio-format mp3 -o " + outputDirectory + " " + direccionWeb + "";

                            System.out.println(nombreTema);
                            System.out.println("outputFileName: " + outputFileName);
                            System.out.println("outputDirectory: " + outputDirectory);
                            System.out.println("projectDir: " + projectDir);
                            System.out.println("executablePath: " + executablePath);
                            System.out.println("downloadCommand: " + downloadCommand);

                            try {
                                Process downloadProcess = Runtime.getRuntime().exec(downloadCommand);

                                // Hilo para leer la salida estándar
                                new Thread(() -> {
                                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(downloadProcess.getInputStream()))) {
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            System.out.println(line);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                                // Hilo para leer la salida de error
                                new Thread(() -> {
                                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(downloadProcess.getErrorStream()))) {
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            System.out.println("Error del yt: " + line);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                                downloadProcess.waitFor();
                            } catch (Exception e) {
                                e.printStackTrace();
                                response.getWriter().println("Error al procesar el video: " + e.getMessage());
                            }
                            publicador.crearTemaCasiCompleto(nombreTema, nombreAlbum, duracionEnSegundos, null, outputFileName, ubicacionTema);
                        }
                        DataTema tema = publicador.retornarTema(nombreTema, nombreAlbum);
                        temas.add(tema);
                    }
                }

                DataAlbum album = publicador.agregarAlbum(nickname, nombreAlbum, fileName, anioCreacion, temas);

                if (nombresTemas != null) {
                    for (String nombreTema : nombresTemas) {
                        DataTema temazo = publicador.retornarTema(nombreTema, nombreAlbum);
                        publicador.actualizarTema(temazo, album);
                    }
                }

                for (String genero : generosArray) {

                    Collection<String> albumesFake = publicador.retornarAlbumesDelGenero(genero);

                    List<String> albumes = new ArrayList<>();

                    for (String albumcito : albumesFake) {
                        albumes.add(albumcito);
                    }

                    DataGenero generoReal = new DataGenero();
                    generoReal.setNombre(genero);

                    publicador.actualizarGenero(generoReal, albumes, album);
                }

                result.setValor(true);
                result.setNumero(0);

            }

            if (result != null) {
                if (result.isValor()) {
                    out.print("{\"status\": \"success\", \"message\": \"Album registrado exitosamente.\"}");
                } else {
                    out.print("{\"status\": \"error\", \"message\": \"" + result.getNumero() + "\"}"); // Mensaje de error específico
                }
            } else {
                out.print("{\"status\": \"error\", \"message\": \"Error desconocido.\"}");
            }
            out.flush();

        } else {
            request.setAttribute("errorMessage", "EL NOMBRE DEL ALBUM ESTA REPETIDO.");
            request.getRequestDispatcher("AltaDeAlbum.jsp").forward(request, response);
        }
    }
}
