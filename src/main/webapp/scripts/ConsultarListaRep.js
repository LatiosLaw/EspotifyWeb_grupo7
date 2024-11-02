document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const segundoCampo = urlParams.get('listaName').split("tipo=")[1];
            const primerCampo = urlParams.get('listaName').split("tipo=")[0];
            console.log(primerCampo);
            console.log(segundoCampo);
    cargarTemas(primerCampo, segundoCampo);
    cargarInfo(primerCampo, segundoCampo);
});

function cargarTemas(listaNombre, tipo) {
    const encodedNombre = encodeURIComponent(listaNombre);
    const encodedTipo = encodeURIComponent(tipo);
    fetch(`ConsultarListaRepServlet?action=getTemasPorLista&listaNombre=${encodedNombre}&tipo=${encodedTipo}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Error: ${response.status} ${response.statusText}. Detalles: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                // Verificar suscripcion antes de llenar la tabla de temas
                checkSuscripcion(data);
            })
            .catch(error => console.error('Error al cargar temas:', error));
}

function cargarInfo(listaNombre, tipo){
    var NOMBRELISTA = document.getElementById('nombrelista');
    var CREADORGENERO = document.getElementById('creadorgenerolista');
    var IMAGENLISTA = document.getElementById('imagenlista');
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    
    
    
    
if(tipo==="1"){
    fetch('http://localhost:8080/EspotifyWeb/ConsultarListaRepServlet?action=devolverInformacionLista&listaNombre=' + encodeURIComponent(listaNombre) + '&tipo=' + encodeURIComponent(tipo))
                    .then(response => response.json())
                    .then(data => {
                        data.forEach(lista => {

                            NOMBRELISTA.value=lista.nombre;
                    CREADORGENERO.value=lista.adicional;
 
                    if((lista.imagen.toString().endsWith(".png") || lista.imagen.toString().endsWith(".jpg"))){
                       IMAGENLISTA.src="imagenes/listas/" + lista.imagen.toString(); 
                    }else{
                        IMAGENLISTA.src="imagenes/listas/defaultList.png";
                    }
                    
                    if(lista.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                       
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                       
                    }

                        });
                    })
                    .catch(error => console.error('Error al datos de la lista:', error));
}else{
    const urlParams = new URLSearchParams(window.location.search);
    const tercerCampo = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
    console.log(tercerCampo);
    fetch('http://localhost:8080/EspotifyWeb/ConsultarListaRepServlet?action=devolverInformacionLista&listaNombre=' + encodeURIComponent(listaNombre) + '&tipo=' + encodeURIComponent(tipo)+ '&usuario=' + encodeURIComponent(tercerCampo))
                    .then(response => response.json())
                    .then(data => {
                        data.forEach(lista => {
                            NOMBRELISTA.value=lista.nombre;
                    if(data.tipo==="1"){
                        // Si es un por defecto, link a todo lo de un genero
                        CREADORGENERO.value=lista.adicional;
                    }else{
                        // Si es un particular, link al perfil del creador
                        CREADORGENERO.value=lista.adicional;
                    }
                    
                    if((lista.imagen.toString().endsWith(".png") || lista.imagen.toString().endsWith(".jpg"))){
                       IMAGENLISTA.src="imagenes/listas/" + lista.imagen.toString(); 
                    }else{
                        IMAGENLISTA.src="imagenes/listas/defaultList.png";
                    }
                    
                     if(lista.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                          
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                         
                    }
                    
                        });
                    })
                    .catch(error => console.error('Error al cargar datos de la lista:', error));
}

}

function checkSuscripcion(temas) {
    fetch('LoginServlet?action=obtenerSuscripcion')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la red: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const suscripcion = data.suscrito;

                // Llenar tabla con los temas y el estado de la suscripción
                llenarTablaTemas(temas, suscripcion);
            })
            .catch(error => console.error('Error al obtener la suscripción:', error));
}

function ensureUrlProtocol(url) {
// Verifica si la URL comienza con http:// o https://
    if (!/^https?:\/\//i.test(url)) {
        // Si no tiene protocolo, agrega http://
        url = 'http://' + url;
    }
    return url;
}

function llenarTablaTemas(temas, tieneSuscripcion) {
    const tbody = document.querySelector('#tablaTemas tbody');
    tbody.innerHTML = ''; // Limpiar tabla antes de agregar nuevos resultados

     
    if (temas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">No hay temas disponibles.</td></tr>'; // Aumentar a 3 columnas
    } else {
        temas.forEach(tema => {
            const urlDescarga = ensureUrlProtocol(tema.identificador_archivo.trim());

            console.log('URL de descarga:', urlDescarga);
           // println(temas.fav);<td><button onclick="algo(this)">NoFav</button></td>
            if(tema.fav === "fav"){
               
                tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${tieneSuscripcion
                    ? `<a href="${urlDescarga}" target="_blank" class="btnsLista">Descargar</a>`
                    : `<button disabled class="btnsLista">Descargar</button>`}
                </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsLista">
                        Agregar a Lista
                    </button>
                </td>
                <td> <button onclick="sacarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}', '${tema.album}')" class="btnsLista">NoFav</button></td>
             
            </tr>`;
            }else{
                 tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${tieneSuscripcion
                    ? `<a href="${urlDescarga}" target="_blank" class="btnsLista">Descargar</a>`
                    : `<button disabled class="btnsLista">Descargar</button>`}
                </td>
                <td>
                    <button onclick="${tieneSuscripcion ? `abrirDialogo('${tema.nombre}', '${tema.album}')` : `alert('Debes tener una suscripcion vigente para agregar temas a una lista.')`}" class="btnsLista">
                        Agregar a Lista
                    </button>
                </td>
                <td> <button onclick="agregarAlgoFav('${tema.nombre}', '${"Tema"}', '${tema.album}', '${"Ninguno"}', '${document.getElementById('albumTema').value}')" class="btnsLista">Fav</button></td>
            </tr>`;
            }
            
        });
    }

    document.getElementById('tablaTemas').style.display = 'table';
}

function agregarAlgoFav(id, coso, creador, tipoLista){    
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/AgregarAlgoFavListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, creaDoorAlboom:creador, tipo:tipoLista})
    })      
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if(coso === "Lista"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                    }else{
                        recargarListas();
                    }
                    
                    alert('Se agrego a tus favoritos.');
                    
                    
                    
                } else {
                    alert('Error al agregar a tus favoritos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar agregar a tus favoritos.');
            });
    
    
}

function sacarAlgoFav(id, coso, creadorAlbum, tipoLista){
    const album = document.getElementById('albumTema').value;
    var LAIK = document.getElementById('favListaBtn');
    var NOLAIK = document.getElementById('sacarDeFavListaBtn');
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/SacarAlgoFavListaServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, coso: coso, creaDoorAlboom:creadorAlbum, tipo:tipoLista})
    })      
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if(coso === "Lista"){
                        NOLAIK.style.display = 'none';
                        LAIK.style.display = 'block';
                    }else{
                        recargarListas();
                    }
                    
                    alert('Se elimino de tus favoritos.');
                    
                    
                    
                } else {
                    alert('Error al agregar a tus favoritos: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar sacar de tus favoritos.');
            });
    
    
}


function llamarAgregarAlgoFav(){
    const urlParams = new URLSearchParams(window.location.search);
     var NOMBRELISTA = document.getElementById('nombrelista');
     var CREADORGENERO = document.getElementById('creadorgenerolista');
     const idCreador = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
     const tipo = urlParams.get('listaName').split("tipo=")[1];
     const idLista =NOMBRELISTA.value;
     // const idCreador =CREADORGENERO.value;
     
    agregarAlgoFav(idLista,"Lista",idCreador,tipo);
}
function llamarSacarAlgoFav(){
    const urlParams = new URLSearchParams(window.location.search);
     var NOMBRELISTA = document.getElementById('nombrelista');
     var CREADORGENERO = document.getElementById('creadorgenerolista');
     const idCreador = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[1];
     const tipo = urlParams.get('listaName').split("tipo=")[1];
     const idLista =NOMBRELISTA.value;
     
     
     
     
     // const idCreador =CREADORGENERO.value;
     
    sacarAlgoFav(idLista,"Lista",idCreador,tipo);
}
function recargarListas(){
     const urlParams = new URLSearchParams(window.location.search);
            const primerCampo = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[0];
            console.log(primerCampo);
            const segundoCampo = urlParams.get('listaName').split("tipo=")[1];
            console.log(segundoCampo);
    cargarTemas(primerCampo, segundoCampo);
 
}