document.addEventListener('DOMContentLoaded', function () {
    cargarPerfil();
});

function cargarPerfil() {
fetch('ConsultarUsuarioServlet?action=cargarPerfil')
        .then(response => response.json())
        .then(data => {
            document.getElementById('nickname').textContent = data.nickname;
            document.getElementById('correo').textContent = data.correo;
            document.getElementById('nombre').textContent = data.nombre;
            document.getElementById('apellido').textContent = data.apellido;
            document.getElementById('fechaNacimiento').textContent = data.fechaNacimiento;
            document.getElementById('imagenPerfil').src = data.imagen ? `imagenes/usuarios/${data.imagen}` : 'imagenes/usuarios/defaultUser.png';
            checkUserType();
        })
        .catch(error => console.error('Error al cargar perfil:', error));
}

function cargarSeguidores(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarSeguidores&nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(seguidores => {
            const seguidoresTbody = document.getElementById('tablaSeguidores');
            seguidores.forEach(seguidor => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${seguidor}</td>`;
                seguidoresTbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar seguidores:', error));
}

function cargarSeguidos(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarSeguidos&nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(seguidos => {
            const seguidosTbody = document.getElementById('tablaSeguidos');
            seguidos.forEach(seguido => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${seguido}</td>`;
                seguidosTbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar seguidos:', error));
}

function cargarListas(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarListas&nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(listas => {
            const listasTbody = document.getElementById('tablaListas');
            listas.forEach(lista => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${lista}</td><td><button onclick="verDetalles('${lista}')">Ver Detalles</button></td>`;
                listasTbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar listas:', error));
}

function cargarAlbumes(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarAlbumes&nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(albumes => {
            document.getElementById('albumes').style.display = 'block';
            const albumesTbody = document.getElementById('tablaAlbumes');
            albumes.forEach(album => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${album}</td><td><button onclick="verDetallesAlbum('${album}')">Ver Detalles</button></td>`;
                albumesTbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar álbumes:', error));
}

function cargarAlbumesFavoritos(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarAlbumesFavoritos&nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(albumes => {
            const albumesTbody = document.getElementById('tablaAlbumesFavoritos');
            albumesTbody.innerHTML = '';

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
        .catch(error => console.error('Error al cargar álbumes:', error));
}

function cargarListasFavoritas(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarListasFavoritas&nickname=${encodeURIComponent(nickname)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red: ' + response.statusText);
            }
            return response.json();
        })
        .then(listas => {
            const listasTbody = document.getElementById('tablaListasFavoritas');
            listasTbody.innerHTML = '';

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
        .catch(error => console.error('Error al cargar listas:', error));
}

function cargarTemasFavoritos(nickname) {
fetch(`ConsultarUsuarioServlet?action=cargarTemasFavoritos&nickname=${encodeURIComponent(nickname)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red: ' + response.statusText);
            }
            return response.json();
        })
        .then(temas => {
            const temasTbody = document.getElementById('tablaTemasFavoritos');
            temasTbody.innerHTML = '';

            if (temas.length === 0) {
                const row = document.createElement('tr');
                row.innerHTML = `<td colspan="3" style="text-align: center;">No hay temas favoritos disponibles.</td>`;
                temasTbody.appendChild(row);
            } else {
                temas.forEach(tema => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
        <td>${tema.nombreTema}</td>
        <td>${tema.nombreAlbumTema}</td>
    `;
                    temasTbody.appendChild(row);
                });
            }
        })
        .catch(error => console.error('Error al cargar temas:', error));
}



function verDetallesLista(lista) {
alert(`Detalles de la lista: ${lista}`);
// wip
}

function verDetallesAlbum(album) {
alert(`Detalles del álbum: ${album}`);
// wip
}

function checkSuscripcion() {
fetch('LoginServlet?action=obtenerSuscripcion')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const suscripcion = data.suscrito;

            if (suscripcion === true) {
                cargarSeguidores(data.nickname);
                cargarSeguidos(data.nickname);
                cargarListas(data.nickname);
                cargarAlbumesFavoritos(data.nickname);
                cargarListasFavoritas(data.nickname);
                cargarTemasFavoritos(data.nickname);

                document.getElementById('seguidores').style.display = 'block';
                document.getElementById('seguidos').style.display = 'block';
                document.getElementById('listas').style.display = 'block';
                document.getElementById('favoritos').style.display = 'block';
            } else if (suscripcion === false) {

                cargarSeguidores(data.nickname);
                document.getElementById('seguidores').style.display = 'block';
                document.getElementById('seguidos').style.display = 'none';
                document.getElementById('listas').style.display = 'none';
                document.getElementById('favoritos').style.display = 'none';
            } else {
                console.warn('No se pudo determinar el estado de la suscripción.');
            }
        })
        .catch(error => console.error('Error al obtener la suscripción:', error));
}

function checkUserType() {
fetch('LoginServlet?action=obtenerTipoUsuario')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const userType = data.userType;

            if (userType === 'Artista') {
                document.getElementById('albumes').style.display = 'block'; // Muestra la sección de álbumes
                document.getElementById('seguidores').style.display = 'block'; // Oculta la sección de álbumes
                cargarAlbumes(nickname);
                cargarSeguidores(data.nickname);

            } else if (userType === 'Cliente') {
                checkSuscripcion();
            } else {
            }
        })
        .catch(error => console.error('Error al obtener el tipo de usuario:', error));
}