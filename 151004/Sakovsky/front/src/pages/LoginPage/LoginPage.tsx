import { FC } from "react";
import styles from "./LoginPage.module.css";
import { LoginForm } from "../../Components/Forms/LoginForm";

export const LoginPage = () => {
    return (
        <div className={styles.formWrap}>
            <LoginForm />
        </div>
    );
};
