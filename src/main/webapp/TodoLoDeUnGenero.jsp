<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" href="estilos/EstilosGenerales.css">
        <link rel="stylesheet" href="estilos/BuscarCosas.css">
        <title>Espotify</title>
    </head>
    <body>
        <%
            String userType = (String) session.getAttribute("userType");
            String nickname = (String) session.getAttribute("nickname");
            Boolean suscrito = (Boolean) session.getAttribute("suscrito");
        %>
        <div class="cuerpo">
            <header class="encaPrin">
                <div>
                    <a href="index.jsp" class="EspotifyLogo">
                        <img src="imagenes/espotify/spotify-logo.png" class="EspotifyIMG">
                        <h1>Espotify</h1>
                    </a>
                </div>

                <div class="busqueda">
                    <input id="searchInput" type="text" placeholder="Tema, Album, Lista" class="barraBusqueda">
                    <a class="btnBusqueda" onclick="emitirBusqueda()">Buscar</a>
                </div>

                <div class="userDiv">
                    <div class="divUserIMG">
                        <% if (nickname != null) { %>
                        <a href="ConsultarUsuario.jsp?usr=<%= nickname%>">
                        <% } %>
                        <img src="imagenes/espotify/user.png" class="userIMG"></a>                       
                    </div>
                    <ul class="listUser">
                        <li class="userName">
                            <a href="ConsultarUsuario.jsp?usr=<%= nickname %>"><p class="name"><%= nickname != null ? nickname : "Visitante"%></a></p>
                        </li>
                        <% if (nickname == null) { %>
                        <li><p><button id="abrirFormLogin">Iniciar sesion</button></p></li>
                                    <% } else {%>
                        <li><p>Tipo: <%= userType != null ? userType : "Desconocido"%></p></li>
                        <li><p><button id="logoutButton">Cerrar sesion</button></p></li>
                                    <% } %>
                    </ul>
                </div>
            </header>

            <div class="mainCon">
                <div class="dinamico">
                    <div class="btnsNav">
                        <a href="TodosLosGeneros.jsp">Generos</a>
                        <a href="TodosLosArtistas.jsp">Artistas</a>
                        <% if ("Cliente".equals(userType)) { %>
                        <a id="publicarListaLink" href="PublicarLista.jsp">Publicar Lista</a>
                        <a id="contratarSuscripcionLink" href="ContratarSuscripcion.jsp">Contratar Suscripcion</a>
                        <a id="actualizarSusLink" href="ActualizarSuscripcion.jsp">Actualizar Suscripcion</a>
                        <% if (suscrito) { %>       
                        <a id="crearListaLink" href="AltaDeLista.jsp">Crear Lista</a>
                        <% } %>
                        <% } %>

                        <% if ("Artista".equals(userType)) { %>
                        <a id="altaDeAlbumLink" href="AltaDeAlbum.jsp">Alta de Album</a>
                        <% }%>
                    </div>

                    <div class="realDinamico">

                            <h2 class="contStart">Todo lo del Genero:</h2>
                            
                            <h3 class="contStart2">Listas del Genero Encontradas:</h3>
                            <div id="listaGeneroGenBody">
                                <!-- aca se carga la lista -->
                            </div>
                            
                            <h3 class="contStart2">Lista de Albumes Encontrados:</h3>
                            <div id="albumGenBody">
                                <!-- aca se carga la lista -->
                            </div>
                        
                    </div>
                </div>

            </div>

            <dialog id="winLogin"> <!-- Dialogo de inicio de sesion -->
                <button id="cerrarFormLogin">Cerrar</button>
                <div class="tituloFormLogin">
                    <h2>Inicio de Sesion</h2>
                </div>
                <form id="loginForm" method="post, dialog">
                    <div>
                        <label for="nicknameLogin">Nickname:</label>
                        <input type="text" id="nicknameLogin" name="nicknameLogin" required>
                    </div>
                    <div>
                        <label for="passLogin">Contraseña:</label>
                        <input type="password" id="passLogin" name="passLogin" required>
                    </div>
                    <div class="btnsFormLogin">
                        <button type="submit">Iniciar Sesion</button>
                        <button type='reset' id="abrirFormSignup">Registrarse</button>
                    </div>
                </form>
                <div id="resultado"></div> <!-- Mensajes de resultado -->
            </dialog>

            <dialog id="winSignup"> <!-- Diálogo de registro de usuario -->
                <button id="cerrarFormSignup">Cerrar</button>
                <div class="tituloFormSignup">
                    <h2>Registro de Usuario</h2>
                </div>
                <form id="altaUsuarioForm" method="post" action="AgregarUsuarioServlet" enctype="multipart/form-data">
                    <c:if test="${not empty errorMessage}">
                        <p id="errorMessage" style="color: red;">${errorMessage}</p>
                    </c:if>
                    <div>
                        <input type="hidden" id='Valido' name='Valido' value="true">  
                        <label for="tipoUsuario">Tipo de Usuario:</label>
                        <select id="tipoUsuario" name="tipoUsuario" required>
                            <option value="">Seleccione...</option>
                            <option value="cliente">Cliente</option>
                            <option value="artista">Artista</option>
                        </select>
                    </div>
                    <div>
                        <label for="nickname">Nickname:</label>
                        <input type="text" id="nickname" onkeyup="checkNickname()" name="nickname" required>
                        <span id="nickValido" style="color: red; display: block;"></span>
                    </div>
                    <div>
                        <label for="nombre">Nombre:</label>
                        <input type="text" id="nombre" name="nombre" required>
                    </div>
                    <div>
                        <label for="apellido">Apellido:</label>
                        <input type="text" id="apellido" name="apellido" required>
                    </div>
                    <div>
                        <label for="mail">Email:</label>
                        <input type="email" id="mail" onkeyup="checkCorreo()" name="mail" required>
                        <span id="correoValido" style="color: red; display: block;"></span>
                    </div>
                    <div>
                        <label for="foto">Foto de Perfil (Opcional):</label>
                        <input type="file" id="foto" name="foto" accept="image/png, image/jpeg" style="border-style: none;">
                    </div>
                    <div id="camposArtista" style="display: none;">
                        <div>
                            <label for="dirWeb">Direccion web (URL):</label>
                            <input type="text" id="dirWeb" name="dirWeb">
                        </div>
                        <div>
                            <label for="biografia">Biografia:</label>
                            <input type="text" id="biografia" name="biografia">
                        </div>
                    </div>
                    <div>
                        <label for="pass">Contraseña:</label>
                        <input type="password" id="pass" name="pass" required>
                    </div>
                    <div>
                        <label for="confirmPass">Confirmar Contraseña:</label>
                        <input type="password" id="confirmPass" name="confirmPass" required>
                    </div>
                    <div>
                        <label for="fechaNac">Fecha de Nacimiento:</label>
                        <input type="date" id="fechaNac" name="fechaNac" required>
                    </div>
                    <div class="btnsFormSignup">
                        <button type="submit">Agregar Usuario</button>
                    </div>
                </form>
            </dialog>
        </div> <!-- Fin Cuerpo -->
        
        <script type="text/javascript">
            const sessionNickname = "${sessionScope.nickname}";
            const sessionUserType = "${sessionScope.userType}";
            const sessionSuscrito = "${sessionScope.suscrito}";
        </script>

        <script>
    function emitirBusqueda() {
        const searchInput = document.getElementById('searchInput').value;

        // Redirigir a la URL con el parámetro de búsqueda
        if(searchInput==="" || searchInput===null){
        alert("Por favor, ingrese un termino de busqueda.");
        }else{
        window.location.href = "BuscarCosas.jsp?search=" + searchInput;
        }
    }
</script>
        <!-- Buscar cosas -->
        <script src="scripts/TodoLoDeUnGenero.js"></script>

        <!-- Script registro de usuario -->
        <script src="scripts/AgregarUsuario.js"></script>
        
        <!-- Script inicio y cierre de sesion -->
        <script src="scripts/Login.js"></script>
        <script src="scripts/Logout.js"></script>

        <!-- Formulario de login y signup -->
        <script src = "scripts/LoginSignupForm.js"></script>
 
        <!-- Cosas del reproductor de musica -->
        <script src="scripts/Reproductor.js"></script>

        <!-- Evitar que las imagenes sean arrastradas -->
        <script>
                            document.addEventListener('dragstart', function (event) {
                                event.preventDefault();
                            });
        </script>
        
        <script src="scripts/ImagenDeUsuario.js"></script>
    </body>
</html>