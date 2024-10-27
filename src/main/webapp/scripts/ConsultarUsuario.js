const urlParams = new URLSearchParams(window.location.search);
const userId = urlParams.get('usr');

document.addEventListener('DOMContentLoaded', function () {
    cargarPerfil();

    document.getElementById('seguidoresBtn').addEventListener('click', function () {
        toggleSection('seguidores', cargarSeguidores);
    });

    document.getElementById('seguidosBtn').addEventListener('click', function () {
        toggleSection('seguidos', cargarSeguidos);
    });

    document.getElementById('listasBtn').addEventListener('click', function () {
        toggleSection('listas', cargarListas);
    });

    document.getElementById('favoritosBtn').addEventListener('click', function () {
        toggleSection('favoritos', cargarFavoritos);
    });

    document.getElementById('albumesBtn').addEventListener('click', function () {
        toggleSection('albumes', cargarAlbumes);
    });
});

function toggleSection(sectionId, loadFunction) {
    const section = document.getElementById(sectionId);
    if (section.style.display === 'block') {
        section.style.display = 'none';
    } else {
        section.style.display = 'block';
        loadFunction();
    }
}

function cargarPerfil() {
    fetch(`ConsultarUsuarioServlet?action=cargarPerfil&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('nickname').textContent = data.nickname;
                document.getElementById('correo').textContent = data.correo;
                document.getElementById('nombre').textContent = data.nombre;
                document.getElementById('apellido').textContent = data.apellido;
                document.getElementById('fechaNacimiento').textContent = data.fechaNacimiento;
                if(data.imagen!=="null" && (data.imagen.endsWith(".png") || data.imagen.endsWith(".jpg"))){
                    document.getElementById('imagenPerfil').src = `imagenes/usuarios/${data.imagen}`;
                }else{
                    document.getElementById('imagenPerfil').src = 'imagenes/usuarios/defaultUser.png';
                }
                
                checkUserType(data.nickname);
            })
            .catch(error => console.error('Error al cargar perfil:', error));
}

function cargarSeguidores() {
    fetch(`ConsultarUsuarioServlet?action=cargarSeguidores&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(seguidores => {
                const seguidoresTbody = document.getElementById('tablaSeguidores');
                seguidoresTbody.innerHTML = ''; // Limpiar tabla
                seguidores.forEach(seguidor => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${seguidor}</td>`;
                    seguidoresTbody.appendChild(row);
                });
                document.getElementById('seguidores').style.display = 'block';
            })
            .catch(error => console.error('Error al cargar seguidores:', error));
}

function cargarSeguidos() {
    fetch(`ConsultarUsuarioServlet?action=cargarSeguidos&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(seguidos => {
                const seguidosTbody = document.getElementById('tablaSeguidos');
                seguidosTbody.innerHTML = ''; // Limpiar tabla
                seguidos.forEach(seguido => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${seguido}</td>`;
                    seguidosTbody.appendChild(row);
                });
                document.getElementById('seguidos').style.display = 'block';
            })
            .catch(error => console.error('Error al cargar seguidos:', error));
}

function cargarListas() {
    fetch(`ConsultarUsuarioServlet?action=cargarListas&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(listas => {
                const listasTbody = document.getElementById('tablaListas');
                listasTbody.innerHTML = ''; // Limpiar tabla
                listas.forEach(lista => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${lista}</td><td><button onclick="verDetalles('${lista}')">Ver Detalles</button></td>`;
                    listasTbody.appendChild(row);
                });
                document.getElementById('listas').style.display = 'block';
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

function cargarFavoritos() {
    const nickname = userId;
    cargarAlbumesFavoritos(nickname);
    cargarListasFavoritas(nickname);
    cargarTemasFavoritos(nickname);
    document.getElementById('favoritos').style.display = 'block';
}

function cargarAlbumes() {
    fetch(`ConsultarUsuarioServlet?action=cargarAlbumes&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(albumes => {
                const albumesTbody = document.getElementById('tablaAlbumes');
                albumesTbody.innerHTML = ''; // Limpiar tabla
                albumes.forEach(album => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${album}</td><td><button onclick="verDetallesAlbum('${album}')">Ver Detalles</button></td>`;
                    albumesTbody.appendChild(row);
                });
                document.getElementById('albumes').style.display = 'block';
            })
            .catch(error => console.error('Error al cargar álbumes:', error));
}

function cargarAlbumesFavoritos(nickname) {
    fetch(`ConsultarUsuarioServlet?action=cargarAlbumesFavoritos&nickname=${encodeURIComponent(nickname)}`)
            .then(response => response.json())
            .then(albumes => {
                const albumesTbody = document.getElementById('tablaAlbumesFavoritos');
                albumesTbody.innerHTML = ''; // Limpiar tabla

                if (albumes.length === 0) {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="2" style="text-align: center;">No hay álbumes favoritos disponibles.</td>`;
                    albumesTbody.appendChild(row);
                } else {
                    albumes.forEach(album => {
                        const row = document.createElement('tr');
                        row.innerHTML = `<td>${album}</td><td><button onclick="verDetallesAlbum('${album}')">Ver Detalles</button></td>`;
                        albumesTbody.appendChild(row);
                    });
                }
            })
            .catch(error => console.error('Error al cargar álbumes favoritos:', error));
}

function cargarListasFavoritas(nickname) {
    fetch(`ConsultarUsuarioServlet?action=cargarListasFavoritas&nickname=${encodeURIComponent(nickname)}`)
            .then(response => response.json())
            .then(listas => {
                const listasTbody = document.getElementById('tablaListasFavoritas');
                listasTbody.innerHTML = ''; // Limpiar tabla

                if (listas.length === 0) {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="2" style="text-align: center;">No hay listas favoritas disponibles.</td>`;
                    listasTbody.appendChild(row);
                } else {
                    listas.forEach(lista => {
                        const row = document.createElement('tr');
                        row.innerHTML = `<td>${lista}</td><td><button onclick="verDetallesLista('${lista}')">Ver Detalles</button></td>`;
                        listasTbody.appendChild(row);
                    });
                }
            })
            .catch(error => console.error('Error al cargar listas favoritas:', error));
}

function cargarTemasFavoritos(nickname) {
    fetch(`ConsultarUsuarioServlet?action=cargarTemasFavoritos&nickname=${encodeURIComponent(nickname)}`)
            .then(response => response.json())
            .then(temas => {
                const temasTbody = document.getElementById('tablaTemasFavoritos');
                temasTbody.innerHTML = ''; // Limpiar tabla

                if (temas.length === 0) {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="3" style="text-align: center;">No hay temas favoritos disponibles.</td>`;
                    temasTbody.appendChild(row);
                } else {
                    temas.forEach(tema => {
                        const row = document.createElement('tr');
                        row.innerHTML = `<td>${tema.nombreTema}</td><td>${tema.nombreAlbumTema}</td>`;
                        temasTbody.appendChild(row);
                    });
                }
            })
            .catch(error => console.error('Error al cargar temas favoritos:', error));
}

function verDetallesLista(lista) {
    alert(`Detalles de la lista: ${lista}`);
    // Implementar 
}

function verDetallesAlbum(album) {
    alert(`Detalles del álbum: ${album}`);
    // Implementar 
}

function checkUserType(nickname) {
    fetch(`LoginServlet?action=obtenerTipoUsuario2&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                const userType = data.userType;
                if (userType === 'Artista') {
                    document.getElementById('seguidoresBtn').style.display = 'block';
                    document.getElementById('albumesBtn').style.display = 'block';
                } else if (userType === 'Cliente') {
                    document.getElementById('seguidoresBtn').style.display = 'block';
                    checkSuscripcion(nickname);
                }
            })
            .catch(error => console.error('Error al obtener el tipo de usuario:', error));
}

function checkSuscripcion(nickname) {
    fetch(`LoginServlet?action=obtenerSuscripcion2&nickname=${encodeURIComponent(nickname)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la red: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const suscripcion = data.suscrito;

                if (suscripcion === true) {
                    document.getElementById('seguidosBtn').style.display = 'block';
                    document.getElementById('listasBtn').style.display = 'block';
                    document.getElementById('favoritosBtn').style.display = 'block';
                } else if (suscripcion === false) {
                    document.getElementById('seguidores').style.display = 'block';
                    document.getElementById('seguidos').style.display = 'none';
                    document.getElementById('listas').style.display = 'none';
                    document.getElementById('favoritos').style.display = 'none';
                    document.getElementById('albumes').style.display = 'none';
                } else {
                    console.warn('No se pudo determinar el estado de la suscripción.');
                }
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));

}