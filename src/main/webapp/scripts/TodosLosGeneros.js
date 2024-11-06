fetch('http://192.168.1.146:8080/EspotifyWeb/obtenerImagenesGeneros')
        .then(response => response.json())
        .then(imagenes => {
            fetch('TodosLosGenerosServlet?action=devolverGeneros')
                    .then(response => response.json())
                    .then(data => {
                        const container = document.getElementById('listaGenerosBody');
                        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

                        data.forEach(genero => {
                            // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                            const imagenAleatoria = imagenes[Math.floor(Math.random() * imagenes.length)];
                            const imgPath = `imagenes/generos/${imagenAleatoria}`;

                            const esArtista = sessionUserType === "Artista";

                            const generoDiv = `
                        <div class="genero">
                            ${esArtista ? '' : `<a href="TodoLoDeUnGenero.jsp?search=${encodeURIComponent(genero.nombre)}">`}
                                <img src="${imgPath}" alt="Imagen de genero" class="imagenGenero">
                            ${esArtista ? '' : '</a>'}
                            <div>
                                ${esArtista ? `<p>${genero.nombre}</p>` : `<a href="TodoLoDeUnGenero.jsp?search=${encodeURIComponent(genero.nombre)}"><p>${genero.nombre}</p></a>`}
                            </div>
                        </div>`;

                            container.innerHTML += generoDiv;
                        });
                    })
                    .catch(error => console.error('Error al cargar generos:', error));

        })
        .catch(error => console.error('Error al obtener imágenes de generos:', error));