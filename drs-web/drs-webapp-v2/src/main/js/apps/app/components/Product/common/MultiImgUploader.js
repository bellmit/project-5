import React, {useState, useMemo, useCallback, useEffect} from 'react';
import {useDropzone} from 'react-dropzone';
import '../../../sass/uploader.scss';
import ImageIcon from '@material-ui/icons/Image';
import DeleteIcon from '@material-ui/icons/Delete';
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import { LinearProgress } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import axios from 'axios';

const baseStyle = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: '20px',
    borderWidth: 2,
    borderRadius: 2,
    borderColor: '#eeeeee',
    borderStyle: 'dashed',
    backgroundColor: '#fafafa',
    color: '#bdbdbd',
    outline: 'none',
    transition: 'border .24s ease-in-out',
    width: '100%',
    height: '100%',
    minHeight: '220px'
};
const activeStyle = {
    borderColor: '#2196f3'
};
const acceptStyle = {
    borderColor: '#00e676'
};
const rejectStyle = {
    borderColor: '#ff1744'
};

export const MultiImgUploader = (props) => {
    // const {
    //     acceptedFiles,
    //     fileRejections,
    //     getRootProps,
    //     getInputProps,
    //     isDragActive,
    //     isDragAccept,
    //     isDragReject,
    // } = useDropzone({
    //     accept: 'image/jpeg, image/png',
    //     maxFiles: 9
    // })
    const {
        acceptedFiles,
        fileRejections,
        getRootProps,
        getInputProps,
        isDragActive,
        isDragAccept,
        isDragReject,
      } = useDropzone({
        onDrop: (accFiles, rejFiles) => { handleDrop(accFiles, rejFiles) },
        maxSize: 10000 * 1024,
        // onDrop
        // validator: nameLengthValidator
      });

    // const [ filename, changeFileName ] = useState({name:''});

    const style = useMemo(() => ({
        ...baseStyle,
        ...(isDragActive ? activeStyle : {}),
        ...(isDragAccept ? acceptStyle : {}),
        ...(isDragReject ? rejectStyle : {})
    }), [
        isDragActive,
        isDragReject,
        isDragAccept
    ]);

    // const maxLength = 20;

    // function nameLengthValidator(file) {
    //     if (file.name.length > maxLength) {
    //         return {
    //         code: "name-too-large",
    //         message: `Name is larger than ${maxLength} characters`
    //         };
    //     }

    //     return null
    // }
    const [progress, setProgress] = useState(0);


    const [accFiles, setAccFiles] = useState(acceptedFiles)
    const [rejFiles, setRejFiles] = useState(fileRejections)

    const onDeleteAccFile = (file) => {
        const curr = accFiles;
        const result = curr.filter((item) => item !== file)
        setAccFiles(result);
    }
    const onDeleteRejFile = ({ file, errors }) => {
        const curr = rejFiles;
        const result = curr.filter((item) => {
            return item.file !== file
        })
        // // setRejFiles((curr) => curr.filter((fw) => fw.file !== file));
        setRejFiles(result)
    }

    const handleDrop = useCallback((accFiles, rejFiles) => {
        // console.log(accFiles, rejFiles)
        // const mappedAcc = accFiles.map((file) => ({ , errors }));
        // const mappedRej = rejFiles.map((file) => ({ file, errors }));
        // console.log(mappedAcc, mappedRej)
        
        accFiles.map(file => {
            Object.assign(file, {preview: URL.createObjectURL(file)})     
        });

    
          
        setAccFiles((curr) => [...curr, ...accFiles]);
        setRejFiles((curr) => [...curr, ...rejFiles]);
        // setProgress(0);
        // setTimeout( setProgress(25) , 3000);
        // setTimeout( setProgress(50) , 6000);
        // setTimeout( setProgress(75) , 9000);
        // setTimeout( setProgress(100) , 12000);

    }, []);

    // const onDrop = useCallback((accFiles, rejFiles) => {
    //     console.log(accFiles, rejFiles)
    //     const mappedAcc = accFiles.map((file) => ({ file, errors }));
    //     const mappedRej = rejFiles.map((file) => ({ file, errors }));
    //     console.log('drop')
    //     setAccFiles((curr) => [...curr, ...mappedAcc]);
    //     setRejFiles((curr) => [...curr, ...mappedRej]);
    // }, []);



    // const acceptedFileItems = acceptedFiles.map(file => (
    //     <li key={file.path}>
    //         <ImageIcon/>
    //         <span>{file.path} - {file.size} bytes</span>
    //         <button onClick={() => {console.log('delete')}}><DeleteIcon/></button>
    //     </li>
    // ));

    // const fileRejectionItems = fileRejections.map(({ file, errors }) => (
    //     <li key={file.path}>
    //         {file.path} - {file.size} bytes
    //         <ul>
    //             {errors.map(e => (
    //             <li key={e.code}>{e.message}</li>
    //             ))}
    //         </ul>
    //     </li>
    // ));
    // useEffect(() => {
    //    console.log(props.index)
    // },[])

    useEffect(() => {
        console.log('accFiles change', accFiles)

    }, [accFiles])


    return (

            <div className="uploader-modal">
               
                <div className="uploader-container">
                    <div className="uploader-container-title-wrapper">
                        <p className="uploader-container-title">上傳圖片</p>
                        <div className="onClose-btn"><button onClick={() => props.onClose(false)}><CloseIcon/></button></div>
                    </div>
                    <div className="uploader-column-container" style={ accFiles.length == 0 && rejFiles.length == 0 ? {flexDirection: 'column'} : null }>
                        <div className="uploader-column" style={ accFiles.length == 0 && rejFiles.length == 0 ? {width: '100%'} : {width: '50%'} }>
                        
                            <section className="dropzone-container" style={{width: '100%', height: '100%', minHeight: '260px'}}>
                                <div {...getRootProps({className: 'dropzone', style})}>
                                    <input {...getInputProps()}
                                        // id={props.id}
                                        // ref={inputEl}
                                        // onChange={(e)=> {
                                        //     const str = e.target.value.slice(12, e.target.value.length);
                                        //     if (e.target.value) {
                                        //         changeFileName({ name: `已上傳： ${str}` })
                                        //     }
                                            
                                        // }}
                                    />
                                    
                                <div className="dropzone-content-wrapper">
                                        <CloudUploadIcon/>
                                        <span className="dropzone-description">Drag files here or click to browse in your computer</span>
                                    </div>
                                    
                                </div>
                                
                            </section>
                        </div>
                        { accFiles.length == 0 && rejFiles.length == 0
                            ? null
                            : <div className="uploader-column"  style={ accFiles.length == 0 && rejFiles.length == 0 ?  {width: '50%'} : {width: '100%'}}>
                                <div className="uploader-column-sub-section">
                                    {accFiles.length ? <p className="uploader-column-title" style={{color: '#1976f5'}}>Accepted files</p> : null}
                                    {/* <ul>{acceptedFileItems}</ul> */}
                                    <ul>
                                        { accFiles.map(file => (
                                            <li className="upload-file-li" key={file.path}>
                                                <div className="upload-file-info-wrapper">
                                                    <div className="upload-file-icon"><ImageIcon/></div>
                                                    
                                                    <div className="upload-file">
                                                        <LinearProgress variant="determinate" value={progress}/>
                                                
                                                        <div className="upload-file-info">
                                                            <span className="upload-file-info-text">{file.path}</span>
                                                            <span className="upload-file-info-text">{file.size} bytes</span>
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <button onClick={() => {onDeleteAccFile(file)}}><DeleteIcon/></button>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            
                                <div className="uploader-column-sub-section">
                                    {rejFiles.length ? <p className="uploader-column-title" style={{color: '#f0142f'}}>Rejected files</p> : null}
                                    {/* <ul>{fileRejectionItems}</ul> */}
                                    <ul>
                                        { rejFiles.map(({ file, errors }) => (
                                            <li className="upload-file-li" key={file.path}>
                                                <div className="upload-file-info-wrapper">
                                                <div className="upload-file-icon"><ImageIcon/></div>
                                                
                                                
                                                    <div className="upload-file">
                                                        <LinearProgress variant="determinate" value={progress} color="secondary"/>
                                                        <div className="upload-file-info">
                                                            <span className="upload-file-info-text">{file.path}</span>
                                                            <span className="upload-file-info-text">{file.size} bytes</span>
                                                        </div>
                                                        <ul className="upload-file-error">
                                                            { errors.map(e => (
                                                                <li className="upload-file-error-text" key={e.code}>{e.message}</li>
                                                            ))}
                                                        </ul>
                                                    </div>
                                                </div>
                                                
                                                <button onClick={() => {onDeleteRejFile({ file, errors })}}><DeleteIcon/></button>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                                
                                <div className="uploader-btngroup-wrapper">
                                    <button className="btn btn-normal" onClick={() => {
                                         console.log('submit upload');
                                        props.onSubmit(accFiles, props.index)
                                    }}>上傳</button>
                                    <button className="btn" onClick={() => props.onClose(false)}> 取消 </button>
                                </div>
                                
                            </div>
                        }
                        
                    </div>
                    
                </div>
            </div>
        
    )
}