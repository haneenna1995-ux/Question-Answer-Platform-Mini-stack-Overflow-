import 'bootstrap/dist/css/bootstrap.min.css';
import { Routes, Route, Navigate } from 'react-router-dom';
import RegisterPage from "./pages/RegisterPage";
import CreateQuestionPage from "./pages/CreateQuestionPage";
import LoginPage from "./pages/LoginPage.jsx";
import AllQuestionsPage from "./pages/AllQuestionsPage.jsx";
import MyQuestionsPage from "./pages/MyQuestionsPage.jsx";
import EditQuestionPage from "./pages/EditQuestionPage.jsx";
import QuestionDetailsPage from "./pages/QuestionDetailsPage.jsx";



function App() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/askQuestion" element={<CreateQuestionPage />} />
            <Route path="/questions" element={<AllQuestionsPage />} />
            <Route path="/myQuestions" element={<MyQuestionsPage/>} />
            <Route path="/questions/edit/:id" element={<EditQuestionPage/>} />
            <Route path="/questions/:id" element={<QuestionDetailsPage/>} />
        </Routes>
    );
}

export default App;