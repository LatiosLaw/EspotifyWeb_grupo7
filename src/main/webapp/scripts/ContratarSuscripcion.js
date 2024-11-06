document.getElementById('contratarSuscripcionForm').addEventListener('submit', function (event) {
            event.preventDefault(); // Evitar que el formulario se envíe de forma tradicional

            const formData = new FormData(this);
            const params = new URLSearchParams(formData).toString();

            console.log("Enviando datos:", params); // Para depuración

            fetch('http://192.168.1.146:8080/EspotifyWeb/ContratarSuscripcionServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params
            })
                    .then(response => {
                        console.log("Respuesta del servidor:", response); // Para depuración

                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("Datos recibidos:", data); // Para depuración

                        if (data.success) {
                            alert("Suscripcion contratada exitosamente.");
                            document.getElementById('resultado').innerText = "Suscripcion contratada exitosamente.";
                        } else {
                            alert("Error al contratar la suscripcion: Código de error " + data.errorCode);
                            document.getElementById('resultado').innerText = "Error al contratar la suscripcion: Codigo de error " + data.errorCode;
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("Error al contratar la suscripcion.");
                    });
            });