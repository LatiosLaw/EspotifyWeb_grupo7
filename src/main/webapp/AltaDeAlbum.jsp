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
        <h1>Alta de Álbum</h1>
        <form id="albumForm" action="AltaDeAlbumServlet" method="post" onsubmit="return validarFormulario()">
            <label for="nombreAlbum">Nombre del Álbum:</label>
            <input type="text" id="nombreAlbum" name="nombreAlbum" required title="Ingresa el nombre del álbum"><br>

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
            <input type="hidden" id="generosSeleccionados" name="generosSeleccionados" >

            <label for="imagenAlbum">Link a Imagen del Álbum (opcional):</label>
            <input type="url" id="imagenAlbum" name="imagenAlbum"><br>

            <h2>Temas del Álbum</h2>
            <div id="temasContainer">
                <div class="tema">
                    <label for='nombreTema'>Nombre del Tema:</label>
                    <input type='text' id='nombreTema' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Canción Favorita'>

                    <label for='duracionTema'>Duración (min:seg):</label>
                    <input type='text' id='duracionTema' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>

                    <label for='ubicacionTema'>Ubicación en el Álbum:</label>
                    <input type='number' id='ubicacionTema' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>

                    <label for='archivoMusica'>Archivo de Música o URL:</label>
                    <input type='url' id='archivoMusica' name='archivoMusica[]' required title='Ingresa la URL o archivo de música'><br><br>
                </div>
            </div>

            <button type="button" onclick="agregarTema()">Agregar Otro Tema</button><br><br>

            <input type="submit" value="Registrar Álbum">
        </form>

        <script>
            let generosSeleccionados = [];

            function cargarGeneros() {
                fetch('AltaDeAlbumServlet')
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

            function agregarTema() {
                const temasContainer = document.getElementById('temasContainer');
                const temaDiv = document.createElement('div');
                temaDiv.className = 'tema';

                temaDiv.innerHTML = `
                    <label for='nombreTema'>Nombre del Tema:</label>
                    <input type='text' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Canción Favorita'>
                    <label for='duracionTema'>Duración (min:seg):</label>
                    <input type='text' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>
                    <label for='ubicacionTema'>Ubicación en el Álbum:</label>
                    <input type='number' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>
                    <label for='archivoMusica'>Archivo de Música o URL:</label>
                    <input type='url' name='archivoMusica[]' required title='Ingresa la URL o archivo de música'>
                    <br><br>
                `;

                temasContainer.appendChild(temaDiv);
            }

            document.getElementById('generos').addEventListener('change', agregarGenero);
        </script>
    </body>
</html>