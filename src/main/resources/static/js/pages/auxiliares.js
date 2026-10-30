document.addEventListener('DOMContentLoaded', function () {

    requireRole('ADMIN');
    if (!canAccess('ADMIN')) return;

    configurarSidebar();

    document.getElementById('form-tipo').addEventListener('submit', cadastrarTipo);
    document.getElementById('form-fornecedor').addEventListener('submit', cadastrarFornecedor);

    carregarTipos();
    carregarFornecedores();
});

function mostrar(elemento, texto) {
    elemento.textContent = texto;
    elemento.classList.add('visivel');
}

function esconder(...elementos) {
    elementos.forEach(el => el.classList.remove('visivel'));
}

// ---------- Tipos de serviço ----------

async function carregarTipos() {
    const tbody = document.getElementById('tabela-tipos');
    const lista = document.getElementById('lista-tipos');

    try {
        const tipos = await apiRequest('/tipos-servico');

        if (tipos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="2" class="estado-vazio">Nenhum tipo cadastrado.</td></tr>';
            lista.innerHTML = '<span class="estado-vazio">Cadastre um tipo de serviço antes de cadastrar fornecedores.</span>';
            return;
        }

        tbody.innerHTML = tipos.map(t => `
            <tr>
                <td>${t.id}</td>
                <td>${escaparHtml(t.tipo)}</td>
            </tr>
        `).join('');

        lista.innerHTML = tipos.map(t => `
            <label class="checkbox-item">
                <input type="checkbox" value="${t.id}">
                <span>${escaparHtml(t.tipo)}</span>
            </label>
        `).join('');

    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="2" class="estado-erro">${escaparHtml(error.message)}</td></tr>`;
        lista.innerHTML = '';
    }
}

async function cadastrarTipo(event) {
    event.preventDefault();

    const form = event.target;
    const erro = document.getElementById('erro-tipo');
    const sucesso = document.getElementById('sucesso-tipo');
    const botao = document.getElementById('btn-tipo');

    esconder(erro, sucesso);
    botao.disabled = true;

    try {
        const salvo = await apiRequest('/tipos-servico', {
            method: 'POST',
            body: JSON.stringify({ tipo: document.getElementById('tipo').value.trim() })
        });

        mostrar(sucesso, `Tipo "${salvo.tipo}" cadastrado com sucesso.`);
        form.reset();
        carregarTipos();

    } catch (error) {
        mostrar(erro, error.message);

    } finally {
        botao.disabled = false;
    }
}

// ---------- Fornecedores ----------

async function carregarFornecedores() {
    const tbody = document.getElementById('tabela-fornecedores');

    try {
        const fornecedores = await apiRequest('/fornecedores');

        if (fornecedores.length === 0) {
            tbody.innerHTML = '<tr><td colspan="3" class="estado-vazio">Nenhum fornecedor cadastrado.</td></tr>';
            return;
        }

        tbody.innerHTML = fornecedores.map(f => `
            <tr>
                <td>${f.id}</td>
                <td>${escaparHtml(f.nome)}</td>
                <td>${(f.tipoServicos || [])
                        .map(t => `<span class="tag">${escaparHtml(t.tipo)}</span>`)
                        .join('')}</td>
            </tr>
        `).join('');

    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="3" class="estado-erro">${escaparHtml(error.message)}</td></tr>`;
    }
}

async function cadastrarFornecedor(event) {
    event.preventDefault();

    const form = event.target;
    const erro = document.getElementById('erro-fornecedor');
    const sucesso = document.getElementById('sucesso-fornecedor');
    const botao = document.getElementById('btn-fornecedor');

    esconder(erro, sucesso);

    const tipoServicoIds = Array.from(
        document.querySelectorAll('#lista-tipos input[type="checkbox"]:checked')
    ).map(cb => Number(cb.value));

    if (tipoServicoIds.length === 0) {
        mostrar(erro, 'Selecione ao menos um tipo de serviço.');
        return;
    }

    botao.disabled = true;

    try {
        const salvo = await apiRequest('/fornecedores', {
            method: 'POST',
            body: JSON.stringify({
                nome: document.getElementById('nome-fornecedor').value.trim(),
                tipoServicoIds: tipoServicoIds
            })
        });

        mostrar(sucesso, `Fornecedor "${salvo.nome}" cadastrado com sucesso.`);
        form.reset();
        carregarFornecedores();

    } catch (error) {
        mostrar(erro, error.message);

    } finally {
        botao.disabled = false;
    }
}