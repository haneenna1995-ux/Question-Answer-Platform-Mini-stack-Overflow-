import {Card, Badge, CardBody} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

function QuestionCard ({question}){
    const navigate= useNavigate();
    return (
        <Card
            onClick={() => navigate(`/questions/${question.id}`)}
            style={{ cursor: "pointer" }}
            className="mb-3"
        >
            <Card.Body className="d-flex justify-content-between align-items-start">

                <div className="me-3 text-center" style={{ minWidth: "7px", color: "#6c757d" }}>
                    <div style={{ fontSize: "1.1rem", fontWeight: "bold" }}>{question.answersCount}</div>
                    <div style={{ fontSize: "0.8rem" }}>answers</div>
                </div>

                <div className="flex-grow-1">
                    <Card.Title style={{ color: "#0074cc" }}>{question.title}</Card.Title>
                    <Card.Text className="text-muted" style={{ fontSize: "0.9rem" }}>
                        {question.description}
                    </Card.Text>
                    <div className="d-flex justify-content-between align-items-center mt-auto">
                        <div className="d-flex flex-wrap gap-2">
                        {question.tags.map(tag => (
                            <span key={tag.id} style={{
                                backgroundColor: "lightgray",
                                color: "black",
                                padding: "2px 8px",
                                borderRadius: "4px",
                                fontSize: "0.7rem",
                                fontWeight:"bold",
                            }}>
                                {tag.name}
                            </span>
                        ))}
                        </div>
                      <small className="text-muted">asked by {question.authorUsername}</small>
                    </div>
                </div>

            </Card.Body>
        </Card>

    );
}


export default QuestionCard;