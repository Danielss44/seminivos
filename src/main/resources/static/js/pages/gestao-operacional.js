let veiculoAtual = null;   // dados do veículo carregado (VeiculoDetalhesResponseDTO)
let ocupado = false;       // impede duas ações ao mesmo tempo (duplo clique)

document.addEventListener('DOMContentLoaded', async function () {

    requireRole('ADMIN', 'GERENTE');
    if (!canAccess('ADMIN', 'GERENTE')) return;

    configurarSidebar();
    preencherDatasPadrao();

    document.getElementById('form-busca').addEventListener('submit', buscarPorPlaca);
    document.getElementById('form-revisao').addEventListener('submit', lancarRevisao);
    document.getElementById('btn-finalizar-revisao').addEventListener('click', finalizarRevisao);
    document.getElementById('form-h3').addEventListener('submit', lancarH3);
    document.getElementById('btn-finalizar-h3').addEventListener('click', finalizarH3);
    document.getElementById('servico-tipo').addEventListener('change', carregarFornecedoresDoTipo);
    document.getElementById('form-servico').addEventListener('submit', adicionarServico);
    document.getElementById('tabela-servicos').addEventListener('click', aoClicarNaTabela);

    await carregarOpcoes();

    // Se veio da tela de detalhes (?id=5), já abre o veículo
    const id = new URLSearchParams(window.location.search).get('id');
    if (id) {
        try {
            await carregarVeiculo(id);
        } catch (error) {
            mostrar(document.getElementById('mensagem-erro'), error.message);
        }
    }
});

// ---------- Carga de dados ----------

async function carregarOpcoes() {
    try {
        const [tipos, fornecedores] = await Promise.all([
            apiRequest('/tipos-servico'),
            apiRequest('/fornecedores')
        ]);

        document.getElementById('servico-tipo').innerHTML =
            '<option value="">Selecione...</option>' +
            tipos.map(t => `<option value="${t.id}">${escaparHtml(t.tipo)}</option>`).join('');

        document.getElementById('h3-fornecedor').innerHTML =
            '<option value="">Selecione...</option>' +
            fornecedores.map(f => `<option value="${f.id}">${escaparHtml(f.nome)}</option>`).join('');

    } catch (error) {
        mostrar(document.getElementById('mensagem-erro'), error.message);
    }
}

async function carregarVeiculo(id) {
    veiculoAtual = await apiRequest(`/veiculos/${encodeURIComponent(id)}/detalhes`);
    renderizarVeiculo(veiculoAtual);
}

async function buscarPorPlaca(event) {
    event.preventDefault();

    const erro = document.getElementById('mensagem-erro');
    esconder(erro, document.getElementById('mensagem-sucesso'));

    const placa = document.getElementById('busca-placa').value.trim().toUpperCase();
    if (!placa) return;

    try {
        const veiculo = await apiRequest('/veiculos/placa/' + encodeURIComponent(placa));
        await carregarVeiculo(veiculo.id);

    } catch (error) {
        veiculoAtual = null;
        document.getElementById('area-veiculo').style.display = 'none';
        mostrar(erro, error.message);
    }
}

async function carregarFornecedoresDoTipo() {
    const tipoId = document.getElementById('servico-tipo').value;
    const select = document.getElementById('servico-fornecedor');

    if (!tipoId) {
        reiniciarFornecedoresServico();
        return;
    }

    select.disabled = true;
    select.innerHTML = '<option value="">Carregando...</option>';

    try {
        const fornecedores = await apiRequest('/fornecedores/tipo/' + encodeURIComponent(tipoId));

        if (fornecedores.length === 0) {
            select.innerHTML = '<option value="">Nenhum fornecedor executa este tipo</option>';
            return;
        }

        select.innerHTML =
            '<option value="">Selecione...</option>' +
            fornecedores.map(f => `<option value="${f.id}">${escaparHtml(f.nome)}</option>`).join('');
        select.disabled = false;

    } catch (error) {
        reiniciarFornecedoresServico();
        mostrar(document.getElementById('mensagem-erro'), error.message);
    }
}

