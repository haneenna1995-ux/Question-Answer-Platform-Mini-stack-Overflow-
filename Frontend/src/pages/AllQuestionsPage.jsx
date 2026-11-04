import {useEffect, useState} from "react";
import {getAllQuestions} from "../services/questionService.js";
import QuestionCard from "../components/questions/QuestionCard.jsx";
import {Container} from "react-bootstrap"


function AllQuestionsPage(){
    const [questions, setQuestions]= useState([]);

    useEffect(() => {
        getAllQuestions().then(data=> setQuestions([...data].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))))
    }, []);

    return(
        <Container className="mt-4">
            <h3>All Questions</h3>
            <div></div>
            {questions.map(q=>(
                < QuestionCard key={q.id} question={q} />

            ))}
        </Container>
    );

}
export default AllQuestionsPage;