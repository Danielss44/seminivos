function hasRole(role) {
    const user = getCurrentUser();
    if (!user) return false;
    return user.role === role;
}

function hasAnyRole(...roles) {
    const user = getCurrentUser();
    if (!user) return false;
    return roles.includes(user.role)

}

function requireAuth() {
    if (!isAuthenticated()){
        window.location.href = '/login.html';
    }
}

function requireRole(...roles){
    requireAuth();
    if (!hasAnyRole(...roles)){
        window.location.href = '/login.html';
    }

    if (!hasAnyRole(...roles)) {
        window.location.href = '/veiculos.html';
    }
}

function canAccess(...roles){
    return hasAnyRole(...roles)
}