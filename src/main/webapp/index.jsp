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

        <h1>Página Principal</h1>

        <a href="Login.html">Iniciar Sesión</a>
        <a href="AgregarUsuario.html">Agregar Usuario</a>

        <%
            // Obtener el tipo de usuario y nickname desde la sesión
            String userType = (String) session.getAttribute("userType");
            String nickname = (String) session.getAttribute("nickname");
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
        <a id="consultarListaLink" href="ConsultarListaRep.jsp">Consultar Lista</a>
        <% } %>

        <% if ("Cliente".equals(userType)) { %>
        <a id="seguirUsuarioLink" href="SeguirUsuario.html">Seguir Usuario</a>
        <a id="dejarDeSeguirLink" href="DejarSeguirUsuario.html">Dejar De Seguir Usuario</a>
        <a id="crearListaLink" href="AltaDeLista.jsp">Crear Lista</a>
        <a id="AgregarTemaListaLink" href="AgregarTemaALista.jsp">Agregar Tema a Lista</a>
        <a id="publicarListaLink" href="PublicarLista.html">Publicar Lista</a>
        <a id="consultarListaLink" href="ConsultarListaRep.jsp">Consultar Lista</a>
        <a id="contratarSuscripcionLink" href="ContratarSuscripcion.jsp">Contratar Suscripción</a>
        <% } %>

        <% if ("Artista".equals(userType)) { %>
        <a id="altaDeAlbumLink" href="AltaDeAlbum.jsp">Alta de Álbum</a>
        <% }%>

    </body>
</html>