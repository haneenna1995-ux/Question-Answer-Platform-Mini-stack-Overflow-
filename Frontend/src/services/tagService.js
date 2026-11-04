import api from "./api.js"
export async function suggestTags(query)
{
    const response = await api.get("/tags/suggest", {params:{query}});
    return response.data;
}