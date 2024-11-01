window.onload = SuccionarInformacion();

function formatearTiempo(segundos) {
// Convertir los segundos a minutos y segundos
    const minutos = Math.floor(segundos / 60);
    const segundosRestantes = segundos % 60;

// Asegurarse de que los segundos tengan siempre dos dígitos (p. ej. "03" en lugar de "3")
    const segundosFormateados = segundosRestantes < 10 ? '0' + segundosRestantes : segundosRestantes;

    return minutos + ":" + segundosFormateados;
}

function esUrlValida(url) {
    try {
        new URL(url);
        return true; // Si no hay error, la URL es válida
    } catch (_) {
        return false; // Si hay un error, la URL no es válida
    }
}

function DescargarTema(boton) {
    const fila = boton.parentElement.parentElement;
    const tercerCampo = fila.querySelector('td:nth-child(3)').innerText;
    window.location.href = 'ConsultarAlbumServlet?action=Download&filename=' + encodeURIComponent(tercerCampo);
}

function VamoAYoutube(boton) {
    const fila = boton.parentElement.parentElement;
    const tercerCampo = fila.querySelector('td:nth-child(3)').innerText;
    if (esUrlValida(tercerCampo)) {
        window.location.href = tercerCampo; // Redirige al usuario si la URL es válida
    } else {
        console.log("URL no válida:", tercerCampo); // O muestra un mensaje en la consola
        alert("La URL no es válida. Inténtalo de nuevo.");
    }
}

document.getElementById('opciones').addEventListener('change', function () {
    const selectedValue = this.value;

    // Ejecutar diferentes funciones dependiendo de la opción seleccionada
    if (selectedValue === 'genero') {
        manejarGenero();
    } else if (selectedValue === 'artista') {
        manejarArtista();
    }
});

// Boolean suscrito = (Boolean) session.getAttribute("suscrito");

function obtenerValorSesion() {
    try {
        return fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverSubscripcion')
                .then(response => response.json())
                .then(data => {
                    if (data.sus === "true") {
                        console.log("Tenia sus");
                        return true;
                    } else {
                        console.log("No tenia sus");
                        return false;
                    }
                })
                .catch(error => {
                    console.log("Revento Adentro Servlet");
                    console.error('Error:', error);
                    return false;
                });
    } catch (error) {
        console.log("Revento Afuera Servlet");
        // Captura el error y lo muestra en la consola o en la página
        return false;
    }

}

async function SuccionarInformacion() {
    const urlParams = new URLSearchParams(window.location.search);
    const primerCampo = urlParams.get('album');
    const resultado = await obtenerValorSesion();

    if (resultado) {
        fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverTemasAlbum&albumName=' + encodeURIComponent(primerCampo))
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('temasBody');
                    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                    data.forEach(tema => {

                    
                        if(tema.fav === "fav"/*El usuario ya le puso laik*/){
                            if (tema.link !== "null") {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td><td><button onclick="VamoAYoutube(this)">Escuchar Tema</button><td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')">Agregar a Lista</button></td><td><button onclick="algo(this)">Fav</button></td></tr>`;
                                tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td><td><button onclick="DescargarTema(this)">Descargar</button></td><td><button onclick="algo(this)">Fav</button></td></tr>`;
                                tbody.innerHTML += row;
                            }
                        
                        
                        }else{
                            if (tema.link !== "null") {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td><td><button onclick="VamoAYoutube(this)">Escuchar Tema</button><td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')">Agregar a Lista</button></td></td><td><button onclick="algo(this)">NoFav</button></td></tr>`;
                                tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td><td><button onclick="DescargarTema(this)">Descargar</button></td></td><td><button onclick="DescargarTema(this)">Fav</button></td><td><button onclick="algo(this)">NoFav</button></td></tr>`;
                                tbody.innerHTML += row;
                            }
                        }
                    });
                })
                .catch(error => console.error('Error al cargar temas del album:', error));
    } else {
        fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverTemasAlbum&albumName=' + encodeURIComponent(primerCampo))
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('temasBody');
                    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                    data.forEach(tema => {
                        if (tema.link !== "null") {
                            const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td><td>Fuck You Pobre</td></tr>`;
                            tbody.innerHTML += row;
                        } else {
                            const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td><td>Fuck You Pobre</td></tr>`;
                            tbody.innerHTML += row;
                        }
                    });
                })
                .catch(error => console.error('Error al cargar temas del album:', error));
    }

    var NOMBREALBUM = document.getElementById('nombrealbum');
    var ALBUMTEMA = document.getElementById('albumTema');
    var ANIOALBUM = document.getElementById('anioalbum');
    var IMAGENALBUM = document.getElementById('imagenalbum');
    var CREADORALBUM = document.getElementById('creadoralbum');
    var GENEROSLIST = document.getElementById('generoslist');
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverInformacionAlbum&albumName=' + encodeURIComponent(primerCampo))
            .then(response => response.json())
            .then(data => {
                data.forEach(album => {
                    NOMBREALBUM.value = album.nombre;
                    ALBUMTEMA.value = album.nombre;
                    ANIOALBUM.value = album.anio;
                    CREADORALBUM.value = album.creador;
                    if (album.imagen !== "" && album.imagen !== null && (album.imagen === "png" || album.imagen === "jpg")) {
                        IMAGENALBUM.src = "imagenes/albumes/" + album.imagen;
                    } else {
                        IMAGENALBUM.src = "imagenes/albumes/defaultAlbum.png";
                    }
                    if(album.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                         
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                         
                    }
                    
                    
                    
                    
                    GENEROSLIST.innerHTML = '';

                    album.generos.forEach(genero => {
                        var li = document.createElement('li'); // Crear un nuevo elemento de lista
                        li.textContent = genero.nombre;        // Asignar el nombre del género al li
                        GENEROSLIST.appendChild(li);           // Añadir el li a la lista
                    });

                });
            })
            .catch(error => console.error('Error al datos del album:', error));

}