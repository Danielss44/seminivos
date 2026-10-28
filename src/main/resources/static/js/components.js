function getStatusLabel(status){
    const labels = {
        'EM_PREPARACAO' : 'Em Preparacao',
        'AGUARDANDO_H3' : 'Aguardando H3',
        'EM_REVISAO' : 'Em Revisao',
        'PRONTO_PARA_VENDA' : 'Pronto para Venda',
        'VENDIDO' : 'Vendido',
        'PENDENTE' : 'Pendente',
        'EM_ANDAMENTO' : 'Em Andamento',
        'CONCLUIDO': 'Concluido'
    };
    return labels[status] || status;
}

function getStatusClass(status){
    const classes = {
        'EM_PREPARACAO' : 'badge-em-preparacao',
        'AGUARDANDO_H3' : 'badge-aguardando-h3',
        'EM_REVISAO' : 'badge-em-revisao',
        'PRONTO_PARA_VENDA' : 'badge-pronto-venda',
        'VENDIDO' : 'badge-vendido',
        'PENDENTE' : 'badge-pendente',
        'EM_ANDAMENTO' : 'badge-em-andamento',
        'CONCLUIDO': 'badge-concluido'
    }
    return classes[status] || '';
}

function formatarData(data){
    if(!data) return '-';
    const [ano, mes, dia] = data.split('-');
    return `${dia}/${mes}/${ano}`;
}

function formatarMoeda(valor) {
    return Number(valor).toLocaleString('pt-BR', {style: 'currency', currency: 'BRl'})
}

function escaparHtml(texto) {
    if (texto === null || texto === undefined) return '';
    return String(texto)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function configurarSidebar() {
    const user = getCurrentUser();
    if (!user) return;

    document.getElementById('usuario-nome').textContent = user.login + ' (' + user.role + ')';

    if (!canAccess('ADMIN', 'GERENTE')) {
        ['menu-dashboard', 'menu-cadastro', 'menu-gestao', 'menu-usuarios', 'menu-auxiliares']
            .forEach(id => {
                document.getElementById(id).parentElement.style.display = 'none';
            });
    }
}