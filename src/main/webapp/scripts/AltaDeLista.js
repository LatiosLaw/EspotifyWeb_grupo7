function validarFormulario() {
    const nombreLista = document.getElementById('nombreAlbum').value;

    if (!nombreLista) {
        alert("Por favor, complete todos los campos requeridos.");
        return false;
    }

    return true;
    }

    var liNameInput = document.getElementById('nombreLista');
    var listaExistsMessage = document.getElementById('ListaExistsMessage');
    var validoField = document.getElementById('Valido');
    var errorMessageElement = document.getElementById("errorMessage");

    liNameInput.addEventListener('input', function() {
    var listaName = liNameInput.value;

    if (listaName.length > 0) {
        // Utiliza fetch para hacer una solicitud GET al servidor
        fetch('AltaDeListaServlet?listaName=' + encodeURIComponent(listaName))
            .then(response => response.text())
            .then(data => {
                errorMessageElement.style.display = "none";
                if (data === 'exists') {
                    validoField.value = "false";
                    listaExistsMessage.textContent = 'Esta lista ya existe en tu biblioteca.';
                } else {
                    validoField.value = "true";
                    listaExistsMessage.textContent = '';
                }
            })
            .catch(error => console.error('Error al verificar la lista:', error));
    } else {
        listaExistsMessage.textContent = '';
    }
});