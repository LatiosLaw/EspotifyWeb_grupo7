document.addEventListener("DOMContentLoaded", () => {
    fetch("RecomendacionesServlet?action=devolverTemazos")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error HTTP: ${response.status}`);
                }
                return response.json(); // Convertir la respuesta a JSON
            })
            .then(data => {
                const container = document.getElementById('listaTopTemasBody');
                container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas filas

                let valor = 1;
                data.forEach((tema, index) => {
                    const row = `
                    <tr>
                        <td>${valor}</td>
                        <td>${tema.identificador.nombreTema}</td>
                        <td><a href="ConsultarAlbum.jsp?album=${encodeURIComponent(tema.identificador.nombreAlbumTema)}">${tema.identificador.nombreAlbumTema}</a></td>
                        <td>${tema.repro}</td>
                        <td>
                            <button class="btn-ver-mas" data-index="${index}">...</button>
                        </td>
                    </tr>`;
                    container.innerHTML += row;
                    valor++;
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
        document.getElementById('dialogTitulo').textContent = tema.identificador.nombreTema;
        document.getElementById('dialogAlbum').textContent = tema.identificador.nombreAlbumTema;
        document.getElementById('dialogReproducciones').textContent = tema.repro;
        document.getElementById('dialogDescargas').textContent = tema.descarga;
        document.getElementById('dialogFavoritos').textContent = tema.favoritos;
        document.getElementById('dialogListas').textContent = tema.listas;

        // Mostrar el diálogo
        dialog.showModal();

        // Cerrar el diálogo cuando se haga clic en el botón "Cerrar"
        document.getElementById('cerrarDialog').addEventListener('click', () => {
            dialog.close();
        });
    }
});
