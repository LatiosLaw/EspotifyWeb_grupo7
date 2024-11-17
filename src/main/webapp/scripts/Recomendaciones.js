fetch('http://localhost:8080/EspotifyWeb/TodosLosGenerosServlet?action=devolverTemazos')
                    .then(response => response.json())
                    .then(data => {
                        const container = document.getElementById('listaGenerosBody');
                        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

                        data.forEach(tema => {
                            // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                            const imgPath = `imagenes/albumes/DefaultAlbum.png`;

                            const esArtista = sessionUserType === "Artista";

                            const generoDiv = `
                        <div class="genero">
                            ${esArtista ? '' : `<a href="nada.jsp?search=${encodeURIComponent(tema.nombre)}">`}
                                <img src="${imgPath}" alt="Imagen de genero" class="imagenGenero">
                            ${esArtista ? '' : '</a>'}
                            <div>
                                ${esArtista ? `<p>${tema.nombre}</p>` : `<a href="nada.jsp?search=${encodeURIComponent(tema.nombre)}"><p>${tema.nombre}</p></a>`}
                            </div>
                        </div>`;

                            container.innerHTML += generoDiv;
                        });
                    })
                    .catch(error => console.error('Error al cargar generos:', error));

        