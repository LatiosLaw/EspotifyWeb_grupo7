function PublicarLista(boton) {
    const fila = boton.parentElement.parentElement;
    const primerCampo = fila.querySelector('td').innerText;

    console.log("Valor de primerCampo:", primerCampo); // Agrega este log para verificar

    const params2 = new URLSearchParams({ primerCampo }).toString();

    fetch('PublicarListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params2
    })
    .then(response => response.json())
    .then(data => {
        const message = data.success ? "Lista publicada exitosamente." : "Error al publicar lista: " + data.errorCode;
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
}


// Cargar la lista de usuarios al entrar a la página
window.onload = loadListas;

function loadListas() {
    fetch('PublicarListaServlet')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                data.forEach(lista => {
                    const row = `<tr><td>${lista.nombre}</td><td>${lista.visibilidad ? 'Publica' : 'Privada'}</td><td><button onclick="PublicarLista(this)">Publicar</button></td></tr>`;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));
}