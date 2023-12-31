const PORT = 8080;
export const BASE_URL = `http://localhost:${PORT}/api/`;
const AUTH_URL = BASE_URL + "auth/";
export const LOGIN_URL = AUTH_URL + "login";
export const REGISTER_URL = AUTH_URL + "register";
export const LOGOUT_URL = AUTH_URL + "logout";
export const INVOICES_URL = BASE_URL + "invoices"
export const MY_INVOICES_URL = BASE_URL + "invoices/user"
export const ALL_LOGS_URL = BASE_URL + "logs"
export const ITEMS_URL = BASE_URL + "items";
export const USERS_URL = BASE_URL + "users"
export const primaryColor = "#007bff";
export const dangerColor = "#dc3545";
export const DELAY = 3000;
