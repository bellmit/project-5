import React, {useEffect, useState, useRef} from 'react';
import { useSelector } from 'react-redux';
import JoditEditor from "jodit-react";
import ErrorIcon from '@material-ui/icons/Error';
import '../../../../sass/joditeditor.scss';

const Editor = (props) => {
    useEffect(() => {
        console.log(props.comment, 'props.comment')
    }, [])
    const editor = useRef(null)
    const comment = useRef(null)
    const [content, setContent] = useState(props.comment)
    
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)
	// const config = {
	// 	readonly: props.readonly
    // }
    const readonlyConfig = {
		toolbar: false,
		readonly: true
	}
    const config = {
		buttons: [
			// 'source', '|',
			'bold',
			'strikethrough',
			'underline',
			'italic', '|',
			'ul',
			'ol', '|',
			'outdent', 'indent',  '|',
			'font',
			'fontsize',
			// 'brush',
			// 'paragraph', '|',
			// 'image',
			// 'video',
			'table',
			'link', '|',
			// 'align', 'undo', 'redo', '|',
			// 'hr',
			// 'eraser',
			// 'copyformat', '|',
			// 'symbol',
			'preview',
			'fullsize',
			// 'print',
			// 'about'
		],
		askBeforePasteHTML: false,
		askBeforePasteFromWord: false,
		defaultActionOnPaste: "insert_clear_html",
		toolbar: true,
		readonly: false
	}
    const handleOnBlur = (newContent) => {
        // console.log('on blur')
        // console.log(newContent.target.innerHTML, 'handle on blur')
        setContent(newContent.target.innerHTML);
        props.handleCommentChange(newContent.target.innerHTML, props.index, props.target)
    }
    
    const handleSave = () => {
        props.handleSaveComment(content, props.index, props.target)
    }

    return (
        <div>
            { props.edit
                ?   <div className="drs-editor-wrapper">
                        <JoditEditor
                            ref={editor}
                            value={props.comment}
                            config={config}
                            tabIndex={1} // tabIndex of textarea
                            onBlur={handleOnBlur}
                        />
                        <div style={{display: 'flex', margin: '8px 0'}}>
                            <button className="drs-btn drs-btn-normal"
                                onClick={()=> { props.handleShow(props.target, props.index);}}
                            >
                                Cancel
                            </button>
                            <button className="drs-btn drs-btn-cta"
                                onClick={()=> { handleSave(); props.handleShow(props.target, props.index);}}
                            >
                                Save
                            </button>
                        </div>
                    </div>
                :   d.u.isSp
                    ?   p2m.currentAp.status == p2m.stMap.get(1)  //In Review
                        ?   null
                        :   props.comment == '' 
                            ?   null
                            :   <div style={{border: '1px solid #ced4da',paddingTop: '12px'}}>
                                    {/* <div style={{borderTop: '1px dashed #1976f5',margin: '3% 1%'}}></div> */}
                                    <p class="application-info-section-title" style={{color: '#0052cc'}}>
                                        <ErrorIcon/> DRS 建議
                                    </p>
                                    <div className="drs-editor-wrapper">
                                        <JoditEditor
                                            ref={comment}
                                            value={props.comment}
                                            config={readonlyConfig}
                                            tabIndex={1} // tabIndex of textarea
                                            // onBlur={handleSetContent}
                                        />
                                    </div>
                                </div>
                        
                    :   props.comment == '' 
                        ?   null
                        :   <div style={{paddingTop: '12px'}}>
                                {/* <div style={{borderTop: '1px dashed #1976f5',margin: '3% 1%'}}></div> */}
                                <p class="application-info-section-title" style={{color: '#0052cc',border: '1px solid #ced4da',padding: '8px', borderRadius: '4px'}}>
                                    <ErrorIcon/> DRS 建議
                                </p>
                                <div className="drs-editor-wrapper">
                                    <JoditEditor
                                        ref={comment}
                                        value={props.comment}
                                        config={readonlyConfig}
                                        tabIndex={1} // tabIndex of textarea
                                        // onBlur={handleSetContent}
                                    />
                                </div>
                            </div>
            }
            
        </div>
    )
}

export default Editor;

// :  <div className="comment-wrapper"
                //     style={props.comment == '' ? {display: 'none'}:{display: 'block'}}>
                //         <p style={{display: 'flex', alignItems: 'center'}}>
                //             <span style={{color: '#f0142f', marginRight: '8px'}}>
                //                 <ErrorIcon fontSize="small"/>
                //             </span>
                //             <span>Comments: {props.comment}</span>
                //         </p>
                //   </div>