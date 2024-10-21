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
                    <a href="" class="EspotifyLogo">
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
                        <img src="imagenes/espotify/user.png" class="userIMG">
                    </div>
                    <ul class="listUser">
                        <%
                            String userType = (String) session.getAttribute("userType");
                            String nickname = (String) session.getAttribute("nickname");
                        %>
                        <li class="userName"><p class="name"><%= nickname != null ? nickname : "Visitante"%></p></li>
                            <% if (nickname == null) { %>
                        <li><p><button id="abrirFormLogin">Iniciar sesión</button></p></li>
                                    <% } else { %>
                        <li><p><button id="logoutButton">Cerrar sesión</button></p></li>
                                    <% }%>
                    </ul>
                </div>
            </header>

            <div class="mainCon">
                <div class="dinamico"></div>
                <div class="reproductor">
                    <div class="temaRep">
                        <img src="imagenes/espotify/user.png" class="artIMG">
                        <h3>Nombre Tema</h3>
                        <h2>Nombre Artista</h2>
                    </div>
                    <div class="controlRep">
                        <audio id="miAudio">
                            <source id="audioSource" src="audio1.mp3" type="audio/mpeg">
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

            <!-- Diálogo de inicio de sesión -->
            <dialog id="winLogin">
                <button id="cerrarFormLogin">Cerrar</button>
                <form id="loginForm" method='post'>
                    <label for="nickname">Nickname:</label>
                    <input type="text" id="nickname" name="nickname" required><br>

                    <label for="pass">Contraseña:</label>
                    <input type="password" id="pass" name="pass" required><br>

                    <button type="submit">Iniciar Sesión</button>
                </form>
                <div id="resultado"></div> <!-- Mensajes de resultado -->
            </dialog>
        </div> <!-- Fin Cuerpo -->

        <!-- Scripts -->
        <script src="scripts.js"></script>

        <script>
                                    // Función para iniciar sesión
                                    function iniciarSesion(event) {
                                        event.preventDefault(); // Evita que se envíe el formulario de forma tradicional

                                        const formData = new FormData(event.target);
                                        const params = new URLSearchParams(formData).toString();

                                        fetch('http://localhost:8080/EspotifyWeb/LoginServlet', {
                                            method: 'POST',
                                            headers: {
                                                'Content-Type': 'application/x-www-form-urlencoded'
                                            },
                                            body: params
                                        })
                                                .then(response => {
                                                    if (!response.ok) {
                                                        throw new Error('Network response was not ok');
                                                    }
                                                    return response.json();
                                                })
                                                .then(data => {
                                                    const message = data.success ? "Inicio de sesión exitoso." : "Error al iniciar sesión: " + data.errorCode;
                                                    document.getElementById('resultado').innerText = message;
                                                    alert(message);

                                                    if (data.success) {
                                                        window.location.reload(); // Recarga la página si el inicio fue exitoso
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    const errorMessage = "Error al intentar iniciar sesión.";
                                                    document.getElementById('resultado').innerText = errorMessage;
                                                    alert(errorMessage);
                                                });
                                    }

                                    // Función para cerrar sesión
                                    function cerrarSesion() {
                                        fetch('http://localhost:8080/EspotifyWeb/CerrarSesionServlet', {
                                            method: 'POST',
                                            headers: {
                                                'Content-Type': 'application/json'
                                            }
                                        })
                                                .then(response => {
                                                    if (!response.ok) {
                                                        throw new Error('Network response was not ok');
                                                    }
                                                    return response.json();
                                                })
                                                .then(data => {
                                                    const message = data.success ? data.message : "Error al cerrar sesión.";
                                                    document.getElementById('resultado').innerText = message;
                                                    alert(message);

                                                    if (data.success) {
                                                        window.location.reload(); // Recarga la página si el cierre fue exitoso
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    const errorMessage = "Error al intentar cerrar sesión.";
                                                    document.getElementById('resultado').innerText = errorMessage;
                                                    alert(errorMessage);
                                                });
                                    }

                                    // Configuración de eventos
                                    document.getElementById('abrirFormLogin')?.addEventListener('click', function () {
                                        document.getElementById('winLogin').showModal();
                                    });

                                    document.getElementById('cerrarFormLogin')?.addEventListener('click', function () {
                                        document.getElementById('winLogin').close();
                                    });

                                    document.getElementById('loginForm')?.addEventListener('submit', iniciarSesion);
                                    document.getElementById('logoutButton')?.addEventListener('click', cerrarSesion);
        </script>
    </body>
</html>
