function cargarDatosSus() {
    fetch('http://localhost:8080/EspotifyWeb/ActualizarSusServlet', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al cargar usuarios');
                }
                return response.json();
            })
            .then(data => {
                const susLista = document.getElementById('susiLista');
                susLista.innerHTML = ''; // Limpiar la lista existente

                if (data.sus && data.sus.length > 0) {
                    data.sus.forEach(sus2 => {
                        const susRow = document.createElement('tr');

                        if (sus2.estado === 'Vencida') {
                            susRow.innerHTML = `
                            <td>${sus2.id}</td>
                            <td>${sus2.fecha}</td>
                            <td>${sus2.tipo}</td>
                            <td>${sus2.estado}</td>
                            <td><button onclick="cancelar('${sus2.id}')">Cancelar</button></td>
                            <td><button onclick="vigentear('${sus2.id}')">Vigentear</button></td>
                        `;
                        } else {
                            susRow.innerHTML = `
                            <td>${sus2.id}</td>
                            <td>${sus2.fecha}</td>
                            <td>${sus2.tipo}</td>
                            <td>${sus2.estado}</td>
                            <td><button onclick="cancelar('${sus2.id}')">Cancelar</button></td>
                            `;
                        }



                        susLista.appendChild(susRow);
                    });
                } else {
                    susLista.innerHTML = '<tr><td colspan="5">Aun no posees una Suscripion.</td></tr>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                // document.getElementById('usuariosList').innerHTML = '<tr><td colspan="3">Error al cargar usuarios.</td></tr>';
            });
}

function cancelar(id) {
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/ActualizarSusServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, estado: "Cancelada"})
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Se Cancelo la Suscripcion de manera exitosa.');
                    cargarDatosSus(); // Recargar la lista de usuarios
                } else {
                    alert('Error al cancelar suscrpcion: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al intentar cancelar suscrpcion.');
            });
}


function vigentear(id) {
    event.preventDefault();
    fetch('http://localhost:8080/EspotifyWeb/ActualizarSusServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id, estado: "Vigente"})
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Se hizo vigente la la Suscripcion de manera exitosa.');
                    cargarDatosSus(); // Recargar la lista de usuarios
                } else {
                    alert('Error al hacer vigente la suscrpcion: ' + (data.error || 'Error desconocido'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al hacer vigente la cancelar suscrpcion.');
            });

}


window.onload = cargarDatosSus;