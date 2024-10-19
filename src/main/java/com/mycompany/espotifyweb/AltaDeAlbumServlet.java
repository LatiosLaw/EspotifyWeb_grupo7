package com.mycompany.espotifyweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import javax.servlet.http.Cookie;
import logica.Genero;
import logica.controladores.ControladorAlbum;
import logica.dt.DataAlbum;
import logica.dt.DataGenero;
import logica.dt.DataTema;
import persistencia.DAO_Genero;

public class AltaDeAlbumServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    String nickname = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nickname".equals(cookie.getName())) {
                    nickname = cookie.getValue();
                    break;
                }
            }
        }
        
    String nombreAlbum = request.getParameter("nombreAlbum");
    int anioCreacion = Integer.parseInt(request.getParameter("anioCreacion"));
    String imagenAlbum = request.getParameter("imagenAlbum");

    String generosSeleccionados = request.getParameter("generosSeleccionados");
    if (generosSeleccionados != null) {
        String[] generosArray = generosSeleccionados.split(", ");
        for (String genero : generosArray) {
            System.out.println(genero);
        }

        Collection<DataTema> temas = new ArrayList<>();

        String[] nombresTemas = request.getParameterValues("nombreTema[]");
        String[] duracionesTemas = request.getParameterValues("duracionTema[]");
        String[] ubicacionesTemas = request.getParameterValues("ubicacionTema[]");
        String[] archivosMusica = request.getParameterValues("archivoMusica[]");

        if (nombresTemas != null) {
            for (int i = 0; i < nombresTemas.length; i++) {
                String nombreTema = nombresTemas[i];
                
                String duracionTema = duracionesTemas[i];
                String[] partesDuracion = duracionTema.split(":");
                int minutos = Integer.parseInt(partesDuracion[0]);
                int segundos = Integer.parseInt(partesDuracion[1]);
                
                int duracionEnSegundos = minutos * 60 + segundos;

                int ubicacionTema = Integer.parseInt(ubicacionesTemas[i]);
                String archivoMusica = archivosMusica[i];

                DataTema tema = new DataTema(nombreTema, nombreAlbum, duracionEnSegundos, ubicacionTema, archivoMusica);
                temas.add(tema);
            }
        }

        ControladorAlbum albumPersistence = new ControladorAlbum();
        DataAlbum album = albumPersistence.agregarAlbum(nickname, nombreAlbum, imagenAlbum, anioCreacion, temas);
        if(album == null){
            System.out.println("Pinga.");
        }
        Collection<DataGenero> generos = new ArrayList<>();
        for (String genero : generosArray) {
            DataGenero g = new DataGenero(genero);
            generos.add(g);
        }

        albumPersistence.actualizarAlbum(album, generos);
    }
}
}
