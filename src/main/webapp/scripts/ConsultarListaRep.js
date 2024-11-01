document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
            const primerCampo = urlParams.get('listaName').split("tipo=")[0].split("&#8206;-")[0].split("/")[0];
            console.log(primerCampo);
            const segundoCampo = urlParams.get('listaName').split("tipo=")[1];
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
                    if(lista.imagen!=="" && lista.imagen!==null && (lista.imagen==="png" || lista.imagen==="jpg")){
                       IMAGENLISTA.src="imagenes/listas/" + lista.imagen; 
                    }else{
                        IMAGENLISTA.src="imagenes/listas/defaultList.png";
                    }
                    
                    if(lista.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                          NOMBRELISTA.value="fav";
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                         NOMBRELISTA.value="nofav";
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
                    
                    if(lista.imagen!=="" && lista.imagen!==null && (lista.imagen==="png" || lista.imagen==="jpg")){
                       IMAGENLISTA.src="imagenes/listas/" + lista.imagen; 
                    }else{
                        IMAGENLISTA.src="imagenes/listas/defaultList.png";
                    }
                    
                     if(lista.fav === "fav"){
                        NOLAIK.style.display = 'block';
                        LAIK.style.display = 'none';
                          //NOMBRELISTA.value="fav";
                    }else{
                        LAIK.style.display = 'block';
                        NOLAIK.style.display = 'none';
                         //NOMBRELISTA.value="nofav"; 
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
     //var NOMBRELISTA = document.getElementById('nombrelista');
     
     
     
     
    if (temas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3">No hay temas disponibles.</td></tr>'; // Aumentar a 3 columnas
    } else {
        temas.forEach(tema => {
            const urlDescarga = ensureUrlProtocol(tema.identificador_archivo.trim());

            console.log('URL de descarga:', urlDescarga);
           // println(temas.fav);
            if(tema.fav === "fav"){
               
                tbody.innerHTML += `
                <tr>
                <td>${tema.nombre}</td>
                <td>${tieneSuscripcion
                    ? `<a href="${urlDescarga}" target="_blank">Descargar</a>`
                    : `<button disabled>Descargar</button>`}
                </td>
                <td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')">Agregar a Lista</button></td>
                <td><button onclick="algo(this)">NoFav</button></td>
            </tr>`;
            }else{
                 tbody.innerHTML += `
            <tr>
                <td>${tema.nombre}</td>
                <td>${tieneSuscripcion
                    ? `<a href="${urlDescarga}" target="_blank">Descargar</a>`
                    : `<button disabled>Descargar</button>`}
                </td>
                <td><button onclick="abrirDialogo('${tema.nombre}', '${tema.album}')">Agregar a Lista</button></td>
                <td><button onclick="algo(this)">Fav</button></td>
            </tr>`;
            }
            
        });
    }

    document.getElementById('tablaTemas').style.display = 'table';
}