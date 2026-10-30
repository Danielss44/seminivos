const BASE_URL = '';

async function apiRequest(url, options = {}){
    const  headers = {
        'Content-Type' : 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token){
        headers['Authorization'] = 'Bearer ' + token;
    }

    let response
    try {
        response = await  fetch(BASE_URL + url, { ...options, headers});
    } catch (error){
        throw new  Error('Mão foi possível conectar ao servidor. Verifique sua conexão.');
    }

    if (url === '/auth/login' && (response.status === 401 || response.status === 403)){
        throw new Error('Login ou senha inválidos.');
    }

    if (response.status === 401) {
        logout();
        throw new Error('Sua sessão expirou. Faça login novamente.');
    }

    if (response.status === 403) {
        throw new Error('Você não tem permissão para realizar esta operação.');
    }

    if (response.status === 204) {
        return null;
    }

    const texto = await response.text();
    let data = null;
    try {
        data = texto ? JSON.parse(texto) : null;
    } catch (error) {
        data = null;
    }

    if (!response.ok) {
        const padrao = response.status >= 500
                ? 'Erro no servidor. Tente novamente em instantes.'
                : 'Não foi possível concluir a operação.';
        throw new Error((data && data.mensagem) || padrao);
    }

    return data;
}