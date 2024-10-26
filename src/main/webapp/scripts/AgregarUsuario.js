document.getElementById('tipoUsuario').addEventListener('change', function () {
                const camposArtista = document.getElementById('camposArtista');
                if (this.value === 'artista') {
                    camposArtista.style.display = 'block';
                } else {
                    camposArtista.style.display = 'none';
                }
            });
            
            var validoField = document.getElementById('Valido');

            function checkNickname() {
                const nickname = document.getElementById("nickname").value;
                const xhr = new XMLHttpRequest();
                xhr.open("GET", "AgregarUsuarioServlet?action=verificarNickname&Nickname=" + encodeURIComponent(nickname), true);

                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        let mensaje = xhr.responseText;
            let nicknameValidoField = document.getElementById("nickValido");
            if (mensaje === "Nickname is available") {
                nicknameValidoField.innerHTML = "<span style='color: green;'>" + mensaje + "</span>";
                validoField.value = "true";
            } else {
                nicknameValidoField.innerHTML = "<span style='color: red;'>" + mensaje + "</span>";
                validoField.value = "false";
            }
                    }
                };

                xhr.send();
            }

            function checkCorreo() {
                const correo = document.getElementById("mail").value;
                const xhr = new XMLHttpRequest();
                xhr.open("GET", "AgregarUsuarioServlet?action=verificarCorreo&correoName=" + encodeURIComponent(correo), true);

                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        let mensaje = xhr.responseText;
            let correoValidoField = document.getElementById("correoValido");
            if (mensaje === "Mail is available") {
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