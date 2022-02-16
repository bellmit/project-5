import React, { createRef, useMemo, useState ,useEffect ,useCallback } from 'react';
import {useDropzone} from 'react-dropzone';
import axios from 'axios';
import {FormattedMessage} from 'react-intl';
import { DOMAIN_NAME} from '../../../constants/action-types'
import '../../../sass/dropzone.scss';

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
    transition: 'border .24s ease-in-out'
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

export const Dropzone = (props) => {

    const [files, setFiles] = useState([]);

    const onDrop = useCallback((acceptedFiles) => {
        
        setFiles(acceptedFiles.map(file => Object.assign(file, {
              preview: URL.createObjectURL(file)
        })));
        

        acceptedFiles.forEach((file) => {
          const reader = new FileReader()
    
          reader.onabort = () => console.log('file reading was aborted')
          reader.onerror = () => console.log('file reading has failed')
          reader.onload = () => {
          // Do whatever you want with the file contents
           // const binaryStr = reader.result
           // console.log(binaryStr)
          }
          //reader.readAsArrayBuffer(file)
               
         //todo arthur chnage null to 0
         props.onChange(file.name, props.index ? props.index : 0)

          var formData = new FormData();
          formData.append('p2mName' , props.p2mName )
          formData.append('file', file);
          //console.log(formData)
          axios({
            method: "POST",
            url: DOMAIN_NAME + '/p2m/upi',
            data: formData,
            headers: {
              "Content-Type": "multipart/form-data"
                }
            }).then(result=> {
                console.log(result, 'upload POST request result')
            })

        })
        
      }, [])

    const {
        getRootProps,
        getInputProps,
        // acceptedFiles,
        // fileRejections,
        isDragActive,
        isDragAccept,
        isDragReject
    } = useDropzone({
        onDrop ,
        noDrag: true,
        accept: 'image/jpeg, image/png',
        maxFiles: 2
    });

    // const thumbsContainer = {
    //     display: 'flex',
    //     flexDirection: 'row',
    //     flexWrap: 'wrap',
    //     marginTop: 16
    //   };

    // const thumb = {
    //     display: 'inline-flex',
    //     borderRadius: 2,
    //     border: '1px solid #eaeaea',
    //     marginBottom: 8,
    //     marginRight: 8,
    //     width: 100,
    //     height: 100,
    //     padding: 4,
    //     boxSizing: 'border-box'
    //   };
      
    //   const thumbInner = {
    //     display: 'flex',
    //     minWidth: 0,
    //     overflow: 'hidden'
    //   };
      
    //   const img = {
    //     display: 'block',
    //     width: 'auto',
    //     height: '100%'
    //   };

    // const thumbs = files.map(file => (
    //     <div style={thumb} key={file.name}>
    //       <div style={thumbInner}>
    //         <img
    //           src={file.preview}
    //           style={img}
    //         />
    //       </div>
    //     </div>
    //   ));
    
      useEffect(() => () => {
        // Make sure to revoke the data uris to avoid memory leaks
        files.forEach(file => URL.revokeObjectURL(file.preview));
        console.log('files change!!!!')
      }, [files]);

    

    //const [ filename, changeFileName ] = useState({name:''});
    const [ imgsrc, changeSrc ] = useState(props.imgUrl == undefined ? {src: ''}: {src: props.imgUrl});
   
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

    useEffect(() => {
        changeSrc({src: props.imgUrl})
        if (props.imgUrl == '') {
            setFiles([])
        }
        console.log('img.src', props.imgUrl)
    },[props.imgUrl]);

    // const inputEl = React.useRef();

    // const readURL = () => {
    //     const input = document.getElementById(props.id)
    //     if (input.files && input.files[0]) {
    //         //console.log(input.files, 'input.files')
    //         const reader = new FileReader;
          
    //         reader.onload = function(e) {
    //             changeSrc({ src: e.target.result })
    //         }
    //         reader.readAsDataURL(input.files[0])

    //         //todo arthur not finish , have to refactor
    //         var formData = new FormData();
    //         formData.append('file', input.files[0]);

    //         /*
    //         const path = input.value;
    //         const fileName = path.split(/(\\|\/)/g).pop();
    //         console.log('File name:', fileName);
    //         */
            
    //         axios({
    //             method: "POST",
    //             url: DOMAIN_NAME + '/p2m/upi',
    //             data: formData,
    //             headers: {
    //               "Content-Type": "multipart/form-data"
    //             }
    //         }).then(result=> {
    //             console.log(result, 'upload POST request result')
    //         })

    //       //==============    
    //     }
    // }   
    // const acceptedFileItems = acceptedFiles.map(file => (
    //     <li className="dropzone-filename" key={file.path}>
    //         <p className="dropzone-file-text">{file.path} </p>
    //         {/* <p className="dropzone-file-text">{file.size} bytes</p> */}
    //     </li>
    // ));

    // const fileRejectionItems = fileRejections.map(({file, errors}) => (
    //     <li key={file.path}>
    //         {file.path} - {file.size} bytes
    //         <ul>
    //             {errors.map(e => (
    //                 <li key={e.code}>{e.message}</li>
    //             ))}
    //         </ul>
    //     </li>
    // ))

    // const files = acceptedFiles.map(file => <li key={file.path}>{file.path}</li>);

    return (
        <section className="dropzone-container">
            <div {...getRootProps({className: 'dropzone', style})}>
                <input {...getInputProps()}
                    id={props.id}
                    //onChange={(e)=> {
                       // const str = e.target.value.slice(12, e.target.value.length);
                       // if (e.target.value) {
                         //   changeFileName({ name: str })
                          //  console.log('onChange ' + str)
                           // changeSrc({src: str})
                          //  console.log('src => ' + imgsrc.src)
                       // }
                       // readURL();
                        //todo arthur this will cause imgUrl property exception temp comment
                       // props.onChange(str, props.index ? props.index : null)
                        //todo arthur temp comment
                       // props.onChange(e,props.index,props.i)
                   // }}
                    disabled={props.disabled}
                />
                 
                 {  files[0] == undefined ? 
                    imgsrc.src == '' ? <img></img> :
                    <img src={`${DOMAIN_NAME}/p2m/mp/i/${props.p2mName}/${imgsrc.src}`}></img> 
                    : <img src={files[0].preview}></img> 
                  
                 }
                { imgsrc.src  
                    ?   null
                    :   <div className="dropzone-content-wrapper">
                            <i class="fa fa-upload" aria-hidden="true" style={{marginRight: '3px'}}></i>
                            <span style={{marginLeft: '1%'}}>
                                <FormattedMessage id="dropzone.description"/>
                            </span>
                        </div>
                }
            </div>
{/* 
            <aside style={thumbsContainer}>
                {thumbs}
            </aside> */}

            {/*<a href={`${DOMAIN_NAME}/p2m/mp/i/${props.kcode}/${filename.name}`} download={filename.name}>*/}
            <div className="dropzone-file-text-wrapper">

                { imgsrc.src == ''
                    ?   <div></div>
                    :   <div style={{display: 'flex'}}>
                            <a href={`${DOMAIN_NAME}/p2m/mp/i/${props.p2mName}/${imgsrc.src}`} download={imgsrc.src} style={{marginBottom :'auto',marginTop :'auto',marginRight :'5px'}}>
                                <span className="dropzone-filename">
                                    { imgsrc.src }
                                </span>
                            </a>
                            <button className="base-input-inline-btn"
                                onClick={() => props.onChange('', props.index ? props.index : 0)}
                                disabled={props.disabled} style={{marginTop : 'auto', marginBottom :'auto'}}
                            >
                                <i class="fa fa-trash-o" aria-hidden="true"></i>
                            </button>
                        </div>
                } 
 
            </div>
            
        </section>
    );
}
