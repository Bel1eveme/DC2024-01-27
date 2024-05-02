import { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { Navbar } from "../Components/Navbar/Navbar";
import { BASE_URL, routes } from "../constants/constants";
import { tweetStore } from "../store/store";
import { Author } from "../types/types";


export const AppLayout = () => {
    const { setUserId, setUserLogin } = tweetStore((state) => state);
    const [isAuth, setIsAuth] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const me = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(BASE_URL + "auth/me", {
                    credentials: "include",
                });
                if (response.status === 401) throw new Error();

                const result = (await response.json()) as Author;
                setUserId(result.id);
                setUserLogin(result.login);
                setIsAuth(true);
                navigate(routes.MAIN_PAGE);
            } catch (error) {
                setIsAuth(false);
                navigate(routes.LOGIN_PAGE);
            } finally {
                setIsLoading(false);
            }
        };

        me();
    }, []);
    return (
        <>
            <Navbar isAuth={isAuth} />
            {!isLoading && <Outlet />}
        </>
    );
};
