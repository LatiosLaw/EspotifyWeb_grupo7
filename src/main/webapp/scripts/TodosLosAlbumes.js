fetch('http://192.168.1.146:8080/EspotifyWeb/TodosLosAlbumesServlet?action=devolverAlbumes')
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById('listaAlbumesBody');
        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

        data.forEach(album => {
            // Determinar la imagen a usar
            const imagen = album.imagen !== "" && album.imagen !== null && (album.imagen.endsWith('.png') || album.imagen.endsWith('.jpg'))
                ? `imagenes/albumes/${album.imagen}`
                : 'imagenes/albumes/defaultAlbum.png';

            const albumDiv = `
                <div class="album">
        <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                    <img src="${imagen}" alt="Imagen de album" class="imagenAlbum">
        </a>
                    <div>
        <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                        <p>${album.nombre}</p>
        </a>
                        <p><a href="ConsultarUsuario.jsp?usr=${album.creador}">${album.creador}</a></p>
                    </div>
                </div>`;

            container.innerHTML += albumDiv;
        });
    })
    .catch(error => console.error('Error al cargar albumes:', error));