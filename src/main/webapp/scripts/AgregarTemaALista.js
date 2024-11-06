function validarFormulario() {
    const nombreLista = document.getElementById('nombreLista').value;
    const nombreTema = document.getElementById('nombreTema').value;

    // Verificar si los campos están vacíos
    if (!nombreLista || !nombreTema) {
        alert("Por favor, complete todos los campos requeridos.");
        return false; // Detener el envío si hay campos vacíos
    }

    // Si la validación es exitosa, llamar a enviarDatos
    enviarDatos(nombreLista, nombreTema);
    return false; // Evitar el envío del formulario por defecto
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

    fetch('http://192.168.1.146:8080/EspotifyWeb/AgregarTemaAListaServlet', {
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
        if (data.status === "success") {
            alert(data.message); 
            cerrarDialogo(); 
            loadListas(); 
            document.getElementById('nombreLista').value = ''; // Limpiar campo nombreLista
            document.getElementById('nombreTema').value = ''; // Limpiar campo nombreTema
            document.getElementById('albumTema').value = ''; // Limpiar campo albumTema
        } else {
            alert("Error: " + data.message);
        }
    })
    .catch(error => {
        console.error('Error al enviar los datos:', error);
        alert("Ocurrió un error al enviar los datos.");
    });
}

window.onload = loadListas;

function loadListas() {
    fetch('http://192.168.1.146:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListas')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('listasBody');
            tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
            data.forEach(lista => {
                const row = `<tr><td>${lista.nombre}</td><td class="tdSelect"><button onclick="seleccionarLista(this)" class="btnSelect">Seleccionar</button></td></tr>`;
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
