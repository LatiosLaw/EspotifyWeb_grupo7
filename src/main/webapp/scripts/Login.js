document.getElementById('loginForm').addEventListener('submit', function (event) {
                event.preventDefault();

                const formData = new FormData(this);
                const params = new URLSearchParams(formData).toString();

                fetch('http://localhost:8080/EspotifyWeb/LoginServlet', {
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
                            document.getElementById('resultado').innerText = message;
                            alert(message);
                            if(message === "Visitante logeado exitosamente."){
                                setTimeout(() => location.reload(), 1000);
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            const errorMessage = "Error al intentar logearse.";
                            document.getElementById('resultado').innerText = errorMessage;
                            alert(errorMessage);
                        });
            });
            
            // Logout
            document.getElementById('logoutButton').addEventListener('click', function () {
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
                            document.getElementById('resultado').innerText = message;
                            alert(message);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            const errorMessage = "Error al intentar cerrar sesion.";
                            document.getElementById('resultado').innerText = errorMessage;
                            alert(errorMessage);
                        });
                setTimeout(() => location.href = 'index.jsp', 1000);
            });