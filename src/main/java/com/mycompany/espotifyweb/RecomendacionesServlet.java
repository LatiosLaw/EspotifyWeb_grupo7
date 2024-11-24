package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.Registro_tema;
import persistencia.DAO_RegistroTema;

public class RecomendacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n-----Recomendaciones GET-----");
        String action = request.getParameter("action");

        response.setContentType("application/json;charset=UTF-8");

        if ("devolverTemazos".equals(action)) {
            try (PrintWriter out = response.getWriter()) {
                DAO_RegistroTema persistence = new DAO_RegistroTema();
                //  Collection<Registro_tema> temas = persistence.buscarLos100MasPopulares();
                Collection<Registro_tema> temas = null;

                // Usa Gson para convertir la colección a JSON
                Gson gson = new Gson();
                String json = gson.toJson(temas);

                // Establece el contenido de la respuesta
                response.setContentType("application/json;charset=UTF-8");
                out.print(json);

                // Log para verificar el JSON generado
                System.out.println("JSON generado: " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n-----Recomendaciones GET-----");
    }

}
