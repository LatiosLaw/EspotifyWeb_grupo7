
document.addEventListener('DOMContentLoaded', function() {
    // Tu código JavaScript aquí
    console.log('El DOM ha sido completamente cargado');
    checkSuscripcion();
    setTimeout(() => {
        recargarTemas();
    }, 250);
    
});

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

                // Llenar tabla con los temas y el estado de la suscripción
                SuccionarInformacion(suscripcion);
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));
}

async function SuccionarInformacion(tieneSuscripcion) {
    const urlParams = new URLSearchParams(window.location.search);
    const primerCampo = urlParams.get('album');
    const resultado = await obtenerValorSesion();

        fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverTemasAlbum&albumName=' + encodeURIComponent(primerCampo))
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('temasBody');
                    tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                    data.forEach(tema => {

                    
                       if(tema.fav === "fav"/*El usuario ya le puso laik*/){
                            if (tema.link !== "null") {
                                const row = `<tr><td>${tema.nombre}</td>
                                <td>${formatearTiempo(tema.duracion)}</td>
                                <td>${tema.link}</td>
                                <td>
                                <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                                : `alert(''Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsAlbum">
                                Descargar
                                </button>
                                </td>
                    
                <td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsAlbum">
                        Agregar a Lista
                    </button>
                </td>
                                 <td>
                                    <button onclick="${tieneSuscripcion ? `sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para eliminar temas de tus favoritos.')`}" class="btnsAlbum">
                                        NoFav
                                    </button>
                                </td>
                                </tr>`;
                                tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td>
                                <td>${formatearTiempo(tema.duracion)}</td>
                                <td>${tema.archivo}</td>
                                <td>
                                <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                                : `alert('Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsAlbum">
                                Descargar
                                </button>
                                </td>
                                <td>
                                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsAlbum">
                                        Agregar a Lista
                                    </button>
                                </td>
                                <td>
                                    <button onclick="${tieneSuscripcion ? `"sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para eliminar temas de tus favoritos.')`}" class="btnsAlbum">
                                        NoFav
                                    </button>
                                </td>
                                </tr>`;
                                tbody.innerHTML += row;
                            }
                        }else{
                            if (tema.link !== "null") {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td>
                <td>
                    <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                    : `alert('Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsAlbum">
                    Descargar
                    </button>
                    </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsAlbum">
                        Agregar a Lista
                    </button>
                </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para eliminar temas de tus favoritos.')`}" class="btnsAlbum">
                    Fav
                    </button>
                </td>    
        </tr>`;
                                         
                    tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td>
                <td>
                    <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                    : `alert('Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsAlbum">
                    Descargar
                    </button>
                    </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsAlbum">
                        Agregar a Lista
                        
                    </button>
                </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `"agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para eliminar temas de tus favoritos.')`}" class="btnsAlbum">
                    Fav
                    </button>
                </td>    
        </tr>`;
                                tbody.innerHTML += row;
                            }
                        }
                    });
                })
                .catch(error => console.error('Error al cargar temas del album:', error));

    var NOMBREALBUM = document.getElementById('nombrealbum');
    var ALBUMTEMA = document.getElementById('albumTema');
    var ANIOALBUM = document.getElementById('anioalbum');
    var IMAGENALBUM = document.getElementById('imagenalbum');
    var CREADORALBUM = document.getElementById('creadoralbum');
    var GENEROSLIST = document.getElementById('generoslist');
    var LAIK = document.getElementById('favAlbumBtn');
    var NOLAIK = document.getElementById('sacarDeFavAlbumBtn');
    var IMAGENREPRO = document.getElementById('imagenReproductor');
    fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=devolverInformacionAlbum&albumName=' + encodeURIComponent(primerCampo))
            .then(response => response.json())
            .then(data => {
                data.forEach(album => {
                    NOMBREALBUM.value = album.nombre;
                    ALBUMTEMA.value = album.nombre;
                    ANIOALBUM.value = album.anio;
                    CREADORALBUM.value = album.creador;
                    if ((album.imagen.toString().endsWith(".png") || album.imagen.toString().endsWith(".jpg"))) {
                        IMAGENALBUM.src = "imagenes/albumes/" + album.imagen;
                        IMAGENREPRO.src = "imagenes/albumes/" + album.imagen;
                    } else {
                        IMAGENALBUM.src = "imagenes/albumes/defaultAlbum.png";
                        IMAGENREPRO.src = "imagenes/albumes/defaultAlbum.png";
                    }
                    if(album.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                         // NOMBREALBUM.value = "fav";
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                         //NOMBREALBUM .value = "nofav";
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

function agregarAlgoFav(id, coso, album){
    var LAIK = document.getElementById('favAlbumBtn');
    var NOLAIK = document.getElementById('sacarDeFavAlbumBtn');
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/AgregarAlgoFavServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, album: album})
        
        
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Se agrego a favoritos.');
                   
            if(coso === "Album"){
                NOLAIK.style.display = 'block';
                LAIK.style.display = 'none';
            }else{
                recargarListas();
            }

                } else {
                    alert('Error al agregar a favotitos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar agregar a favoritos.');
            });
    
    
}

function sacarAlgoFav(id, coso, album){
    var LAIK = document.getElementById('favAlbumBtn');
    var NOLAIK = document.getElementById('sacarDeFavAlbumBtn');
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/SacarAlgoFavServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, album:album})
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Se elimino de tus favoritos.');
                    
                    if(coso === "Album"){
                        NOLAIK.style.display = 'none';
                        LAIK.style.display = 'block';
                    }else{
                        recargarListas();
            }
                    
                } else {
                    alert('Error al eliminar de tus favoritos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar eliminar de tus favoritos.');
            });
    
    
}

async function recargarListas() {
    
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
                                const row = `<tr><td>${tema.nombre}</td>
                                <td>${formatearTiempo(tema.duracion)}</td>
                                <td>${tema.link}</td>
                                <td><button onclick="VamoAYoutube(this)" class="btnsAlbum">Escuchar Tema</button><td>
                                <button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')"class="btnsAlbum" >Agregar a Lista</button></td>
                         <td><button onclick="sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')"class="btnsAlbum">NoFav</button></td>
                                </tr>`;
                                tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td>
                                <td>${formatearTiempo(tema.duracion)}</td>
                                <td>${tema.archivo}</td>
                                <td><button onclick="DescargarTema(this)" class="btnsAlbum">Descargar</button></td>
                                <td><button onclick="sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')"class="btnsAlbum">NoFav</button></td>
                                </tr>`;
                                tbody.innerHTML += row;
                            }
                        }else{
                            if (tema.link !== "null") {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td><td><button onclick="VamoAYoutube(this)" class="btnsAlbum">Escuchar Tema</button><td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')" class="btnsAlbum">Agregar a Lista</button></td></td><td><button onclick="agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')" class="btnsAlbum">Fav</button></td></tr>`;
                                tbody.innerHTML += row;
                            } else {
                                const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td><td><button onclick="DescargarTema(this)" class="btnsAlbum">Descargar</button></td></td><td><button onclick="agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}')" class="btnsAlbum">Fav</button></td></tr>`;
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
                            const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.link}</td><td>Sin Subscripcion</td></tr>`;
                            tbody.innerHTML += row;
                        } else {
                            const row = `<tr><td>${tema.nombre}</td><td>${formatearTiempo(tema.duracion)}</td><td>${tema.archivo}</td><td>Sin Subscripcion</td></tr>`;
                            tbody.innerHTML += row;
                        }
                    });
                })
                .catch(error => console.error('Error al cargar temas del album:', error));
    }
    
}
        function agregarAlbumAFav() {
            var NOMBREALBUM = document.getElementById('nombrealbum');
            
            const idAlbum =NOMBREALBUM.value;
           
            agregarAlgoFav(idAlbum,"Album","none");
                
                
        }
        
        
        function obtenerArchivosTemas(){
            const temasBody = document.getElementById("temasBody"); // Selecciona el cuerpo de la tabla
    const filas = temasBody.getElementsByTagName("tr"); // Obtiene todas las filas en el cuerpo de la tabla
    const temas = []; // Array para almacenar los valores de la columna
    //temas.push('temas/DONMAI.mp3');
    // Iterar sobre cada fila para obtener el valor de la columna "Archivo / Link"
    for (let i = 0; i < filas.length; i++) {
        const celdas = filas[i].getElementsByTagName("td"); // Obtiene todas las celdas de la fila
        if (celdas.length > 0) {
            const tema = celdas[2].innerText; // La celda de la columna "Archivo / Link" está en el índice 2
            temas.push(('temas/'+tema)); // Agrega el archivo al array
        }
    }
console.log(temas);
    return temas;
        }
        
        function obtenerNombresTemas(){
            const temasBody = document.getElementById("temasBody"); // Selecciona el cuerpo de la tabla
    const filas = temasBody.getElementsByTagName("tr"); // Obtiene todas las filas en el cuerpo de la tabla
    const temas = []; // Array para almacenar los valores de la columna
    //temas.push('temas/DONMAI.mp3');
    // Iterar sobre cada fila para obtener el valor de la columna "Archivo / Link"
    for (let i = 0; i < filas.length; i++) {
        const celdas = filas[i].getElementsByTagName("td"); // Obtiene todas las celdas de la fila
        if (celdas.length > 0) {
            const tema = celdas[0].innerText; // La celda de la columna "Archivo / Link" está en el índice 2
            temas.push((tema)); // Agrega el archivo al array
        }
    }
console.log(temas);
    return temas;
        }
        
        function sacarAlbumAFav() {
            var NOMBREALBUM = document.getElementById('nombrealbum');
            
            const idAlbum =NOMBREALBUM.value;
           
            sacarAlgoFav(idAlbum,"Album","none");
                
                
        }

