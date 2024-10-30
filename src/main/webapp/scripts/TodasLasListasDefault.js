fetch('http://localhost:8080/EspotifyWeb/TodasLasListasDefaultServlet?action=devolverListas')
            .then(response => response.json())
            .then(data => {
                const tbody = document.getElementById('listaListasBody');
                tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas

                data.forEach(lista => {
                    // Seleccionar una imagen aleatoria de la lista obtenida del servlet
                    // Crear una fila con la imagen aleatoria y el nombre del género
                    if(lista.imagen!=="" && lista.imagen!==null && (lista.imagen==="png" || lista.imagen==="jpg")){
                    const row = `
                        <tr>
                            <td><img src="imagenes/listas/${lista.imagen}" alt="Imagen de lista" class="imagenLista" style="width:50px; height:50px;"></td>
                            <td>${lista.nombre}</td>
                            <td>${lista.genero}</td>
                        </tr>`;
            
            tbody.innerHTML += row;
                    }else{
                      const row = `
                        <tr>
                            <td><img src="imagenes/listas/defaultList.png" alt="Imagen de lista" class="imagenLista" style="width:50px; height:50px;"></td>
                            <td>${lista.nombre}</td>
                            <td>${lista.genero}</td>
                        </tr>`;  
            
            tbody.innerHTML += row;
                    }

                    
                });
            })
            .catch(error => console.error('Error al cargar listas:', error));