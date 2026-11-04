import api from "./api.js";

export async function getAnswers(questionId){
    const response= await api.get(`/questions/${questionId}/answers`);
    return response.data;
}

export async function createAnswer(questionId,answerData){
    const response=  await  api.post(`/questions/${questionId}/answers`, answerData);
    return response.data;
}