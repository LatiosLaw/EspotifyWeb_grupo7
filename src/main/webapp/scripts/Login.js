document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById('loginForm');
    const resultado = document.getElementById('resultado');

    // Manejo del formulario de inicio de sesión
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const formData = new FormData(this);
            const params = new URLSearchParams(formData).toString();

            fetch('http://192.168.1.146:8080/EspotifyWeb/LoginServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const message = data.success ? "Visitante logeado exitosamente." : "Error al intentar logearse: " + data.errorCode;
                if (resultado) {
                    resultado.innerText = message;
                }
                alert(message);
                if (message === "Visitante logeado exitosamente.") {
                    setTimeout(() => location.reload(), 1000);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                const errorMessage = "Error al intentar logearse.";
                if (resultado) {
                    resultado.innerText = errorMessage;
                }
                alert(errorMessage);
            });
        });
    }
});