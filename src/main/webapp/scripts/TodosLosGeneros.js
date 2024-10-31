fetch('http://localhost:8080/EspotifyWeb/obtenerImagenesGeneros')
    .then(response => response.json())
    .then(imagenes => {
        fetch('http://localhost:8080/EspotifyWeb/TodosLosGenerosServlet?action=devolverGeneros')
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('listaGenerosBody');
                container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

                data.forEach(genero => {
                    // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                    const imagenAleatoria = imagenes[Math.floor(Math.random() * imagenes.length)];
                    const imgPath = `imagenes/generos/${imagenAleatoria}`;

                    const generoDiv = `
                        <div class="genero">
                            <img src="${imgPath}" alt="Imagen de genero" class="imagenGenero">
                            <div>
                                <p>${genero.nombre}</p>
                            </div>
                        </div>`;

                    container.innerHTML += generoDiv;
                });
            })
            .catch(error => console.error('Error al cargar generos:', error));

    })
    .catch(error => console.error('Error al obtener imágenes de generos:', error));