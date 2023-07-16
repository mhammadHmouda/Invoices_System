
const token = localStorage.getItem("token");

export function parseJwt() {
    try {
      return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
      return e;
    }
}

export function getUserId() {
    const { id } = parseJwt(token);
    return id;
}

