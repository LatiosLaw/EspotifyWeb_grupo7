const urlParams = new URLSearchParams(window.location.search);
const userId = urlParams.get('usr');

document.addEventListener('DOMContentLoaded', function () {
    cargarPerfil();
    verificarParaEliminar();

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
    console.log(sessionUserType);
    console.log(sessionNickname);sessionUserType
        if(sessionUserType !== "Artista" || (sessionNickname === userId)){
        fetch(`ConsultarUsuarioServlet?action=cargarPerfil&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('nickname').textContent = data.nickname;
                document.getElementById('correo').textContent = data.correo;
                document.getElementById('nombre').textContent = data.nombre;
                document.getElementById('apellido').textContent = data.apellido;
                document.getElementById('fechaNacimiento').textContent = data.fechaNacimiento;
                if (data.imagen !== "null" && (data.imagen.endsWith(".png") || data.imagen.endsWith(".jpg"))) {
                    document.getElementById('imagenPerfil').src = `imagenes/usuarios/${data.imagen}`;
                } else {
                    document.getElementById('imagenPerfil').src = 'imagenes/usuarios/defaultUser.png';
                }

                checkUserType(data.nickname);
            })
            .catch(error => console.error('Error al cargar perfil:', error));
    } else {
        window.location.href = "index.jsp";
    }
}

function confirmarEliminacion() {
  // Mostramos el cuadro de confirmación
  var confirmar = confirm("Estas seguro de que quieres eliminar tu perfil?");
  
  // Si el usuario confirma, ejecutamos el código de eliminación
  if (confirmar) {
      fetch('CerrarSesionServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const message = data.success ? data.message : "Error al cerrar sesion.";
                alert(message);
            })
            .catch(error => {
                console.error('Error:', error);
                const errorMessage = "Error al intentar cerrar sesion.";
                alert(errorMessage);
            });

            setTimeout(() => location.href = 'index.jsp', 1000);
    // ANDRES ACA METE EL CODIGO PARA ELIMINAR ARTISTA CON TUS COSAS MAGICAS
    // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    alert("Perfil eliminado con exito");
  } else {
    alert("La eliminación ha sido cancelada");
  }
}

function verificarParaEliminar() {
    console.log(sessionUserType);
    console.log(sessionNickname);
    const eliminararti = document.getElementById('eliminarPerfil');
    const eliminarboton = document.getElementById('eliminarArte');
        if(sessionUserType === "Artista" || (sessionNickname === userId)){
        eliminararti.style.display = 'block';   // Hacemos visible el div
  eliminarboton.style.display = 'inline-block'; // Hacemos visible el botón
    }
}

function cargarSeguidores() {
    fetch(`ConsultarUsuarioServlet?action=cargarSeguidores&nickname=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(seguidores => {
                const seguidoresTbody = document.getElementById('tablaSeguidores');
                seguidoresTbody.innerHTML = ''; // Limpiar tabla
                seguidores.forEach(seguidor => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td><a href="ConsultarUsuario.jsp?usr=${seguidor}">${seguidor}</a></td>`;
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
                    const nombreUsuario = seguido.split("/")[0];
                    const row = document.createElement('tr');
                    row.innerHTML = `<td><a href="ConsultarUsuario.jsp?usr=${nombreUsuario}">${seguido}</a></td>`;
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
                    if ((lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))) {
                        row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/listas/${lista.imagen}" class="imagenLsita" alt="Imagen de Lista"></td><td>${lista.nombre}</td><td><button onclick="verDetallesLista('${lista.nombre}')">Ver Detalles</button></td>`;
                        listasTbody.appendChild(row);
                    } else {
                        row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/listas/defaultList.png" class="imagenLsita" alt="Imagen de Lista"></td><td>${lista.nombre}</td><td><button onclick="verDetallesLista('${lista.nombre}')">Ver Detalles</button></td>`;
                        listasTbody.appendChild(row);
                    }
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
                    if ((album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))) {
                        row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/albumes/${album.imagen}" class="imagenAlbum" alt="Imagen de Album"></td><td>${album.nombre}</td><td><button onclick="verDetallesAlbum('${album.nombre}')">Ver Detalles</button></td>`;
                        albumesTbody.appendChild(row);
                    } else {
                        row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/albumes/defaultAlbum.png" class="imagenAlbum" alt="Imagen de Album"></td><td>${album.nombre}</td><td><button onclick="verDetallesAlbum('${album.nombre}')">Ver Detalles</button></td>`;
                        albumesTbody.appendChild(row);
                    }
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
                        if ((album.imagen.endsWith(".png") || album.imagen.endsWith(".jpg"))) {
                            row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/albumes/${album.imagen}" class="imagenAlbum" alt="Imagen de Album"></td><td>${album.nombre}</td><td><button onclick="verDetallesAlbum('${album.nombre}')">Ver Detalles</button></td>`;
                            albumesTbody.appendChild(row);
                        } else {
                            row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/albumes/defaultAlbum.png" class="imagenAlbum" alt="Imagen de Album"></td><td>${album.nombre}</td><td><button onclick="verDetallesAlbum('${album.nombre}')">Ver Detalles</button></td>`;
                            albumesTbody.appendChild(row);
                        }
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
                        if ((lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))) {
                            row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/listas/${lista.imagen}" class="imagenLsita" alt="Imagen de Lista"></td><td>${lista.nombre}</td><td><button onclick="verDetallesLista('${lista.nombre}')">Ver Detalles</button></td>`;
                            listasTbody.appendChild(row);
                        } else {
                            row.innerHTML = `<td><img width="50px" height="50px" src="imagenes/listas/defaultList.png" class="imagenLsita" alt="Imagen de Lista"></td><td>${lista.nombre}</td><td><button onclick="verDetallesLista('${lista.nombre}')">Ver Detalles</button></td>`;
                            listasTbody.appendChild(row);
                        }
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
    var tipo = "0";
    if (lista.toString().endsWith("-")) {
        var tipo = "1";
        lista = lista.slice(0, -1);
        window.location.href = "ConsultarListaRep.jsp?listaName=" + lista + "tipo=" + tipo;
    } else {
        var tipo = "2";
        window.location.href = "ConsultarListaRep.jsp?listaName=" + lista + "tipo=" + tipo;
    }
}

function verDetallesAlbum(album) {
    window.location.href = "ConsultarAlbum.jsp?album=" + album;
}

function checkVisitante() {
    if (sessionNickname) {
        // Usuario logueado
        return true;
    } else {
        // Usuario no logueado
        return false;
    }
}

function checkUserType(nickname) {

    console.log(sessionNickname);

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
                    const logeado = checkVisitante();
                    if (!logeado) {
                        document.getElementById('seguidoresBtn').style.display = 'none';
                        document.getElementById('seguidosBtn').style.display = 'none';
                        document.getElementById('listasBtn').style.display = 'block';
                        document.getElementById('favoritosBtn').style.display = 'none';
                        document.getElementById('albumesBtn').style.display = 'none';
                        document.getElementById('pCorreo').style.display = 'none';
                        document.getElementById('pNya').style.display = 'none';
                        document.getElementById('pFechaNacimiento').style.display = 'none';
                    }
                } else if (suscripcion === false) {
                    document.getElementById('seguidores').style.display = 'block';
                    document.getElementById('seguidos').style.display = 'none';
                    document.getElementById('listas').style.display = 'none';
                    document.getElementById('favoritos').style.display = 'none';
                    document.getElementById('albumes').style.display = 'none';
                    const logeado = checkVisitante();
                    if (!logeado) {
                        document.getElementById('seguidoresBtn').style.display = 'none';
                        document.getElementById('seguidosBtn').style.display = 'none';
                        document.getElementById('listasBtn').style.display = 'block';
                        document.getElementById('favoritosBtn').style.display = 'none';
                        document.getElementById('albumesBtn').style.display = 'none';
                        document.getElementById('pCorreo').style.display = 'none';
                        document.getElementById('pNya').style.display = 'none';
                        document.getElementById('pFechaNacimiento').style.display = 'none';
                    }
                } else {
                    console.warn('No se pudo determinar el estado de la suscripción.');
                }
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));
}