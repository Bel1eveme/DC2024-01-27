import { faEye } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ChangeEvent, FC, useState } from "react";
import styles from "../Forms/Forms.module.css";

type PasswordInputProps = {
    onChange: (e: ChangeEvent<HTMLInputElement>) => void;
};

export const PasswordInput: FC<PasswordInputProps> = ({ onChange }) => {
    const [passwordVisible, setPasswordVisible] = useState<boolean>(false);

    const mousedownHandle = () => {
        setPasswordVisible(true);
    };
    const mouseupHandle = () => {
        setPasswordVisible(false);
    };

    return (
        <div className={styles.inputElem}>
            <div className={styles.inputBox}>
                <input
                    type={passwordVisible ? "text" : "password"}
                    required
                    onChange={onChange}
                />
                <label htmlFor="userpass">Пароль</label>
                <FontAwesomeIcon
                    icon={faEye}
                    onMouseDown={mousedownHandle}
                    onMouseUp={mouseupHandle}
                    style={{ cursor: "pointer" }}
                />
            </div>
        </div>
    );
};
