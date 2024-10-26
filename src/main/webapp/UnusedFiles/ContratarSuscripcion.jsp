<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="estilos.css">
        <title>Contratar Suscripci�n</title>
        <style>
            body {
                font-family: Arial, sans-serif;
            }
            .container {
                width: 300px;
                margin: auto;
            }
            label {
                display: block;
                margin-top: 10px;
            }
            input[type="submit"] {
                margin-top: 20px;
            }
        </style>
    </head>
    <body>
        <a href="index.jsp">P�gina Principal</a>

        <div class="container">
            <h1>Contratar Suscripci�n</h1>
            <form id="contratarSuscripcionForm">
                <label for="tipoSuscripcion">Seleccione tipo de suscripci�n:</label>
                <select id="tipoSuscripcion" name="tipoSuscripcion" required>
                    <option value="">Seleccione una opci�n</option>
                    <option value="Semanal">Semanal - $5</option>
                    <option value="Mensual">Mensual - $15</option>
                    <option value="Anual">Anual - $150</option>
                </select>

                <input type="submit" value="Confirmar Suscripci�n">
            </form>

            <div id="resultado"></div>
        </div>

        <script>
            document.getElementById('contratarSuscripcionForm').addEventListener('submit', function (event) {
                event.preventDefault(); // Evitar que el formulario se env�e de forma tradicional

                const formData = new FormData(this);
                const params = new URLSearchParams(formData).toString();

                console.log("Enviando datos:", params); // Para depuraci�n

                fetch('http://localhost:8080/EspotifyWeb/ContratarSuscripcionServlet', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: params
                })
                        .then(response => {
                            console.log("Respuesta del servidor:", response); // Para depuraci�n

                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log("Datos recibidos:", data); // Para depuraci�n

                            if (data.success) {
                                alert("Suscripci�n contratada exitosamente.");
                                document.getElementById('resultado').innerText = "Suscripci�n contratada exitosamente.";
                            } else {
                                alert("Error al contratar la suscripci�n: C�digo de error " + data.errorCode);
                                document.getElementById('resultado').innerText = "Error al contratar la suscripci�n: C�digo de error " + data.errorCode;
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert("Error al contratar la suscripci�n.");
                        });
            });
        </script>
    </body>
</html>