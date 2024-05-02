import { faA } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styles from "../Forms/Forms.module.css";
import { ChangeEvent, FC } from "react";

type TextInputProps = {
    label: string,
    name: string,
    onChange: (e: ChangeEvent<HTMLInputElement>)=> void,
}

export const TextInput: FC<TextInputProps> = ({label, name, onChange}) => {

    return (
        <div className={styles.inputElem}>
            <div className={styles.inputBox}>
                <input
                    type="text"
                    id="name"
                    name={name}
                    autoComplete="off"
                    onChange={onChange}
                    required
                />
                <label htmlFor="username">{label}</label>
                <FontAwesomeIcon icon={faA} />
            </div>
        </div>
    );
};
