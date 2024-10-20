<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Álbum</title>
    <link rel="stylesheet" type="text/css" href="estilos.css">
</head>
<body onload="cargarGeneros()">
    
    <a href="index.jsp">Página Principal</a>
    
    <h1>Alta de Álbum</h1>
    <c:if test="${not empty errorMessage}">
        <p id="errorMessage" style="color: red;">${errorMessage}</p>
    </c:if>
    <form id="albumForm" action="AltaDeAlbumServlet" method="post" onsubmit="return validarFormulario()" enctype="multipart/form-data">
        <input type="hidden" id='Valido' name='Valido' value="true">  
        <label for="nombreAlbum">Nombre del Álbum:</label>
        <input type="text" id="nombreAlbum" name="nombreAlbum" required title="Ingresa el nombre del álbum"><br>
        <span id="albumExistsMessage" style="color: red;"></span>

        <label for="anioCreacion">Año de Creación:</label>
        <input type="number" id="anioCreacion" name="anioCreacion" min="1900" max="2100" required title="Ingresa el año de creación"><br>

        <label for="generos">Géneros:</label>
        <select id="generos" name="generos">
            <option value="">Seleccione un género</option>
            <!-- se llenan con AJAX -->
        </select><br>

        <label>Géneros Seleccionados:</label>
        <div id="selectedGenerosContainer"></div>

        <!-- Campo oculto para enviar los géneros seleccionados -->
        <input type="hidden" id="generosSeleccionados" name="generosSeleccionados">

        <label for="imagenAlbum">Imagen del Álbum (opcional):</label>
        <input type="file" id="imagenAlbum" name="imagenAlbum" accept="image/png, image/jpeg">

        <h2>Temas del Álbum</h2>
        <div id="temasContainer">
        </div>

        <button type="button" onclick="agregarTemaMP3()">Agregar Otro Tema - Archivo MP3</button><br>
        <button type="button" onclick="agregarTemaWeb()">Agregar Otro Tema - Direccion URL</button><br>

        <input type="submit" value="Registrar Álbum">
    </form>

    <script>
            
        let generosSeleccionados = [];

        function cargarGeneros() {
            fetch('AltaDeAlbumServlet?action=cargarGeneros')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    let select = document.getElementById('generos');
                    select.innerHTML = ''; // Limpia las opciones
                    select.append(new Option('Seleccione un género', ''));
                    data.forEach(genero => {
                        select.append(new Option(genero, genero)); // Crear opcion con genero cargado
                    });
                })
                .catch(error => console.error('Error al cargar los géneros:', error));
        }

        function agregarGenero() {
            const select = document.getElementById('generos');
            const generoSeleccionado = select.value;

            // Comprobar si el género ya esta seleccionado
            if (generoSeleccionado && !generosSeleccionados.includes(generoSeleccionado)) {
                generosSeleccionados.push(generoSeleccionado);
                actualizarCampoGeneros();
                // Remover el genero de la lista de selecciones
                select.remove(select.selectedIndex);
            }
        }

        function actualizarCampoGeneros() {
            const container = document.getElementById('selectedGenerosContainer');
            container.innerHTML = '';

            generosSeleccionados.forEach(genero => {
                const div = document.createElement('div');
                div.className = 'genero-item';
                div.textContent = genero;
                div.onclick = () => removerGenero(genero);
                container.appendChild(div);
            });

            // Actualizar el campo oculto con los generos seleccionados
            document.getElementById('generosSeleccionados').value = generosSeleccionados.join(', ');
        }

        function removerGenero(genero) {
            // Agregar el género de vuelta al combobox 
            const select = document.getElementById('generos');
            const option = new Option(genero, genero);
            select.append(option);

            // Remover el genero de la lista de generos seleccionados
            generosSeleccionados = generosSeleccionados.filter(g => g !== genero);
            actualizarCampoGeneros();
        }

        function validarFormulario() {
            const nombreAlbum = document.getElementById('nombreAlbum').value;
            const anioCreacion = document.getElementById('anioCreacion').value;

            if (!nombreAlbum || !anioCreacion) {
                alert("Por favor, complete todos los campos requeridos.");
                return false;
            }
            
            if (generosSeleccionados.length === 0) {
                alert("Por favor, seleccione al menos un género.");
                return false;
            }

            return true;
        }

        function agregarTemaWeb() {
            const temasContainer = document.getElementById('temasContainer');
            const temaDiv = document.createElement('div');
            temaDiv.className = 'tema';

            temaDiv.innerHTML = `
                <label for='tipoTema'>Tipo de Tema: Direccion Web</label>
                <input type="hidden" id='tipoTema[]' name='tipoTema[]' value="direccionWeb">  
                <label for='nombreTema'>Nombre del Tema:</label>
                <input type='text' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Canción Favorita'>
                <label for='duracionTema'>Duración (min:seg):</label>
                <input type='text' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>
                <label for='ubicacionTema'>Ubicación en el Álbum:</label>
                <input type='number' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>
                <label for='dirWeb[]'>Dirección Web (URL):</label>
                <input type="url" id='dirWeb[]' name='dirWeb[]' required>
                <input type="hidden" id='archivo_MP3[]' name='archivo_MP3[]' value="mp3">
                <br><br>
            `;

            temasContainer.appendChild(temaDiv);
        }
        
        function agregarTemaMP3() {
            const temasContainer = document.getElementById('temasContainer');
            const temaDiv = document.createElement('div');
            temaDiv.className = 'tema';

            temaDiv.innerHTML = `
                <label for='tipoTema'>Tipo de Tema: MP3</label>
                <input type="hidden" id='tipoTema[]' name='tipoTema[]' value="archivo_mp3">  
                <label for='nombreTema'>Nombre del Tema:</label>
                <input type='text' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Canción Favorita'>
                <label for='duracionTema'>Duración (min:seg):</label>
                <input type='text' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>
                <label for='ubicacionTema'>Ubicación en el Álbum:</label>
                <input type='number' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>
                <label for='archivo_MP3[]'>Archivo MP3:</label>
                <input type="file" id='archivo_MP3[]' name='archivo_MP3[]' accept=".mp3" required> 
                <input type="hidden" id='dirWeb[]' name='dirWeb[]' value="0">  
                <br><br>
            `;

            temasContainer.appendChild(temaDiv);
        }
        
        var albumNameInput = document.getElementById('nombreAlbum');
        var albumExistsMessage = document.getElementById('albumExistsMessage');
        var validoField = document.getElementById('Valido');
        var errorMessageElement = document.getElementById("errorMessage");

        albumNameInput.addEventListener('input', function() {
            var albumName = albumNameInput.value;

            if (albumName.length > 0) {
                // Utiliza fetch para hacer una solicitud GET al servidor
                fetch('AltaDeAlbumServlet?action=verificarAlbum&albumName=' + encodeURIComponent(albumName))
                    .then(response => response.text())
                    .then(data => {
                        errorMessageElement.style.display = "none";
                        if (data === 'exists') {
                            validoField.value = "false";
                            albumExistsMessage.textContent = 'Esta lista ya existe en tu biblioteca.';
                        } else {
                            validoField.value = "true";
                            albumExistsMessage.textContent = '';
                        }
                    })
                    .catch(error => console.error('Error al verificar la lista:', error));
            } else {
                albumExistsMessage.textContent = '';
            }
        });

        document.getElementById('generos').addEventListener('change', agregarGenero);
    </script>

</body>
</html>