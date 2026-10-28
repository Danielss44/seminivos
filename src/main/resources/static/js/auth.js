function getCurrentUser(){
    const token = getToken();
    if (!token) return null;

    const payload = token.split('.')[1];

    const decoded = JSON.parse(atob(payload));

    return {
        login: decoded.sub,
        role: decoded.role
    };
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
    return getToken() !== null;
}

