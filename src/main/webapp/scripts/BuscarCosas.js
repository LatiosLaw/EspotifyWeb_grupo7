window.onload = buscarDatos();

var Genero = "0";

function checkGenero() {
const urlParams = new URLSearchParams(window.location.search);
const buscarName = urlParams.get('search');
const xhr = new XMLHttpRequest();
xhr.open("GET", "http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=VerificarSiEsGenero&buscar=" + encodeURIComponent(buscarName), true);

xhr.onreadystatechange = function() {
    
    if (xhr.readyState === 4 && xhr.status === 200) {
        let mensaje = xhr.responseText;
if (mensaje==="existe") {
cargarGeneroStuff();
} else {
descargarGenero();
}
    }
};

xhr.send();
}

 function buscarDatos(){
const urlParams = new URLSearchParams(window.location.search);
     var buscarName = urlParams.get('search');

     if(buscarName!==null && buscarName!==""){

        fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasU&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('listaUsuarioBody');
                container.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const imagen = lista.imagen !== "null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))
                        ? `imagenes/listas/${lista.imagen}`
                        : 'imagenes/listas/defaultList.png';
                    const listaUserDiv = `
                        <div class="listaUser">
                 <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(`${lista.nombre}/${lista.creador}`).replace(/%2F/g, '/')}tipo=2">
                            <img src="${imagen}" class="imagenUser alt="Imagen del Usuario">
                 </a>
                            <div>
                 <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(`${lista.nombre}/${lista.creador}`).replace(/%2F/g, '/')}tipo=2">
                                <p>${lista.nombre}</p>
                 </a>
                            </div>
                            <div>
                                <p>${lista.creador}</p>
                            </div>
                        </div>`;
                 
                    container.innerHTML += listaUserDiv;
                });
            })
        .catch(error => console.error('Error al cargar listas:', error));

        fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasG&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('listaGeneroBody');
                container.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const imagen = lista.imagen !== "null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))
                        ? `imagenes/listas/${lista.imagen}`
                        : 'imagenes/listas/defaultList.png';

                    const listaGeneroDiv = `
                        <div class="listaGenero">
                 <a href="ConsultarListaRep.jsp?listaName=${encodeURIComponent(lista.nombre)}tipo=1">
                            <img src="${imagen}" class="imagenGenero" alt="Imagen del Álbum">
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

                    container.innerHTML += listaGeneroDiv;
                });
            })
        .catch(error => console.error('Error al cargar listas:', error));


    fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarAlbumes&buscar=' + encodeURIComponent(buscarName))
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById('albumBody');
        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevos álbumes
        data.forEach(album => {
            const imagen = album.imagen !== "null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))
                ? `imagenes/albumes/${album.imagen}`
                : 'imagenes/albumes/defaultAlbum.png';
            
            const albumDiv = `
                <div class="album">
                 <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">
                    <img src="${imagen}" class="imagenAlbum" alt="Imagen del Álbum">
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
            
            container.innerHTML += albumDiv;
        });
    })
    .catch(error => console.error('Error al cargar Álbumes:', error));

    
        fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarTemas&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const container = document.getElementById('temasBody');
                container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevos temas
                data.forEach(temazo => {
                    const temazoDiv = `
                        <div class="temazo">
                            <div>
                                <p>${temazo.nombre}</p>
                            </div>
                            <div>
                 <a href="ConsultarAlbum.jsp?album=${encodeURIComponent(temazo.album)}">
                                <p>${temazo.album}</p>
                 </a>
                            </div>
                            <div>
                                <p>${temazo.artista}</p>
                            </div>
                        </div>`;

                    container.innerHTML += temazoDiv;
                });
            })
        .catch(error => console.error('Error al cargar Temas:', error));

    checkGenero();
     }
              }
              
              function cargarGeneroStuff(){
                  const urlParams = new URLSearchParams(window.location.search);
                       var buscarName = urlParams.get('search');
    document.getElementById("todoDeUnGenero").style.display = "block";

            fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasDelGeneroU&buscar=' + encodeURIComponent(buscarName))
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


    fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarAlbumesDelGeneroU&buscar=' + encodeURIComponent(buscarName))
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