fetch('http://localhost:8080/EspotifyWeb/TodosLosAlbumesServlet?action=devolverAlbumes')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaAlbumesBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas

                data.forEach(album => {
                    // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                    // Crear una fila con la imagen aleatoria y el nombre del género
                    if(album.imagen!=="" && album.imagen!==null && (album.imagen==="png" || album.imagen==="jpg")){
                    const row = `
                        <tr>
                            <td><img src="imagenes/albumes/${album.imagen}" alt="Imagen de album" class="imagenAlbum" style="width:50px; height:50px;"></td>
                            <td>${album.nombre}</td>
                            <td><a href="ConsultarUsuario.jsp?usr=${album.creador}">${album.creador}</a></td>
                        </tr>`;
            
            tbody.innerHTML += row;
                    }else{
                      const row = `
                        <tr>
                            <td><img src="imagenes/albumes/defaultAlbum.png" alt="Imagen de album" class="imagenAlbum" style="width:50px; height:50px;"></td>
                            <td>${album.nombre}</td>
                            <td><a href="ConsultarUsuario.jsp?usr=${album.creador}">${album.creador}</a></td>
                        </tr>`;  
            
            tbody.innerHTML += row;
                    }

                    
                });
            })
            .catch(error => console.error('Error al cargar albumes:', error));