const isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent);

document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const segundoCampo = urlParams.get('listaName').split("tipo=")[1];
            const primerCampo = urlParams.get('listaName').split("tipo=")[0];
            console.log(primerCampo);
            console.log(segundoCampo);
    cargarTemas(primerCampo, segundoCampo);
    cargarInfo(primerCampo, segundoCampo);
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

function DescargarTema(boton) {
    const fila = boton.parentElement.parentElement;
    const descarga = fila.querySelector('td:nth-child(4)').innerText;
    const nombrealbum = fila.querySelector('td:nth-child(3)').innerText;
    const nombretema = fila.querySelector('td:nth-child(1)').innerText;
    window.location.href = 'ConsultarAlbumServlet?action=Download&filename=' + encodeURIComponent(descarga) + '&nombreTema='+ encodeURIComponent(nombretema)+ '&nombreAlbum='+ encodeURIComponent(nombrealbum);
}

function cargarTemas(listaNombre, tipo) {
    const encodedNombre = encodeURIComponent(listaNombre);
    const encodedTipo = encodeURIComponent(tipo);
    fetch(`ConsultarListaRepServlet?action=getTemasPorLista&listaNombre=${encodedNombre}&tipo=${encodedTipo}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Error: ${response.status} ${response.statusText}. Detalles: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                // Verificar suscripcion antes de llenar la tabla de temas
                checkSuscripcion(data);
            })
            .catch(error => console.error('Error al cargar temas:', error));
}

function cargarInfo(listaNombre, tipo) {
    var NOMBRELISTA = document.getElementById('nombrelista');
    var CREADORGENERO = document.getElementById('creadorgenerolista');
    var IMAGENLISTA = document.getElementById('imagenlista');
    if (!isMobile) {
        var IMAGENREPRO = document.getElementById('imagenReproductor');
    }
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');

    if (tipo === "1") {
        fetch('ConsultarListaRepServlet?action=devolverInformacionLista&listaNombre=' + encodeURIComponent(listaNombre) + '&tipo=' + encodeURIComponent(tipo))
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.length > 0) {
                    const lista = data[0];
                    NOMBRELISTA.value = lista.nombre;
                    CREADORGENERO.value = lista.adicional;

                    if (!isMobile) {
                        if (lista.imagen && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))) {
                            IMAGENLISTA.src = "imagenes/listas/" + lista.imagen;
                            IMAGENREPRO.src = "imagenes/listas/" + lista.imagen;
                        } else {
                            IMAGENLISTA.src = "imagenes/listas/defaultList.png";
                            IMAGENREPRO.src = "imagenes/listas/defaultList.png";
                        }
                    }

                    if (lista.fav === "fav") {
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                    } else {
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                    }
                } else {
                    console.error("No se encontraron datos válidos");
                }
            })
            .catch(error => console.error('Error al cargar datos de la lista:', error));
    } else {
        const urlParams = new URLSearchParams(window.location.search);
        const tercerCampo = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
        console.log(tercerCampo);
        fetch('ConsultarListaRepServlet?action=devolverInformacionLista&listaNombre=' + encodeURIComponent(listaNombre) + '&tipo=' + encodeURIComponent(tipo) + '&usuario=' + encodeURIComponent(tercerCampo))
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.length > 0) {
                    const lista = data[0];
                    NOMBRELISTA.value = lista.nombre;
                    CREADORGENERO.value = lista.adicional;

                    if (lista.imagen && (lista.imagen.endsWith(".png") || lista.imagen.endsWith(".jpg"))) {
                        IMAGENLISTA.src = "imagenes/listas/" + lista.imagen;
                        IMAGENREPRO.src = "imagenes/listas/" + lista.imagen;
                    } else {
                        IMAGENLISTA.src = "imagenes/listas/defaultList.png";
                        IMAGENREPRO.src = "imagenes/listas/defaultList.png";
                    }

                    if (lista.fav === "fav") {
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                    } else {
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                    }
                } else {
                    console.error("No se encontraron datos válidos");
                }
            })
            .catch(error => console.error('Error al cargar datos de la lista:', error));
    }
}


function checkSuscripcion(temas) {
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
                llenarTablaTemas(temas, suscripcion);
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));
}

function ensureUrlProtocol(url) {
// Verifica si la URL comienza con http:// o https://
    if (!/^https?:\/\//i.test(url)) {
        // Si no tiene protocolo, agrega http://
        url = 'http://' + url;
    }
    return url;
}

function llenarTablaTemas(temas, tieneSuscripcion) {
    const tbody = document.querySelector('#tablaTemas tbody');
    tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

     let registros = [];
    if (temas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">No hay temas disponibles.</td></tr>'; // Aumentar a 3 columnas
    } else {
        temas.forEach((tema, index) => {
            
            fetch('ConsultarListaRepServlet?action=devolverInformacionTema&nombreAlbum=' + encodeURIComponent(tema.album)+"&nombreTema="+ encodeURIComponent(tema.nombre))
                .then(response => response.json())
                .then(data => {
                       registros[index] = data;
                });
                
            const urlDescarga = ensureUrlProtocol(tema.identificador_archivo.trim());

            console.log('URL de descarga:', urlDescarga);
           // println(temas.fav);<td><button onclick="algo(this)">NoFav</button></td>
            if(tema.fav === "fav"){
               
                tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${formatearTiempo(tema.duracion)}</td>
                <td>${tema.album}</td>
                <td>${tema.identificador_archivo}</td>
                <td>
                        <button class="btn-ver-mas" data-index="${index}">...</button>
                    </td>
                <td>
                <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                : `alert('Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsLista">
                Descargar
                </button>
                </td>
                
                ${isMobile ? '' : `<td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsLista">
                        Agregar a Lista
                    </button>
                </td>`}

                 <td>
                    <button onclick="${tieneSuscripcion ? `sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para eliminar temas de tus favoritos.')`}" class="btnsLista">
                        NoFav
                    </button>
                </td>
                
             
             
             
             
            </tr>`;
            }else{
                 tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${formatearTiempo(tema.duracion)}</td>
                <td>${tema.album}</td>
                <td>${tema.identificador_archivo}</td>
                <td>
                        <button class="btn-ver-mas" data-index="${index}">...</button>
                    </td>
                <td>
                <button onclick="${tieneSuscripcion ? `DescargarTema(this)` 
                : `alert('Debes tener una suscripcion vigente para descargar temas.')`}" class="btnsLista">
                Descargar
                </button>
                </td>
                ${isMobile ? '' : `<td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsLista">
                        Agregar a Lista
                    </button>
                </td>`}
                
                 <td>
                    <button onclick="${tieneSuscripcion ? `agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}', '${"Ninguno"}', '${document.getElementById('albumTema').value}')` : `alert('Debes tener una suscripcion vigente para agregar temas a tus favoritos.')`}" class="btnsLista">
                        Fav
                    </button>
                </td>
                



             </tr>`;
            }
            
            document.querySelectorAll('.btn-ver-mas').forEach(button => {
            button.addEventListener('click', (event) => {
                const index = event.target.getAttribute('data-index');
                const infotem = registros[index];
                mostrarInformacionAdicional(infotem);
            });
        });
            
        });
    }

    document.getElementById('tablaTemas').style.display = 'table';
}


function mostrarInformacionAdicional(infotem) {
    const dialog = document.getElementById('detalleDialog');
    document.getElementById('dialogTitulo').textContent = infotem.identificador.nombre_tema;
    document.getElementById('dialogAlbum').textContent = infotem.identificador.nombre_album;
    document.getElementById('dialogReproducciones').textContent = infotem.reproducciones;
    document.getElementById('dialogDescargas').textContent = infotem.descargas;
    document.getElementById('dialogFavoritos').textContent = infotem.favoritos;
    document.getElementById('dialogListas').textContent = infotem.agregado_a_lista;

    // Mostrar el diálogo
    dialog.showModal();

    // Cerrar el diálogo cuando se haga clic en el botón "Cerrar"
    document.getElementById('cerrarDialog').addEventListener('click', () => {
        dialog.close();
    });
}

function agregarAlgoFav(id, coso, creador, tipoLista){    
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    event.preventDefault();
    fetch('AgregarAlgoFavListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, creaDoorAlboom:creador, tipo:tipoLista})
    })      
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if(coso === "Lista"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                    }else{
                        recargarListas();
                    }
                    
                    alert('Se agrego a tus favoritos.');
                    
                    
                    
                } else {
                    alert('Error al agregar a tus favoritos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar agregar a tus favoritos.');
            });
    
    
}

function sacarAlgoFav(id, coso, creadorAlbum, tipoLista){
    const album = document.getElementById('albumTema').value;
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    event.preventDefault();
    fetch('SacarAlgoFavListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, creaDoorAlboom:creadorAlbum, tipo:tipoLista})
    })      
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if(coso === "Lista"){
                        NOLAIK.style.display = 'none';
                        LAIK.style.display = 'block';
                    }else{
                        recargarListas();
                    }
                    
                    alert('Se elimino de tus favoritos.');
                    
                    
                    
                } else {
                    alert('Error al agregar a tus favoritos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar sacar de tus favoritos.');
            });
    
    
}


function llamarAgregarAlgoFav(){
    const urlParams = new URLSearchParams(window.location.search);
     var NOMBRELISTA = document.getElementById('nombrelista');
     var CREADORGENERO = document.getElementById('creadorgenerolista');
     const idCreador = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
     const tipo = urlParams.get('listaName').split("tipo=")[1];
     const idLista =NOMBRELISTA.value;
     // const idCreador =CREADORGENERO.value;
     
    agregarAlgoFav(idLista,"Lista",idCreador,tipo);
}
function llamarSacarAlgoFav(){
    const urlParams = new URLSearchParams(window.location.search);
     var NOMBRELISTA = document.getElementById('nombrelista');
     var CREADORGENERO = document.getElementById('creadorgenerolista');
     const idCreador = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
     const tipo = urlParams.get('listaName').split("tipo=")[1];
     const idLista =NOMBRELISTA.value;
     
     
     
     
     // const idCreador =CREADORGENERO.value;
     
    sacarAlgoFav(idLista,"Lista",idCreador,tipo);
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
            const tema = celdas[3].innerText; // La celda de la columna "Archivo / Link" está en el índice 2
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
        
        function obtenerNombresAlbumes(){
            const temasBody = document.getElementById("temasBody"); // Selecciona el cuerpo de la tabla
    const filas = temasBody.getElementsByTagName("tr"); // Obtiene todas las filas en el cuerpo de la tabla
    const temas = []; // Array para almacenar los valores de la columna
    //temas.push('temas/DONMAI.mp3');
    // Iterar sobre cada fila para obtener el valor de la columna "Archivo / Link"
    for (let i = 0; i < filas.length; i++) {
        const celdas = filas[i].getElementsByTagName("td"); // Obtiene todas las celdas de la fila
        if (celdas.length > 0) {
            const tema = celdas[2].innerText; // La celda de la columna "Archivo / Link" está en el índice 2
            temas.push((tema)); // Agrega el archivo al array
        }
    }
console.log(temas);
    return temas;
        }
        
function recargarListas(){
     const urlParams = new URLSearchParams(window.location.search);
            const primerCampo = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[0];
            console.log(primerCampo);
            const segundoCampo = urlParams.get('listaName').split("tipo=")[1];
            console.log(segundoCampo);
    cargarTemas(primerCampo, segundoCampo);
 
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
                                let albumsNames;
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
                                    albumsNames = obtenerNombresAlbumes();
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
                                        let primerCampo = albumsNames[currentNombreIndex].toString();
                                        return fetch('ConsultarAlbumServlet?action=incrementarReproduccion&nombreAlbum=' + encodeURIComponent(primerCampo)+"&nombreTema="+ encodeURIComponent(nombretema));
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
                                        let primerCampo = albumsNames[currentNombreIndex].toString();
                                        return fetch('ConsultarAlbumServlet?action=incrementarReproduccion&nombreAlbum=' + encodeURIComponent(primerCampo)+"&nombreTema="+ encodeURIComponent(nombretema));
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