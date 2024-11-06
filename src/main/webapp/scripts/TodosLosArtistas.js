fetch('http://192.168.1.146:8080/EspotifyWeb/TodosLosArtistasServlet?action=devolverArtias')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('artistasBody');
            container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
            data.forEach(artista => {
                const imagen = artista.imagen !== "null" && (artista.imagen.endsWith(".png") || artista.imagen.endsWith(".jpg"))
                        ? `imagenes/usuarios/${artista.imagen}`
                        : 'imagenes/usuarios/defaultUser.png';

                const esArtista = sessionUserType === "Artista";

                const artistaDiv = `
                <div class="artista">
                    ${esArtista ? '' : `<a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}">`}
                        <img src="${imagen}" class="imagenUser" alt="Imagen del Usuario">
                        <div>
                            <p>${artista.nombre}</p>
                        </div>
                    ${esArtista ? '' : '</a>'}
                </div>`;

                container.innerHTML += artistaDiv;
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));