window.onload = cargarGeneroStuff();

var Genero = "0";
              
              function cargarGeneroStuff(){
                  const urlParams = new URLSearchParams(window.location.search);
                       var buscarName = urlParams.get('search');

            fetch('http://localhost:8080/EspotifyWeb/TodoLoDeUnGeneroServlet?action=MostrarListasDelGeneroU&buscar=' + encodeURIComponent(buscarName))
                .then(response => response.json())
                .then(data => {
                    const container = document.getElementById('listaGeneroGenBody');
                    container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
                    data.forEach(lista => {
                        const imagen = lista.imagen !== "null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))
                            ? `imagenes/listas/${lista.imagen}`
                            : 'imagenes/listas/defaultList.png';

                        const listaGeneroGenDiv = `
                            <div class="listaGeneroGen">
                          <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(lista.nombre)}tipo=1">
                                <img src="${imagen}" class="imagenGeneroGen" alt="Imagen del Álbum">
                          </a>
                                <div>
                          <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(lista.nombre)}tipo=1">
                                    <p>${lista.nombre}</p>
                          </a>
                                </div>
                                <div>
                                    <p>${lista.genero}</p>
                                </div>
                            </div>`;

                        container.innerHTML += listaGeneroGenDiv;
                    });
                })
            .catch(error => console.error('Error al cargar listas:', error));


    fetch('http://localhost:8080/EspotifyWeb/TodoLoDeUnGeneroServlet?action=MostrarAlbumesDelGeneroU&buscar=' + encodeURIComponent(buscarName))
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('albumGenBody');
            container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevos álbumes
            data.forEach(album => {
                const imagen = album.imagen !== "null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))
                    ? `imagenes/albumes/${album.imagen}`
                    : 'imagenes/albumes/defaultAlbum.png';

                const albumGenDiv = `
                    <div class="albumGen">
                          <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                        <img src="${imagen}" class="imagenAlbumGen" alt="Imagen del Álbum">
                          </a>
                        <div>
                          <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                            <p>${album.nombre}</p>
                          </a>
                        </div>
                        <div>
                            <p>${album.artista}</p>
                        </div>
                        <div>
                            <p>${album.anio}</p>
                        </div>
                    </div>`;

                container.innerHTML += albumGenDiv;
            });
        })
    .catch(error => console.error('Error al cargar álbumes:', error));

}

function descargarGenero(){
   document.getElementById("todoDeUnGenero").style.display = "none"; 
}