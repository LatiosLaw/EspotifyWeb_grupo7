<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Lista</title>
    <link rel="stylesheet" type="text/css" href="estilos.css">
</head>
<body>
    
    <a href="index.jsp">Página Principal</a>
    
    <h1>Alta de Lista</h1>
    <c:if test="${not empty errorMessage}">
        <p id="errorMessage" style="color: red;">${errorMessage}</p>
    </c:if>
    <form id="ListaForm" action="AltaDeListaServlet" method="post" onsubmit="return validarFormulario()" enctype="multipart/form-data">
        <input type="hidden" id='Valido' name='Valido' value="true">  
        <label for="nombreLista">Nombre de la Lista : </label>
        <input type="text" id="nombreLista" name="nombreLista" required title="Ingresa el nombre de la Lista"><br>
        <span id="ListaExistsMessage" style="color: red;"></span>

        <label for="imagenLista">Imagen de la Lista (opcional):</label>
        <input type="file" id="imagenLista" name="imagenLista" accept="image/png, image/jpeg">

        <input type="submit" value="Registrar Lista">
    </form>

    <script>
        
        function validarFormulario() {
            const nombreLista = document.getElementById('nombreAlbum').value;
            
            if (!nombreLista) {
                alert("Por favor, complete todos los campos requeridos.");
                return false;
            }

            return true;
        }
        
        var liNameInput = document.getElementById('nombreLista');
        var listaExistsMessage = document.getElementById('ListaExistsMessage');
        var validoField = document.getElementById('Valido');
        var errorMessageElement = document.getElementById("errorMessage");

        liNameInput.addEventListener('input', function() {
            var listaName = liNameInput.value;

            if (listaName.length > 0) {
                // Utiliza fetch para hacer una solicitud GET al servidor
                fetch('AltaDeListaServlet?listaName=' + encodeURIComponent(listaName))
                    .then(response => response.text())
                    .then(data => {
                        errorMessageElement.style.display = "none";
                        if (data === 'exists') {
                            validoField.value = "false";
                            listaExistsMessage.textContent = 'Esta lista ya existe en tu biblioteca.';
                        } else {
                            validoField.value = "true";
                            listaExistsMessage.textContent = '';
                        }
                    })
                    .catch(error => console.error('Error al verificar la lista:', error));
            } else {
                listaExistsMessage.textContent = '';
            }
        });
        
    </script>
</body>
</html>