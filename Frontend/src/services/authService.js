import api from "./api.js"

export async function register(userData) {
    const response = await api.post("/auth/register", userData);
    return response.data;
}

export async function login(credentials)
{
    const response= await api.post("/auth/login", credentials);
    const {token, user}= response.data;
    localStorage.setItem("token", token);
    return user;
}

export async function logout() {
    const token = localStorage.getItem("token");
    if(token){
        await api.post("/auth/logout", null,{headers: {Authorization: `Bearer ${token}`}});
    }

    localStorage.removeItem("token");
}