package com.mycompany.espotifyweb;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.Genero;
import logica.ListaPorDefecto;
import persistencia.DAO_Genero;
import persistencia.DAO_ListaReproduccion;

public class ConsultarListaRepServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CrearListaRepServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CrearListaRepServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        switch (action) {
            case "getGeneros":
                DAO_Genero persistence = new DAO_Genero();
                Collection<Genero> generosObjeto = persistence.findAll();
                ArrayList<String> generosString = new ArrayList<>();

                for (Genero g : generosObjeto) {
                    generosString.add(g.getNombre());
                }

                // Convertir la lista a JSON
                Gson gson = new Gson();
                String json = gson.toJson(generosString);

                response.getWriter().write(json);
                break;

            case "getListasParticulares":
                //out.print(getListasParticularesAsJson());
                break;

            case "getListasPorGenero":
    String genero = request.getParameter("genero");
    if (genero == null || genero.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El género es requerido");
        return;
    }

    DAO_ListaReproduccion daoListaRep = new DAO_ListaReproduccion();
    Collection<ListaPorDefecto> listas = daoListaRep.findListasPorGeneros(genero);

    List<Map<String, Object>> listasRetornables = new ArrayList<>();

    for (ListaPorDefecto lista : listas) {
        Map<String, Object> listaMap = new HashMap<>();
        listaMap.put("nombre", lista.getNombreLista());
        listaMap.put("genero", lista.getGenero().getNombre());
        listaMap.put("imagen", lista.getFoto());
        listasRetornables.add(listaMap);
    }

    Gson gson2 = new Gson();
    String json2 = gson2.toJson(listasRetornables);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json2);
    break;

            case "getAllListas":

                // out.print(getAllListasAsJson());
                break;

            case "getTemasPorLista":
                String listaId = request.getParameter("id");
                // out.print(getTemasPorListaAsJson(listaId)); // Método que devuelve los temas por ID
                break;

            default:
                out.print("{\"error\": \"Acción no válida\"}");
                break;
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
