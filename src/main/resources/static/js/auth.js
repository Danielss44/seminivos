function getCurrentUser(){
    const token = getToken();
    if (!token) return null;

    try {
        const payload = decodificarPayload(token);
        return { login: payload.sub, role: payload.role };
    } catch (error) {
        return null;
    }
}

function login(token){
    localStorage.setItem("token", token);
}

function logout(){
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}

function getToken(){
    const token = localStorage.getItem("token");
    return token;
}

function isAuthenticated(){
    return verificarToken();
}

function decodificarPayload(token) {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
        atob(base64)
            .split('')
            .map(c => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
            .join('')
    );
    return JSON.parse(json);
}

function verificarToken() {
    const token = getToken();
    if (!token) return false;

    try {
        const payload = decodificarPayload(token);
        if (payload.exp * 1000 < Date.now()) {
            localStorage.removeItem('token');
            return false;
        }
        return true;
    } catch (error) {
        localStorage.removeItem('token');
        return false;
    }
}
