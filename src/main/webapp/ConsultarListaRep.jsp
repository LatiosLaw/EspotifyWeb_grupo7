<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" href="estilos/EstilosGenerales.css">
        <link rel="stylesheet" href="estilos/ConsultarListaRep.css">
        <%
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            boolean isMobile = userAgent.contains("mobi") || userAgent.contains("android") || userAgent.contains("iphone");
            
            if (isMobile) {
        %>
        <link rel="stylesheet" href="estilos/DistribucionSinRep.css">
        <%
            } else {
        %>
        <link rel="stylesheet" href="estilos/DistribucionConRep.css"/>
        <%
            }
        %>
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
                    <% if(isMobile) { %>
                    <div class="menu-icon" id="menu-icon">
                        <div class="bar"></div>
                        <div class="bar"></div>
                        <div class="bar"></div>
                    </div>
                    <div class="menu" id="menu">
                        <ul>
                            <li><a href="TodosLosGeneros.jsp">Generos</a></li>
                            <li><a href="TodosLosArtistas.jsp">Artistas</a></li>
                            <li><a href="Recomendaciones.jsp">Lo Mejor de Lo Mejor</a></li>
                            <li><button id="logoutButton"><a>Cerrar sesion</a></button></li>
                        </ul>
                    </div>
                    <% }%>
                    <input id="searchInput" type="text" placeholder="Tema, Album, Lista" class="barraBusqueda">
                    <a class="btnBusqueda" onclick="emitirBusqueda()">Buscar</a>
                </div>

                <div class="userDiv">
                    <div class="divUserIMG">
                        <% if (nickname != null && !isMobile) {%>
                        <a href="ConsultarUsuario.jsp?usr=<%= nickname%>">
                            <img id="imagenUser" src="imagenes/usuarios/defaultUser.png" class="userIMG">
                        </a>
                        <% } else {%>
                        <img id="imagenUser" src="imagenes/usuarios/defaultUser.png" class="userIMG">
                        <% }%>
                    </div>
                    <ul class="listUser">
                        <li class="userName">
                            <% if (nickname != null && !isMobile) {%>
                            <a href="ConsultarUsuario.jsp?usr=<%= nickname%>">
                                <p class="name">
                                    <%= nickname != null ? nickname : "Visitante"%>
                                </p>
                            </a>
                            <% } else {%>
                            <p class="name">
                                <%= nickname != null ? nickname : "Visitante"%>
                            </p>
                            <% }%>
                        </li>
                        <% if (nickname == null) { %>
                        <li>
                            <p><button id="abrirFormLogin">Iniciar sesion</button></p>
                        </li>
                        <% } else {%>
                        <li>
                            <p>Tipo: <%= userType != null ? userType : "Desconocido"%>
                            </p>
                        </li>
                            <% if (!isMobile) { %>
                        <li>
                            <p><button id="logoutButton">Cerrar sesion</button></p>
                        </li>
                            <% } %>
                        <% } %>
                    </ul>
                </div>
            </header>

            <div class="mainCon">
                <div class="dinamico">
                    <% if (!isMobile) { %>
                    <div class="btnsNav">
                        <a href="TodosLosGeneros.jsp">Generos</a>
                        <a href="TodosLosArtistas.jsp">Artistas</a>
                        <a href="Recomendaciones.jsp">Lo Mejor de Lo Mejor</a>
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
                    <% }%>

                    <div class="realDinamico">
                        
                        <h2 class="contStart">Informacion de la Lista</h2>
                        <img src="imagenes/listas/defaultList.png" id="imagenlista" alt="Imagen de la Lista">
                        <div class="camposLista">
                            <%if (nickname != null && suscrito == true) { %>
                            <button onclick="llamarAgregarAlgoFav()" id="favListaBtn" style="display:none;">Fav</button>
                            <button onclick="llamarSacarAlgoFav()" id="sacarDeFavListaBtn" style="display:none;">NoFav</button>
                             <% } else { %>
                                <button onclick="popup('${"Necesita una Suscripcion para poder usar esta opcion"}')" id="favListaBtn" style="display:none;">Fav</button>
                                <button onclick="popup('${"Necesita una Suscripcion para poder usar esta opcion"}')" id="sacarDeFavListaBtn" style="display:none;">NoFav</button>
                             <% }%>
                             
                             
                            <input type="text" id="nombrelista" value="" readonly>
                            <input type="text" id="creadorgenerolista" value="" readonly>
                        </div>
                        <h2 class="contStart contStart2">Temas de la Lista Seleccionada</h2>
                        <div class="contTablaTemas">
                            <table id="tablaTemas" style="display:none;">
                                <thead>
                                    <tr>
                                        <th>Tema</th>
                                        <th>Duracion</th>
                                        <th>Album</th>
                                        <th>Archivo</th>
                                        <th>Detalles</th>
                                        <th colspan="3">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="temasBody">
                                    <!-- Aca se carga la lista -->
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                    <% if (!isMobile) { %>
                <div class="reproductor">
                    <div class="temaRep">
                        <img src="imagenes/espotify/user.png" class="artIMG" id="imagenReproductor">
                        <h2 id="nombreTema">Nombre Tema</h2>
                    </div>

                    <div class="controlRep">

                        <audio id="miAudio">
                            <source id="audioSource" type="audio/mpeg">
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
                    <% }%>
                    
                    <dialog id="detalleDialog" class="detalleDialog">
                           <div id="dialogContent">
                               <p class="Info"><strong>Tema:</strong> <span id="dialogTitulo"></span></p>
                               <p class="Info"><strong>Album:</strong> <span id="dialogAlbum"></span></p>
                               <p class="Info"><strong>Reproducciones:</strong> <span id="dialogReproducciones"></span></p>
                               <p class="Info"><strong>Descargas:</strong> <span id="dialogDescargas"></span></p>
                               <p class="Info"><strong>Favoritos:</strong> <span id="dialogFavoritos"></span></p>
                               <p class="Info"><strong>Listas:</strong> <span id="dialogListas"></span></p>
                               <button id="cerrarDialog" class="btn-info">Cerrar</button>
                           </div>
                       </dialog>
                    
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
                        <label for="passLogin">Contraseï¿½a:</label>
                        <input type="password" id="passLogin" name="passLogin" required>
                    </div>
                    <div class="btnsFormLogin">
                        <button type="submit">Iniciar Sesion</button>
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
                            <label for="dirWeb">Direccion web (URL):</label>
                            <input type="text" id="dirWeb" name="dirWeb">
                        </div>
                        <div>
                            <label for="biografia">Biografia:</label>
                            <input type="text" id="biografia" name="biografia">
                        </div>
                    </div>
                    <div>
                        <label for="pass">Contraseï¿½a:</label>
                        <input type="password" id="pass" name="pass" required>
                    </div>
                    <div>
                        <label for="confirmPass">Confirmar Contraseï¿½a:</label>
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

            <dialog id="dialogoAgregarTema">
                <h2 class="addTema">Agregar Tema a Lista</h2>
                <h3 class="tusListas">Tus Listas</h3>
                <table id="listasTable">
                    <thead>
                        <tr>
                            <th>Nombre de la Lista</th>
                            <th class="action">Acciï¿½n</th>
                        </tr>
                    </thead>
                    <tbody id="listasBody">
                        <!-- Aquï¿½ se carga la lista -->
                    </tbody>
                </table>

                <form id="agregarTemaListaForm" onsubmit="return validarFormulario()">
                    <div>
                        <label for="nombreLista">Tu Lista a la que Agregar el Tema:</label>
                        <input type="text" id="nombreLista" name="nombreLista" required title="Ingresa el nombre de una lista" readonly>
                        <input type="hidden" id='albumTema' name='albumTema' value="">
                    </div>
                    <div>
                        <label for="nombreTema">Nombre del Tema:</label>
                        <input type="text" id="nombreTema" name="nombreTema" required title="Ingresa el nombre de un tema" readonly> 
                    </div>
                    <div class="btnsAddTema">
                        <button type="submit">Agregar Tema a Lista</button>
                        <button onclick="cerrarDialogo()" type="reset">Cerrar</button>
                    </div>
                </form>
            </dialog>
                    
        </div> <!-- Fin Cuerpo -->

        <script>
            function emitirBusqueda() {
                const searchInput = document.getElementById('searchInput').value;

                // Redirigir a la URL con el parï¿½metro de bï¿½squeda
                if (searchInput === "" || searchInput === null) {
                    alert("Por favor, ingrese un termino de busqueda.");
                    
                } else {
                    window.location.href = "BuscarCosas.jsp?search=" + searchInput;
                }
            }
        </script>
        <script>
            function popup(texto) {
               alert(texto);
            }
        </script>

        <script>
            function abrirDialogo(nombreTema, albumTema) {
                document.getElementById('nombreTema').value = nombreTema;
                document.getElementById('albumTema').value = albumTema;

                const dialog = document.getElementById('dialogoAgregarTema');
                dialog.showModal(); // Usar showModal para abrir como un modal
            }

            function cerrarDialogo() {
                const dialog = document.getElementById('dialogoAgregarTema');
                dialog.close(); 
            }

            document.getElementById('agregarTemaListaForm').addEventListener('submit', function (event) {
                event.preventDefault(); 
            });

            function validarFormulario() {
                const nombreLista = document.getElementById('nombreLista').value;
                const nombreTema = document.getElementById('nombreTema').value;

                if (!nombreLista || !nombreTema) {
                    alert("Por favor, complete todos los campos requeridos.");
                    return false;
                }

                return true;
            }
        </script>
        <!-- Consultar listas de reproduccion -->
        <script src="scripts/ConsultarListaRep.js"></script>

        <!-- Script registro de usuario -->
        <script src = "scripts/AgregarUsuario.js"></script>

        <!-- Script inicio y cierre de sesion -->
        <script src="scripts/Login.js"></script>
        <script src="scripts/Logout.js"></script>

        <!-- Formulario de login y signup -->
        <script src = "scripts/LoginSignupForm.js"></script>

        <script src = "scripts/AgregarTemaALista.js"></script>

        <!-- Evitar que las imagenes sean arrastradas -->
        <script>
            document.addEventListener('dragstart', function (event) {
                event.preventDefault();
            });
        </script>

        <script src="scripts/ImagenDeUsuario.js"></script>
        
        <% if (isMobile && nickname == null) { %>
        <script>
            // Reemplaza la página actual en el historial del navegador con MobileLogin.jsp, lo que significa que el usuario no podrá volver a la página anterior usando el botón "Atrás" del navegador
            window.location.replace('MobileLogin.jsp');
        </script>
        <% }%>
        
        <% if (isMobile) { %>
        <!-- Boton hamburguesa -->
        <script>
            const menuIcon = document.getElementById("menu-icon");
            const menu = document.getElementById("menu");

            // Añadimos un evento de clic al icono del menú
            menuIcon.addEventListener("click", () => {
                menu.classList.toggle("show");  // Mostramos o escondemos el menú
                menuIcon.classList.toggle("active");  // Animamos el icono de hamburguesa
            });
        </script>
        <% }%>
        
    </body>
</html>
