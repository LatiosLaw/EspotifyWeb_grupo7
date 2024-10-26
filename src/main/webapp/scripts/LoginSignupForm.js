document.addEventListener("DOMContentLoaded", () => {
    const winLogin = document.querySelector("#winLogin");
    const abrirFormLogin = document.querySelector("#abrirFormLogin");
    const cerrarFormLogin = document.querySelector("#cerrarFormLogin");
    const abrirFormSignup = document.querySelector("#abrirFormSignup");
    const cerrarFormSignup = document.querySelector("#cerrarFormSignup");
    const winSignup = document.querySelector("#winSignup");

    if (abrirFormLogin) {
        abrirFormLogin.addEventListener("click", () => {
            winLogin.showModal();
        });
    }

    if (cerrarFormLogin) {
        cerrarFormLogin.addEventListener("click", () => {
            winLogin.close();
        });
    }

    if (abrirFormSignup) {
        abrirFormSignup.addEventListener("click", () => {
            winSignup.showModal();
        });
    }

    if (cerrarFormSignup) {
        cerrarFormSignup.addEventListener("click", () => {
            winSignup.close();
        });
    }

});