const audio = document.getElementById('miAudio');
                                const audioSource = document.getElementById('audioSource');
                                const playBtn = document.getElementById('playBtn');
                                const pauseBtn = document.getElementById('pauseBtn');
                                const muteBtn = document.getElementById('muteBtn');
                                const unmuteBtn = document.getElementById('unmuteBtn');
                                const progressBar = document.getElementById('progressBar');
                                const progress = document.getElementById('progress');
                                const volumeBar = document.getElementById('volumeBar');
                                const volumeLevel = document.getElementById('volumeLevel');
                                const prevBtn = document.getElementById('prevBtn');
                                const nextBtn = document.getElementById('nextBtn');
                                const currentTimeDisplay = document.getElementById('currentTime');
                                const totalTimeDisplay = document.getElementById('totalTime');

                                let audioFiles;
                                let temasNames;
                                let currentAudioIndex = 0;
                                let currentNombreIndex = 0;

                                let previousVolume = 0.5; // Almacena el volumen anterior
                                audio.volume = previousVolume; // Inicializa el volumen al cargar
                                
                                let wasPaused; // Variable para almacenar si el audio estaba pausado

                                // Evento para reproducir el siguiente audio al finalizar
                                audio.addEventListener('ended', nextAudio);

                                // Actualiza el tiempo total y el tiempo actual
                                audio.addEventListener('loadedmetadata', () => {
                                    totalTimeDisplay.textContent = formatTime(audio.duration);
                                });

                                audio.addEventListener('timeupdate', () => {
                                    const percentage = (audio.currentTime / audio.duration) * 100;
                                    progress.style.width = percentage + '%';
                                    currentTimeDisplay.textContent = formatTime(audio.currentTime);
                                });

                                audio.addEventListener('ended', () => {
                                    progress.style.width = '0%'; // Resetea la barra de progreso
                                });

                                playBtn.addEventListener('click', () => {
                                    audio.play();
                                    playBtn.style.display = 'none';
                                    pauseBtn.style.display = 'inline';
                                });

                                pauseBtn.addEventListener('click', () => {
                                    audio.pause();
                                    playBtn.style.display = 'inline';
                                    pauseBtn.style.display = 'none';
                                });

                                function setProgress(event) {
                                    const totalWidth = progressBar.offsetWidth;
                                    const clickX = event.offsetX;

                                    // Verifica que el clic esté dentro de los límites de la barra de progreso
                                    if (clickX >= 0 && clickX <= totalWidth) {
                                        const newTime = (clickX / totalWidth) * audio.duration;
                                        audio.currentTime = newTime;
                                    }
                                }

                                function startAdjustingProgressBar(event) {
                                    wasPaused = audio.paused; // Guarda el estado del audio
                                    audio.pause();
                                    setProgress(event); // Ajusta el progreso al hacer clic
                                    document.addEventListener('mousemove', setProgress); // Ajusta el progreso mientras se mueve el ratón
                                    document.addEventListener('mouseup', stopAdjustingProgressBar); // Detiene el ajuste al soltar el botón
                                }
                                
                                function recargarTemas(){
                                    audioFiles = obtenerArchivosTemas();
                                    temasNames = obtenerNombresTemas();
                                    OnlyLoadAudio();
                                }

                                function stopAdjustingProgressBar() {
                                    document.removeEventListener('mousemove', setProgress); // Detiene el ajuste de progreso
                                    document.removeEventListener('mouseup', stopAdjustingProgressBar); // Detiene el evento de soltar

                                    // Restablece el estado del audio
                                    if (wasPaused) {
                                        playBtn.style.display = 'inline';
                                        pauseBtn.style.display = 'none';
                                        audio.pause();
                                    } else {
                                        playBtn.style.display = 'none';
                                        pauseBtn.style.display = 'inline';
                                        audio.play();
                                    }
                                }

                                function setVolume(event) {
                                    const totalWidth = volumeBar.offsetWidth;
                                    const clickX = event.offsetX;
                                    const newVolume = clickX / totalWidth;
                                    
                                    if (clickX >= 0 && clickX <= totalWidth) {
                                        audio.volume = Math.max(0, Math.min(1, newVolume)); // Asegura que el volumen esté entre 0 y 1
                                        previousVolume = audio.volume; // Actualiza el volumen anterior
                                        volumeLevel.style.width = (newVolume * 100) + '%'; // Actualiza la barra de volumen
                                    }
                                }

                                function startAdjustingVolume(event) {
                                    setVolume(event); // Ajusta el volumen al hacer clic
                                    document.addEventListener('mousemove', setVolume); // Ajusta el volumen mientras se mueve el ratón
                                    document.addEventListener('mouseup', stopAdjustingVolume); // Detiene el ajuste al soltar el botón
                                }

                                function stopAdjustingVolume() {
                                    document.removeEventListener('mousemove', setVolume); // Detiene el ajuste de volumen
                                    document.removeEventListener('mouseup', stopAdjustingVolume); // Detiene el evento de soltar
                                }

                                function muteVolume() {
                                    previousVolume = audio.volume; // Guarda el volumen actual
                                    audio.volume = 0; // Silencia el audio
                                    volumeLevel.style.width = '0%'; // Actualiza la barra de volumen
                                    muteBtn.style.display = 'none'; // Oculta el botón de silenciar
                                    unmuteBtn.style.display = 'inline'; // Muestra el botón de restaurar volumen
                                }

                                function unmuteVolume() {
                                    audio.volume = previousVolume; // Restaura el volumen anterior
                                    volumeLevel.style.width = (previousVolume * 100) + '%'; // Actualiza la barra de volumen
                                    unmuteBtn.style.display = 'none'; // Oculta el botón de restaurar volumen
                                    muteBtn.style.display = 'inline'; // Muestra el botón de silenciar
                                }

                                function prevAudio() {
                                    currentAudioIndex = (currentAudioIndex - 1 + audioFiles.length) % audioFiles.length;
                                    currentNombreIndex = currentAudioIndex;
                                    loadAudio();
                                }

                                function nextAudio() {
                                    currentAudioIndex = (currentAudioIndex + 1) % audioFiles.length;
                                    currentNombreIndex = currentAudioIndex;
                                    loadAudio();
                                }
                                
                                function OnlyLoadAudio() {
                                    audioSource.src = audioFiles[currentAudioIndex];
                                    audio.load(); // Carga el nuevo archivo de audio
                                    document.getElementById('nombreTema').innerText = temasNames[currentNombreIndex];
                                    
                                    let nombretema = temasNames[currentNombreIndex];
                                    
                                     try {
                                         const urlParams = new URLSearchParams(window.location.search);
                                        const primerCampo = urlParams.get('album');
                                        return fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=incrementarReproduccion&nombreAlbum=' + encodeURIComponent(primerCampo)+"&nombreTema="+ encodeURIComponent(nombretema));
                                    } catch (error) {
                                        console.log("Revento Afuera Servlet");
                                        // Captura el error y lo muestra en la consola o en la página
                                        return false;
                                    }
                                }

                                function loadAudio() {
                                    audioSource.src = audioFiles[currentAudioIndex];
                                    audio.load(); // Carga el nuevo archivo de audio
                                    document.getElementById('nombreTema').innerText = temasNames[currentNombreIndex];
                                    audio.play(); // Reproduce el nuevo audio
                                    playBtn.style.display = 'none';
                                    pauseBtn.style.display = 'inline';
                                    
                                    let nombretema = temasNames[currentNombreIndex];
                                    
                                     try {
                                         const urlParams = new URLSearchParams(window.location.search);
                                        const primerCampo = urlParams.get('album');
                                        return fetch('http://localhost:8080/EspotifyWeb/ConsultarAlbumServlet?action=incrementarReproduccion&nombreAlbum=' + encodeURIComponent(primerCampo)+"&nombreTema="+ encodeURIComponent(nombretema));
                                    } catch (error) {
                                        console.log("Revento Afuera Servlet");
                                        // Captura el error y lo muestra en la consola o en la página
                                        return false;
                                    }
                                }

                                function formatTime(seconds) {
                                    const minutes = Math.floor(seconds / 60);
                                    const secs = Math.floor(seconds % 60);
                                    return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
                                }