document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById('loginForm');
    const resultado = document.getElementById('resultado');

    // Manejo del formulario de inicio de sesión
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
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
                if (resultado) {
                    resultado.innerText = message;
                }
                alert(message);
                if (message === "Visitante logeado exitosamente.") {
                    regitroMomento();
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


function regitroMomento(){
    //que ta' usando
               var os = "nose";
               var nave = "quien sabe";
               
                if (navigator.userAgent.indexOf("Win") !== -1) os =  
                "Windows"; 
                if (navigator.userAgent.indexOf("Mac") !== -1) os =  
                  "Macintosh"; 
                if (navigator.userAgent.indexOf("Linux") !== -1) os =  
                  "Linux"; 
                if (navigator.userAgent.indexOf("Android") !== -1) os =  
                  "Android"; 
                if (navigator.userAgent.indexOf("like Mac") !== -1) os =  
                  "iOS"; 
               
               if (navigator.userAgent.indexOf("Chrome") !== -1) nave =  
                "Chromium"; 
                if (navigator.userAgent.indexOf("Firefox") !== -1) nave =  
                  "FireFox"; 
    //fin que ta´usando
    
    fetch('http://localhost:8080/EspotifyWeb/regiServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: JSON.stringify({os: os, nave: nave})
            })
                    .then(response => {
                        console.log("Respuesta del servidor:", response); // Para depuración

                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("Error registrar el login.");
                    });
    
                 
    
    
}