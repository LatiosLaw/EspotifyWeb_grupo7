function validarFormulario() {
    const nombreLista = document.getElementById('nombreLista').value;

    if (!nombreLista) {
        alert("Por favor, complete todos los campos requeridos.");
        return false;
    }

    return true;
}

function submitForm(event) {
    event.preventDefault(); // Evita que el formulario se envíe por defecto

    // Validar el formulario antes de enviar
    if (!validarFormulario()) {
        return; // Si la validación falla, no continuar
    }

    const formData = new FormData(document.getElementById('ListaForm'));

    fetch('AltaDeListaServlet', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "success") {
            alert(data.message); 
            document.getElementById('nombreLista').value = '';
        } else {
            alert("Error: " + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Ocurrió un error en el registro.");
    });
}

document.getElementById('ListaForm').addEventListener('submit', submitForm);
