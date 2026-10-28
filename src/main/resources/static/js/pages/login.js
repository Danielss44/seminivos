document.addEventListener('DOMContentLoaded', function (){

        if (isAuthenticated()){
            window.location.href = '/veiculos.html';
            return;
        }



    const form = document.getElementById("form-login");
    const inputLogin = document.getElementById('login');
    const inputSenha = document.getElementById('senha');
    const mensagemErro = document.getElementById('mensagem-erro');
    const btnEntrar = document.getElementById('btn-entrar');
    const loading = document.getElementById('loading');

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        mensagemErro.classList.remove('visivel');
        loading.classList.add('visivel');
        btnEntrar.disabled = true;

        try {
            const data = await apiRequest('/auth/login', {
                method: 'POST',
                body: JSON.stringify({
                    login: inputLogin.value,
                    senha: inputSenha.value
                })
            });

            login(data.token);
            window.location.href = '/veiculos.html';

        } catch (error) {

            mensagemErro.textContent = error.message;
            mensagemErro.classList.add('visivel');

        } finally {

            loading.classList.remove('visivel');
            btnEntrar.disabled = false;
        }
    });


});