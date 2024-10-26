function validarFormulario() {
const nombreLista = document.getElementById('nombreLista').value;
const nombreTema = document.getElementById('nombreTema').value;

if (!nombreLista || !nombreTema) {
    alert("Por favor, complete todos los campos requeridos.");
    return false;
}

return true;
}

window.onload = loadListas;

 function loadListas() {
    fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListas')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const row = `<tr><td>${lista.nombre}</td><td><button onclick="seleccionarLista(this)">Seleccionar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

document.getElementById('opciones').addEventListener('change', function() {
const selectedValue = this.value;

// Ejecutar diferentes funciones dependiendo de la opción seleccionada
if (selectedValue === 'default') {
    manejarListaPorDefecto();
} else if (selectedValue === 'publica') {
    manejarListaPublica();
} else if (selectedValue === 'album') {
    manejarAlbum();
}
});

function manejarListaPorDefecto() {
   fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListasDefault')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('filtroBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const row = `<tr><td>${lista.nombre}</td><td>${lista.genero}</td><td><button onclick="buscarTemasListaDefecto(this)">Buscar Temas</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
    // Lógica para manejar la opción 'Lista por defecto'
}

function manejarListaPublica() {
     fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListasParticular')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('filtroBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const row = `<tr><td>${lista.nombre}</td><td>${lista.nombrecreador}</td><td><button onclick="buscarTemasListaParticular(this)">Buscar Temas</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
    // Lógica para manejar la opción 'Lista particular pública'
}

function manejarAlbum() {
    fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarAlbumes')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('filtroBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(album => {
                    const row = `<tr><td>${album.nombre}</td><td>${album.nombrecreador}</td><td><button onclick="buscarTemasAlbum(this)">Buscar Temas</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
    // Lógica para manejar la opción 'Álbum'
}


function buscarTemasListaParticular(boton) {
// Obtener la fila del botón
const fila = boton.parentElement.parentElement;  
// Obtener el texto del primer 'td' (primer campo de la fila)
const primerCampo = fila.querySelector('td').innerText;

fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=buscarTemasListaParticular&listaName=' + encodeURIComponent(primerCampo))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('temasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(tema => {
                    const row = `<tr><td>${tema.nombre}</td><td>${tema.album}</td><td><button onclick="seleccionarTema(this)">Seleccionar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

function buscarTemasListaDefecto(boton) {
// Obtener la fila del botón
const fila = boton.parentElement.parentElement;  
// Obtener el texto del primer 'td' (primer campo de la fila)
const primerCampo = fila.querySelector('td').innerText;
fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=buscarTemasListaDefecto&listaName=' + encodeURIComponent(primerCampo))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('temasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(tema => {
                    const row = `<tr><td>${tema.nombre}</td><td>${tema.album}</td><td><button onclick="seleccionarTema(this)">Seleccionar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

function buscarTemasAlbum(boton) {
// Obtener la fila del botón
const fila = boton.parentElement.parentElement;  
// Obtener el texto del primer 'td' (primer campo de la fila)
const primerCampo = fila.querySelector('td').innerText;
fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=buscarTemasAlbum&albumName=' + encodeURIComponent(primerCampo))
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('temasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(tema => {
                    const row = `<tr><td>${tema.nombre}</td><td>${tema.album}</td><td><button onclick="seleccionarTema(this)">Seleccionar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

var campoLista = document.getElementById('nombreLista');
var campoTema = document.getElementById('nombreTema');
var albumTema = document.getElementById('albumTema');

function seleccionarLista(boton) {
// Obtener la fila del botón
const fila = boton.parentElement.parentElement;  
// Obtener el texto del primer 'td' (primer campo de la fila)
const primerCampo = fila.querySelector('td').innerText;
campoLista.value=primerCampo;
// Usar el API de clipboard para copiar el texto al portapapeles
}

function seleccionarTema(boton) {
// Obtener la fila del botón
const fila = boton.parentElement.parentElement;  

// Obtener el texto del primer 'td' (primer campo de la fila)
const primerCampo = fila.querySelector('td').innerText;

// Obtener el texto del segundo 'td' (segundo campo de la fila)
const segundoCampo = fila.querySelector('td:nth-child(2)').innerText;

// Asignar el texto del primer campo a campoTema
campoTema.value = primerCampo;
albumTema.value = segundoCampo;

// Usar el API de clipboard para copiar el texto al portapapeles
}