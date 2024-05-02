import { FC } from "react";
import { TweetItem } from "../../../types/types";
import { Tweet } from "../Tweet/Tweet";
import style from "./TweetList.module.css";

type TweetListProps = {
    tweets: TweetItem[];
};

export const TweetList: FC<TweetListProps> = ({ tweets }) => {
    return (
        <div className={style.list}>
            {tweets.map((tweet) => (
                <Tweet
                    key={tweet.id}
                    login={tweet.author?.login}
                    title={tweet.title}
                    content={tweet.content}
                    date={new Date(tweet.created).toLocaleDateString("ru-Ru")}
                    comments={tweet.comments}
                    id={tweet.id}
                />
            ))}
        </div>
    );
};
