document.addEventListener('DOMContentLoaded', function () {

    requireRole('ADMIN', 'GERENTE');
    if (!canAccess('ADMIN', 'GERENTE')) return;

    configurarSidebar();

    const form = document.getElementById('form-usuario');
    const selectPerfil = document.getElementById('perfil');
    const mensagemErro = document.getElementById('mensagem-erro');
    const mensagemSucesso = document.getElementById('mensagem-sucesso');
    const btnSalvar = document.getElementById('btn-salvar');

    // Só ADMIN pode criar outro ADMIN: some com a opção para o GERENTE.
    if (!hasRole('ADMIN')) {
        const opcaoAdmin = selectPerfil.querySelector('option[value="ADMIN"]');
        if (opcaoAdmin) opcaoAdmin.remove();
    }

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        mensagemErro.classList.remove('visivel');
        mensagemSucesso.classList.remove('visivel');

        const usuario = {
            nome: document.getElementById('nome').value.trim(),
            login: document.getElementById('login').value.trim(),
            senha: document.getElementById('senha').value,
            perfil: selectPerfil.value
        };

        // Confirmação antes de uma operação crítica
        if (usuario.perfil === 'ADMIN' &&
            !confirm('Você está criando um usuário ADMINISTRADOR. Deseja continuar?')) {
            return;
        }

        btnSalvar.disabled = true;
        btnSalvar.textContent = 'Cadastrando...';

        try {
            await apiRequest('/auth/cadastrar', {
                method: 'POST',
                body: JSON.stringify(usuario)
            });

            mensagemSucesso.textContent = `Usuário ${usuario.login} cadastrado com sucesso.`;
            mensagemSucesso.classList.add('visivel');
            form.reset();

        } catch (error) {
            mensagemErro.textContent = error.message;
            mensagemErro.classList.add('visivel');

        } finally {
            btnSalvar.disabled = false;
            btnSalvar.textContent = 'Cadastrar';
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    });
});