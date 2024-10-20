<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Tema a Lista</title>
    <link rel="stylesheet" type="text/css" href="estilos.css">
</head>
<body>
    
    <a href="index.jsp">Página Principal</a>
    
    <h1>Agregar Tema a Lista</h1>

     <table id="listasTable">
            <thead>
                <tr>
                    <th>Nombre de la Lista</th>
                </tr>
            </thead>
            <tbody id="listasBody">
                <!-- aca se carga la lista -->
            </tbody>
        </table>
    
    <label for="tipoLista">Selecciona un filtro para encontrar el tema a agregar:</label>
  <select id="tipoLista" name="tipoLista">
    <option value="default">Lista por defecto</option>
    <option value="particularPublica">Lista particular pública</option>
    <option value="album">Álbum</option>
  </select>

     <form>  
        
    </form>
    
    <script>
        
        window.onload = loadListas;
            
             function loadListas() {
                fetch('http://localhost:8080/EspotifyWeb/AgregarTemaAListaServlet?action=cargarListas')
                        .then(response => response.json())
                        .then(data => {
                            const tbody = document.getElementById('listasBody');
                            tbody.innerHTML = ''; // Limpiar la tabla antes de cargar nuevas listas
                            data.forEach(lista => {
                                const row = `<tr><td>${lista.nombre}</td><td>`;
                                tbody.innerHTML += row;
                            });
                        })
                        .catch(error => console.error('Error al cargar listas:', error));
            }
    
    </script>

</body>
</html>