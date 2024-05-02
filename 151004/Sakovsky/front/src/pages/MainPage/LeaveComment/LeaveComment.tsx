import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ChangeEvent, FC, KeyboardEvent, useState } from "react";
import { BASE_URL } from "../../../constants/constants";
import { tweetStore } from "../../../store/store";
import style from "./LeaveComment.module.css";

type LeaveCommentProps = {
    tweetId: number;
};

export const LeaveComment: FC<LeaveCommentProps> = ({ tweetId }) => {
    const [commentText, setCommentText] = useState("");
    const setTweetComment = tweetStore(state=> state.setTweetCommet)

    const leaveComment = async () => {
        if (commentText) {
            const createCommentDto = {
                tweetId,
                content: commentText,
            };
            try {
                const response = await fetch(BASE_URL + "comments", {
                    method: "POST",
                    body: JSON.stringify(createCommentDto),
                    headers: {
                        "Content-Type": "application/json",
                    },
                });
                if (!response.ok) throw new Error("Дела плохи");
                
                const comment = await response.json();
                setTweetComment(tweetId, comment);
                setCommentText('');
            } catch (error) {
                console.log(error);
            }
        }
    };

    const changeHandler = (e: ChangeEvent<HTMLInputElement>)=> {
        const value = e.target.value;
        setCommentText(value);
    }
    const addComment = (e:KeyboardEvent<HTMLInputElement>) => {
        if(e.key.toLowerCase() === 'enter') {
            leaveComment();
        }
    }

    return (
        <div className={style.leaveComment}>
            <input
                type="text"
                placeholder="Оставить комментарий..."
                className={style.input}
                value={commentText}
                onChange={changeHandler}
                onKeyDown={addComment}
            />
            <button className={style.sendBtn} onClick={leaveComment}>
                <FontAwesomeIcon
                    className={style.paperPlane}
                    icon={faPaperPlane}
                />
            </button>
        </div>
    );
};
