import { useEffect, useState } from "react";
import { ColorRing } from "react-loader-spinner";
import { Paginator } from "../../Components/Paginator/Paginator";
import { BASE_URL } from "../../constants/constants";
import { tweetStore } from "../../store/store";
import { TweetsDto } from "../../types/types";
import { AddTweet } from "./AddTweet/AddTweet";
import style from "./MainPage.module.css";
import { TweetList } from "./TweetList/TweetList";

export const MainPage = () => {
    const { currPage, limit, tweets, setTweets } = tweetStore((state) => state);
    const [total, setTotal] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    const getTweets = async (page: number, limit: number, loading: boolean = true) => {
        setIsLoading(true && loading);
        let query = "";
        if (page) {
            query = `?page=${page}&limit=${limit}`;
        }
        try {
            const response = await fetch(BASE_URL + `tweets${query}`);
            if (!response.ok) throw new Error();
            const result = (await response.json()) as TweetsDto;
            setTweets(result.tweets);
            setTotal(result.total);
        } catch (error) {
            console.log(error);
        } finally {
            setTimeout(()=> {
                setIsLoading(false);
            }, 150)
            
        }
    };
    useEffect(() => {
        getTweets(currPage, limit);
    }, [currPage, limit]);
    return (
        <div className={style.mainPageContainer}>
            <div className={style.mainPage}>
                {isLoading ? (
                    <div className={style.loaderWrap}>
                        <ColorRing
                            visible={true}
                            height="60"
                            width="60"
                            ariaLabel="color-ring-loading"
                            wrapperClass="color-ring-wrapper"
                            colors={[
                                "#63c8ff",
                                "#1d1d9c",
                                "#63c8ff",
                                "#1d1d9c",
                                "#63c8ff",
                            ]}
                        />
                    </div>
                ) : (
                    <>
                        <TweetList tweets={tweets} />
                        <Paginator current={currPage} total={total} />
                    </>
                )}
            </div>

            <AddTweet onAdd={getTweets} />
        </div>
    );
};
