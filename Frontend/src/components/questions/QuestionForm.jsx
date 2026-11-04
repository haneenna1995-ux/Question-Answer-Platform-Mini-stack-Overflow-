import { useState } from "react";
import { createQuestion } from "../../services/questionService.js";
import TagInput from "./TagInput.jsx";
import {Button} from "react-bootstrap";
import {Form} from "react-bootstrap";
import {Container} from "react-bootstrap";
import {Card} from "react-bootstrap";
import {Alert} from "react-bootstrap";

function QuestionForm() {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [selectedTags, setSelectedTags] = useState([]);
    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState({text:"", type:""});

    const handleSubmit = async (event) => {
        event.preventDefault();

        const newErrors = {};
        setMessage({text: "", type: ""});

        if (!title.trim()) {
            newErrors.title = "Title is required";
        }

        if (!description.trim()) {
            newErrors.description = "Description is required";
        }

        if (selectedTags.length === 0) {
            newErrors.tags = "At least one tag is required";
        }

        setErrors(newErrors);

        if (Object.keys(newErrors).length > 0) {
            return;
        }

        const questionData = {
            title: title.trim(),
            description: description.trim(),
            tagIds: selectedTags.map(tag=>tag.id),
        };

        try {
            await createQuestion(questionData);

            setTitle("");
            setDescription("");
            setSelectedTags([]);
            setErrors({});
            setMessage({text: "Question created successfully",type: "success"});
        } catch (error) {
            console.error("Error creating question:", error);
            setMessage({ text: error.message || "Failed to create question", type: "danger"});
        }
    };

    return (
        <Container className="py-4" style={{ maxWidth: "800px" }}>
            <Card className="shadow-lg">
                <Card.Header as="h5" className="bg-white fw-bold">
                    Ask a Question
                </Card.Header>
                <Card.Body>
                    {message.text && (
                        <Alert variant={message.type} dismissible onClose={() => setMessage({ text: "", type: "" })}>
                            {message.text}
                        </Alert>
                    )}

                    <Form onSubmit={handleSubmit} noValidate>
                        <Form.Group className="mb-3" controlId="questionTitle">
                            <Form.Label className="fw-semibold">Title</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="e.g. How do I reverse a string in Python?"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                isInvalid={!!errors.title}
                                data-cy="question-title"
                            />
                            <Form.Text className="text-muted">
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                {errors.title}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="questionDescription">
                            <Form.Label className="fw-semibold">Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={6}
                                placeholder="Describe your problem in detail..."
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                isInvalid={!!errors.description}
                                data-cy="question-description"
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.description}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-4" controlId="questionTags">
                            <Form.Label className="fw-semibold">Tags</Form.Label>
                            <TagInput
                                selectedTags={selectedTags}
                                setSelectedTags={setSelectedTags}
                            />
                            {errors.tags && (
                                <div className="text-danger small mt-1">{errors.tags}</div>
                            )}
                            <Form.Text className="text-muted">
                            </Form.Text>
                        </Form.Group>

                        <div className="d-flex justify-content-end">
                            <Button variant="primary" type="submit" data-cy="submit">
                                Post Your Question
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}

export default QuestionForm;