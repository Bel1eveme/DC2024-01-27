import { faAngleLeft, faAngleRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { FC, ReactNode } from "react";
import cn from 'classnames'
import style from './Paginator.module.css';
import { tweetStore } from "../../store/store";

type PaginatorProps = {
    current: number,
    total: number,
}

export const Paginator: FC<PaginatorProps> = ({current, total}) => {
    const {setCurrPage, limit} = tweetStore(state=> state);
    const changePage = (ind: number)=> {
        setCurrPage(ind);
    }
    const pages: ReactNode[] = [];
    for (let i = 0; i < total/limit; i++) {
        const page = (
            <span key={i+1} className={cn(style.paginationItem, {[style.active]: i+1 === current})} onClick={()=> changePage(i+1)}>{i+1}</span>
        );
        pages.push(page);
    }
    return(
        <div className={style.paginator}>
            <span className={cn(style.paginationItem, style.arrow)}><FontAwesomeIcon icon={faAngleLeft}/></span>
            {
                pages
            }
            <span className={cn(style.paginationItem, style.arrow)}> <FontAwesomeIcon icon={faAngleRight}/></span>
        </div>
    )
}