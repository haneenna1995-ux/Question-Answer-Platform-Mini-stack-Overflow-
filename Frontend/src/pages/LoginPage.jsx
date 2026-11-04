import {useState} from "react";
import {login} from "../services/authService.js";
import { useNavigate, Link } from "react-router-dom";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";


function LoginPage()
{
    const [email, setEmail]= useState("");
    const [password,setPassword]= useState("");
    const [errors, setErrors]= useState({});
    const [message, setMessage]= useState({text:"", type:""});
    const navigate = useNavigate();

    const handleSubmit= async (event)=>{
        event.preventDefault();
        const newErrors={};
        setMessage({text:"",type:""});
        if(!email.trim()) newErrors.email="Email is required"
        if(!password) newErrors.password="Password is required";

        setErrors(newErrors);
        if (Object.keys(newErrors).length > 0) return;

        try
        {
            await login({
                email: email.trim(),
                password
            });
            navigate("/askQuestion")
        }

        catch (error) {
            const status = error.response?.status;
            const data = error.response?.data;

            if (status === 423) {
                setMessage({
                    text: data?.error || "Account locked. Please try again in 15 minutes.",
                    type: "danger",
                });
            } else if (status === 401) {
                setMessage({
                    text: "Invalid email or password.",
                    type: "danger",
                });
            } else {
                setMessage({
                    text: data?.error || "Login failed",
                    type: "danger",
                });
            }
        }
    };
    return(
        <Container className="py-4" style={{ maxWidth: "500px" }}>
            <Card className="shadow-lg">
                <Card.Header as="h5" className="bg-white fw-bold">
                    Log In
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
                        <Form.Group className="mb-3" controlId="loginEmail">
                            <Form.Label className="fw-semibold">Email</Form.Label>
                             <Form.Control
                                 type="email"
                                 placeholder="you@example.com"
                                 value={email}
                                 onChange={(e)=> setEmail(e.target.value)}
                                 isInvalid={!!errors.email}
                                 data-cy="email-input"
                             />
                            <Form.Control.Feedback type="invalid">
                                {errors.email}
                            </Form.Control.Feedback>


                         </Form.Group>

                        <Form.Group className="mb-4" controlId="loginPassword">
                            <Form.Label className="fw-semibold">Password </Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Your password"
                                value={password}
                                onChange={(e)=>setPassword(e.target.value)}
                                isInvalid={!!errors.password}
                                data-cy="password-input"
                             />
                            <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                        </Form.Group>


                        <div className="text-center mb-3">
                            <small className="text-muted">
                                Don't have an account? <Link to="/register" data-cy="link" >Sign up</Link>
                            </small>
                        </div>

                        <div className="d-grid">
                            <Button variant="primary" type="submit" data-cy="submit">
                                Log In
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>


    );

}

export default LoginPage;