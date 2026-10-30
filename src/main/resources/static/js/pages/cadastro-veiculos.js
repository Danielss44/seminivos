document.addEventListener('DOMContentLoaded', function () {

    requireRole('ADMIN', 'GERENTE');
    if (!canAccess('ADMIN', 'GERENTE')) return;

    configurarSidebar();

    const form = document.getElementById('form-veiculo');
    const inputDataEntrada = document.getElementById('data-entrada');
    const mensagemErro = document.getElementById('mensagem-erro');
    const mensagemSucesso = document.getElementById('mensagem-sucesso');
    const btnSalvar = document.getElementById('btn-salvar');

    inputDataEntrada.value = hojeLocal();

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        mensagemErro.classList.remove('visivel');
        mensagemSucesso.classList.remove('visivel');
        btnSalvar.disabled = true;
        btnSalvar.textContent = 'Salvando...';

        const veiculo = {
            placa: document.getElementById('placa').value.trim().toUpperCase(),
            modelo: document.getElementById('modelo').value.trim(),
            ano: Number(document.getElementById('ano').value),
            cor: document.getElementById('cor').value.trim(),
            quilometragem: Number(document.getElementById('quilometragem').value),
            valor: Number(document.getElementById('valor').value),
            dataEntrada: inputDataEntrada.value
        };

        try {
            const salvo = await apiRequest('/veiculos', {
                method: 'POST',
                body: JSON.stringify(veiculo)
            });

            mensagemSucesso.textContent = `Veículo ${salvo.placa} cadastrado com sucesso.`;
            mensagemSucesso.classList.add('visivel');

            form.reset();
            inputDataEntrada.value = hojeLocal();

        } catch (error) {
            mensagemErro.textContent = error.message;
            mensagemErro.classList.add('visivel');

        } finally {
            btnSalvar.disabled = false;
            btnSalvar.textContent = 'Salvar';
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    });
});

