import { FC } from "react";
import style from "./Tweet.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { CommentList } from "../CommentList/CommentList";
import { CommentItem } from "../../../types/types";
import { title } from "process";

type TweetProps = {
    login: string;
    date: string;
    title: string;
    content: string;
    comments: CommentItem[];
    id: number;
};

export const Tweet: FC<TweetProps> = ({
    login,
    date,
    content,
    comments,
    id,
    title,
}) => {
    return (
        <div className={style.tweetContainer}>
            <div className={style.tweet}>
                <div className={style.tweetHeader}>
                    <div className={style.userIcon}>
                        <FontAwesomeIcon className={style.icon} icon={faUser} />
                    </div>
                    <div className={style.tweetInfo}>
                        <p className={style.userLogin}>{login}</p>
                        <p className={style.date}>{date}</p>
                    </div>
                </div>
                <div className={style.contentWrap}>
                    <h3 className={style.title}>{title}</h3>
                    <p className={style.content}>{content}</p>
                </div>
            </div>
            <div className={style.divider}></div>
            <CommentList comments={comments} tweetId={id} />
        </div>
    );
};
