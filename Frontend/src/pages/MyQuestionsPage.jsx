import {getMyQuestions,deleteQuestions} from "../services/questionService.js";
import {useState, useEffect} from "react";
import {Container, Card, Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

function MyQuestionsPage(){
    const [questions, setQuestions]= useState([]);
    const navigate= useNavigate();

    useEffect(() => {getMyQuestions().then(data => setQuestions(data))

    }, []);

    function handleDelete(id)
    {
        deleteQuestions(id).then(() =>
        {setQuestions(questions.filter(q =>q.id !==id));
        });
    }

 return(
   <Container className="mt-4">
       <h2> My Questions</h2>
       {questions.map(q=>(
           <Card key={q.id} className="mb-3" data-cy="question-card">
               <Card.Body>
                   <Card.Title>{q.title} </Card.Title>
                   <Card.Text> {q.description}</Card.Text>
                   <div className="d-flex flex-wrap gap-2">
                       {q.tags.map(tag => (
                           <span key={tag.id} style={{
                               backgroundColor: "lightgray",
                               color: "black",
                               padding: "2px 8px",
                               borderRadius: "4px",
                               fontSize: "0.8rem"
                           }}>
                              {tag.name}
                           </span>
                       ))}
                   </div>
                   <div >
                       <Button variant="outline-primary" data-cy="edit-button" onClick={() => navigate(`/questions/edit/${q.id}`)}>Edit</Button>
                       <Button variant="outline-danger" data-cy="delete-button" onClick={() => handleDelete(q.id)}>Delete</Button>
                   </div>
               </Card.Body>
           </Card>
       ))}
   </Container>
 );

}

export default MyQuestionsPage;
