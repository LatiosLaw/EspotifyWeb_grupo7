    fetch(`http://192.168.1.146:8080/EspotifyWeb/ImagenDeUsuarioServlet?action=cargarImagenUser`)
        .then(response => response.json())
        .then(data => {
            
            const usuario = data[0];
    
            if(usuario.imagen!=="null" && (usuario.imagen.endsWith(".png") || usuario.imagen.endsWith(".jpg"))){
                document.getElementById('imagenUser').src = `imagenes/usuarios/${usuario.imagen}`;
            }else{
                document.getElementById('imagenUser').src = 'imagenes/usuarios/defaultUser.png';
            }
        })
        .catch(error => console.error('Error al cargar imagen:', error));
