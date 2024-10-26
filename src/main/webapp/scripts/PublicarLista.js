document.getElementById('buscarListasBtn').addEventListener('click', function () {
fetch('http://localhost:8080/EspotifyWeb/PublicarListaServlet')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const tbody = document.getElementById('listasBody');
            tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas

            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="2">No se encontraron listas.</td></tr>';
                return;
            }

            data.forEach(lista => {
                const row = `<tr><td>${lista.nombre}</td><td>${lista.visibilidad ? 'Pública' : 'Privada'}</td></tr>`;
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error al cargar listas:', error));
});

document.getElementById('publicarListaForm').addEventListener('submit', function (event) {
event.preventDefault();

const formData = new FormData(this);
const params = new URLSearchParams(formData).toString();

fetch('http://localhost:8080/EspotifyWeb/PublicarListaServlet', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: params
})
        .then(response => response.json())
        .then(data => {
            const message = data.success ? "Lista publicada exitosamente." : "Error al publicar lista." + data.errorCode;
            document.getElementById('resultado').innerText = message;
            alert(message); // Mostrar un popup con el resultado
            loadListas(); // Cargar listas después de haber hecho pública una
        })
        .catch(error => {
            console.error('Error:', error);
            const errorMessage = "Error al publicar lista.";
            document.getElementById('resultado').innerText = errorMessage;
            alert(errorMessage);
        });
});

// Cargar la lista de usuarios al entrar a la página
window.onload = loadListas;

function loadListas() {
fetch('http://localhost:8080/EspotifyWeb/PublicarListaServlet')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('listasBody');
            tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
            data.forEach(lista => {
                const row = `<tr><td>${lista.nombre}</td><td>${lista.visibilidad ? 'Pública' : 'Privada'}</td></tr>`;
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error al cargar listas:', error));
}