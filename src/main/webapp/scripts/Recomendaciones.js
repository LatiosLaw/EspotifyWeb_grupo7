document.addEventListener("DOMContentLoaded", () => {
fetch("http://localhost:8080/EspotifyWeb/RecomendacionesServlet?action=devolverTemazos")
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        return response.json(); // Convertir la respuesta a JSON
    })
    .then(data => {
        const container = document.getElementById('listaTopTemasBody');
        container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas filas

        // Iterar sobre los temas y generar filas de tabla
        valor=1;
        data.forEach((tema, index) => {
            const row = `
                <tr>
                    <td>${valor}</td>
                    <td>${tema.identificador.nombre_tema}</td>
                    <td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(tema.identificador.nombre_album)}">${tema.identificador.nombre_album}</a></td>
                    <td>${tema.reproducciones}</td>
                    <td>
                        <button class="btn-ver-mas" data-index="${index}">...</button>
                    </td>
                </tr>`;
            container.innerHTML += row;
            valor = valor +1;
        });

        // Asignar eventos a los botones "ver más"
        document.querySelectorAll('.btn-ver-mas').forEach(button => {
            button.addEventListener('click', (event) => {
                const index = event.target.getAttribute('data-index');
                const tema = data[index];
                mostrarInformacionAdicional(tema);
            });
        });
    })
    .catch(error => console.error("Error al cargar los temas:", error));
    
    // Función para mostrar información adicional
function mostrarInformacionAdicional(tema) {
    const dialog = document.getElementById('detalleDialog');
    document.getElementById('dialogTitulo').textContent = tema.identificador.nombre_tema;
    document.getElementById('dialogAlbum').textContent = tema.identificador.nombre_album;
    document.getElementById('dialogReproducciones').textContent = tema.reproducciones;
    document.getElementById('dialogDescargas').textContent = tema.descargas;
    document.getElementById('dialogFavoritos').textContent = tema.favoritos;
    document.getElementById('dialogListas').textContent = tema.agregado_a_lista;

    // Mostrar el diálogo
    dialog.showModal();

    // Cerrar el diálogo cuando se haga clic en el botón "Cerrar"
    document.getElementById('cerrarDialog').addEventListener('click', () => {
        dialog.close();
    });
}

});

