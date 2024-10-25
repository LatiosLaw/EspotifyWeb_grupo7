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
        <input type="text" id="nombreLista" name="nombreLista" onkeyup="checkLista()" required title="Ingresa el nombre de la Lista"><br>
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
        
        var validoField = document.getElementById('Valido');
        var errorMessageElement = document.getElementById("errorMessage");
        
        function checkLista() {
            const lista = document.getElementById("nombreLista").value;
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "AltaDeListaServlet?listaName=" + encodeURIComponent(lista), true);

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    errorMessageElement.style.display = "none";
                    let mensaje = xhr.responseText;
        let listaValidoField = document.getElementById("ListaExistsMessage");
        if (mensaje === "Lista name is available") {
            listaValidoField.innerHTML = "<span style='color: green;'>" + mensaje + "</span>";
            validoField.value = "true";
        } else {
            listaValidoField.innerHTML = "<span style='color: red;'>" + mensaje + "</span>";
            validoField.value = "false";
        }
                }
            };

            xhr.send();
        }
        
    </script>
</body>
</html>