fetch('http://localhost:8080/EspotifyWeb/PaginaInicioServlet?action=buscarArtistas')
.then(response => response.json())
.then(data => {
    const tbody = document.getElementById('artistasBody');
    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
   data.forEach(artista => {
       if(artista.imagen!=="null" && (artista.imagen.endsWith(".png") || artista.imagen.endsWith(".jpg"))){
           const row = `<tr><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}"><img src="imagenes/usuarios/${artista.imagen}" id="imagenuser" alt="Imagen del Usuario" width="150"></a></td><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}">${artista.nombre}</td></a></tr>`;
        tbody.innerHTML += row;
        }else{
           const row = `<tr><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}"><img src="imagenes/usuarios/defaultUser.png" id="imagenuser" alt="Imagen del Usuario" width="150"></a></td><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(artista.nombre)}">${artista.nombre}</td></a></tr>`;
       tbody.innerHTML += row;
       }
   });
})
.catch(error => console.error('Error al cargar usuarios:', error));

fetch('http://localhost:8080/EspotifyWeb/PaginaInicioServlet?action=buscarAlbumes')
.then(response => response.json())
.then(data => {
    const tbody = document.getElementById('albumBody');
    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
   data.forEach(album => {
       if(album.imagen!=="null" && (album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))){
           const row = `<tr><td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}"><img src="imagenes/albumes/${album.imagen}" id="imagenalbum" alt="Imagen del Album" width="150"></a></td><td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">${album.nombre}</a></td></tr>`;
        tbody.innerHTML += row;
        }else{
           const row = `<tr><td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}"><img src="imagenes/albumes/defaultAlbum.png" id="imagenalbum" alt="Imagen del Album" width="150"></a></td><td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(album.nombre)}">${album.nombre}</a></td></tr>`;
       tbody.innerHTML += row;
       }
   });
})
.catch(error => console.error('Error al cargar albumes:', error));

fetch('http://localhost:8080/EspotifyWeb/PaginaInicioServlet?action=buscarListas')
.then(response => response.json())
.then(data => {
    const tbody = document.getElementById('listasBody');
    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
   data.forEach(lista => {
       if(lista.imagen!=="null" && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))){
           const row = `<tr><td><a href="ConsultarListaRep.jsp?list=${encodeURIComponent(lista.nombre)}"><img src="imagenes/listas/${lista.imagen}" id="imagenlist" alt="Imagen del Lista" width="150"></a></td><td><a href="ConsultarListaRep.jsp?list=${encodeURIComponent(lista.nombre)}">${lista.nombre}</a></td></tr>`;
        tbody.innerHTML += row;
        }else{
           const row = `<tr><td><a href="ConsultarListaRep.jsp?list=${encodeURIComponent(lista.nombre)}"><img src="imagenes/listas/defaultList.png" id="imagenlist" alt="Imagen del Lista" width="150"></a></td><td><a href="ConsultarListaRep.jsp?list=${encodeURIComponent(lista.nombre)}">${lista.nombre}</a></td></tr>`;
       tbody.innerHTML += row;
       }
   });
})
.catch(error => console.error('Error al cargar listas:', error));

fetch('http://localhost:8080/EspotifyWeb/PaginaInicioServlet?action=buscarClientes')
.then(response => response.json())
.then(data => {
    const tbody = document.getElementById('usuariosBody');
    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
   data.forEach(cliente => {
       if(cliente.imagen!=="null" && (cliente.imagen.endsWith(".png") || cliente.imagen.endsWith(".jpg"))){
           const row = `<tr><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}"><img src="imagenes/usuarios/${cliente.imagen}" id="imagencliente" alt="Imagen del Cliente" width="150"></a></td><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}">${cliente.nombre}</a></td></tr>`;
        tbody.innerHTML += row;
        }else{
           const row = `<tr><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}"><img src="imagenes/usuarios/defaultUser.png" id="imagencliente" alt="Imagen del Cliente" width="150"></a></td><td><a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}">${cliente.nombre}</a></td></tr>`;
       tbody.innerHTML += row;
       }
   });
})
.catch(error => console.error('Error al cargar clientes:', error));