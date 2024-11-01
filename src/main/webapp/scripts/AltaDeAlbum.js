window.onload = cargarGeneros();

let generosSeleccionados = [];

function cargarGeneros() {
    fetch('AltaDeAlbumServlet?action=cargarGeneros')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                let select = document.getElementById('generos');
                select.innerHTML = ''; // Limpia las opciones
                select.append(new Option('Seleccione un genero', ''));
                data.forEach(genero => {
                    select.append(new Option(genero, genero)); // Crear opcion con genero cargado
                });
            })
            .catch(error => console.error('Error al cargar los generos:', error));
}

function agregarGenero() {
    const select = document.getElementById('generos');
    const generoSeleccionado = select.value;

    // Comprobar si el género ya esta seleccionado
    if (generoSeleccionado && !generosSeleccionados.includes(generoSeleccionado)) {
        generosSeleccionados.push(generoSeleccionado);
        actualizarCampoGeneros();
        // Remover el genero de la lista de selecciones
        select.remove(select.selectedIndex);
    }
}

function actualizarCampoGeneros() {
    const container = document.getElementById('selectedGenerosContainer');
    container.innerHTML = '';

    generosSeleccionados.forEach(genero => {
        const div = document.createElement('div');
        div.className = 'genero-item';
        div.textContent = genero;
        div.onclick = () => removerGenero(genero);
        container.appendChild(div);
    });

    // Actualizar el campo oculto con los generos seleccionados
    document.getElementById('generosSeleccionados').value = generosSeleccionados.join(', ');
}

function removerGenero(genero) {
    // Agregar el género de vuelta al combobox 
    const select = document.getElementById('generos');
    const option = new Option(genero, genero);
    select.append(option);

    // Remover el genero de la lista de generos seleccionados
    generosSeleccionados = generosSeleccionados.filter(g => g !== genero);
    actualizarCampoGeneros();
}

function validarFormulario() {
    const nombreAlbum = document.getElementById('nombreAlbum').value;
    const anioCreacion = document.getElementById('anioCreacion').value;

    if (!nombreAlbum || !anioCreacion) {
        alert("Por favor, complete todos los campos requeridos.");
        return false;
    }

    if (generosSeleccionados.length === 0) {
        alert("Por favor, seleccione al menos un genero.");
        return false;
    }

    return true;
}

function eliminarTema(button) {
    const temaDiv = button.parentElement; // Obtén el div que contiene el tema
    temaDiv.remove(); // Elimina el tema específico
}

function agregarTemaWeb() {
    const temasContainer = document.getElementById('temasContainer');
    const temaDiv = document.createElement('div');
    temaDiv.className = 'tema';

    temaDiv.innerHTML = `
        <label for='tipoTema'>Tipo de Tema: Direccion Web</label>
        <input type="hidden" id='tipoTema[]' name='tipoTema[]' value="direccionWeb">  
        <label for='nombreTema'>Nombre del Tema:</label>
        <input type='text' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Cancion Favorita'>
        <label for='duracionTema'>Duracion (min:seg):</label>
        <input type='text' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>
        <label for='ubicacionTema'>Ubicacion en el Album:</label>
        <input type='number' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>
        <label for='dirWeb[]'>Direccion Web (URL):</label>
        <input type="url" id='dirWeb[]' name='dirWeb[]' required>
        <input type="hidden" id='archivo_MP3[]' name='archivo_MP3[]' value="mp3">
    <button type="button" onclick="eliminarTema(this)">Eliminar Tema</button>
        <br><br>
    `;

    temasContainer.appendChild(temaDiv);
}

function agregarTemaMP3() {
    const temasContainer = document.getElementById('temasContainer');
    const temaDiv = document.createElement('div');
    temaDiv.className = 'tema';

    temaDiv.innerHTML = `
        <label for='tipoTema'>Tipo de Tema: MP3</label>
        <input type="hidden" id='tipoTema[]' name='tipoTema[]' value="archivo_mp3">  
        <label for='nombreTema'>Nombre del Tema:</label>
        <input type='text' name='nombreTema[]' required title='Ingresa el nombre del tema' placeholder='Ej. Mi Cancion Favorita'>
        <label for='duracionTema'>Duracion (min:seg):</label>
        <input type='text' name='duracionTema[]' required pattern="[0-9]{1,2}:[0-9]{2}" title='Formato: mm:ss' placeholder='Ej. 03:45'>
        <label for='ubicacionTema'>Ubicacion en el Album:</label>
        <input type='number' name='ubicacionTema[]' required min='1' title='Ingresa la ubicación del tema'>
        <label for='archivo_MP3[]'>Archivo MP3:</label>
        <input type="file" id='archivo_MP3[]' name='archivo_MP3[]' accept=".mp3" required> 
        <input type="hidden" id='dirWeb[]' name='dirWeb[]' value="0">  
    <button type="button" onclick="eliminarTema(this)">Eliminar Tema</button>
        <br><br>
    `;

    temasContainer.appendChild(temaDiv);
}

var validoField = document.getElementById('Valido');
var errorMessageElement = document.getElementById("errorMessage");

function checkAlbum() {
    const album = document.getElementById("nombreAlbum").value;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "AltaDeAlbumServlet?action=verificarAlbum&albumName=" + encodeURIComponent(album), true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            errorMessageElement.style.display = "none";
            let mensaje = xhr.responseText;
            let correoValidoField = document.getElementById("albumExistsMessage");
            if (mensaje === "Album name is available") {
                correoValidoField.innerHTML = "<span style='color: green;'>" + mensaje + "</span>";
                validoField.value = "true";
            } else {
                correoValidoField.innerHTML = "<span style='color: red;'>" + mensaje + "</span>";
                validoField.value = "false";
            }
        }
    };

    xhr.send();
}

document.getElementById('generos').addEventListener('change', agregarGenero);

document.getElementById('albumForm').onsubmit = function (event) {
    event.preventDefault();

    if (!validarFormulario()) {
        return;
    }

    const formData = new FormData(this);

    fetch('AltaDeAlbumServlet', {
        method: 'POST',
        body: formData
    })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    alert(data.message);
                    this.reset(); // Resetea el formulario

                    document.getElementById('selectedGenerosContainer').innerHTML = '';
                    document.getElementById('generosSeleccionados').value = '';

                    const temasContainer = document.getElementById('temasContainer');
                    while (temasContainer.firstChild) {
                        temasContainer.removeChild(temasContainer.firstChild);
                    }
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Ocurrio un error en el registro.");
            });
};
