import { ChangeEvent, FC, FormEvent, useState } from "react";
import { BASE_URL } from "../../../../constants/constants";
import { tweetStore } from "../../../../store/store";
import { createTweetDto } from "../../../../types/types";
import style from "./CreateTweetModal.module.css";

type CreateTweetModalProps = {
    refetch: (page: number, limit: number, loading?: boolean) => void;
    onSuccess: () => void;
    onRequestStart: () => void;
    onRequestEnd: () => void;
};
export const CreateTweetModal: FC<CreateTweetModalProps> = ({
    refetch,
    onSuccess,
    onRequestStart,
    onRequestEnd,
}) => {
    const { userId, currPage, limit } = tweetStore((state) => state);
    const [title, setTilte] = useState("");
    const [content, setContent] = useState("");

    const onTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setTilte(value);
    };

    const onContentChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
        const value = e.target.value;
        setContent(value);
    };

    const handleSubmit = async (e: FormEvent) => {
        onRequestStart();
        e.preventDefault();
        const createTweetDto: createTweetDto = {
            authorId: Number(userId),
            content,
            title,
        };
        try {
            const response = await fetch(BASE_URL + "tweets", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(createTweetDto),
            });

            if (!response.ok) throw new Error("Беда откуда не ждали");
            onSuccess();
            refetch(currPage, limit, false);
        } catch (error: any) {
            console.log(error.message);
        } finally {
            setTimeout(() => {
                onRequestEnd();
            }, 500);
        }
    };
    return (
        <form className={style.form} onSubmit={handleSubmit}>
            <div className={style.inputs}>
                <div className={style.inputBox}>
                    <label htmlFor="title" className={style.label}>
                        Название
                    </label>
                    <input
                        type="text"
                        id="title"
                        className={style.input}
                        value={title}
                        onChange={onTitleChange}
                    />
                </div>
                <div className={style.inputBox}>
                    <label htmlFor="content" className={style.label}>
                        Контент
                    </label>
                    <textarea
                        id="content"
                        className={style.input}
                        value={content}
                        onChange={onContentChange}
                    />
                </div>
            </div>
            <button
                className={style.button}
                type="submit"
                disabled={!title || !content}
            >
                Создать
            </button>
        </form>
    );
};
