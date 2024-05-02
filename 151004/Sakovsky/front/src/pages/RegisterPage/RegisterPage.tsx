import { RegisterForm } from "../../Components/Forms/RegisterForm";
import styles from "./RegisterPage.module.css";

export const RegisterPage = () => {
    return (
        <div className={styles.formWrap}>
            <RegisterForm />
        </div>
    );
};
