import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getQuestionById } from "../services/questionService.js";
import { getAnswers, createAnswer } from "../services/answerService.js";
import { Container, Card, Form, Button, Alert, Spinner } from "react-bootstrap";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function QuestionDetailsPage() {
    const { id } = useParams();
    const [question, setQuestion] = useState(null);
    const [answers, setAnswers] = useState([]);
    const [body, setBody] = useState("");
    const [error, setError] = useState("");
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        getQuestionById(id).then(setQuestion);
        getAnswers(id).then(setAnswers);
    }, [id]);

    useEffect(() => {
        const token = localStorage.getItem("token");
        const client = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            onConnect: () => {
                client.subscribe(`/topic/questions/${id}/answers`, (message) => {
                    const newAnswer = JSON.parse(message.body);
                    setAnswers(prev => {
                        if (prev.some(a => a.id === newAnswer.id)) return prev;
                        return [...prev, newAnswer];
                    });
                });
            }
        });
        client.activate();
        return () => client.deactivate();
    }, [id]);

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setSubmitting(true);
        try {
            const newAnswer = await createAnswer(id, { body });
            setBody("");
        } catch (err) {
            setError(err.response?.data?.message || "Failed to post answer.");
        } finally {
            setSubmitting(false);
        }
    }

    if (!question) return <Container className="mt-sm-3"><Spinner animation="border" /></Container>;

    return (
        <Container className="mt-sm-3">

            {/* Question */}
            <h2 style={{ color: "#0074cc" }}>{question.title}</h2>
            <p>{question.description}</p>

            <p className="text-secondary-emphasis" style={{ fontSize: "0.85rem" }}>
                asked by <strong>{question.authorUsername}</strong>
            </p>
            <div className="d-flex flex-wrap gap-2 mb-4">
                {question.tags.map(tag => (
                    <span key={tag.id} style={{
                        backgroundColor: "lightgray",
                        padding: "2px 8px",
                        borderRadius: "4px",
                        fontSize: "0.75rem",
                        fontWeight: "bold"
                    }}>
                        {tag.name}
                    </span>
                ))}
            </div>

            <hr />

            {/* Answers list */}
            {answers.map(answer => (
                <Card key={answer.id} className="mb-sm-1" bg="light">
                    <Card.Body>
                        <p >{answer.body}</p>
                        <small className="text-muted">
                            answered by <strong>{answer.authorUserName}</strong>
                        </small>
                    </Card.Body>
                </Card>
            ))}

            <hr />

            {/* Answer form */}
            <h6> Post your Answer</h6>
            {error && <Alert variant="danger">{error}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-sm-1">
                    <Form.Control
                        as="textarea"
                        rows={5}
                        placeholder="Write your answer here..."
                        value={body}
                        onChange={e => setBody(e.target.value)}
                        className="bg-light"
                        required
                    />
                </Form.Group>
                <Button type="submit" disabled={submitting}>
                    {submitting ? "Posting..." : "Post Your Answer"}
                </Button>
            </Form>

        </Container>
    );
}

export default QuestionDetailsPage;