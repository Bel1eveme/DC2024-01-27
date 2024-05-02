import { Author } from "src/entities/Author";
import { Comment } from "src/entities/Comment";

export class TweetResponseTo {
    id: number;
    author: Omit<Author, 'password'>;
    title: string;
    content: string;
    created: Date;
    modified: Date | null;
    comments: Comment[];
}