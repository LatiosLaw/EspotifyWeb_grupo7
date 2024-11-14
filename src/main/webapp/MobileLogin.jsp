<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" href="estilos/MobileLogin.css">
        <title>Espotify</title>
    </head>

    <body>
        <div class="cuerpo">
 
            <form id="loginForm" method="post, dialog">
                <div class="tituloFormLogin">
                    <h2>Inicio de Sesion</h2>
                </div>
                <div>
                    <label for="nicknameLogin">Nickname:</label>
                    <input type="text" id="nicknameLogin" name="nicknameLogin" required>
                </div>
                <div>
                    <label for="passLogin">Contrasena:</label>
                    <input type="password" id="passLogin" name="passLogin" required>
                </div>
                <div class="btnsFormLogin">
                    <button type="submit">Iniciar Sesion</button>
                </div>
                
                <div id="resultado"></div> <!-- Mensajes de resultado -->
            </form>
            
            

        </div> <!-- Fin Cuerpo -->

        <!-- Script inicio y cierre de sesion -->
        <script src="scripts/Login.js"></script>

    </body>

</html>