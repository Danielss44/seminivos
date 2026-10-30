document.addEventListener('DOMContentLoaded', function () {

    requireRole('ADMIN', 'GERENTE');
    if (!canAccess('ADMIN', 'GERENTE')) return;

    configurarSidebar();

    document.getElementById('btn-atualizar').addEventListener('click', carregarDashboard);

    carregarDashboard();
});

async function carregarDashboard() {
    const loading = document.getElementById('loading');
    const erro = document.getElementById('mensagem-erro');
    const conteudo = document.getElementById('conteudo-dashboard');
    const botao = document.getElementById('btn-atualizar');

    erro.classList.remove('visivel');
    loading.style.display = 'block';
    botao.disabled = true;

    try {
        const d = await apiRequest('/dashboard');

        renderizarIndicadores(d);
        renderizarProntos(d.veiculosProntosParaVenda);
        renderizarEmPreparacao(d.veiculosEmPreparacao);
        renderizarServicos(d.servicosEmAndamento);

        conteudo.style.display = 'block';

        const hora = new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
        document.getElementById('ultima-atualizacao').textContent = 'Atualizado às ' + hora;

    } catch (error) {
        erro.textContent = error.message;
        erro.classList.add('visivel');

    } finally {
        loading.style.display = 'none';
        botao.disabled = false;
    }
}

function renderizarIndicadores(d) {
    const cards = [{ titulo: 'Total de veículos', valor: d.totalVeiculos }];

    // Um card por status, usando o que a API devolveu
    Object.entries(d.veiculoPorStatus).forEach(([status, quantidade]) => {
        cards.push({ titulo: getStatusLabel(status), valor: quantidade });
    });

    cards.push({ titulo: 'Serviços em andamento', valor: d.servicosEmAndamento.length });

    document.getElementById('indicadores').innerHTML = cards.map(c => `
        <div class="indicador">
            <div class="titulo">${escaparHtml(c.titulo)}</div>
            <div class="valor">${c.valor}</div>
        </div>
    `).join('');
}

function renderizarProntos(veiculos) {
    const tbody = document.getElementById('tabela-prontos');

    if (veiculos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="estado-vazio">Nenhum veículo pronto para venda.</td></tr>';
        return;
    }

    tbody.innerHTML = veiculos.map(v => `
        <tr>
            <td>${escaparHtml(v.placa)}</td>
            <td>${escaparHtml(v.modelo)}</td>
            <td>${escaparHtml(v.ano)}</td>
            <td>${formatarMoeda(v.valor)}</td>
            <td><a href="/detalhes.html?id=${v.id}" class="btn-secundario">Ver Detalhes</a></td>
        </tr>
    `).join('');
}

function renderizarEmPreparacao(veiculos) {
    const tbody = document.getElementById('tabela-preparacao');

    if (veiculos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="estado-vazio">Nenhum veículo em preparação.</td></tr>';
        return;
    }

    tbody.innerHTML = veiculos.map(v => `
        <tr>
            <td>${escaparHtml(v.placa)}</td>
            <td>${escaparHtml(v.modelo)}</td>
            <td>${formatarData(v.dataEntrada)}</td>
            <td>${diasDesde(v.dataEntrada)} dias</td>
            <td><a href="/detalhes.html?id=${v.id}" class="btn-secundario">Ver Detalhes</a></td>
        </tr>
    `).join('');
}

function renderizarServicos(servicos) {
    const tbody = document.getElementById('tabela-servicos');

    if (servicos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="estado-vazio">Nenhum serviço em andamento.</td></tr>';
        return;
    }

    tbody.innerHTML = servicos.map(s => `
        <tr>
            <td>${escaparHtml(s.tipoServicoNome)}</td>
            <td>${escaparHtml(s.fornecedorNome)}</td>
            <td>${formatarMoeda(s.valor)}</td>
            <td>${formatarData(s.dataInicio)}</td>
            <td><span class="badge ${getStatusClass(s.status)}">${getStatusLabel(s.status)}</span></td>
        </tr>
    `).join('');
}

// Quantos dias se passaram desde a data "yyyy-mm-dd".
// Compara em UTC para o horário de verão e o fuso não mudarem o resultado.
function diasDesde(dataIso) {
    const [ano, mes, dia] = dataIso.split('-').map(Number);
    const entrada = Date.UTC(ano, mes - 1, dia);
    const hoje = new Date();
    const hojeUtc = Date.UTC(hoje.getFullYear(), hoje.getMonth(), hoje.getDate());
    return Math.max(0, Math.round((hojeUtc - entrada) / 86400000));
}