import { Route, Routes } from "react-router-dom";
import { AppLayout } from "./layout/AppLayout";
import { LoginPage } from "./pages/LoginPage/LoginPage";
import { RegisterForm } from "./Components/Forms/RegisterForm";
import { RegisterPage } from "./pages/RegisterPage/RegisterPage";
import { MainPage } from "./pages/MainPage/MainPage";

function App() {
    return (
        <Routes>
            <Route path="" element={<AppLayout />}>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/main" element={<MainPage />} />
            </Route>
        </Routes>
    );
}

export default App;
