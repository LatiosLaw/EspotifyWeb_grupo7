<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" href="estilos.css">
        <title>Espotify</title>
    </head>
    <body>
        <div class="cuerpo">
            
            <header class="encaPrin">
                <div>
                    <a href="index.jsp" class="EspotifyLogo">
                        <img src="imagenes/espotify/spotify-logo.png" class="EspotifyIMG">
                        <h1>Espotify</h1>
                    </a>
                </div>
                
                <div class="busqueda">
                    <input type="text" placeholder="Tema, Album, Lista" class="barraBusqueda">
                    <button class="btnBusqueda">Buscar</button>
                </div>
                
                <div class="userDiv">
                    <div class="divUserIMG">
                        <a href="ConsultarUsuario.jsp"><img src="imagenes/espotify/user.png" class="userIMG"></a>
                    </div>
                    <ul class="listUser">
                        <%
                            String userType = (String) session.getAttribute("userType");
                            String nickname = (String) session.getAttribute("nickname");
                            Boolean suscrito = (Boolean) session.getAttribute("suscrito");
                        %>
                        <li class="userName"><a href="ConsultarUsuario.jsp"><p class="name"><%= nickname != null ? nickname : "Visitante"%></a></p></li>
                            <% if (nickname == null) { %>
                        <li><p><button id="abrirFormLogin">Iniciar sesi?n</button></p></li>
                                    <% } else { %>
                        <li><p>Tipo: <%= userType != null ? userType : "Desconocido"%></p></li>
                        <li><p><button id="logoutButton">Cerrar sesi?n</button></p></li>
                                    <% } %>
                    </ul>
                </div>
            </header>

            <div class="btnsEx">
                <button>Generos</button>
                <button>Artistas</button>
            </div>

            <div class="mainCon">
                
                <div class="dinamico">
                    
                    <div class="btnsNav">
                        <% if ("Cliente".equals(userType) || userType == null) { %>
                        <a id="consultarListaLink" href="index.jsp">Consultar Album</a>
                        <a id="consultarListaLink" href="ConsultarListaRep.jsp">Consultar Lista</a>
                        <% } %>

                        <% if ("Cliente".equals(userType)) { %>
                        <a id="AgregarTemaListaLink" href="AgregarTemaALista.jsp">Agregar Tema a Lista</a>
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
                        <h1>Consulta de Perfil de Usuario</h1>

                        <div id="perfil">
                            <h2>Perfil</h2>
                            <p><strong>Nickname:</strong> <span id="nickname"></span></p>
                            <p><strong>Correo Electronico:</strong> <span id="correo"></span></p>
                            <p><strong>Nombre:</strong> <span id="nombre"></span></p>
                            <p><strong>Apellido:</strong> <span id="apellido"></span></p>
                            <p><strong>Fecha de Nacimiento:</strong> <span id="fechaNacimiento"></span></p>
                            <p><strong>Imagen:</strong> <img id="imagenPerfil" src="" alt="Imagen de perfil"></p>
                        </div>

                        <div id="seguidores">
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

                        <div id="seguidos">
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

                        <div id="listas">
                            <h2>Listas de Reproduccion</h2>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="tablaListas"></tbody>
                            </table>
                        </div>

                        <div id="favoritos">
                            <h2>Favoritos</h2>

                            <div>
                                <h3>Listas Favoritas</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nombre de la Lista</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaListasFavoritas"></tbody>
                                </table>
                            </div>

                            <div>
                                <h3>Albumes Favoritos</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nombre del Album</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaAlbumesFavoritos"></tbody>
                                </table>
                            </div>

                            <div>
                                <h3>Temas Favoritos</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nombre del Tema</th>
                                            <th> Nombre del Album</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tablaTemasFavoritos"></tbody>
                                </table>
                            </div>

                        </div>

                        <div id="albumes">
                            <h2>Albumes</h2>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre del Album</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="tablaAlbumes"></tbody>
                            </table>
                        </div>
                    </div>
                    
                </div>
                
                <div class="reproductor">
                    <div class="temaRep">
                        <img src="imagenes/espotify/user.png" class="artIMG">
                        <h3>Nombre Tema</h3>
                        <h2>Nombre Artista</h2>
                    </div>
                    
                    <div class="controlRep">
                        
                        <audio id="miAudio">
                            <source id="audioSource" src="temas/DONMAI.mp3" type="audio/mpeg">
                            Tu navegador no soporta el elemento audio.
                        </audio>
                        
                        <div class="tiempoRep">
                            <div id="progressBar" onmousedown="startAdjustingProgressBar(event)">
                                <div id="progress"></div>
                            </div>
                            <div class="tiempos">
                                <span id="currentTime">0:00</span><span id="totalTime">0:00</span>
                            </div>
                            
                            <div class="volumen">
                                <button id="muteBtn" onclick="muteVolume()"><img src="imagenes/espotify/volume-on.png"></button>
                                <button id="unmuteBtn" onclick="unmuteVolume()" style="display: none;"><img src="imagenes/espotify/volume-off.png"></button>
                                <div id="volumeBar" onmousedown="startAdjustingVolume(event)">
                                    <div id="volumeLevel"></div>
                                </div>
                            </div>
                            
                            <div class="btnsMedia">
                                <button id="prevBtn" onclick="prevAudio()"><img src="imagenes/espotify/back-button.png"></button>
                                <button id="pauseBtn" hidden><img src="imagenes/espotify/pause-button.png"></button>
                                <button id="playBtn"><img src="imagenes/espotify/play-button.png"></button>
                                <button id="nextBtn" onclick="nextAudio()"><img src="imagenes/espotify/next-button.png"></button>
                            </div>
                            
                        </div>
                    </div>
                </div>
            </div>
                    
            <dialog id="winLogin"> <!-- Di?logo de inicio de sesi?n -->
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
                        <label for="passLogin">Contrase?a:</label>
                        <input type="password" id="passLogin" name="passLogin" required>
                    </div>
                    <div class="btnsFormLogin">
                        <button type="submit">Iniciar Sesi?n</button>
                        <button type='reset' id="abrirFormSignup">Registrarse</button>
                    </div>
                </form>
                <div id="resultado"></div> <!-- Mensajes de resultado -->
            </dialog>
                    
            <dialog id="winSignup"> <!-- Di?logo de registro de usuario -->
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
                        <label for="pass">Contrase?a:</label>
                        <input type="password" id="pass" name="pass" required>
                    </div>
                    <div>
                        <label for="confirmPass">Confirmar Contrase?a:</label>
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
        
        <!-- Consultar usuario -->
        <script src="scripts/ConsultarUsuario.js"></script>
        
        <!-- Script registro de usuario -->
        <script src = "scripts/AgregarUsuario.js"></script>
        
        <!-- Script inicio y cierre de sesion -->
        <script src="scripts/Login.js"></script>
        <script src="scripts/Logout.js"></script>

        <!-- Formulario de login y signup -->
        <script src = "scripts/LoginSignupForm.js"></script>
        
        <!-- Cosas del reproductor de musica -->
        <script src="scripts/Reproductor.js"></script>
        
        <!-- Evitar que las imagenes sean arrastradas -->
        <script>
            document.addEventListener('dragstart', function(event) {
                event.preventDefault();
            });
        </script>
    </body>
</html>