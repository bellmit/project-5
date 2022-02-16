import React, {useEffect, useState} from 'react';
import { useSelector} from 'react-redux';
import { LinearProgress} from '@material-ui/core';
import {FormattedMessage} from 'react-intl';

const FileUploader = (props) => {
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)
    const [progress, setProgress] = useState(0)

    const handleupload = (event) => {

        setProgress(0);
        setTimeout(setProgress(25), 3000);
        setTimeout(setProgress(50) , 6000);
        setTimeout(setProgress(75) , 9000);
        setTimeout(setProgress(100) , 12000);
        props.onChange('upload', props.index, event)
    }

    const handleDrop = () => {
        setProgress(0)
        props.onChange('drop', props.index)
    }
    // useEffect(() => {
    //     if(props.fileName == 'Choose a file') {
    //         setProgress(0)
    //     }
    //     console.log('props.id change')
    // },[props.id])

    useEffect(() => {
        if(props.fileName == 'Choose a file') {
            setProgress(0)
        }
        // console.log('something change')
    },[])

    // useEffect(() => {
    //     console.log('initial' , props.id)
    // },[])

    return (
        <div className="base-input-file">
            <div className="base-input-file-uploader">
                <FormattedMessage id="regional.chooseafile">
                    { placeholder =>
                        <label for="file" className={`fileContainer ${props.disabled ? 'disabled':''}`}>
                            <input
                                style={{width: '100%', cursor: `${props.disabled? 'not-allowed': 'pointer'}`}}
                                type="file"
                                onChange={(e)=> handleupload(e)}
                                disabled={props.disabled}
                            >
                            </input>
                            { props.fileName == 'Choose a file'
                                ?   <i class="fa fa-cloud-upload" aria-hidden="true" style={{marginRight: '5px'}}></i> 
                                :   <i class="fa fa-file-image-o" aria-hidden="true" style={{marginRight: '5px'}}></i>
                            }
                            { props.fileName == 'Choose a file'
                                ?   placeholder
                                :   props.fileName
                            }
                        </label>
                    }
                </FormattedMessage>
                { p2m.currentAp.status == p2m.stMap.get(0)
                    ?   <div className="base-input-file-progressbar-wrapper">
                            <LinearProgress variant="determinate" value={progress} />
                        </div>
                    :   null
                }
                
            </div>
            { props.fileName == 'Choose a file'
                ?   null
                :   <div className="base-input-inline-download-link">
                        <a href={props.href} download={props.fileName}>
                            <i class="fa fa-download" aria-hidden="true"></i>
                        </a>
                    </div>
            }
                    
            <button className="base-input-inline-btn"
                onClick={() => handleDrop()}
                disabled={props.disabled}
            >
                <i class="fa fa-trash-o" aria-hidden="true"></i>
            </button>
        </div>
    )
}

export default FileUploader
