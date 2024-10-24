//Formulario de login
const abrirFormLogin = document.querySelector("#abrirFormLogin");
const cerrarFormLogin = document.querySelector("#cerrarFormLogin");
const winLogin = document.querySelector("#winLogin");

abrirFormLogin.addEventListener("click", () => {
    winLogin.showModal();
})

cerrarFormLogin.addEventListener("click", () => {
    winLogin.close();
})
//Fin formulario de login

//Formulario de signup
const abrirFormSignup = document.querySelector("#abrirFormSignup");
const cerrarFormSignup = document.querySelector("#cerrarFormSignup");
const winSignup = document.querySelector("#winSignup");

abrirFormSignup.addEventListener("click", () => {
    winSignup.showModal();
})

cerrarFormSignup.addEventListener("click", () => {
    winSignup.close();
})
//Fin formulario de signup

//Cosas del reproductor de musica
const audio = document.getElementById('miAudio');
const audioSource = document.getElementById('audioSource');
const playBtn = document.getElementById('playBtn');
const pauseBtn = document.getElementById('pauseBtn');
const muteBtn = document.getElementById('muteBtn');
const unmuteBtn = document.getElementById('unmuteBtn');
const progressBar = document.getElementById('progressBar');
const progress = document.getElementById('progress');
const volumeBar = document.getElementById('volumeBar');
const volumeLevel = document.getElementById('volumeLevel');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const currentTimeDisplay = document.getElementById('currentTime');
const totalTimeDisplay = document.getElementById('totalTime');

const audioFiles = ['temas/DONMAI.mp3', 'temas/audio1.mp3', 'temas/audio2.mp3', 'temas/audio3.mp3']; //Necesita conocer la lista de los audios para que funcionen los botones de back y next, y para que se reproduzca el siguiente audio al terminar de ser reproducido el actual
let currentAudioIndex = 0;

let previousVolume = 0.5; // Almacena el volumen anterior
audio.volume = previousVolume; // Inicializa el volumen al cargar

// Evento para reproducir el siguiente audio al finalizar
audio.addEventListener('ended', nextAudio);

// Actualiza el tiempo total y el tiempo actual
audio.addEventListener('loadedmetadata', () => {
    totalTimeDisplay.textContent = formatTime(audio.duration);
});

audio.addEventListener('timeupdate', () => {
    const percentage = (audio.currentTime / audio.duration) * 100;
    progress.style.width = percentage + '%';
    currentTimeDisplay.textContent = formatTime(audio.currentTime);
});

audio.addEventListener('ended', () => {
    progress.style.width = '0%'; // Resetea la barra de progreso
});

playBtn.addEventListener('click', () => {
    audio.play();
    playBtn.style.display = 'none';
    pauseBtn.style.display = 'inline';
});

pauseBtn.addEventListener('click', () => {
    audio.pause();
    playBtn.style.display = 'inline';
    pauseBtn.style.display = 'none';
});

function setProgress(event) {
    const totalWidth = progressBar.offsetWidth;
    const clickX = event.offsetX;
    const newTime = (clickX / totalWidth) * audio.duration;
    audio.currentTime = newTime;
}

function startAdjustingProgressBar(event) {

    if (audio.pause()) {
        playBtn.style.display = 'inline';
        pauseBtn.style.display = 'none';
    } else {
        playBtn.style.display = 'none';
        pauseBtn.style.display = 'inline';
    }
    audio.pause();

    setProgress(event); // Ajusta el progreso al hacer clic
    document.addEventListener('mousemove', setProgress); // Ajusta el progreso mientras se mueve el ratón
    document.addEventListener('mouseup', stopAdjustingProgressBar); // Detiene el ajuste al soltar el botón
}

function stopAdjustingProgressBar() {
    document.removeEventListener('mousemove', setProgress); // Detiene el ajuste de progreso
    document.removeEventListener('mouseup', stopAdjustingProgressBar); // Detiene el evento de soltar

    if (audio.pause()) {
        playBtn.style.display = 'inline';
        pauseBtn.style.display = 'none';
    } else {
        playBtn.style.display = 'none';
        pauseBtn.style.display = 'inline';
    }
    audio.play();
}

function setVolume(event) {
    const totalWidth = volumeBar.offsetWidth;
    const clickX = event.offsetX;
    const newVolume = clickX / totalWidth;

    audio.volume = Math.max(0, Math.min(1, newVolume)); // Asegura que el volumen esté entre 0 y 1
    previousVolume = audio.volume; // Actualiza el volumen anterior
    volumeLevel.style.width = (newVolume * 100) + '%'; // Actualiza la barra de volumen
}

function startAdjustingVolume(event) {
    setVolume(event); // Ajusta el volumen al hacer clic
    document.addEventListener('mousemove', setVolume); // Ajusta el volumen mientras se mueve el ratón
    document.addEventListener('mouseup', stopAdjustingVolume); // Detiene el ajuste al soltar el botón
}

function stopAdjustingVolume() {
    document.removeEventListener('mousemove', setVolume); // Detiene el ajuste de volumen
    document.removeEventListener('mouseup', stopAdjustingVolume); // Detiene el evento de soltar
}

function muteVolume() {
    previousVolume = audio.volume; // Guarda el volumen actual
    audio.volume = 0; // Silencia el audio
    volumeLevel.style.width = '0%'; // Actualiza la barra de volumen
    muteBtn.style.display = 'none'; // Oculta el botón de silenciar
    unmuteBtn.style.display = 'inline'; // Muestra el botón de restaurar volumen
}

function unmuteVolume() {
    audio.volume = previousVolume; // Restaura el volumen anterior
    volumeLevel.style.width = (previousVolume * 100) + '%'; // Actualiza la barra de volumen
    unmuteBtn.style.display = 'none'; // Oculta el botón de restaurar volumen
    muteBtn.style.display = 'inline'; // Muestra el botón de silenciar
}

function prevAudio() {
    currentAudioIndex = (currentAudioIndex - 1 + audioFiles.length) % audioFiles.length;
    loadAudio();
}

function nextAudio() {
    currentAudioIndex = (currentAudioIndex + 1) % audioFiles.length;
    loadAudio();
}

function loadAudio() {
    audioSource.src = audioFiles[currentAudioIndex];
    audio.load(); // Carga el nuevo archivo de audio
    audio.play(); // Reproduce el nuevo audio
    playBtn.style.display = 'none';
    pauseBtn.style.display = 'inline';
}

function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
}