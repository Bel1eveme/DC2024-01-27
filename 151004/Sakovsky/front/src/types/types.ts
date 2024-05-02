export type Author = {
    id: number;
    login: string;
    firstname: string;
    lastname: string;
};
export type CommentItem = {
    id: number;
    tweetId: number;
    content: string;
    country: string;
};

export type TweetsDto = {
    tweets: TweetItem[];
    total: number;
};

export type TweetItem = {
    id: number;
    author: Author;
    content: string;
    title: string;
    created: string;
    modified: string | null;
    comments: CommentItem[];
};

export type createTweetDto = {
    authorId: number;
    title: string;
    content: string;
};
