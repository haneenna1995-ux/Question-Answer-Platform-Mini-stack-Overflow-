import axios from "axios";

const api= axios.create({baseURL:import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",});

api.interceptors.request.use((config)=> {
const token= localStorage.getItem("token");

if(token)
{
    config.headers.Authorization= `Bearer ${token}`
}

    return config;
});

// when the backend send http response 401(unauthenticated)
api.interceptors.response.use(
    (response) => response,
    (error) => {

        const isAuthEndpoint = error.config?.url?.includes("/auth/");
        if (error.response?.status === 401 && !isAuthEndpoint) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);
export default api;

