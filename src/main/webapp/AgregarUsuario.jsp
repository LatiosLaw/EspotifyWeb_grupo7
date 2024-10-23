<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="estilos.css">
        <title>Espotify Web</title>
    </head>
    <body>

        <a href="index.jsp">Pagina Principal</a>

        <h1>Alta de Usuario</h1>

        <form id="altaUsuarioForm">
            
            <c:if test="${not empty errorMessage}">
        <p id="errorMessage" style="color: red;">${errorMessage}</p>
    </c:if>
    
            <input type="hidden" id='Valido' name='Valido' value="true">  
            <label for="tipoUsuario">Tipo de Usuario:</label>
            <select id="tipoUsuario" name="tipoUsuario" required>
                <option value="">Seleccione...</option>
                <option value="cliente">Cliente</option>
                <option value="artista">Artista</option>
            </select><br>

            <label for="nickname">Nickname:</label>
            <input type="text" id="nickname" name="nickname" onkeyup="checkNickname()" required><br>
            <span id="nickValido" style="color: red;"></span>

            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" required><br>

            <label for="apellido">Apellido:</label>
            <input type="text" id="apellido" name="apellido" required><br>

            <label for="mail">Email:</label>
            <input type="email" id="mail" name="mail" onkeyup="checkCorreo()" required><br>
            <span id="correoValido" style="color: red;"></span>

            <label for="foto">Foto (URL):</label>
            <input type="text" id="foto" name="foto"><br>

            <div id="camposArtista" style="display: none;">
                <label for="dirWeb">Direccion web (URL):</label>
                <input type="text" id="dirWeb" name="dirWeb"><br>

                <label for="biografia">Biografia:</label>
                <input type="text" id="biografia" name="biografia"><br>     
            </div>

            <label for="pass">Contraseña:</label>
            <input type="password" id="pass" name="pass" required><br>

            <label for="confirmPass">Confirmar Contraseña:</label>
            <input type="password" id="confirmPass" name="confirmPass" required><br>

            <label for="fechaNac">Fecha de Nacimiento (YYYY-MM-DD):</label>
            <input type="date" id="fechaNac" name="fechaNac" required><br>

            <button type="submit">Agregar Usuario</button>
        </form>

        <div id="resultado"></div>

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

                // Validar que las contraseÃ±as coincidan
                const pass = document.getElementById('pass').value;
                const confirmPass = document.getElementById('confirmPass').value;
                if (pass !== confirmPass) {
                    alert("Las contraseÃ±as no coinciden.");
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