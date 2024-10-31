fetch('http://localhost:8080/EspotifyWeb/TodasLasListasDefaultServlet?action=devolverListas')
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById('listaListasBody');
        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas

        data.forEach(lista => {
            // Determinar la imagen a usar
            const imagen = lista.imagen !== "" && lista.imagen !== null && (lista.imagen.endsWith('.png') || lista.imagen.endsWith('.jpg'))
                ? `imagenes/listas/${lista.imagen}`
                : 'imagenes/listas/defaultList.png';

            const listaDiv = `
                <div class="lista">
                    <img src="${imagen}" alt="Imagen de lista" class="imagenLista">
                    <div>
                        <p>${lista.nombre}</p>
                        <p>${lista.genero}</p>
                    </div>
                </div>`;

            container.innerHTML += listaDiv;
        });
    })
    .catch(error => console.error('Error al cargar listas:', error));

    
    //lista.imagen!=="" && lista.imagen!==null && (lista.imagen==="png" || lista.imagen==="jpg")