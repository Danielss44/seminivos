document.addEventListener('DOMContentLoaded', function () {
    requireAuth();
    configurarSidebar();

    const id = new URLSearchParams(window.location.search).get('id');

    if (!id) {
        window.location.href = '/veiculos.html';
        return;
    }

    carregarDetalhes(id);
});

async function carregarDetalhes(id) {
    const loading = document.getElementById('loading');
    const mensagemErro = document.getElementById('mensagem-erro');

    try {
        const dados = await apiRequest(`/veiculos/${encodeURIComponent(id)}/detalhes`);
        preencher(dados);

    } catch (error) {
        mensagemErro.textContent = error.message;
        mensagemErro.classList.add('visivel');

    } finally {
        loading.style.display = 'none';
    }
}

function preencher(d) {
    document.getElementById('conteudo-detalhes').style.display = 'block';

    document.getElementById('det-placa').textContent = d.placa;
    document.getElementById('det-modelo').textContent = d.modelo;
    document.getElementById('det-ano').textContent = d.ano;
    document.getElementById('det-cor').textContent = d.cor;
    document.getElementById('det-km').textContent = d.quilometragem.toLocaleString('pt-BR') + ' km';
    document.getElementById('det-valor').textContent = formatarMoeda(d.valor);
    document.getElementById('det-entrada').textContent = formatarData(d.dataEntrada);
    document.getElementById('det-disponivel').textContent = d.prontoParaVenda ? 'Sim' : 'Não';

    document.getElementById('det-status').innerHTML =
        `<span class="badge ${getStatusClass(d.status)}">${getStatusLabel(d.status)}</span>`;

    preencherRevisao(d.revisao);
    preencherH3(d.h3);
    preencherServicos(d.servicos);
}

function preencherRevisao(revisao) {
    const div = document.getElementById('det-revisao');

    if (!revisao) {
        div.innerHTML = '<p class="estado-vazio">Revisão ainda não lançada.</p>';
        return;
    }

    const status = revisao.finalizado ? 'CONCLUIDO' : 'PENDENTE';

    div.innerHTML = `
        <div class="info-grid">
            <div class="info-item"><label>Situação</label>
                <span class="badge ${getStatusClass(status)}">${getStatusLabel(status)}</span></div>
            <div class="info-item"><label>Data</label>
                <span>${formatarData(revisao.dataRevisao)}</span></div>
            <div class="info-item"><label>Observações</label>
                <span>${escaparHtml(revisao.observacoes) || '—'}</span></div>
        </div>
    `;
}

function preencherH3(h3) {
    const div = document.getElementById('det-h3');

    if (!h3) {
        div.innerHTML = '<p class="estado-vazio">H3 ainda não lançada.</p>';
        return;
    }

    const status = h3.finalizado ? 'CONCLUIDO' : 'PENDENTE';

    div.innerHTML = `
        <div class="info-grid">
            <div class="info-item"><label>Situação</label>
                <span class="badge ${getStatusClass(status)}">${getStatusLabel(status)}</span></div>
            <div class="info-item"><label>Empresa responsável</label>
                <span>${escaparHtml(h3.fornecedorNome)}</span></div>
            <div class="info-item"><label>Data</label>
                <span>${formatarData(h3.dataH3)}</span></div>
        </div>
    `;
}

function preencherServicos(servicos) {
    const tbody = document.getElementById('det-servicos');

    if (!servicos || servicos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="estado-vazio">Nenhum serviço registrado.</td></tr>';
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
        </tr>
    `).join('');
}