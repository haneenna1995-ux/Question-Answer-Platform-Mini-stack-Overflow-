import { useState, useEffect } from "react";
import { getQuestionById, updateQuestion } from "../services/questionService.js";
import { useParams, useNavigate } from "react-router-dom";
import TagInput from "../components/questions/TagInput.jsx";
import { Container, Form, Button } from "react-bootstrap";

function EditQuestionPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [tags, setTags] = useState([]);

    useEffect(() => {
        getQuestionById(id).then(data => {
            setTitle(data.title);
            setDescription(data.description);
            setTags(data.tags);
        });
    }, [id]);

    function handleSubmit(e) {
        e.preventDefault();
        updateQuestion(id, { title, description, tagIds: tags.map(t => t.id) })
            .then(() => navigate("/myQuestions"));
    }

    return (
        <Container className="mt-4">
            <h2>Edit Question</h2>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>Title</Form.Label>
                    <Form.Control
                        data-cy="edit-title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Description</Form.Label>
                    <Form.Control
                        as="textarea"
                        data-cy="edit-description"
                        rows={4}
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Tags</Form.Label>
                    <TagInput selectedTags={tags} setSelectedTags={setTags} />
                </Form.Group>
                <Button type="submit" data-cy="save-changes" variant="primary">Save Changes</Button>
            </Form>
        </Container>
    );
}

export default EditQuestionPage;