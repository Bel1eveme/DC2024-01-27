import { ChangeEvent, FormEvent, useState } from "react";
import { ColorRing } from "react-loader-spinner";
import { Link, useNavigate } from "react-router-dom";
import { BASE_URL, routes } from "../../constants/constants";
import { PasswordInput } from "../Inputs/PasswordInput";
import { TextInput } from "../Inputs/TextInput";
import styles from "./Forms.module.css";

export const RegisterForm = () => {
    const [warningText, setWarningText] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [login, setLogin] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [firstname, setFirstName] = useState<string>("");
    const [lastname, setLastName] = useState<string>("");
    const navigate = useNavigate();

    const signUp = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        const registerDto = {
            login,
            firstname,
            lastname,
            password,
        };
        try {
            const response = await fetch(BASE_URL + "auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(registerDto),
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error(
                    `Пользователь с логином ${login} уже существует`
                );
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
    };

    const onFirstNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setFirstName(value);
    };

    const onLastNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setLastName(value);
    };

    const onPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setPassword(value);
    };

    return (
        <>
            <form className={styles.form} onSubmit={signUp}>
                <h2 className={styles.formTitle}>Регистрация</h2>

                <TextInput
                    label="Логин"
                    name="логин"
                    onChange={onLoginChange}
                />
                <TextInput
                    label="Имя"
                    name="firstname"
                    onChange={onFirstNameChange}
                />
                <TextInput
                    label="Фамилия"
                    name="lastname"
                    onChange={onLastNameChange}
                />

                <PasswordInput onChange={onPasswordChange} />

                <button
                    className={styles.loginBtn}
                    type="submit"
                    disabled={!login || !firstname || !lastname || !password}
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
                        <span>Зарегистрироваться</span>
                    </div>
                </button>
            </form>
            <div className={styles.register}>
                <p>
                    Уже есть аккаунт?<Link to={routes.LOGIN_PAGE}>Войти</Link>
                </p>
            </div>
            <div className={styles.warning}>{warningText}</div>
        </>
    );
};
