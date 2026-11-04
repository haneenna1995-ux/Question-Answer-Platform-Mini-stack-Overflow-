import api from "./api.js";

export async function createQuestion(questionData) {
    const response = await api.post("/questions", questionData);
    return response.data;
}

export async function getAllQuestions(){
    const response= await api.get("/questions");// the url works here with "" because it is a normal static string - no  id path variable
    return response.data;
}

export async function getQuestionById(id){
    const response= await api.get(`/questions/${id}`);
    return response.data;
}

export async function getMyQuestions(){
    const response= await api.get("/questions/me")
    return response.data;
}

export async function updateQuestion(id,questionData){

    const response =await api.put(`/questions/${id}`, questionData);
    return response.data;

}

export async function deleteQuestions(id){
    const response= await api.delete(`/questions/delete/${id}`);
    return response.data;
}