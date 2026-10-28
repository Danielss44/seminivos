const BASE_URL = 'http://localhost:8080';

async function apiRequest(url, options = {}){
    const  headers = {
        'Content-Type' : 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token){
        headers['Authorization'] = 'Bearer ' + token;
    }

    const response = await  fetch(BASE_URL + url, {
        ...options,
        headers
    });

    if (url === '/auth/login' && (response.status === 401 || response.status === 403)){
        throw new Error('Login ou senha inválidos.');
    }

    if (response.status === 401) {
        logout();
        return;
    }

    if (response.status === 403) {
        throw new Error('Você não tem permissão para realizar esta operação.');
    }

    if (response.status === 204) {
        return null;
    }

    const texto = await response.text();
    const data = texto ? JSON.parse(texto) : null;

    if (!response.ok) {
        throw new Error((data && data.mensagem) || 'Erro inesperado.');
    }

    return data;
}