import { jwtDecode } from 'jwt-decode';

export function isTokenExpired(token) {
  try {
    const decoded = jwtDecode(token);
    return decoded.exp * 1000 < Date.now();
  } catch (e) {
    return true; 
  }
}

export function getToken() {
  return localStorage.getItem('authToken');
}

export function logout() {
  localStorage.removeItem('authToken');
  window.location.href = '/';
}
