import { FC } from "react";
import { useNavigate } from "react-router-dom";
import { BASE_URL, routes } from "../../constants/constants";
import styles from "./Navbar.module.css";

type NavbarProps = {
    isAuth: boolean;
};

export const Navbar: FC<NavbarProps> = ({ isAuth }) => {
    const navigate = useNavigate();
    const loginClick = () => {
        navigate(routes.LOGIN_PAGE);
    };
    const registerClick = () => {
        navigate(routes.REGISTER_PAGE);
    };
    const logoutClick = async () => {
        try {
            const response = await fetch(BASE_URL + "auth/logout", {
                method: "POST",
                credentials: "include",
            });

            if (!response.ok) throw new Error("Руки им поотрывайте");

            navigate(routes.LOGIN_PAGE);
        } catch (error) {
            console.log(error);
        }
    };
    return (
        <div className={styles.container}>
            <nav className={styles.nav}>
                <div className={styles.logo}>DC_LAB6</div>
                <div className={styles.buttonsWrap}>
                    {!isAuth ? (
                        <div className={styles.isAuthButtons}>
                            <button className={styles.btn} onClick={loginClick}>
                                Log in
                            </button>
                            <button
                                className={styles.btn}
                                onClick={registerClick}
                            >
                                Register
                            </button>
                        </div>
                    ) : (
                        <button className={styles.btn} onClick={logoutClick}>
                            Log out
                        </button>
                    )}
                </div>
            </nav>
        </div>
    );
};
