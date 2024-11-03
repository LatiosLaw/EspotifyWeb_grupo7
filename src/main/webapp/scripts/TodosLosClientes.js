fetch('http://localhost:8080/EspotifyWeb/TodosLosClientesServlet?action=devolverClientes')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('clientesBody');
            container.innerHTML = ''; // Limpiar el contenedor antes de cargar nuevas listas
            data.forEach(cliente => {
                const imagen = cliente.imagen !== "null" && (cliente.imagen.endsWith(".png") || cliente.imagen.endsWith(".jpg"))
                        ? `imagenes/usuarios/${cliente.imagen}`
                        : 'imagenes/usuarios/defaultUser.png';

                const esArtista = sessionUserType === "Artista";

                const clienteDiv = `
                <div class="cliente">
                    ${esArtista ? '' : `<a href="ConsultarUsuario.jsp?usr=${encodeURIComponent(cliente.nombre)}">`}
                        <img src="${imagen}" class="imagenUser" alt="Imagen del Usuario">
                        <div>
                            <p>${cliente.nombre}</p>
                        </div>
                    ${esArtista ? '' : '</a>'}
                </div>`;

                container.innerHTML += clienteDiv;
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));