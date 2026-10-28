document.addEventListener('DOMContentLoaded', function () {

    requireAuth();

    const user = getCurrentUser();
    document.getElementById('usuario-nome').textContent = user.login + ' (' + user.role + ')';

    if (!canAccess('ADMIN', 'GERENTE')) {
        document.getElementById('menu-dashboard').parentElement.style.display = 'none';
        document.getElementById('menu-cadastro').parentElement.style.display = 'none';
        document.getElementById('menu-gestao').parentElement.style.display = 'none';
        document.getElementById('menu-usuarios').parentElement.style.display = 'none';
        document.getElementById('menu-auxiliares').parentElement.style.display = 'none';
    }

    listarTodos();

});

async function listarTodos() {
    const tbody = document.getElementById('tabela-veiculos');
    tbody.innerHTML = '<tr><td colspan="8" class="estado-loading">Carregando...</td></tr>';

    try {

        const emPreparacao = await apiRequest('/veiculos/status/EM_PREPARACAO');
        const prontos = await apiRequest('/veiculos/status/PRONTO_PARA_VENDA');

        const todos = [...emPreparacao, ...prontos];

        renderizarTabela(todos);

    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="8" class="estado-erro">${escaparHtml(error.message)}</td></tr>`;
    }
}

    async function pesquisar() {
        const tipo = document.getElementById('tipo-busca').value;
        const valor = document.getElementById('campo-busca').value.trim();

        if(!valor) {
            listarTodos();
            return;
        }

        const tbody = document.getElementById('tabela-veiculos');
        tbody.innerHTML = `<tr><td colspan="8" class="estado-loading">Pesquisando...</td></tr>`;

        try {
            let veiculos;

            if (tipo === 'placa') {
                const veiculo = await apiRequest('/veiculos/placa/' + encodeURIComponent(valor.toUpperCase()));
                veiculos = [veiculo];
            } else {
                veiculos = await apiRequest('/veiculos/modelo?modelo=' + encodeURIComponent(valor));
            }

            renderizarTabela(veiculos);
        } catch (error) {
            tbody.innerHTML = `<tr><td colspan="8" class="estado-erro">${escaparHtml(error.message)}</td></tr>`;
        }
    }

    function renderizarTabela(veiculos){
        const tbody = document.getElementById('tabela-veiculos');

        if(veiculos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="estado-vazio">Nenhum veículo encontrado.</td></tr>';
            return

        }

        tbody.innerHTML = veiculos.map(v => `
            <tr>
                <td>${v.id}</td>
                <td>${escaparHtml(v.placa)}</td>
                <td>${escaparHtml(v.modelo)}</td>
                <td>${escaparHtml(v.ano)}</td>
                <td>${escaparHtml(v.cor)}</td>
                <td>${v.quilometragem.toLocaleString('pt-BR')} km</td>
                <td><span class="badge ${getStatusClass(v.status)}">${getStatusLabel(v.status)}</span></td>
                <td>
                    <a href="/detalhes.html?id=${v.id}" class="btn-secundario">
                        Ver Detalhes
                    </a>
                </td>
            </tr>
            `).join('');
    }

