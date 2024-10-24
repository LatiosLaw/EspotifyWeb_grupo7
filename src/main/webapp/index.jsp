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
                        <li><p><button id="abrirFormLogin">Iniciar sesi?n</button></p></li>
                                    <% } else { %>
                        <li class="userFav"><a href=""><img src="imagenes/espotify/star.png" class="favIMG"></a><p><a href="">Favoritos</a></p></li> 
                        <li><p><button id="logoutButton">Cerrar sesi?n</button></p></li>
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
                <form id="loginForm" method='post, dialog'>
                    <div>
                        <label for="nickname">Nickname:</label>
                        <input type="text" id="nickname" name="nickname" required>
                    </div>
                    <div>
                        <label for="pass">Contrase?a:</label>
                        <input type="password" id="pass" name="pass" required>
                    </div>
                    <div class="btnsFormLogin">
                        <button type="submit">Iniciar Sesi?n</button>
                        <button id="abrirFormSignup">Registrarse</button>
                    </div>
                </form>
                <div id="resultado"></div> <!-- Mensajes de resultado -->
            </dialog>
            <dialog id="winSignup"> <!-- Di?logo de registro de usuario -->
                <button id="cerrarFormSignup">Cerrar</button>
                <div class="tituloFormSignup">
                    <h2>Registro de Usuario</h2>
                </div>
                <form id="altaUsuarioForm" method="dialog" enctype="multipart/form-data">
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

        <!-- Scripts macumbas de sonido y parte de los dialog -->
        <script src="scripts.js"></script>

        <!-- Script inicio de sesion -->
        <script>
                                    // Funci?n para iniciar sesi?n
                                    function iniciarSesion(event) {
                                        event.preventDefault(); // Evita que se env?e el formulario de forma tradicional

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
                                                    const message = data.success ? "Inicio de sesi?n exitoso." : "Error al iniciar sesi?n: " + data.errorCode;
                                                    document.getElementById('resultado').innerText = message;
                                                    alert(message);

                                                    if (data.success) {
                                                        window.location.reload(); // Recarga la p?gina si el inicio fue exitoso
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    const errorMessage = "Error al intentar iniciar sesi?n.";
                                                    document.getElementById('resultado').innerText = errorMessage;
                                                    alert(errorMessage);
                                                });
                                    }

                                    // Funci?n para cerrar sesi?n
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
                                                    const message = data.success ? data.message : "Error al cerrar sesi?n.";
                                                    document.getElementById('resultado').innerText = message;
                                                    alert(message);

                                                    if (data.success) {
                                                        window.location.reload(); // Recarga la p?gina si el cierre fue exitoso
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    const errorMessage = "Error al intentar cerrar sesi?n.";
                                                    document.getElementById('resultado').innerText = errorMessage;
                                                    alert(errorMessage);
                                                });
                                    }

                                    // Configuraci?n de eventos
                                    document.getElementById('abrirFormLogin')?.addEventListener('click', function () {
                                        document.getElementById('winLogin').showModal();
                                    });

                                    document.getElementById('cerrarFormLogin')?.addEventListener('click', function () {
                                        document.getElementById('winLogin').close();
                                    });

                                    document.getElementById('loginForm')?.addEventListener('submit', iniciarSesion);
                                    document.getElementById('logoutButton')?.addEventListener('click', cerrarSesion);
        </script>
        <!-- Script inicio de sesion -->
        <script>
                                    document.getElementById('tipoUsuario').addEventListener('change', function () {
                                        const camposArtista = document.getElementById('camposArtista');
                                        if (this.value === 'artista') {
                                            camposArtista.style.display = 'block';
                                        } else {
                                            camposArtista.style.display = 'none';
                                        }
                                    });

                                    document.getElementById('altaUsuarioForm').addEventListener('submit', function (event) {
                                        event.preventDefault();

                                        // Validar que las contraseñas coincidan
                                        const pass = document.getElementById('pass').value;
                                        const confirmPass = document.getElementById('confirmPass').value;
                                        if (pass !== confirmPass) {
                                            alert("Las contraseñas no coinciden.");
                                            return;
                                        }

                                        const formData = new FormData(this);
                                        const params = new URLSearchParams(formData).toString();

                                        fetch('http://localhost:8080/EspotifyWeb/AgregarUsuarioServlet', {
                                            method: 'POST',
                                            headers: {
                                                'Content-Type': 'application/x-www-form-urlencoded'
                                            },
                                            body: params
                                        })
                                                .then(response => response.json())
                                                .then(data => {
                                                    const message = data.success ? "Usuario agregado exitosamente." : "Error al agregar usuario: " + data.errorCode;
                                                    document.getElementById('resultado').innerText = message;
                                                    alert(message);
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    const errorMessage = "Error al agregar usuario.";
                                                    document.getElementById('resultado').innerText = errorMessage;
                                                    alert(errorMessage);
                                                });
                                    });

                                var validoField = document.getElementById('Valido');
                                var errorMessageElement = document.getElementById("errorMessage");

                                function checkNickname() {
                                    const nickname = document.getElementById("nickname").value;
                                    const xhr = new XMLHttpRequest();
                                    xhr.open("GET", "AgregarUsuarioServlet?action=verificarNickname&Nickname=" + encodeURIComponent(nickname), true);

                                    xhr.onreadystatechange = function() {
                                        if (xhr.readyState === 4 && xhr.status === 200) {
                                            errorMessageElement.style.display = "none";
                                            let mensaje = xhr.responseText;
                                let nicknameValidoField = document.getElementById("nickValido");
                                if (mensaje === "Nickname is available") {
                                    nicknameValidoField.innerHTML = "<span style='color: green;'>" + mensaje + "</span>";
                                    validoField.value = "true";
                                } else {
                                    nicknameValidoField.innerHTML = "<span style='color: red;'>" + mensaje + "</span>";
                                    validoField.value = "false";
                                }
                                        }
                                    };

                                    xhr.send();
                                }

                                function checkCorreo() {
                                    const correo = document.getElementById("mail").value;
                                    const xhr = new XMLHttpRequest();
                                    xhr.open("GET", "AgregarUsuarioServlet?action=verificarCorreo&correoName=" + encodeURIComponent(correo), true);

                                    xhr.onreadystatechange = function() {
                                        if (xhr.readyState === 4 && xhr.status === 200) {
                                            errorMessageElement.style.display = "none";
                                            let mensaje = xhr.responseText;
                                let correoValidoField = document.getElementById("correoValido");
                                if (mensaje === "Mail is available") {
                                    correoValidoField.innerHTML = "<span style='color: green;'>" + mensaje + "</span>";
                                    validoField.value = "true";
                                } else {
                                    correoValidoField.innerHTML = "<span style='color: red;'>" + mensaje + "</span>";
                                    validoField.value = "false";
                                }
                                        }
                                    };

                                    xhr.send();
                                }

        </script>
    </body>
</html>
