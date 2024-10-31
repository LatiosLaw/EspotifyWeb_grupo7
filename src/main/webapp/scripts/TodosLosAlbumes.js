fetch('http://localhost:8080/EspotifyWeb/TodosLosAlbumesServlet?action=devolverAlbumes')
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
                    <img src="${imagen}" alt="Imagen de album" class="imagenAlbum">
                    <div>
                        <p>${album.nombre}</p>
                        <p><a href="ConsultarUsuario.jsp?usr=${album.creador}">${album.creador}</a></p>
                    </div>
                </div>`;

            container.innerHTML += albumDiv;
        });
    })
    .catch(error => console.error('Error al cargar albumes:', error));