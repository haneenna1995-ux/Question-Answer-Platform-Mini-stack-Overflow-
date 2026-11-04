import { useState } from "react";
import { register } from "../services/authService.js";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

// Front-end validation rules — keep in sync with backend RegisterRequest DTO.
const USERNAME_REGEX = /^[a-zA-Z0-9_]{3,20}$/;
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/;

function RegisterPage() {
    const [userName, setUserName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState({ text: "", type: "" });

    const handleSubmit = async (event) => {
        event.preventDefault();

        const newErrors = {};
        setMessage({ text: "", type: "" });

        if (!userName.trim()) {
            newErrors.userName = "Username is required";
        } else if (!USERNAME_REGEX.test(userName.trim())) {
            newErrors.userName = "3–20 characters. Letters, numbers, or underscore only.";
        }

        if (!email.trim()) {
            newErrors.email = "Email is required";
        } else if (!EMAIL_REGEX.test(email.trim())) {
            newErrors.email = "Enter a valid email address.";
        }

        if (!password) {
            newErrors.password = "Password is required";
        } else if (!PASSWORD_REGEX.test(password)) {
            newErrors.password = "At least 8 characters, with one uppercase, one lowercase, and one number.";
        }

        setErrors(newErrors);
        if (Object.keys(newErrors).length > 0) return;

        try {
            await register({
                userName: userName.trim(),
                email: email.trim(),
                password,
            });

            setUserName("");
            setEmail("");
            setPassword("");
            setErrors({});
            setMessage({ text: "Account created. You can now log in.", type: "success" });
        } catch (error) {
            const data = error.response?.data;

            // Backend returns a field map for validation errors (400)
            if (data && typeof data === "object" && !data.error) {
                setErrors(data);
                setMessage({ text: "Please fix the errors below.", type: "danger" });
            } else {
                setMessage({
                    text: data?.error || "Registration failed",
                    type: "danger",
                });
            }
        }
    };

    return (
        <Container className="py-4" style={{ maxWidth: "500px" }}>
            <Card className="shadow-lg">
                <Card.Header as="h5" className="bg-white fw-bold">
                    Create an Account
                </Card.Header>
                <Card.Body>
                    {message.text && (
                        <Alert
                            variant={message.type}
                            dismissible
                            onClose={() => setMessage({ text: "", type: "" })}
                        >
                            {message.text}
                        </Alert>
                    )}

                    <Form onSubmit={handleSubmit} noValidate>
                        <Form.Group className="mb-3" controlId="registerUserName">
                            <Form.Label className="fw-semibold">Username</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Your name"
                                value={userName}
                                onChange={(e) => setUserName(e.target.value)}
                                isInvalid={!!errors.userName}
                                data-cy="username-input"
                            />

                            <Form.Control.Feedback type="invalid">
                                {errors.userName}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="registerEmail">
                            <Form.Label className="fw-semibold">Email</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="you@example.com"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                isInvalid={!!errors.email}
                                data-cy= "email-input"
                            />

                            <Form.Control.Feedback type="invalid">
                                {errors.email}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-4" controlId="registerPassword">
                            <Form.Label className="fw-semibold">Password</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Choose a strong password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                isInvalid={!!errors.password}
                                data-cy="password-input"
                            />

                            <Form.Control.Feedback type="invalid">
                                {errors.password}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <div className="text-center mb-3">
                            <small className="text-muted">
                                Already have an account? <a data-cy ="link" href="/login">Log in</a>
                            </small>
                        </div>

                        <div className="d-grid">
                            <Button variant="primary" type="submit" data-cy="submit">
                                Sign Up
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}

export default RegisterPage;