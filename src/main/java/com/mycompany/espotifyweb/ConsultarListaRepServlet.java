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
import logica.ListaParticular;
import logica.ListaPorDefecto;
import logica.controladores.ControladorTema;
import logica.dt.DataTema;
import persistencia.DAO_Genero;
import persistencia.DAO_ListaReproduccion;

public class ConsultarListaRepServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        if (action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El parámetro 'action' es requerido");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        System.out.println("Acción recibida: " + action);

        if ("getGeneros".equals(action)) {
            obtenerGeneros(out);
        } else if ("getListasParticulares".equals(action)) {
            obtenerListasParticulares(out);
        } else if ("getListasPorGenero".equals(action)) {
            String genero = request.getParameter("genero");
            if (genero == null || genero.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El género es requerido");
                return;
            }
            obtenerListasPorGenero(genero, out);
        } else if ("getTemasPorLista".equals(action)) {
            String nombreLista = request.getParameter("listaNombre");
            System.out.println("Nombre de la lista recibido: " + nombreLista);
            if (nombreLista == null || nombreLista.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
                return;
            }
            obtenerTemasPorLista(nombreLista, out, response); 
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }

        out.flush();
    }

    private void obtenerGeneros(PrintWriter out) throws IOException {
        DAO_Genero persistence = new DAO_Genero();
        Collection<Genero> generosObjeto = persistence.findAll();
        ArrayList<String> generosString = new ArrayList<>();

        for (Genero g : generosObjeto) {
            generosString.add(g.getNombre());
        }

        Gson gson = new Gson();
        String json = gson.toJson(generosString);
        out.write(json);
    }

    private void obtenerListasParticulares(PrintWriter out) throws IOException {
        DAO_ListaReproduccion daoListaPart = new DAO_ListaReproduccion();
        Collection<ListaParticular> listaP = daoListaPart.findAllListasParticulares();

        List<Map<String, Object>> listasParticularesRetornables = new ArrayList<>();
        
        for (ListaParticular lista : listaP) {
            if (lista.getVisibilidad()) {
                Map<String, Object> listaMap = new HashMap<>();
                listaMap.put("nombre", lista.getNombreLista());
                listaMap.put("cliente", lista.getCliente().getNickname());
                listaMap.put("imagen", lista.getFoto());
                listasParticularesRetornables.add(listaMap);
            }
        }

        Gson gson2 = new Gson();
        String json2 = gson2.toJson(listasParticularesRetornables);
        out.write(json2);
    }

    private void obtenerListasPorGenero(String genero, PrintWriter out) throws IOException {
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

        Gson gson3 = new Gson();
        String json3 = gson3.toJson(listasRetornables);
        out.write(json3);
    }

    private void obtenerTemasPorLista(String nombreLista, PrintWriter out, HttpServletResponse response) throws IOException {
    if (nombreLista == null || nombreLista.isEmpty()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre de la lista es requerido");
        return;
    }

    try {
        ControladorTema ctrlTema = new ControladorTema();
        Collection<DataTema> temas = ctrlTema.retornarTemasDeLaLista(nombreLista, 2);

        if (temas.isEmpty()) {
            out.print("[]");
            return;
        }

        StringBuilder jsonResponse = new StringBuilder("[");
        for (DataTema tema : temas) {
            jsonResponse.append("{\"nombre\":\"").append(tema.getNickname()).append("\",")
                        .append("\"album\":\"").append(tema.getNomAlb()).append("\"},");
        }

        jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Eliminar la última coma
        jsonResponse.append("]");
        out.print(jsonResponse.toString());
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los temas");
    }
}



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
