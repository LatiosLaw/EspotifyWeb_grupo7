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
                select.append(new Option('Seleccione un genero', ''));
                data.forEach(genero => {
                    select.append(new Option(genero, genero));
                });
                console.log('Géneros cargados:', data);
            })
            .catch(error => console.error('Error al cargar los géneros:', error));
}

function cargarListasParticulares() {
    fetch('ConsultarListaRepServlet?action=getListasParticulares')
            .then(response => response.json())
            .then(data => llenarTablaParticular(data))
            .catch(error => console.error('Error al cargar listas particulares:', error));
}

function llenarTablaResultados(listas) {
    const tbody = document.querySelector('#tablaResultados tbody');
    tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

    if (listas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4">No hay listas disponibles.</td></tr>';
        return;
    }

    listas.forEach(lista => {
        const imagenSrc = lista.imagen ? `imagenes/listas/${lista.imagen}` : 'imagenes/listas/defaultList.png';
        console.log('Foto:', lista.foto);
        tbody.innerHTML += `
<tr>
<td>${lista.nombre}</td>
<td>${lista.genero}</td>
<td><img src="${imagenSrc}" alt="${lista.nombre}" width="50"/></td>
<td><button type="button" onclick="cargarTemas('${lista.nombre}')">Ver Temas</button></td>
</tr>`;
    });
}

function llenarTablaParticular(listas) {
    const tbody = document.querySelector('#tablaParticulares tbody');
    tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

    listas.forEach(lista => {
        const imagenSrc = lista.imagen ? `imagenes/listas/${lista.imagen}` : 'imagenes/listas/defaultList.png';
        console.log('Foto:', lista.imagen);
        tbody.innerHTML += `
<tr data-lista-id="${lista.nombre}">
<td>${lista.nombre}</td>
<td>${lista.cliente}</td>
<td><img src="${imagenSrc}" alt="${lista.nombre}" width="50"/></td>
<td><button type="button" onclick="cargarTemas('${lista.nombre}')">Ver Temas</button></td>
</tr>`;
    });
}

function filtrarListas() {
    const generoSeleccionado = document.getElementById('generos').value;

    fetch(`ConsultarListaRepServlet?action=getListasPorGenero&genero=${encodeURIComponent(generoSeleccionado)}`)
            .then(response => response.json())
            .then(data => llenarTablaResultados(data))
            .catch(error => console.error('Error al filtrar listas:', error));
}

function cargarTemas(listaNombre) {
    const encodedNombre = encodeURIComponent(listaNombre);
    fetch(`ConsultarListaRepServlet?action=getTemasPorLista&listaNombre=${encodedNombre}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Error: ${response.status} ${response.statusText}. Detalles: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                // Verificar suscripcion antes de llenar la tabla de temas
                checkSuscripcion(data);
            })
            .catch(error => console.error('Error al cargar temas:', error));
}

function checkSuscripcion(temas) {
    fetch('LoginServlet?action=obtenerSuscripcion')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la red: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const suscripcion = data.suscrito;

                // Llenar tabla con los temas y el estado de la suscripción
                llenarTablaTemas(temas, suscripcion);
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));
}

function ensureUrlProtocol(url) {
// Verifica si la URL comienza con http:// o https://
    if (!/^https?:\/\//i.test(url)) {
        // Si no tiene protocolo, agrega http://
        url = 'http://' + url;
    }
    return url;
}

function llenarTablaTemas(temas, tieneSuscripcion) {
    const tbody = document.querySelector('#tablaTemas tbody');
    tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

    if (temas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">No hay temas disponibles.</td></tr>'; // Aumentar a 3 columnas
    } else {
        temas.forEach(tema => {
            const urlDescarga = ensureUrlProtocol(tema.identificador_archivo.trim());

            console.log('URL de descarga:', urlDescarga);

            tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${tieneSuscripcion
                    ? `<a href="${urlDescarga}" target="_blank">Descargar</a>`
                    : `<button disabled>Descargar</button>`}
                </td>
                <td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')">Agregar a Lista</button></td> <!-- Botón para agregar -->
            </tr>`;
        });
    }

    document.getElementById('tablaTemas').style.display = 'table';
}