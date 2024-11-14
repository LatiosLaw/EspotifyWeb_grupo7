<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="estilos/ConsultarUsuario.css">
        <link rel="stylesheet" type="text/css" href="estilos/EstilosGenerales.css">
        <link rel="stylesheet" href="estilos/DistribucionSinRep.css">
        <title>Espotify</title>
    </head>
    <body>
        <%
            String userType = (String) session.getAttribute("userType");
            String nickname = (String) session.getAttribute("nickname");
            String userToFollow = request.getParameter("usr");
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
                        <% if (nickname != null) {%>
                        <a href="ConsultarUsuario.jsp?usr=<%= nickname%>">
                            <% }%>
                            <img id="imagenUser" src="imagenes/usuarios/defaultUser.png" class="userIMG">
                        <% if (nickname != null) {%>
                        </a>
                            <% }%>
                    </div>
                    <ul class="listUser">
                        <li class="userName">
                            <% if (nickname != null) {%>
                            <a href="ConsultarUsuario.jsp?usr=<%= nickname%>">
                            <% }%>
                                <p class="name">
                                    <%= nickname != null ? nickname : "Visitante"%>
                                </p>
                            <% if (nickname != null) {%>
                            </a>
                            <% }%>
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
                        <% if (suscrito == true) { %>
                        <a id="publicarListaLink" href="PublicarLista.jsp">Publicar Lista</a>
                        <% } %>
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

                        <div id="perfil">
                            <h2>Perfil</h2>
                            <button id="seguirUsuarioBtn" style="display:none;">Follow</button>
                            <button id="dejarSeguirUsuarioBtn" style="display:none;">Unfollow</button>
                            <p id = "pNickname"><strong>Nickname:</strong> <span id="nickname"><%= userToFollow != null ? userToFollow : nickname%></span></p>
                            <p id = "pCorreo"><strong>Correo Electronico:</strong> <span id="correo"></span></p>
                            <p id = "pNya" class="nya"><strong>Nombre y Apellido:</strong> <span id="nombre"></span> <span id="apellido"></span></p>
                            <p id = "pFechaNacimiento"><strong>Fecha de Nacimiento:</strong> <span id="fechaNacimiento"></span></p>
                            <img id="imagenPerfil" alt="Imagen de perfil">
                        </div>

                        <div class="btnsPerfil">
                            <button id="seguidoresBtn" style="display:none;">Seguidores</button>
                            <button id="seguidosBtn" style="display:none;">Seguidos</button>
                            <button id="listasBtn" style="display:none;">Listas</button>
                            <button id="favoritosBtn" style="display:none;">Favoritos</button>
                            <button id="albumesBtn" style="display:none;">Álbumes</button>
                        </div>

                        <div class="contPerfil">

                            <div id="seguidores" style="display:none;">
                                <h2>Seguidores</h2>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nickname</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaSeguidores"></tbody>
                                </table>
                            </div>

                            <div id="seguidos" style="display:none;">
                                <h2>Seguidos</h2>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nickname</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaSeguidos"></tbody>
                                </table>
                            </div>

                            <div id="listas" style="display:none;">
                                <h2>Listas de Reproducción</h2>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Perfil</th>
                                            <th>Nombre</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaListas"></tbody>
                                </table>
                            </div>

                            <div id="favoritos" style="display:none;">
                                <h2>Favoritos</h2>

                                <div class="listFavs">
                                    <h3>Listas Favoritas</h3>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Perfil</th>
                                                <th>Nombre de la Lista</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody id="tablaListasFavoritas"></tbody>
                                    </table>
                                </div>

                                <div class="albFavs">
                                    <h3>Álbumes Favoritos</h3>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Perfil</th>
                                                <th>Nombre del Álbum</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody id="tablaAlbumesFavoritos"></tbody>
                                    </table>
                                </div>

                                <div class="temFavs">
                                    <h3>Temas Favoritos</h3>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Nombre del Tema</th>
                                                <th>Nombre del Álbum</th>
                                            </tr>
                                        </thead>
                                        <tbody id="tablaTemasFavoritos"></tbody>
                                    </table>
                                </div>

                            </div>

                            <div id="albumes" style="display:none;">
                                <h2>Álbumes</h2>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Perfil</th>
                                            <th>Nombre del Álbum</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaAlbumes"></tbody>
                                </table>
                            </div>

                        </div>
                    </div>

                </div>

            </div>

            <dialog id="winLogin"> <!-- Diálogo de inicio de sesión -->
                <button id="cerrarFormLogin">Cerrar</button>
                <div class="tituloFormLogin">
                    <h2>Inicio de Sesión</h2>
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
                        <button type="submit">Iniciar Sesión</button>
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
                <form id="altaUsuarioForm" method="post" enctype="multipart/form-data">
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
                            <label for="dirWeb">Dirección web (URL):</label>
                            <input type="text" id="dirWeb" name="dirWeb">
                        </div>
                        <div>
                            <label for="biografia">Biografía:</label>
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
                        <button type="button" onclick="submitForm()">Agregar Usuario</button>
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
            document.addEventListener('DOMContentLoaded', function () {
                // Obtener el nickname del usuario a seguir
                const nicknameElement = document.getElementById('nickname');
                const nickToFollow = nicknameElement ? nicknameElement.innerText : null;

                const sessionNickname = "${sessionScope.nickname}";

                if (sessionNickname) {
                    if (nickToFollow) {
                        verificarEstadoSeguimiento(nickToFollow);
                        document.getElementById('seguirUsuarioBtn').style.display = 'block';
                        document.getElementById('dejarSeguirUsuarioBtn').style.display = 'block';
                    }
                } else {
                    document.getElementById('seguirUsuarioBtn').style.display = 'none';
                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'none';
                }

                function verificarEstadoSeguimiento(nick) {
                    fetch('http://localhost:8080/EspotifyWeb/SeguirUsuarioServlet?id=' + nick)
                            .then(response => response.json())
                            .then(data => {
                                if (nick === sessionNickname) {
                                    // Si es el mismo nickname, ocultar ambos botones
                                    document.getElementById('seguirUsuarioBtn').style.display = 'none';
                                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'none';
                                } else if (data.isFollowed) {
                                    // Si esta siendo seguido, mostrar el boton de dejar de seguir
                                    document.getElementById('seguirUsuarioBtn').style.display = 'none';
                                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'block';
                                } else {
                                    // Si no esta siendo seguido, mostrar el boton de seguir
                                    document.getElementById('seguirUsuarioBtn').style.display = 'block';
                                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'none';
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });
                }

                function seguirUsuario(nick) {
                    fetch('http://localhost:8080/EspotifyWeb/SeguirUsuarioServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({id: nick})
                    })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    alert('Ahora sigues a este usuario.');
                                    document.getElementById('seguirUsuarioBtn').style.display = 'none';
                                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'block';
                                } else {
                                    alert('Error al seguir al usuario: ' + (data.error || 'Error desconocido'));
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('Error al intentar seguir al usuario.');
                            });
                }

                function dejarSeguirUsuario(nick) {
                    fetch('http://localhost:8080/EspotifyWeb/DejarDeSeguirUsuarioServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({id: nick})
                    })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    alert('Has dejado de seguir a este usuario.');
                                    document.getElementById('dejarSeguirUsuarioBtn').style.display = 'none';
                                    document.getElementById('seguirUsuarioBtn').style.display = 'block';
                                } else {
                                    alert('Error al dejar de seguir al usuario: ' + (data.error || 'Error desconocido'));
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('Error al intentar dejar de seguir al usuario.');
                            });
                }

                document.getElementById('seguirUsuarioBtn').addEventListener('click', function () {
                    seguirUsuario(nickToFollow);
                });

                document.getElementById('dejarSeguirUsuarioBtn').addEventListener('click', function () {
                    dejarSeguirUsuario(nickToFollow);
                });
            });
        </script>

        <script>
            function emitirBusqueda() {
                const searchInput = document.getElementById('searchInput').value;

                // Redirigir a la URL con el parámetro de búsqueda
                if (searchInput === "" || searchInput === null) {
                    alert("Por favor, ingrese un termino de busqueda.");
                } else {
                    window.location.href = "BuscarCosas.jsp?search=" + searchInput;
                }
            }
        </script>
        <!-- Consultar usuario -->
        <script src="scripts/ConsultarUsuario.js"></script>

        <!-- Script registro de usuario -->
        <script src="scripts/AgregarUsuario.js"></script>

        <!-- Script inicio y cierre de sesión -->
        <script src="scripts/Login.js"></script>
        <script src="scripts/Logout.js"></script>

        <!-- Formulario de login y signup -->
        <script src="scripts/LoginSignupForm.js"></script>

        <!-- Cosas del reproductor de música -->
        <script src="scripts/Reproductor.js"></script>

        <!-- Evitar que las imágenes sean arrastradas -->
        <script>
            document.addEventListener('dragstart', function (event) {
                event.preventDefault();
            });
        </script>
        
        <script src="scripts/ImagenDeUsuario.js"></script>
    </body>
</html>