import { create } from "zustand";
import { CommentItem, TweetItem } from "../types/types";
import { immer } from "zustand/middleware/immer";
import { devtools } from "zustand/middleware";

type State = {
    tweets: TweetItem[];
    userId: null | number;
    userLogin: string | null;
    currPage: number;
    limit: number;
};

type Actions = {
    setUserId: (id: number) => void;
    setUserLogin: (login: string) => void;
    setCurrPage: (page: number) => void;
    setTweets: (tweets: TweetItem[]) => void;
    setTweetCommet: (tweetId: number, comment: CommentItem)=> void;
};

export const tweetStore = create<State & Actions>()(
    devtools(
        immer((set) => ({
            tweets: [],
            userId: null,
            userLogin: null,
            currPage: 1,
            limit: 3,
            setUserId: (id: number) =>
                set((state) => {
                    state.userId = id;
                }),
            setUserLogin: (login: string) =>
                set((state) => {
                    state.userLogin = login;
                }),
            setCurrPage: (page: number) =>
                set((state) => {
                    state.currPage = page;
                }),
            setTweets: (tweets: TweetItem[]) =>
                set((state) => {
                    state.tweets = tweets;
                }),
                setTweetCommet: (tweetId: number, comment: CommentItem) =>
                    set((state) => {
                        state.tweets = state.tweets.map(tweet => {
                            if(tweet.id === tweetId) {
                                tweet.comments.push(comment);
                            }
                            return tweet;
                        });
                    }),
        }))
    )
);
