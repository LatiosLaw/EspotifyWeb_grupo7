<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Consulta de Lista de Reproducción</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="estilos.css">
        <style>
            #tablaResultados, #tablaParticulares, #tablaTemas {
                margin-top: 20px;
                border-collapse: collapse;
                width: 100%;
            }
            #tablaResultados th, #tablaParticulares th, #tablaTemas th,
            #tablaResultados td, #tablaParticulares td, #tablaTemas td {
                border: 1px solid #ddd;
                padding: 8px;
            }
            #tablaResultados th, #tablaParticulares th, #tablaTemas th {
                background-color: #f2f2f2;
            }
        </style>
    </head>
    <body>

        <h1>Consulta de Lista de Reproducción</h1>

        <h2>Listas de Reproducción</h2>
        <form id="formularioFiltro" action="CrearListaRepServlet" method="get">
            <label for="selectGenero">Selecciona un Género:</label>
            <select id="generos" name="genero">
                <option value="">Seleccione un género</option>
            </select>
            <button type="submit">Filtrar</button>
        </form>

        <table id="tablaResultados">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Género</th>
                    <th>Imagen</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <h2>Listas Particulares Públicas</h2>
        <table id="tablaParticulares">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Cliente</th>
                    <th>Imagen</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <h2>Temas de la Lista Seleccionada</h2>
        <table id="tablaTemas" style="display:none;">
            <thead>
                <tr>
                    <th>Tema</th>
                    <th>Descargar</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                cargarGeneros();
                cargarListasParticulares();
            });

            function cargarGeneros() {
                fetch('AltaDeAlbumServlet?action=cargarGeneros')
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('La respuesta de la red no fue correcta');
                            }
                            return response.json();
                        })
                        .then(data => {
                            let select = document.getElementById('generos');
                            select.innerHTML = ''; // Limpia las opciones
                            select.append(new Option('Seleccione un género', ''));
                            data.forEach(genero => {
                                select.append(new Option(genero, genero)); // Crear opción con género
                            });
                            console.log('Géneros cargados:', data); // Log para verificar carga
                        })
                        .catch(error => console.error('Error al cargar los géneros:', error));
            }

            function cargarListasParticulares() {
                fetch('CrearListaRepServlet?action=getListasParticulares')
                        .then(response => response.json())
                        .then(data => poblarTablaParticular(data))
                        .catch(error => console.error('Error al cargar listas particulares:', error));
            }

            function llenarTablaResultados(listas) {
                const tbody = document.querySelector('#tablaResultados tbody');
                tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

                if (listas.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="4">No hay listas disponibles.</td></tr>';
                    return; // Salir si no hay listas
                }

                listas.forEach(lista => {
                    const imagenSrc = lista.imagen ? `imagenes/listas/${lista.imagen}` : 'imagenes/defaultList.png';
                    tbody.innerHTML += `
                        <tr>
                            <td>${lista.nombre}</td>
                            <td>${lista.genero}</td>
                            <td><img src="${imagenSrc}" alt="${lista.nombre}" width="50"/></td>
                            <td><button onclick="cargarTemas('${lista.nombre}')">Ver Temas</button></td>
                        </tr>`;
                });
            }

            function llenarTablaParticular(listas) {
                const tbody = document.querySelector('#tablaParticulares tbody');
                tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

                listas.forEach(lista => {
                    const imagenSrc = lista.foto ? lista.foto : 'imagenes/defaultList.png';
                    tbody.innerHTML += `
                        <tr data-lista-id="${lista.nombre}">
                            <td>${lista.nombre}</td>
                            <td>${lista.cliente}</td>
                            <td><img src="${imagenSrc}" alt="${lista.nombre}" width="50"/></td>
                            <td><button onclick="cargarTemas('${lista.nombre}')">Ver Temas</button></td>
                        </tr>`;
                });
            }

            function cargarTemas(listaNombre) {
                const encodedNombre = encodeURIComponent(listaNombre);
                fetch(`CrearListaRepServlet?action=getTemasPorLista&nombre=${encodedNombre}`)
                        .then(response => response.json())
                        .then(data => poblarTablaTemas(data))
                        .catch(error => console.error('Error al cargar temas:', error));
            }

            function llenarTablaTemas(temas) {
                const tbody = document.querySelector('#tablaTemas tbody');
                tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

                temas.forEach(tema => {
                    tbody.innerHTML += `
                        <tr>
                            <td>${tema.nombre}</td>
                            <td><a href="${tema.descargaUrl}">Descargar</a></td>
                        </tr>`;
                });

                document.getElementById('tablaTemas').style.display = 'table';
            }
        </script>

    </body>
</html>
