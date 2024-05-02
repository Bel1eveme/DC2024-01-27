import { ChangeEvent, FormEvent, useState } from "react";
import { ColorRing } from "react-loader-spinner";
import { Link, useNavigate } from "react-router-dom";
import { BASE_URL, routes } from "../../constants/constants";
import { PasswordInput } from "../Inputs/PasswordInput";
import { TextInput } from "../Inputs/TextInput";
import styles from "./Forms.module.css";

interface LoginFormData {
    email: string;
    password: string;
}

export const LoginForm = ({}) => {
    const [warningText, setWarningText] = useState<string>("");
    const [login, setLogin] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [isLoading, setIsLoading] = useState(false);
    const [disabled, setDisabled] = useState(true);
    const navigate = useNavigate();

    const signIn = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        const loginDto = {
            login,
            password,
        };
        try {
            const response = await fetch(BASE_URL + "auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(loginDto),
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error('Неверная почта или пароль');
            }
            const result = await response.json();
            navigate(routes.MAIN_PAGE);
        } catch (error: any) {
            setWarningText(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const onLoginChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setLogin(value);
        if (value === "" || password === "") {
            setDisabled(true);
        } else {
            setDisabled(false);
        }
    };

    const onPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setPassword(value);
        if (value === "" || login === "") {
            setDisabled(true);
        } else {
            setDisabled(false);
        }
    };

    return (
        <>
            <form className={styles.form} onSubmit={signIn}>
                <h2 className={styles.formTitle}>Вход</h2>
                <TextInput
                    label="Логин"
                    name="login"
                    onChange={onLoginChange}
                />
                <PasswordInput onChange={onPasswordChange} />

                <button
                    className={styles.loginBtn}
                    type="submit"
                    disabled={disabled}
                >
                    <div className={styles.buttonContentWrap}>
                        {isLoading && (
                            <ColorRing
                                visible={true}
                                height="18"
                                width="18"
                                ariaLabel="color-ring-loading"
                                wrapperStyle={{}}
                                wrapperClass="color-ring-wrapper"
                                colors={[
                                    "#fff",
                                    "#fff",
                                    "#fff",
                                    "#fff",
                                    "#fff",
                                ]}
                            />
                        )}
                        <span>Войти</span>
                    </div>
                </button>
            </form>
            <div className={styles.register}>
                <p>
                    Ещё нет аккаунта?
                    <Link to={routes.REGISTER_PAGE}>Зарегистрироваться</Link>
                </p>
            </div>
            <div className={styles.warning}>{warningText}</div>
        </>
    );
};