function reiniciarFornecedoresServico() {
    const select = document.getElementById('servico-fornecedor');
    select.innerHTML = '<option value="">Selecione o tipo primeiro</option>';
    select.disabled = true;
}

// ---------- Execução de ações ----------

// Roda uma ação de escrita, recarrega o veículo e mostra a mensagem.
// Devolve true se deu tudo certo.
async function executar(acao, mensagemSucesso) {
    if (ocupado) return false;
    ocupado = true;

    const erro = document.getElementById('mensagem-erro');
    const sucesso = document.getElementById('mensagem-sucesso');
    esconder(erro, sucesso);

    try {
        await acao();
        await carregarVeiculo(veiculoAtual.id);
        mostrar(sucesso, mensagemSucesso);
        return true;

    } catch (error) {
        mostrar(erro, error.message);
        return false;

    } finally {
        ocupado = false;
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function limparFormulario(form) {
    form.reset();
    preencherDatasPadrao();
    if (form.id === 'form-servico') reiniciarFornecedoresServico();
}

function preencherDatasPadrao() {
    const hoje = hojeLocal();
    ['revisao-data', 'h3-data', 'servico-data'].forEach(id => {
        document.getElementById(id).value = hoje;
    });
}

// ---------- Revisão ----------

async function lancarRevisao(event) {
    event.preventDefault();
    const form = event.target;

    const ok = await executar(() => apiRequest('/revisoes', {
        method: 'POST',
        body: JSON.stringify({
            veiculoId: veiculoAtual.id,
            finalizado: false,
            dataRevisao: document.getElementById('revisao-data').value,
            observacoes: document.getElementById('revisao-obs').value.trim()
        })
    }), 'Revisão lançada com sucesso.');

    if (ok) limparFormulario(form);
}

function finalizarRevisao() {
    if (!confirm('Finalizar a revisão deste veículo? Essa ação não pode ser desfeita.')) return;

    executar(() => apiRequest('/revisoes/finalizar/' + encodeURIComponent(veiculoAtual.placa), {
        method: 'PATCH'
    }), 'Revisão finalizada com sucesso.');
}

// ---------- H3 ----------

async function lancarH3(event) {
    event.preventDefault();
    const form = event.target;

    const ok = await executar(() => apiRequest('/h3', {
        method: 'POST',
        body: JSON.stringify({
            veiculoId: veiculoAtual.id,
            fornecedorId: Number(document.getElementById('h3-fornecedor').value),
            finalizado: false,
            dataH3: document.getElementById('h3-data').value
        })
    }), 'H3 lançado com sucesso.');

    if (ok) limparFormulario(form);
}

function finalizarH3() {
    if (!confirm('Finalizar o H3 deste veículo? Essa ação não pode ser desfeita.')) return;

    executar(() => apiRequest('/h3/finalizar/' + encodeURIComponent(veiculoAtual.placa), {
        method: 'PATCH'
    }), 'H3 finalizado com sucesso.');
}

// ---------- Serviços ----------

async function adicionarServico(event) {
    event.preventDefault();
    const form = event.target;

    const ok = await executar(() => apiRequest('/servicos', {
        method: 'POST',
        body: JSON.stringify({
            veiculoId: veiculoAtual.id,
            tipoServicoId: Number(document.getElementById('servico-tipo').value),
            fornecedorId: Number(document.getElementById('servico-fornecedor').value),
            valor: Number(document.getElementById('servico-valor').value),
            dataInicio: document.getElementById('servico-data').value
        })
    }), 'Serviço adicionado com sucesso.');

    if (ok) limparFormulario(form);
}

// Um único ouvinte na tabela atende todos os botões "Concluir"
function aoClicarNaTabela(event) {
    const botao = event.target.closest('button[data-concluir]');
    if (!botao) return;

    if (!confirm('Marcar este serviço como concluído?')) return;

    executar(() => apiRequest('/servicos/concluir/' + encodeURIComponent(botao.dataset.concluir), {
        method: 'PATCH'
    }), 'Serviço concluído com sucesso.');
}

// ---------- Renderização ----------

function renderizarVeiculo(v) {
    document.getElementById('area-veiculo').style.display = 'block';

    document.getElementById('v-placa').textContent = v.placa;
    document.getElementById('v-modelo').textContent = v.modelo;
    document.getElementById('v-status').innerHTML =
        `<span class="badge ${getStatusClass(v.status)}">${getStatusLabel(v.status)}</span>`;
    document.getElementById('v-disponivel').textContent = v.prontoParaVenda ? 'Sim' : 'Não';

    renderizarRevisao(v.revisao);
    renderizarH3(v.h3);
    renderizarServicos(v.servicos);
}

function renderizarRevisao(revisao) {
    const estado = document.getElementById('revisao-estado');
    const form = document.getElementById('form-revisao');
    const botao = document.getElementById('btn-finalizar-revisao');

    if (!revisao) {
        estado.innerHTML = '<p class="texto-discreto">Revisão ainda não lançada.</p>';
        form.style.display = 'block';
        botao.style.display = 'none';
        return;
    }

    const status = revisao.finalizado ? 'CONCLUIDO' : 'PENDENTE';

    estado.innerHTML = `
        <div class="info-grid">
            <div class="info-item"><label>Situação</label>
                <span class="badge ${getStatusClass(status)}">${getStatusLabel(status)}</span></div>
            <div class="info-item"><label>Data</label>
                <span>${formatarData(revisao.dataRevisao)}</span></div>
            <div class="info-item"><label>Observações</label>
                <span>${escaparHtml(revisao.observacoes) || '—'}</span></div>
        </div>
    `;
    form.style.display = 'none';
    botao.style.display = revisao.finalizado ? 'none' : 'inline-block';
}

function renderizarH3(h3) {
    const estado = document.getElementById('h3-estado');
    const form = document.getElementById('form-h3');
    const botao = document.getElementById('btn-finalizar-h3');

    if (!h3) {
        estado.innerHTML = '<p class="texto-discreto">H3 ainda não lançado.</p>';
        form.style.display = 'block';
        botao.style.display = 'none';
        return;
    }

    const status = h3.finalizado ? 'CONCLUIDO' : 'PENDENTE';

    estado.innerHTML = `
        <div class="info-grid">
            <div class="info-item"><label>Situação</label>
                <span class="badge ${getStatusClass(status)}">${getStatusLabel(status)}</span></div>
            <div class="info-item"><label>Empresa responsável</label>
                <span>${escaparHtml(h3.fornecedorNome)}</span></div>
            <div class="info-item"><label>Data</label>
                <span>${formatarData(h3.dataH3)}</span></div>
        </div>
    `;
    form.style.display = 'none';
    botao.style.display = h3.finalizado ? 'none' : 'inline-block';
}

function renderizarServicos(servicos) {
    const tbody = document.getElementById('tabela-servicos');

    if (servicos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="estado-vazio">Nenhum serviço registrado.</td></tr>';
        return;
    }

    tbody.innerHTML = servicos.map(s => `
        <tr>
            <td>${escaparHtml(s.tipoServicoNome)}</td>
            <td>${escaparHtml(s.fornecedorNome)}</td>
            <td>${formatarMoeda(s.valor)}</td>
            <td>${formatarData(s.dataInicio)}</td>
            <td>${formatarData(s.dataConclusao)}</td>
            <td><span class="badge ${getStatusClass(s.status)}">${getStatusLabel(s.status)}</span></td>
            <td>${s.status === 'CONCLUIDO'
                ? ''
                : `<button class="btn-sucesso" data-concluir="${s.id}">Concluir</button>`}</td>
        </tr>
    `).join('');
}