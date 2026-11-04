import { useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";
import { logout } from "../../services/authService";

function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <Button variant="outline-danger" onClick={handleLogout}>
            Logout
        </Button>
    );
}

export default LogoutButton;