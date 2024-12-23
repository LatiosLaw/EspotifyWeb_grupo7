<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Espotify Web</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="estilos.css">
        <style>
            /* contenedor del usuario */
            #userInfo {
                position: absolute;
                top: 10px;
                right: 10px;
                background-color: #f2f2f2;
                padding: 10px;
                border-radius: 5px;
            }
        </style>
    </head>
    <body>

        <h1>Pagina Principal</h1>

        <a href="Login.html">Iniciar Sesion</a>
        <a href="PaginaInicio.html">Inicio</a>
        <a href="AgregarUsuario.jsp">Agregar Usuario</a>
        <a href="ConsultarUsuario.html">Consultar Usuario</a>

        <%
            // Obtener el tipo de usuario y nickname desde la sesion
            String userType = (String) session.getAttribute("userType");
            String nickname = (String) session.getAttribute("nickname");
            Boolean suscrito = (Boolean) session.getAttribute("suscrito");
        %>

        <div id="userInfo">
            <% if (nickname != null) {%>
            <p>Usuario: <strong><%= nickname%></strong></p>
            <p>Tipo: <strong><%= userType != null ? userType : "Desconocido"%></strong></p>
            <% } else { %>
            <p>Usuario: <strong>Visitante</strong></p>
            <% } %>
        </div>

        <% if ("Cliente".equals(userType) || userType == null) { %>
        <a id="consultarListaLink" href="ConsultarListaRep.html">Consultar Lista</a>
        <a id="consultarAlbumLink" href="ConsultarAlbum.html">Consultar Album</a>
        <a id="BuscarLink" href="BuscarCosas.html">Buscar</a>
        <% } %>

        <% if ("Cliente".equals(userType)) { %>
        <a id="seguirUsuarioLink" href="SeguirUsuario.html">Seguir Usuario</a>
        <a id="dejarDeSeguirLink" href="DejarSeguirUsuario.html">Dejar De Seguir Usuario</a>
        <a id="AgregarTemaListaLink" href="AgregarTemaALista.html">Agregar Tema a Lista</a>
        <a id="publicarListaLink" href="PublicarLista.html">Publicar Lista</a>
        <a id="contratarSuscripcionLink" href="ContratarSuscripcion.jsp">Contratar Suscripcion</a>
        <a id="actualizarSusLink" href="ActualizarSuscripcion.html">Actualizar Suscripcion</a>
            <% if (suscrito) { %>       
            <a id="crearListaLink" href="AltaDeLista.jsp">Crear Lista</a>
            <% } %>
        <% } %>

        <% if ("Artista".equals(userType)) { %>
        <a id="altaDeAlbumLink" href="AltaDeAlbum.jsp">Alta de Album</a>
        <% }%>

    </body>
</html>