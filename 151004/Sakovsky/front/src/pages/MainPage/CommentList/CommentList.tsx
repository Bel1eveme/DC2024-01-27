import { FC, useEffect } from "react";
import style from "./CommentList.module.css";
import { Comment } from "../Comment/Comment";
import { LeaveComment } from "../LeaveComment/LeaveComment";
import { CommentItem } from "../../../types/types";

type CommentListProps = {
    comments: CommentItem[];
    tweetId: number;
};

export const CommentList: FC<CommentListProps> = ({ comments, tweetId }) => {
    return (
        <>
            <div className={style.commentList}>
                {comments.map((comment) => (
                    <Comment key={comment.id} content={comment.content} />
                ))}
            </div>
            <div className={style.leaveCommentWrap}>
                <LeaveComment tweetId={tweetId}/>
            </div>
        </>
    );
};
