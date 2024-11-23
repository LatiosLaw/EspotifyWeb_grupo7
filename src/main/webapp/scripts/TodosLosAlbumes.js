fetch('TodosLosAlbumesServlet?action=devolverAlbumes')
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById('listaAlbumesBody');
        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

        data.forEach(album => {
            // Determinar la imagen a usar
            const imagen = album.imagen !== "" && album.imagen !== null && (album.imagen.endsWith('.png') || album.imagen.endsWith('.jpg'))
                ? `imagenes/albumes/${album.imagen}`
                : 'imagenes/albumes/defaultAlbum.png';
                
            const isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent);
            
            const albumDiv = `
                <div class="album">
        <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                    <img src="${imagen}" alt="Imagen de album" class="imagenAlbum">
        </a>
                    <div>
        <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                        <p>${album.nombre}</p>
        </a>
                        ${isMobile ? `<p>${album.creador}</p>` : `<a href="ConsultarUsuario.jsp?usr=${album.creador}"><p>${album.creador}</p></a>`}
                    </div>
                </div>`;

            container.innerHTML += albumDiv;
        });
    })
    .catch(error => console.error('Error al cargar albumes:', error));