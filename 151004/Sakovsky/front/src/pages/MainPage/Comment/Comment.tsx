import { FC } from "react";
import style from './Comment.module.css';

type CommentProps = {
    content: string,
}

export const Comment: FC<CommentProps> = ({content}) => {
    return(
        <div className={style.comment}>
            <p className={style.name}>Who are you, warrior</p>
            <p className={style.commentContent}>{content}</p>
        </div>
    )
}