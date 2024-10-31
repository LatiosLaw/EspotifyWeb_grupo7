function validarFormulario() {
    const nombreLista = document.getElementById('nombreLista').value;
    const nombreTema = document.getElementById('nombreTema').value;

    if (!nombreLista || !nombreTema) {
        alert("Por favor, complete todos los campos requeridos.");
        return false;
    }

    enviarDatos(nombreLista, nombreTema);
    return false;
}

function enviarDatos(nombreLista, nombreTema) {
    const albumTema = document.getElementById('albumTema').value;

    const data = {
        nombreLista: nombreLista,
        nombreTema: nombreTema,
        albumTema: albumTema
    };

    console.log("Nombre Lista:", nombreLista);
    console.log("Nombre Tema:", nombreTema);
    console.log("Album Tema:", albumTema);

    fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data) // Convertir el objeto a JSON
    })
            .then(response => {
                if (response.ok) {
                    return response.json(); // Procesar la respuesta JSON
                }
                throw new Error('Error en la red');
            })
            .then(data => {
                console.log('Éxito:', data);
                cerrarDialogo(); // Cerrar el dialogo despues de enviar
                loadListas();
            })
            .catch(error => console.error('Error al enviar los datos:', error));
}

window.onload = loadListas;

function loadListas() {
    fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListas')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const row = `<tr><td>${lista.nombre}</td><td><button onclick="seleccionarLista(this)" class="btnSelect">Seleccionar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}

var campoLista = document.getElementById('nombreLista');
var campoTema = document.getElementById('nombreTema');
var albumTema = document.getElementById('albumTema');

function seleccionarLista(boton) {
    const fila = boton.parentElement.parentElement;
    const primerCampo = fila.querySelector('td').innerText;
    campoLista.value = primerCampo;
}

function seleccionarTema(boton) {
    const fila = boton.parentElement.parentElement;
    const primerCampo = fila.querySelector('td').innerText;
    const segundoCampo = fila.querySelector('td:nth-child(2)').innerText;

    campoTema.value = primerCampo;
    albumTema.value = segundoCampo;
}