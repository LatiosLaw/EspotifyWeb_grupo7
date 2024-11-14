<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="imagenes/espotify/spotify-logo.png" type="image/x-icon">
        <link rel="stylesheet" href="estilos/">
        <title>Espotify</title>
    </head>

    <body>
        <% String userType = (String) session.getAttribute("userType");
        String nickname = (String) session.getAttribute("nickname");
        Boolean suscrito = (Boolean) session.getAttribute("suscrito"); %>
        <div class="cuerpo">

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
                    <button type="submit">Iniciar Sesion</button>
                </div>
            </form>
            
            <div id="resultado"></div> <!-- Mensajes de resultado -->

        </div> <!-- Fin Cuerpo -->

        <script type="text/javascript">
            const sessionNickname = "${sessionScope.nickname}";
            const sessionUserType = "${sessionScope.userType}";
            const sessionSuscrito = "${sessionScope.suscrito}";
        </script>

        <!-- Script inicio y cierre de sesion -->
        <script src="scripts/Login.js"></script>

    </body>

</html>