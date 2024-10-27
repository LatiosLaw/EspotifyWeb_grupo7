function checkGenero() {

const buscarName = buscarInput.value;
const xhr = new XMLHttpRequest();
xhr.open("GET", "http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=VerificarSiEsGenero&buscar=" + encodeURIComponent(buscarName), true);

xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
        let mensaje = xhr.responseText;
if (mensaje==="existe") {
isGenero.value="1";
} else {
isGenero.value="2";
}
    }
};

xhr.send();
}


var buscarInput = document.getElementById("buscar");
  var isGenero = document.getElementById("es-gen");
 function buscarDatos(){

     var buscarName = buscarInput.value;

     if(buscarName!==null && buscarName!==""){

         fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasU&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaUsuarioBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    if(lista.imagen!=="null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))){
                    const row = `<tr><td><img src="imagenes/listas/${lista.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.creador}</td></tr>`;
                    tbody.innerHTML += row;
                }else{
                    const row = `<tr><td><img src="imagenes/listas/defaultList.png" id="imagenlista" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.creador}</td></tr>`;
                    tbody.innerHTML += row;
                    }
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));

    fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasG&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaGeneroBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    if(lista.imagen!=="null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))){
                    const row = `<tr><td><img src="imagenes/listas/${lista.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.genero}</td></tr>`;
                    tbody.innerHTML += row;
                }else{
                    const row = `<tr><td><img src="imagenes/listas/defaultList.png" id="imagenlista" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.genero}</td></tr>`;
                    tbody.innerHTML += row;
                    }
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));

    fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarAlbumes&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('albumBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(album => {
                    if(album.imagen!=="null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))){
                        const row = `<tr><td><img src="imagenes/albumes/${album.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${album.nombre}</td><td>${album.artista}</td><td>${album.anio}</td></tr>`;
                    tbody.innerHTML += row;
                    }else{
                        const row = `<tr><td><img src="imagenes/albumes/defaultAlbum.png" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${album.nombre}</td><td>${album.artista}</td><td>${album.anio}</td></tr>`;
                    tbody.innerHTML += row;
                    }
                });
            })
            .catch(error => console.error('Error al cargar Albumes:', error));

    checkGenero();

           if(isGenero.value==="1"){
               document.getElementById("todoDeUnGenero").style.display = "block";

               fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarListasDelGeneroU&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaGeneroGenBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    if(lista.imagen!=="null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))){
                    const row = `<tr><td><img src="imagenes/listas/${lista.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.genero}</td></tr>`;
                    tbody.innerHTML += row;
                }else{
                    const row = `<tr><td><img src="imagenes/listas/defaultList.png" id="imagenlista" alt="Imagen del Album" width="150"></td><td>${lista.nombre}</td><td>${lista.genero}</td></tr>`;
                    tbody.innerHTML += row;
                    }
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));

    fetch('http://localhost:8080/EspotifyWeb/BuscarCosasServlet?action=MostrarAlbumesDelGeneroU&buscar=' + encodeURIComponent(buscarName))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('albumGenBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
               data.forEach(album => {
                   if(album.imagen!=="null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))){
                       const row = `<tr><td><img src="imagenes/albumes/${album.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${album.nombre}</td><td>${album.artista}</td><td>${album.anio}</td></tr>`;
                    tbody.innerHTML += row;
                    }else{
                       const row = `<tr><td><img src="imagenes/albumes/defaultAlbum.png" id="imagenalbum" alt="Imagen del Album" width="150"></td><td>${album.nombre}</td><td>${album.artista}</td><td>${album.anio}</td></tr>`;
                   tbody.innerHTML += row;
                   }
               });
           })
            .catch(error => console.error('Error al cargar listas:', error));
           }else{
               document.getElementById("todoDeUnGenero").style.display = "none";
           }
     }
              }