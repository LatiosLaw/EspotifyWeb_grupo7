fetch('PaginaInicioServlet?action=buscarArtistas')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('artistasBody');
            container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
            data.forEach(artista => {
                const imagen = artista.imagen !== "null" && (artista.imagen.endsWith(".png") || artista.imagen.endsWith(".jpg"))
                        ? `imagenes/usuarios/${artista.imagen}`
                        : 'imagenes/usuarios/defaultUser.png';

                const esArtista = sessionUserType === "Artista";
                const isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent);

                const artistaDiv = `
                <div class="artista">
                    ${esArtista || isMobile ? '' : `<a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}">`}
                        <img src="${imagen}" class="imagenUser" alt="Imagen del Usuario">
                        <div>
                            <p>${artista.nombre}</p>
                        </div>
                    ${esArtista || isMobile ? '' : '</a>'}
                </div>`;

                container.innerHTML += artistaDiv;
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));


if (sessionUserType !== "Artista") {
    fetch('PaginaInicioServlet?action=buscarAlbumes')
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('albumBody');
                container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
                data.forEach(album => {
                    const imagen = album.imagen !== "null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))
                            ? `imagenes/albumes/${album.imagen}`
                            : 'imagenes/albumes/defaultAlbum.png';

                    const albumDiv = `
            <div class="album">
                <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                    <img src="${imagen}" class="imagenAlbum" alt="Imagen del Álbum">
                    <div>
                        <p>${album.nombre}</p>
                    </div>
                </a>
            </div>`;

                    container.innerHTML += albumDiv;
                });
            })
            .catch(error => console.error('Error al cargar albumes:', error));
}

if (sessionUserType !== "Artista") {
    fetch('PaginaInicioServlet?action=buscarListas')
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('listasBody');
                container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
                data.forEach(lista => {
                    const imagen = lista.imagen !== "null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))
                            ? `imagenes/listas/${lista.imagen}`
                            : 'imagenes/listas/defaultList.png';

                    const listaDiv = `
            <div class="lista">
                <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(lista.nombre)}tipo=1">
                    <img src="${imagen}" class="imagenLista" alt="Imagen de la Lista">
                    <div>
                        <p>${lista.nombre}</p>
                    </div>
                </a>
            </div>`;

                    container.innerHTML += listaDiv;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

fetch('PaginaInicioServlet?action=buscarClientes')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('clientesBody');
            container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
            data.forEach(cliente => {
                const imagen = cliente.imagen !== "null" && (cliente.imagen.endsWith(".png") || cliente.imagen.endsWith(".jpg"))
                        ? `imagenes/usuarios/${cliente.imagen}`
                        : 'imagenes/usuarios/defaultUser.png';

                const esArtista = sessionUserType === "Artista";
                const isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent);

                const clienteDiv = `
                <div class="cliente">
                    ${esArtista || isMobile ? '' : `<a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}">`}
                        <img src="${imagen}" class="imagenCliente" alt="Imagen del Cliente">
                        <div>
                            <p>${cliente.nombre}</p>
                        </div>
                    ${esArtista || isMobile ? '' : '</a>'}
                </div>`;

                container.innerHTML += clienteDiv;
            });
        })
        .catch(error => console.error('Error al cargar clientes:', error));