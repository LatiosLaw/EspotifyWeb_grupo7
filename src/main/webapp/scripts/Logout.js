document.addEventListener("DOMContentLoaded", () => {
    const logoutButton = document.getElementById('logoutButton');
    const resultado = document.getElementById('resultado');

    // Manejo del botón de cerrar sesión
    if (logoutButton) {
        logoutButton.addEventListener('click', function () {
            fetch('http://localhost:8080/EspotifyWeb/CerrarSesionServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const message = data.success ? data.message : "Error al cerrar sesion.";
                if (resultado) {
                    resultado.innerText = message;
                }
                alert(message);
            })
            .catch(error => {
                console.error('Error:', error);
                const errorMessage = "Error al intentar cerrar sesion.";
                if (resultado) {
                    resultado.innerText = errorMessage;
                }
                alert(errorMessage);
            });

            setTimeout(() => location.href = 'index.jsp', 1000);
        });
    }
});