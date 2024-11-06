document.addEventListener("DOMContentLoaded", function () {
            document.getElementById('tipoUsuario').addEventListener('change', function () {
                const camposArtista = document.getElementById('camposArtista');
                camposArtista.style.display = this.value === 'artista' ? 'block' : 'none';
            });
        });

        function checkNickname() {
            const nickname = document.getElementById("nickname").value;
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "AgregarUsuarioServlet?action=verificarNickname&Nickname=" + encodeURIComponent(nickname), true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    let mensaje = xhr.responseText;
                    let nicknameValidoField = document.getElementById("nickValido");
                    nicknameValidoField.innerHTML = mensaje === "Nickname is available" ? 
                        "<span style='color: green;'>" + mensaje + "</span>" : 
                        "<span style='color: red;'>" + mensaje + "</span>";
                }
            };
            xhr.send();
        }

        function checkCorreo() {
            const correo = document.getElementById("mail").value;
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "AgregarUsuarioServlet?action=verificarCorreo&correoName=" + encodeURIComponent(correo), true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    let mensaje = xhr.responseText;
                    let correoValidoField = document.getElementById("correoValido");
                    correoValidoField.innerHTML = mensaje === "Mail is available" ? 
                        "<span style='color: green;'>" + mensaje + "</span>" : 
                        "<span style='color: red;'>" + mensaje + "</span>";
                }
            };
            xhr.send();
        }

        function submitForm() {
            const formData = new FormData(document.getElementById('altaUsuarioForm'));

            fetch('AgregarUsuarioServlet', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    alert(data.message); 
                    setTimeout(() => location.reload(), 1000);
                } else {
                    alert("Error: " + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Ocurrió un error en el registro.");
            });
        }