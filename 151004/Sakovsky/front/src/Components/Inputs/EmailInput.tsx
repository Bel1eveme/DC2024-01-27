import { faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { FC, useState } from "react";
import styles from "../Forms/Forms.module.css";


export const EmailInput = () => {
    const [emailEmpty, setEmailEmpty] = useState<boolean>(true);
 
    const blurEmailHandler = (e: React.FocusEvent<HTMLInputElement>) => {
        if (e.target.value !== "") {
            setEmailEmpty(false);
        } else {
            setEmailEmpty(true);
        }
    };

    return (
        <div className={styles.inputElem}>
            <div className={styles.inputBox}>
                <input
                    className={!emailEmpty ? styles.valid : ""}
                    type="email"
                    autoComplete="off"
                    required
                    onBlur={blurEmailHandler} 
                />
                <label htmlFor="email">Почта</label>
                <FontAwesomeIcon icon={faEnvelope} />
            </div>
        </div>
    );
};
