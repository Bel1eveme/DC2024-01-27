import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import cn from 'classnames';
import { FC, useEffect, useRef, useState } from "react";
import style from "./AddTweet.module.css";
import { CreateTweetModal } from "./CreateTweetModal/CreateTweetModal";
import { ColorRing } from "react-loader-spinner";

type AddTweetProps = {
    onAdd: (page: number, limit: number, loading?: boolean)=> void
}

export const AddTweet: FC<AddTweetProps> = ({onAdd}) => {
    const [isModal, setisModal] = useState(false);
    const [isLoading, setisLoading] = useState(false);
    const elemRef = useRef<HTMLDivElement>(null);

    const closeModdal = ()=> setisModal(false);

    const showModal = ()=> {
        if(!isModal){
            setisModal(true);
        }
    }

    const showLoading = ()=> setisLoading(true);
    const hideLoading = ()=> setisLoading(false);

    useEffect(()=> {
        const onOutsideModalClick = (e: MouseEvent  )=> {
            if (!elemRef.current?.contains(e.target as HTMLElement)) {
                closeModdal();
            }
        }

        window.addEventListener('mouseup', onOutsideModalClick);

        return ()=> window.removeEventListener('mouseup', onOutsideModalClick);
    }, [])
    return (
        <div className={cn(style.addBtn, {[style.modal]: isModal})} onClick={showModal} ref={elemRef}>
            {!isModal && (isLoading ? <ColorRing
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
                        /> : <FontAwesomeIcon className={style.icon} icon={faPlus} />)}
            {isModal && <CreateTweetModal onRequestStart={showLoading} onRequestEnd={hideLoading} onSuccess={closeModdal} refetch={onAdd}/>}
            
        </div>
    );
};
