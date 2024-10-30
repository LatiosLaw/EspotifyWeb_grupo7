fetch('http://localhost:8080/EspotifyWeb/obtenerImagenesGeneros')
    .then(response => response.json())
    .then(imagenes => {
        fetch('http://localhost:8080/EspotifyWeb/TodosLosGenerosServlet?action=devolverGeneros')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaGenerosBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas

                data.forEach(genero => {
                    // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                    const imagenAleatoria = imagenes[Math.floor(Math.random() * imagenes.length)];
                    const imgPath = `imagenes/generos/${imagenAleatoria}`;

                    // Crear una fila con la imagen aleatoria y el nombre del género
                    const row = `
                        <tr>
                            <td><img src="${imgPath}" alt="Imagen de género" class="imagenGenero" style="width:50px; height:50px;"></td>
                            <td>${genero.nombre}</td>
                        </tr>`;

                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar géneros:', error));
    })
    .catch(error => console.error('Error al obtener imágenes de géneros:', error));