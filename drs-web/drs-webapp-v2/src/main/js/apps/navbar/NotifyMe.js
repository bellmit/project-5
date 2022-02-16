/*
MIT License

Copyright (c) 2020 Tapas Adhikary

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
import React, { useState, useEffect, useRef } from 'react';
// import { useSelector, useDispatch } from 'react-redux'
import PropTypes from "prop-types";

import Overlay from 'react-bootstrap/Overlay';
import Popover from 'react-bootstrap/Popover';
// import Button from 'react-bootstrap/Button';
import moment from 'moment';
import { reactLocalStorage } from 'reactjs-localstorage';
import { Bell, BellOff, BookOpen, AlertTriangle } from 'react-feather';
import NotificationsIcon from '@material-ui/icons/Notifications';
import ClearIcon from '@material-ui/icons/Clear';
import BookmarkIcon from '@material-ui/icons/Bookmark';
import InfiniteScroll from 'react-infinite-scroll-component';
import Tooltip from '@material-ui/core/Tooltip';
import axios from 'axios';
import {DOMAIN_NAME} from './constants/index'

// import 'bootstrap/dist/css/bootstrap.min.css';
import './notification.scss';

const Styles = {
    // drsNotification: {
    //     background: 'transparent',
    //     border: '0',
    //     fontSize: '1rem',
    //     display: 'flex',
    //     outline: '0',
    //     color: '#9B9B9B',
    //     // color: '#344563',
    //     cursor: 'pointer',
    //     textAlign: 'left'
    // },
    drsPopover: {
        position: 'absolute',
        inset: '0px auto auto 0px',
        // margin: '0px',
        transform:'translate3d(-212px, 29px, 0px)!important',
        width: '500px',
        boxShadow: 'rgb(9 30 66 / 25%) 0px 4px 8px -2px, rgb(9 30 66 / 31%) 0px 0px 1px'
        // backgroundColor: '#fff'
    }
  }

const NotifyMe = props => {

    moment.locale(navigator.languages[0].toLowerCase());

    // State variabls
    const [showCount, setShowCount] = useState(true);
    const [messageCount, setMessageCount] = useState(0);
    const [show, setShow] = useState(false);
    const [target, setTarget] = useState(null);
    const [readIndex, setReadIndex] = useState(0);

    // Useref for the overlay
    const ref = useRef(null);

    // Props passed to the component
    // const data = props.data;
    const storageKey = props.storageKey || 'notification_timeline_storage_id';
    const key = props.notific_key;
    const notificationMsg = props.notific_value;
    const notificLink = props.notific_link;
    const topic = props.topic
    const currentIndex = props.notific_index;
    const read = props.notific_read;
    const clear = props.notific_clear;
    const sortedByKey = props.sortedByKey;
    const heading = props.heading || 'Notifications';
    const bellSize = props.size || 32;
    const bellColor = props.color || '#344563';
    const multiLineSplitter = props.multiLineSplitter || '\n';
    const showDate = props.showDate || false;

    const [data, setData] = useState([]);
    const [totalPages, setTotalPages] = useState([]);
    const [pageIndex, setPageIndex] = useState([]);
    const [hasMore, setHasMore] = useState(true)

    // when initiate
    useEffect(() => {

        initListener(props.uid);

        axios.post(DOMAIN_NAME + '/c/n',{
            pi:1
        },{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }).then(res => {
            if (res != null) {
                let result = res.data.notifications;
                result.map(item => item["clear"] = false);
                setData(result);
                setTotalPages(res.data.totalPages);
                setPageIndex(res.data.pageIndex)
            }
          })
          
        
    }, [])

    useEffect(() => {
        if (data.length == 0) {
            setHasMore(false)
        }
        let count = 0
        data.map(item => {
            if(!item[read] && !item[clear]) {
               count = count + 1
            }
        })
        count > 0 ? setShowCount(true) : setShowCount(false)
        setMessageCount(count)
    }, [data])



    const initListener = (uid) => {

   
        const eventSource = new EventSource( DOMAIN_NAME + "/subscription/"+uid);
        eventSource.onopen = (e) => {
             //console.log("open");
             //console.log(e)
        } 
    
        eventSource.onerror = (e) => {
          if (e.readyState == EventSource.CLOSED) {
            // console.log("close");
           } else {
            // console.log(e);
           }
          //this.initListener();
        };
    
        eventSource.addEventListener(
          //this.state.loggedInUser,
          "test",
          handleServerEvent,
          false
        );
        
      };
    
     const handleServerEvent = (e) => {
        const json = JSON.parse(e.data);
        setData(result => [...result , json]);
        
    
      //  let newNotifications = this.state.newNotifications;
        //newNotifications.unshift({
          //from: json.from,
          //message: json.message,
          //isRead: false,
        //});
    
      };


    // useEffect(() => {
    //     if (!sortedByKey) {
    //         data.sort((a, b) => b[key] - a[key]);
    //     }

    //     // We read if any last read item id is in the local storage
    //     let readItemLs = reactLocalStorage.getObject(storageKey);
    //     let readMsgId = Object.keys(readItemLs).length > 0 ? readItemLs['id'] : '';

    //     // if the id found, we check what is the index of that message in the array and query it. If not found,
    //     // nothing has been read. Hence count should be same as all the message count.
    //     let readIndex = (readMsgId === '') ? data.length : data.findIndex(elem => elem[key] === readMsgId);

    //     // if the id is not found, it all flushed out and start again
    //     readIndex === -1 ? readIndex = data.length : readIndex;

    //     setReadIndex(readIndex);

    //     // If there are messages and readIndex is pointing to at least one message, we will show the count bubble.
    //     (data.length && readIndex) > 0 ? setShowCount(true) : setShowCount(false);
        
    //     setMessageCount(readIndex);
    // }, [data]);

    // Handle the click on the notification bell
    const handleClick = (event) => {
        setShow(!show);
        setTarget(event.target);
    }

    // Calculate the day diff
    const getDayDiff = timestamp => {
        let a = moment();
        let b = moment(timestamp);
        let diff = a.diff(b, 'year');
        if (diff === 0) {
            diff = a.diff(b, 'month');
            if (diff === 0) {
                diff = a.diff(b, 'days');
                if (diff === 0) {
                    diff = a.diff(b, 'hour');
                    if (diff === 0) {
                        diff = a.diff(b, 'minute');
                        if (diff === 0) {
                            diff = a.diff(b, 'second');
                            return `${diff} second(s) before`;
                        } else {
                            return `${diff} minute(s) before`;
                        }
                    } else {
                        return `${diff} hour(s) before`;
                    }
                } else {
                    return `${diff} days(s) before`;
                }
            } else {
                return `${diff} month(s) before`;
            }
        } else {
            return `${diff} year(s) before`;
        }
    };

    const getWhen = timestamp => {
        let when = `${moment(timestamp).format('L')} ${moment(timestamp).format('LTS')}`;
        return when;
    }

    // Get the notification message
    // const getContent = message => {
    //     console.log(message, 'message');
    //     if (message.indexOf(multiLineSplitter) >= 0) {
    //         let splitted = message.split(multiLineSplitter);
    //         let ret = '<ul>';

    //         for (let i = 0; i <= splitted.length - 1; i++) {
    //             if (splitted[i] !== '') {
    //                 ret = ret + '<li>' + splitted[i] + '</li>';
    //             }
    //         }

    //         ret = ret + '</ul>';
    //         return {
    //             __html: ret
    //         };
    //     }
    //     return {
    //         __html: `<ul><li>${message}</li></ul>`
    //     };
    // };

    // Hide the notification on clicking outside
    const hide = () => {
        setShow(false);
    }

    // Call the function when mark as read link is clicked
    const markAllAsRead = () => {
        

        axios.post(DOMAIN_NAME + '/c/manr',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          });

        const arr = [...data];
        arr.map(item => item[read] = true)
        setData(arr);
        setShowCount(false);
        reactLocalStorage.setObject(storageKey, { 'id': data[0][key] });
        setReadIndex(0);


    }
 
    const fetchMore = () => {
    
        if (totalPages == pageIndex) {
            setHasMore(false)
        }

        const asyncCallback = async () =>{
        
            axios.post(DOMAIN_NAME + '/c/n',{
                pi:(pageIndex+1)
            },{
                headers: { 
                  'Content-Type': 'application/json',
                  'Access-Control-Allow-Origin': '*',
                }
              }).then(res => {
                if (res != null) {
    
                    let nextData = res.data.notifications;                
                    const result = [...data];
                    nextData.map(item => result.push(item));
                
                    setData(result);
                    setTotalPages(res.data.totalPages);
                    setPageIndex(res.data.pageIndex)
                }
              })

        }
        asyncCallback()
    }
    

    const handleClear = (index) => {
        const arr = [...data];
        if (confirm('Are you sure to clear this message? It will not show up again.')) {
            axios.post(DOMAIN_NAME + '/c/dn',{
                id:arr[index]._id.$oid
            },{
                headers: { 
                  'Content-Type': 'application/json',
                  'Access-Control-Allow-Origin': '*',
                }
              });
    
            // copyData.splice(index, 1);
            arr[index][clear] = true;
            setData(arr);
            
            const tar = document.getElementById(`message-${index}`);
            const next = document.getElementById(`message-${index+1}`);
            tar.className = 'drs-notification-message fade-out'
            
            setTimeout(function() {
                next.className = "drs-notification-message move-up"
            }, 100)
            setTimeout(function() {
                tar.style.display = "none";
            }, 400);
            
            // setListClass('drs-notification-message fade-out');
        }
    }
    
    const handleMarkRead = (index) => {

        const arr = [...data];
    
        arr[index][read] ? arr[index][read] = false : arr[index][read] = true;

        let url = DOMAIN_NAME + '/c/mnur'
        if( arr[index][read]){
            url = DOMAIN_NAME + '/c/mnr'
        }

        axios.post(url,{
            id:arr[index]._id.$oid
        },{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        });

        setData(arr)
    }


    return (
        <>
            <div className="drs-notify-icon-wrapper">
                <div className={showCount ? 'drs-notification drs-notify show-count' : 'drs-notification drs-notify'}
                    // style={ Styles.drsNotification}
                    data-count={messageCount}
                    onClick={event => handleClick(event)}>
                    {/* <Bell color={bellColor} size={bellSize} /> */}
                    <span className="drs-notification-icon-wrapper"><NotificationsIcon/></span>
                    
                </div>
            </div>

            <div ref={ref}>
                <Overlay
                    show={show}
                    target={target}
                    // placement="bottom"
                    placement="bottom-start"
                    container={ref.current}
                    containerPadding={20}
                    rootClose={true}
                    onHide={hide}
                >
                    <Popover id="popover-contained" style={Styles.drsPopover}>
                        { data.length > 0 ? <Popover.Title as="h3">{heading}</Popover.Title> : null}
                        <Popover.Content style={{ padding: '3px 3px' }}>
                            {showCount && 
                            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center',padding: '6px 0'}}>
                                <span style={{margin: 'auto 3%', lineHeight: '1.5rem',color: '#585a5c', fontSize: '13px'}}>Latest</span>
                                <button style={{color: '#007bff', fontSize: '1em', margin: 'auto 3%', fontSize: '13px', fontWeight: '500'}}
                                    // onClick={props.markAsReadFn || markAsRead}
                                    onClick={()=> markAllAsRead()}
                                >
                                    {/* <BookOpen size={24} /> */}
                                    Mark all as read
                                </button>
                            </div>
                            }
                            
                            <ul className="drs-notification-info-panel">
                                <InfiniteScroll
                                    dataLength={data.length}
                                    height={data.length == 0 ? 50 : 300}
                                    next={fetchMore}
                                    hasMore={hasMore}
                                    loader={<h5>Loading...</h5>}
                                    // endMessage={
                                    //     <p style={{ textAlign: 'center' }}>
                                    //         <b>End of Notifications</b>
                                    //     </p>
                                    // }
                                >
                                {
                                    data.length > 0 ?
                                    data.map((message, index) => 
                                        // <li className={index < readIndex ? 'drs-notification-message unread' : 'drs-notification-message'}
                                        
                                        <li className='drs-notification-message'
                                            key={index} id={`message-${index}`}>
                                            <div style={{display: 'flex', justifyContent: 'space-between'}}>
                                                <div>
                                                    <div>
                                                        <span className="drs-notification-content">{message[topic]}</span>
                                                    </div>
                                                    <div>
                                                        <span><a className="drs-notification-link" href={message[notificLink]}>{message[notificationMsg]}</a></span>
                                                    </div>
                                                    <div>
                                                        <span className="drs-notification-timestamp">{getDayDiff(message[key])}</span>
                                                        {showDate && <span style={{fontSize: '12px'}}>{' ('}{getWhen(message[key])}{')'}</span>}
                                                    </div>
                                                </div>
                                                <div style={{display: 'flex', justifyContent: 'flex-end'}}>
                                                    <span className="drs-notification-list-icon" onClick={() => handleClear(index)}>
                                                        <Tooltip title="clear" aria-label="clear">
                                                            <ClearIcon fontSize="small"/>
                                                        </Tooltip>
                                                    </span>
                                                    <span className={message[read]? "drs-notification-list-icon read" : "drs-notification-list-icon"}
                                                        onClick={() => handleMarkRead(index)}>
                                                        <Tooltip
                                                            title={message[read]? "mark as unread": "mark as read"}
                                                            aria-label={message[read]? "mark as unread": "mark as read"}
                                                        >
                                                            <BookmarkIcon fontSize="small"/>
                                                        </Tooltip>
                                                    </span>
                                                </div>
                                            </div>
                                        </li>
                                    
                                    ) :

                                    <>
                                        {/* <AlertTriangle color='#000000' size={24} />
                                        <h5 className="nodata">No Notifications found!</h5> */}
                                        <div style={{display: 'flex', alignItems: 'center',height: '100%',padding: '3%'}}>
                                            <p style={{textAlign: 'center'}}>No Notifications</p>
                                        </div>
                                    </>
                                }
                                </InfiniteScroll>
                            </ul>
                        </Popover.Content>
                    </Popover>
                </Overlay>
            </div>
        </>
    )
};

NotifyMe.prototype = {
    storageKey: PropTypes.string,
    notific_key: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    notific_value: PropTypes.string.isRequired,
    sortedByKey: PropTypes.bool,
    color: PropTypes.string,
    size: PropTypes.string,
    heading: PropTypes.string,
    multiLineSplitter: PropTypes.string,
    showDate: PropTypes.bool,
    markAsReadFn: PropTypes.func
}

export default NotifyMe;