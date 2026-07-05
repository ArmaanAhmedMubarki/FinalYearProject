// Logout function
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role'); // Clear role on logout
    localStorage.removeItem('roleId');
    window.location.href = 'Login.html';
    }

// Check if user is logged in
if (!localStorage.getItem('token')) {
    window.location.href = 'Login.html';
    }

// Token Expiration Utilities
function isTokenExpired(token) {
    if (!token) return true;

    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = JSON.parse(atob(base64));
        const currentTime = Math.floor(Date.now() / 1000);
        return jsonPayload.exp < currentTime;
    } catch (error) {
        console.error("Error decoding token:", error);
        return true;
    }
}

function checkAndRemoveExpiredToken() {
    const token = localStorage.getItem('token');
    if (isTokenExpired(token)) {
        console.warn("Token expired. Removing it.");
        localStorage.removeItem('token');
        window.location.href = 'Login.html'; // Redirect to login
    }
}

// API Request Utility
function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('token');
    if (isTokenExpired(token)) {
        console.warn("Token expired. Redirecting to login.");
        localStorage.removeItem('token');
        window.location.href = 'Login.html';
        return Promise.reject(new Error("Token expired"));
    }

    const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`,
    };

    return fetch(url, { ...options, headers });
}

// Run check on page load
document.addEventListener('DOMContentLoaded', () => {
    checkAndRemoveExpiredToken();
});
