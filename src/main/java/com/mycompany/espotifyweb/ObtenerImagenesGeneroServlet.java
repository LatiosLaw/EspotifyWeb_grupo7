package com.mycompany.espotifyweb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/obtenerImagenesGeneros")
public class ObtenerImagenesGeneroServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ruta de la carpeta donde se encuentran las imágenes
        String rutaCarpeta = getServletContext().getRealPath("/imagenes/generos");
        File carpeta = new File(rutaCarpeta);
        
        // Lista para almacenar los nombres de los archivos
        List<String> nombresArchivos = new ArrayList<>();

        // Asegurarse de que la carpeta existe y es un directorio
        if (carpeta.exists() && carpeta.isDirectory()) {
            // Filtrar solo archivos .png y .jpg
            File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));
            
            // Agregar los nombres de los archivos a la lista
            if (archivos != null) {
                for (File archivo : archivos) {
                    nombresArchivos.add(archivo.getName());
                }
            }
        }

        // Convertir la lista a JSON y enviarla en la respuesta
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(nombresArchivos));
    }
}
