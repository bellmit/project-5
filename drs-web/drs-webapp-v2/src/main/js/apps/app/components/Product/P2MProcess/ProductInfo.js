import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux'
import { Link,Redirect, useParams } from 'react-router-dom';
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css';
import RadioButton from '../common/RadioButton'
import { initP2MProductInfo, saveP2MProductInfo, updatePai, getExactApplication } from '../../../actions';
// import Example from '../../../assets/images/Example@2x.png';
import { MultiImgUploader } from '../common/MultiImgUploader';
import Select from 'react-select';
// import InfoIcon from '@material-ui/icons/Info';
import InsertPhotoIcon from '@material-ui/icons/InsertPhoto';
import RateReviewIcon from '@material-ui/icons/RateReview';
import Comment from './common/Comment';
import {FormattedMessage} from 'react-intl';
import axios from 'axios';
import {DOMAIN_NAME} from '../../../constants/action-types';
import { toast } from 'react-toastify';
import FileUploader from './common/FileUploader';
import '../../../sass/table.scss';
import P2MApplicationInfo from './P2MApplicationInfo';
// import { Tooltip } from '@material-ui/core';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import Modal from '@material-ui/core/Modal';
import OpenInNewIcon from '@material-ui/icons/OpenInNew';


const customStyles = {
    control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#d7dbec',
        minHeight: '37px',
        boxShadow: state.isFocused ? null : null,
    }),
    valueContainer: (provided, state) => ({
        ...provided,
        position:'static',
        display: 'grid',
        padding: '0 6px'
    }),
    multiValue: (provided, state) => ({
        ...provided,
        width: 'fit-content',
    }),
    input: (provided, state) => ({
        ...provided,
        margin: '0px',
        padding: '0px',
    }),
    indicatorSeparator: state => ({
        display: 'none',
    }),
    indicatorsContainer: (provided, state) => ({
        ...provided,
        height: '100%',
    }),
};

const batteryCategoryOptions =  [
    { value: '無電池(No battery)',
      label: <FormattedMessage id="productinfo.nobattery"/>
    },
    { value: '鹹性電池(Alkali-manganese batteries)、碳鋅電池(Zinc-Carbon batteries)、鎳鎘電池(Nickel-cadmium battries)',
      label: <FormattedMessage id="productinfo.multiplebatteries"/>
    },
    { value: '鋰電池(Lithium batteries)',
      label: <FormattedMessage id="productinfo.lithiumbattery"/>
    },
    { value: '濕式電池',
      label: <FormattedMessage id="productinfo.wetbattery"/>
    },
    // { value: 'AAA', label: 'AAA' },
    // { value: 'AAAA', label: 'AAAA' },
    // { value: 'C', label: 'C' },
    // { value: 'CR123A', label: 'CR123A' },
    // { value: 'CR2', label: 'CR2' },
    // { value: 'CR5', label: 'CR5' },
    // { value: 'D', label: 'D' },
    // { value: '鋰離子', label: '鋰離子' },
    // { value: '鋰金屬', label: '鋰金屬' },
    // { value: '鎳氫', label: '鎳氫' },
    // { value: 'P76', label: 'P76' },
    // { value: '產品專屬電池', label: '產品專屬電池' },
];

const ProductInfo = () => {
    const dispatch = useDispatch()
    const {id} = useParams()
    const p2m = useSelector(state => state.P2M);
    const d = useSelector(state => state.d);
    const toaster = <FormattedMessage id = "managep2maction.savedsuccessfully"/>;
    const [notification, setNotification] = useState('');

    useEffect(() => {
        const p2mId = p2m.currentAp._id.$oid;

        if (!p2mId) {
            console.log('refresh get')
            // dispatch(getExactApplication(id))
            // dispatch(initP2MProductInfo(id));
        } else {
            dispatch(initP2MProductInfo(p2mId))
        }

        if (p2m.smoothScroll[0]) {
            smoothScroll(document.getElementById(p2m.smoothScroll[1]))
        }
      }, []);

    const option = [
      {value: 'Select All', label: 'Select All'}
    ]
    const options = option.concat(p2m.skuOptions).concat([{value: 'Clear All', label: 'Clear All'}])

    const checkUrlAllSkuApplied = (event) => {
        const targetSubAp = p2m.pai
        targetSubAp.urlAllSkuApplied = event.target.value
        dispatch(updatePai(targetSubAp))
    }
    const checkStartDateAllSkuApplied = (event) => {
        const targetSubAp = p2m.pai
        targetSubAp.startDateAllSkuApplied = event.target.value
        dispatch(updatePai(targetSubAp))
    }
    const checkManufactureDaysAllSkuApplied = (event) => {
        const targetSubAp = p2m.pai
        targetSubAp.manufactureDaysAllSkuApplied= event.target.value
        dispatch(updatePai(targetSubAp))
    }
    const checkManufacturePlaceAllSkuApplied = (event) => {
        const targetSubAp = p2m.pai
        targetSubAp.manufacturePlaceAllSkuApplied = event.target.value
        dispatch(updatePai(targetSubAp))
    }
    const checkModelNumberAllSkuApplied = (event) => {
        const targetSubAp = p2m.pai
        targetSubAp.modelNumberAllSkuApplied = event.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onUrlChange = (e, index) => {
        const targetSubAp = p2m.pai;
        if (p2m.pai.urlAllSkuApplied == 'no') {
            targetSubAp.appliedSku[index].url = e.target.value;
        } else {
            targetSubAp.appliedSku.map(item =>{
                item.url = e.target.value;
            })
            targetSubAp.url = e.target.value;
        }
        dispatch(updatePai(targetSubAp))
    }

    const onStartDateChange = (date,index) => {
        const targetSubAp = p2m.pai;

        if (p2m.pai.startDateAllSkuApplied == 'no') {
            targetSubAp.appliedSku[index].startDate = date;
        } else {
            targetSubAp.appliedSku.map(item =>{
                item.startDate = date;
            })
            targetSubAp.startDate = date;
        }
        dispatch(updatePai(targetSubAp))
    }

    const onManufactureDaysChange = (e, index) => {
        const targetSubAp = p2m.pai;

        if (p2m.pai.manufactureDaysAllSkuApplied == 'no') {
            targetSubAp.appliedSku[index].manufactureDays = e.target.value;
        } else {
            targetSubAp.appliedSku.map(item =>{
                item.manufactureDays = e.target.value;
            })
            targetSubAp.manufactureDays = e.target.value;
        }
        dispatch(updatePai(targetSubAp))
    }

    const onManufaturePlaceChange = (e, index) => {
        const targetSubAp = p2m.pai;

        if (p2m.pai.manufacturePlaceAllSkuApplied == 'no') {
            targetSubAp.appliedSku[index].manufacturePlace = e.target.value;
        } else {
            targetSubAp.appliedSku.map(item =>{
                item.manufacturePlace = e.target.value;
            })
            targetSubAp.manufacturePlace =  e.target.value;
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    const onModelNumberChange = (e, index) => {
        const targetSubAp = p2m.pai;

        if (p2m.pai.modelNumberAllSkuApplied == 'no') {
            targetSubAp.appliedSku[index].modelNumber = e.target.value;
        } else {
            targetSubAp.appliedSku.map(item =>{
                item.modelNumber = e.target.value;
            })
            targetSubAp.modelNumber =  e.target.value;
        }
        dispatch(updatePai(targetSubAp))
    }
    const onProductIdByDrsChange = (e,index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].productIdByDrs = e.target.value;
        dispatch(updatePai(targetSubAp))
    }

    const handleProductIdByDrsSave = (index) => {
        const productInfo = p2m.pai;
        productInfo.p2mApplicationId = p2m.currentAp._id;
        const isComment = true;
        const redirect = isComment ? '' : '/product/application';

        dispatch(saveP2MProductInfo(productInfo,toaster,redirect))
    }

    const handleSaveComment = (content,index,target) => {
        const productInfo = p2m.pai;
        productInfo.p2mApplicationId = p2m.currentAp._id;
        const isComment = true;
        // const redirect = isComment ? '' : `/product/application/${id}`;
        const redirect = isComment ? '' : '/product/application';

        if (target == 'url') {
            productInfo.comment.url = content
        } else if (target == 'startDate') {
            productInfo.comment.startDate = content
        } else if (target == 'manufactureDays') {
            productInfo.comment.manufactureDays = content
        } else if (target == 'manufacturePlace') {
            productInfo.comment.manufacturePlace = content
        } else if (target == 'modelNumber') {
            productInfo.comment.modelNumber = content
        } else if (target == 'productId') {
            productInfo.comment.productId = content
        } else if (target == 'packageImg') {
            productInfo.comment.packageImg = content
        } else if (target == 'manualImg') {
            productInfo.comment.manualImg = content
        } else if (target == 'manualFile') {
            productInfo.comment.manualFile = content
        } else if (target == 'woodenFile') {
            productInfo.comment.woodenFile = content
        } else if (target == 'wirelessFile') {
            productInfo.comment.wirelessFile = content
        } else if (target == 'batteryFile') {
            productInfo.comment.batteryFile = content
        } else if (target == 'exportSideHsCode') {
            productInfo.comment.exportSideHsCode = content
        } else if (target == 'salesSideHsCode') {
            productInfo.comment.salesSideHsCode = content
        } else if (target == 'hsCode') {
            productInfo.comment.hsCode = content
        } else if (target == 'ingredient') {
            productInfo.comment.ingredient = content
        }

        dispatch(saveP2MProductInfo(productInfo,toaster,redirect))
    }

    const savingMsg = <FormattedMessage id = "managep2m.savingMessage"/>
    const [saveTimeout, setSaveTimeout] = useState(false)

    const handleSave = (status) => {
        setSaveTimeout(true)
        toast(savingMsg)

        const productInfo = p2m.pai;
        productInfo.p2mApplicationId = p2m.currentAp._id;
        productInfo.status = status;
        const isComment = false;

        productInfo.packageFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.packageFile[index] = item.name : null
                })
            })
        })
        productInfo.manualFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.manualFile[index] = item.name : null
                })
            })
        })
        productInfo.ingredientFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.ingredientFile[index] = item.name : null
                })
            })
        })
        productInfo.woodenFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.woodenFile[index] = item.name : null
                })
            })
        })
        productInfo.wirelessFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.wirelessFile[index] = item.name : null
                })
            })
        })
        productInfo.batteryFile.map((item,index) => {
            item.appliedSku.map(sku => {
                productInfo.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.batteryFile[index] = item.name : null
                })
            })
        })

        productInfo.appliedSku.map((item,index) => {
            item.manualImg.map( img => {img.preview = ''})
            item.packageImg.map( img => {img.preview = ''})
        })

        // const redirect = isComment ? '' : `/product/application/${id}`;
        const redirect = isComment ? '' : '/product/application';

        setTimeout(() => {
            setSaveTimeout(false)
            dispatch(saveP2MProductInfo(productInfo,toaster,redirect))
        }, 3000)

    }

    const [showUploader, setShowUploader] = useState(false);
    const [showIndex, setShowIndex] = useState(0)

    const onUpload = (index) => {
        console.log('onUpload')
        setShowIndex(index)
        if (showUploader){
            return setShowUploader(false)
        }
        setShowUploader(true)
        setShowValidWarning(false)
    }

    const handleSubmitImg = (accFiles,index) => {
        const arr = p2m.pai.appliedSku;
        if (accFiles.length > 0) {
            arr[index].packageImg = accFiles.map(item => {
                return { name: item.name, preview: item.preview }
            })
        }
        accFiles.forEach((file) => {
            uploadImg(file)
        })
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku = arr;
        dispatch(updatePai(targetSubAp))
        setShowUploader(false)
    }
    const [showManualImgUploader, setShowManualImgUploader] = useState(false);
    const [showManualImgIndex, setShowManualImgIndex] = useState(0)

    const upload = (input) =>{
        //todo arthur not finish , have to refactor
        var formData = new FormData();
        formData.append('p2mName' , p2m.currentAp.name )
        formData.append('file', input);

        axios({
            method: "POST",
            url: DOMAIN_NAME + '/p2m/upf',
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    }

    const uploadImg = (input) => {
        //todo arthur not finish , have to refactor
        var formData = new FormData();
        formData.append('p2mName' , p2m.currentAp.name )
        formData.append('file', input);

        axios({
            method: "POST",
            url: DOMAIN_NAME + '/p2m/upi',
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
    }

    const onManualImgUpload = (index) => {
        console.log('onManualImgUpload')
        setShowManualImgIndex(index)
        if (showManualImgUploader) {
            return setShowManualImgUploader(false)
        }
        setShowManualImgUploader(true)
    }
    const handleManualImgSubmit = (accFiles,index) => {
        accFiles.forEach((file) => {
            uploadImg(file)
        })
        const arr = p2m.pai.appliedSku;
        if (accFiles.length > 0) {
            arr[index].manualImg = accFiles.map(item => {
                return { name: item.name, preview: item.preview }
            })
        }
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku = arr;
        dispatch(updatePai(targetSubAp))
        setShowManualImgUploader(false)
    }

    const handlePackageFile = (action,index,event) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.packageFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.packageFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.packageFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.packageFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = { name: 'Choose a file', appliedSku: [] };
            targetSubAp.packageFile.push(newUploadInput)
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    const handleSelectSku = (e, index, target) => {
        if (!e) { e = []}
        const selectLabel = e.map(item => {return item.label})
        const targetSubAp = p2m.pai;
        setShowValidWarning(false)
        switch(target) {
            case 'package':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.packageFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true){
                    targetSubAp.packageFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.packageFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            case 'manual':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.manualFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true) {
                    targetSubAp.manualFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.manualFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            case 'ingredient':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.ingredientFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true) {
                    targetSubAp.ingredientFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.ingredientFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            case 'wooden':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.woodenFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true) {
                    targetSubAp.woodenFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.woodenFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            case 'wireless':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.wirelessFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true) {
                    targetSubAp.wirelessFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.wirelessFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            case 'battery':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.batteryFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updatePai(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true) {
                    targetSubAp.batteryFile[index].appliedSku = [];
                    return dispatch(updatePai(targetSubAp))
                } else {
                    targetSubAp.batteryFile[index].appliedSku = e;
                    return dispatch(updatePai(targetSubAp))
                }
            default:
                return null
        }
    }

    const handleManualFile = (action, index, event ) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.manualFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.manualFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.manualFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.manualFile[index].name = 'Choose a file'
        } else if (action == 'add') {
            let newUploadInput = { name: 'Choose a file', appliedSku: [] };
            targetSubAp.manualFile.push(newUploadInput)
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    // wooden files
    const handleWoodenFile = (action, index, event) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.woodenFile[index].name = event.target.files[0].name
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.woodenFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.woodenFile.splice(index, 1)
        }  else if (action == 'drop') {
            targetSubAp.woodenFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = { name: 'Choose a file', appliedSku: [] }
            targetSubAp.woodenFile.push(newUploadInput)
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    // ingredient files
    const handleIngredientFile = (action, index, event) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.ingredientFile[index].name = event.target.files[0].name
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.ingredientFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.ingredientFile.splice(index, 1)
        }  else if (action == 'drop') {
            targetSubAp.ingredientFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = { name: 'Choose a file', appliedSku: [] }
            targetSubAp.ingredientFile.push(newUploadInput)
        }
        dispatch(updatePai(targetSubAp))
    }

    // wireless files
    const handleWirelessFile = (action,index,event) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.wirelessFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.wirelessFile.length > 1 ? null: alert('必須上傳一份文件')
            targetSubAp.wirelessFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.wirelessFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = { name: 'Choose a file', appliedSku: [] }
            targetSubAp.wirelessFile.push(newUploadInput)
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    // battery files
    const handleBatteryFile = (action, index, event) => {
        const targetSubAp = p2m.pai
        if (action == 'upload') {
            targetSubAp.batteryFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.batteryFile.length > 1 ? null: alert('必須上傳一份文件')
            targetSubAp.batteryFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.batteryFile[index].name = 'Choose a file'
        }else if (action == 'add'){
            let newUploadInput = { name: 'Choose a file', appliedSku: [], batteryCategory: '' };
            targetSubAp.batteryFile.push(newUploadInput)
        }
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }

    const onBatteryCategoryChange = (target,index) => {
        if (target) {
            const targetSubAp = p2m.pai
            targetSubAp.batteryFile[index].batteryCategory = target.value;
            let notice = '';
            if (target.value == '鹹性電池(Alkali-manganese batteries)、碳鋅電池(Zinc-Carbon batteries)、鎳鎘電池(Nickel-cadmium battries)') {
                notice = '需附產品的 MSDS 和 UN38.3 測試報告';
            } else if (target.value = '鋰電池(Lithium batteries)') {
                notice = '需附產品的 MSDS 和 Vibration Test、Pressure Differential Test 測試報告'
            }
            targetSubAp.batteryFile[index].notification = notice;
            setShowValidWarning(false)
            dispatch(updatePai(targetSubAp))
        }
    }
    // const [ingredient, setIngredient] = useState('')
    // const onIngredientChange = (e) => {
    //     // setIngredient(e.target.value)
    // }
    const onIngredientChange = (e) => {
        const targetSubAp = p2m.pai;
        targetSubAp.ingredient = e.target.value;
        // targetSubAp.appliedSku.map(item => {
        //     item.ingredient = e.target.value
        // })
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const onSuggestedPricingNoTaxChange = (e,index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].suggestedPricingNoTax = e.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onSuggestedPricingTaxChange = (e, index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].suggestedPricingTax = e.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onSuggestedRetailPriceNoTaxChange = (e,index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].suggestedRetailPriceNoTax = e.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onSuggestedRetailPriceTaxChange = (e, index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].suggestedRetailPriceTax = e.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onDdpUnitPriceChange = (e,index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].ddpUnitPrice = e.target.value
        dispatch(updatePai(targetSubAp))
    }
    const onSalesSideHsCodeChange = (e,index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].salesSideHsCode = e.target.value
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const onExportSideHsCodeChange = (e, index) => {
        const targetSubAp = p2m.pai;
        targetSubAp.appliedSku[index].exportSideHsCode = e.target.value
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const checkIsWooden = (event) => {
        const targetSubAp = p2m.pai;
        targetSubAp.isWooden = event.target.value;
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const checkIsBattery = (event) => {
        const targetSubAp = p2m.pai;
        targetSubAp.isBattery = event.target.value;
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const checkIsWireless = (event) => {
        const targetSubAp = p2m.pai;
        targetSubAp.isWireless = event.target.value;
        setShowValidWarning(false)
        dispatch(updatePai(targetSubAp))
    }
    const [showUrl, setShowUrlEditor] = useState(false)
    const [showStartDate, setShowStartDateEditor] = useState(false)
    const [showManufactureDays, setShowManufactureDaysEditor] = useState(false)
    const [showManufacturePlace, setShowManufacturePlaceEditor] = useState(false)
    const [showModelNumber, setShowModelNumberEditor] = useState(false)
    const [showProductId, setShowProductIdEditor] = useState(false)
    const [showPackageImg, setShowPackageImgEditor] = useState(false)
    const [showPackageFile, setShowPackageFileEditor] = useState(false)
    const [showManualImg, setShowManualImgEditor] = useState(false)
    const [showManualFile, setShowManualFileEditor] = useState(false)
    const [showWoodenFile, setShowWoodenFileEditor] = useState(false)
    const [showWirelessFile, setShowWirelessFileEditor] = useState(false)
    const [showBatteryFile, setShowBatteryFileEditor] = useState(false)
    const [showHsCode, setShowHsCodeEditor] = useState(false)
    const [showIngredient, setShowIngredientEditor] = useState(false)

    const handleShowEditor = (target) => {

        switch(target) {
            case 'url':
                return showUrl ? setShowUrlEditor(false) : setShowUrlEditor(true)
            case 'startDate':
                return showStartDate ? setShowStartDateEditor(false): setShowStartDateEditor(true)
            case 'manufactureDays':
                return showManufactureDays ? setShowManufactureDaysEditor(false): setShowManufactureDaysEditor(true)
            case 'manufacturePlace':
                return showManufacturePlace ? setShowManufacturePlaceEditor(false): setShowManufacturePlaceEditor(true)
            case 'modelNumber':
                return showModelNumber ? setShowModelNumberEditor(false): setShowModelNumberEditor(true)
            case 'productId':
                return showProductId ? setShowProductIdEditor(false): setShowProductIdEditor(true)
            case 'packageImg':
                return showPackageImg ? setShowPackageImgEditor(false): setShowPackageImgEditor(true)
            case 'packageFile':
                    return showPackageFile ? setShowPackageFileEditor(false): setShowPackageFileEditor(true)
            case 'manualImg':
                return showManualImg ? setShowManualImgEditor(false): setShowManualImgEditor(true)
            case 'manualFile':
                return showManualFile ? setShowManualFileEditor(false): setShowManualFileEditor(true)
            case 'woodenFile':
                return showWoodenFile ? setShowWoodenFileEditor(false): setShowWoodenFileEditor(true)
            case 'wirelessFile':
                return showWirelessFile ? setShowWirelessFileEditor(false): setShowWirelessFileEditor(true)
            case 'batteryFile':
                return showBatteryFile ? setShowBatteryFileEditor(false): setShowBatteryFileEditor(true)
            case 'hsCode':
                return showHsCode ? setShowHsCodeEditor(false): setShowHsCodeEditor(true)
            case 'ingredient':
                return showIngredient ? setShowIngredientEditor(false): setShowIngredientEditor(true)
            default:
                return null
        }
    }

    const handleCommentChange = (content, index, target) => {
        // console.log('');
        const targetSubAp = p2m.pai;
        if (target == 'url') {
            targetSubAp.comment.url = content
        } else if (target == 'startDate') {
            targetSubAp.comment.startDate = content
        } else if (target == 'manufactureDays') {
            targetSubAp.comment.manufactureDays = content
        } else if (target == 'manufacturePlace') {
            targetSubAp.comment.manufacturePlace = content
        } else if (target == 'modelNumber') {
            targetSubAp.comment.modelNumber = content
        } else if (target == 'productId') {
            targetSubAp.comment.productId = content
        } else if (target == 'packageImg') {
            targetSubAp.comment.packageImg = content
        } else if (target == 'packageFile') {
            targetSubAp.comment.packageFile = content
        } else if (target == 'manualImg') {
            targetSubAp.comment.manualImg = content
        } else if (target == 'manualFile') {
            targetSubAp.comment.manualFile = content
        } else if (target == 'woodenFile') {
            targetSubAp.comment.woodenFile = content
        } else if (target == 'wirelessFile') {
            targetSubAp.comment.wirelessFile = content
        } else if (target == 'batteryFile') {
            targetSubAp.comment.batteryFile = content
        } else if (target == 'exportSideHsCode') {
            targetSubAp.comment.exportSideHsCode = content
        } else if (target == 'salesSideHsCode') {
            targetSubAp.comment.salesSideHsCode = content
        } else if (target == 'hsCode') {
            targetSubAp.comment.hsCode = content
        } else if (target == 'ingredient') {
            targetSubAp.comment.ingredient = content
        }
        dispatch(updatePai(targetSubAp));
    }

    const smoothScroll = (target) => {

        var scrollContainer = target;
        do { //find scroll container
            scrollContainer = scrollContainer.parentNode;
            if (!scrollContainer) return;
            scrollContainer.scrollTop += 1;
        } while (scrollContainer.scrollTop == 0);

        var targetY = 0;
        do { //find the top of target relatively to the container
            if (target == scrollContainer) break;
            targetY += target.offsetTop;
            targetY = targetY - 50
        } while (target = target.offsetParent);

        scroll = function(c, a, b, i) {
            i++; if (i > 30) return;
            c.scrollTop = a + (b - a) / 30 * i;
            setTimeout(function(){ scroll(c, a, b, i); }, 20);
        }
        // start scrolling
        scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
    }

    const handleUploadAgain = (target , index) => {

        if (target == 'manualImg') {
            setShowManualImgIndex(index);
            setShowManualImgUploader(true);
        } else if(target == 'packageImg') {
            setShowIndex(index);
            setShowUploader(true);
        }
    }

    const handleRemoveAll = ( target,index) => {
        const targetSubAp = p2m.pai;

        if (target == 'manualImg') {
            targetSubAp.appliedSku[index].manualImg = []
            dispatch(updatePai(targetSubAp))
        } else if (target == 'packageImg') {
            targetSubAp.appliedSku[index].packageImg = []
            dispatch(updatePai(targetSubAp))
        }
    }

    const [showValidWarning, setShowValidWarning] = useState(false)

    const handleValid = () => {
        let checkValid = []

        p2m.pai.packageFile.map((item, index) => {
            item.appliedSku == '' || item.name == 'Choose a file' ? checkValid.push(false): null
        })
        p2m.pai.manualFile.map(item => {
            item.appliedSku == '' || item.name == 'Choose a file' ? checkValid.push(false): null
        })
        p2m.pai.appliedSku.map(item => {
            item.exportSideHsCode == ''|| item.salesSideHsCode == '' || item.packageImg.length == 0
                ? checkValid.push(false): null
        })

        // for (let index = 0; index < p2m.pai.appliedSku.length; index++) {
            // if (p2m.pai.packageFile[index].appliedSku == ''){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.manualFile[index].appliedSku == ''){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.packageFile[index].name == 'Choose a file'){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.manualFile[index].name == 'Choose a file'){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.appliedSku[index].manufacturePlace == ''){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.appliedSku[index].exportSideHsCode == ''){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.appliedSku[index].salesSideHsCode == ''){
            //     checkValid.push(false)
            // }
            // if (p2m.pai.appliedSku[index].packageImg.length == 0){
            //     checkValid.push(false)
            // }
        // }
        if (p2m.pai.isWooden == 'yes') {
            p2m.pai.woodenFile.map(item => {
                item.appliedSku == '' || item.name == 'Choose a file' ? checkValid.push(false): null
            })
            // for (let index = 0; index < p2m.pai.appliedSku.length; index++) {
            //     if (p2m.pai.woodenFile[index].appliedSku == ''){
            //         checkValid.push(false)
            //     }
            //     if (p2m.pai.woodenFile[index].name == 'Choose a file'){
            //         checkValid.push(false)
            //     }
            // }
        }
        if (p2m.pai.isWireless == 'yes') {
            p2m.pai.wirelessFile.map(item => {
                item.appliedSku == '' || item.name == 'Choose a file'? checkValid.push(false): null
            })
            // for (let index = 0; index < p2m.pai.appliedSku.length; index++) {
            //     if (p2m.pai.wirelessFile[index].appliedSku == ''){
            //         checkValid.push(false)
            //     }
            //     if (p2m.pai.wirelessFile[index].name == 'Choose a file'){
            //         checkValid.push(false)
            //     }
            // }
        }
        if (p2m.pai.isBattery == 'yes') {
            p2m.pai.batteryFile.map(item => {
                item.appliedSku == '' || item.batteryCategory == ''||item.name == 'Choose a file'
                    ? checkValid.push(false): null
            })
            // for (let index = 0; index < p2m.pai.appliedSku.length; index++) {
            //     if (p2m.pai.batteryFile[index].appliedSku == ''){
            //         checkValid.push(false)
            //     }
            //     if (p2m.pai.batteryFile[index].batteryCategory == ''){
            //         checkValid.push(false)
            //     }
            //     if (p2m.pai.batteryFile[index].name == 'Choose a file'){
            //         checkValid.push(false)
            //     }
            // }
        }
        if (p2m.pai.manufacturePlaceAllSkuApplied == 'yes'){
            if (p2m.pai.manufacturePlace == '') {
                checkValid.push(false)
            }
        }else {
            p2m.pai.appliedSku.map(item => {
                item.manufacturePlace == ''
                    ? checkValid.push(false): null
            })
        }
        if (p2m.pai.ingredient == '') {
            checkValid.push(false)
        }
        // if (p2m.pai.ingredient == ''){
        //     checkValid.push(false)
        // }

        if (checkValid.length !== 0) {
            setShowValidWarning(true)
        } else {
            handleSave("Pending")
        }
    }

    const [reminder, setReminder] = useState(false);
    const [batteryReminder, setBatteryReminder] = useState(false);
    const [packageReminder, setPackageReminder] = useState(false);

    const handleReminderOpen = () => {
        setReminder(true);
    }

    const handleReminderClose = () => {
        setReminder(false);
    }

    const handleBatteryReminderOpen = () => {
        setBatteryReminder(true);
    }

    const handleBatteryReminderClose = () => {
        setBatteryReminder(false);
    }

    const handlePackageReminderOpen = () => {
        setPackageReminder(true);
    }

    const handlePackageReminderClose = () => {
        setPackageReminder(false);
    }

    const learnMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '1%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-13%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleReminderClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
            <p style={{lineHeight : '1.5rem'}}>
                <p>國際商品統一分類代碼為 The Harmonized Commodity Description and Coding System 使用之商品編碼，簡稱為 HS code。台灣參照此規則而制定 CCC code (The Standard Classification of Commodities of the Republic of China)，此編碼常用於貨物清關，故有時也被稱作 Tariff code 或是 Tariff number。</p>
                <p style={{marginTop : '2%'}}>貨物出口與進口時皆需要提供此碼，故您需要個別提供供應端地區的稅則代碼與銷售端地區的稅則代碼，例如：您在台灣供貨，請填寫台灣 CCC code 於「供應端的稅則代碼」欄位；如本申請案目標市場是美國，請至美國海關網站查詢稅則代碼，並填寫於「銷售端的稅則代碼」欄位。</p>
                <p style={{marginTop : '2%'}}>各國 HS code 的前六碼通常是相同的，但尾碼在各國有各自的分類規則，請提供各國完整代碼。</p>
                <p style={{marginTop : '2%'}}>為加速通關及有效處理清關作業，以海關認定的稅則代碼為主，DRS 作為貨物進、出口通關之報關者，有權決定貨物進、出口通關所使用的稅則代碼，如您不同意最後採用的稅則代碼，可以提出官方正式文件證明，例如：台灣海關的「進口貨物稅則預先審核」，另外，如有涉及相關進、出口規定，還需請您配合提供符合規定之文件，以助順利清關。</p>
            </p>
        </div>

        );

    const learnBatteryMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '1%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleBatteryReminderClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
            <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}}>如果產品使用鋰電池，您需要提供以下文件：</p>
            <p style={{lineHeight : '1.5rem'}}>
                <p>(1) MSDS</p>
                   <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                        <li>若電池是內嵌在產品內，您需要提供的是完整產品帶電池的 MSDS 而非電池本身的 MSDS。產品的 MSDS 必須秀產品品名及型號，非電池型號。請以完整產品送驗給測試單位。</li>
                        <li>如果鋰電池獨立於產品本身，例如分開包裝的鈕扣電池，則您可提供電池本身的 MSDS。</li>
                   </ul>
                <p style={{marginTop : '2%'}}>(2) UN38.3 八大測試報告。同 MSDS ，您需要視情況提供產品本身的測試報告或是電池的測試報告。</p>
                <p style={{marginTop : '2%'}}>(3) 1.2公尺掉落測試報告。同 MSDS ，您需要視情況提供產品本身的測試報告或是電池的測試報告。</p>
                <p style={{marginTop : '2%'}}>(4) 空運及海運貨物運輸報告。此報告效期只有一年，請於出貨前檢查您的文件是否仍在有效期間。</p>
            </p>
            <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop : '3%'}}>如產品使用濕式電池，您需要提供以下文件：</p>
            <p style={{lineHeight : '1.5rem'}}>
                <p>(1) 產品的 MSDS</p>
                <p style={{marginTop : '2%'}}>(2) Vibration Test</p>
                <p style={{marginTop : '2%'}}>(3) Pressure Differential Test 測試報告</p>
            </p>

        </div>

        );

        const learnPackageMoreModal = (

            <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
                transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                    borderRadius: '4px', padding : '1%'}}>
                    <div className="modal-close-wrapper" style={{position: 'absolute',top: '-6%', left: '0%'}}>
                        <button className="modal-close-btn" onClick={() => {handlePackageReminderClose()}}>
                        <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                        <span>
                            <FormattedMessage id="addnewproduct.close"/>
                        </span>
                        </button>
                    </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}}>建議您在產品包裝加入以下資訊，您上傳的包裝照片必須能清楚看出這些資訊。：</p>
                <p style={{lineHeight : '1.5rem'}}>
                       <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                            <li>使用當地語言標示品名</li>
                            <li>產品規格</li>
                            <li>認證標誌</li>
                            <li>處理／回收標誌</li>
                            <li>產地（產品或包裝上應至少擇一標示產地）</li>
                            <li>製造商資訊</li>
                            <li>品牌商資訊。許多消費者對於來源不明的產品容易有戒心。</li>
                            <li>當地代表資訊。CE 及 UKCA 產品必須擁有當地代理人（歐代或英代），且您的包裝上必須顯示這些代理人的公司名稱及地址。並於 市場合規性 Regional Compliance 頁面上傳授權文件。</li>
                       </ul>
                       <p>若包裝含塑膠袋且塑膠袋開口大於 12 公分，須在塑膠袋上標示防窒息警語</p>
                       <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                            <li>防窒息警語如：WARNING：To avoid danger of suffocation,
                                 keep this plastic bag away from babies and children. Do not use this bag in cribs, beds, carriages or play pens. This bag is not a toy.</li>
                            <li>請依據塑膠袋尺寸，標上適當的警語字體大小。</li>
                            <li>塑膠袋或收縮包裝不得超過產品 7 公分。</li>
                            <li>為避免造成風險，無論產品在何處銷售，請一律在產品塑膠袋上顯示當地語言的警告標語。</li>
                       </ul>
                </p>
                <div style={{fontSize: '18px', textAlign:'center', marginBottom:'20px'}}>
                    <table style={{border : 'black solid 1px' ,borderCollapse :'collapse', margin:'auto' , width: '90%'}}>

                        <tr>
                        <th className="col-md-6" style={{border : 'black solid 1px', padding:'10px', backgroundColor:'#46ddb6'}}>塑膠袋的長+寬總和</th>
                        <th className="col-md-6" style={{border : 'black solid 1px', padding:'10px', backgroundColor:'#46ddb6'}}>最小字體尺寸</th>
                        </tr>
                        <tr>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>大於或等 152.4 公分</td>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>24 point</td>
                        </tr>
                        <tr>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>101.6 公分到 149.86 公分</td>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>18 point</td>
                        </tr>
                        <tr>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>76.2 公分到 99.06 公分</td>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>14 point</td>
                        </tr>
                        <tr>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>小於 73.66 公分</td>
                        <td className="col-md-6" style={{border : 'black solid 1px', padding:'10px'}}>10 point</td>
                        </tr>

                    </table>
                </div>

            </div>

            );

    if (p2m.redirect !== '') {
        return <Redirect to={p2m.redirect}/>
    }

    return (
        <div>
            { p2m.isPending == 'loading' || saveTimeout || p2m.result == 'loading'
                ?   <div className="spinner-wrapper">
                        <div className="spinner"></div>
                        <div className="spinner-text"><span>Loading...</span></div>
                    </div>
                :   null
            }
            <div className="application-wrapper">
                <div className="application">
                    <div className="application-title-wrapper">
                        <p className="application-subtitle">
                            <FormattedMessage id="p2mapplication.form"/>
                            {p2m.currentAp.name}-<FormattedMessage id="subApplication.productinfoTitle"/>
                        </p>
                        <div className="application-subtitle-info-wrapper">
                            <div className="application-info-column" style={{width: '69%'}}>
                                <div className="application-info">
                                    <p className="application-info-title" style={{width: '30%'}}>
                                        <FormattedMessage id="p2mapplication.formtype"/>
                                    </p>
                                    <p className="application-info-content">
                                        <FormattedMessage  id="subApplication.productinfo"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <P2MApplicationInfo/>

                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                            <p className="application-info-section-title">
                                <FormattedMessage id="productinfo.title1"/>
                            </p>
                        </div>
                        <div className="application-info-section column align-center mt-1">
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        1.  <FormattedMessage id="productinfo.subtitle1"/>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex"}}>
                                                <FormattedMessage id="productinfo.labelseparate">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkUrlAllSkuApplied(e)}
                                                            id="1"
                                                            isSelected={ p2m.pai.urlAllSkuApplied === "no" }
                                                            label={label}
                                                            value="no"
                                                        />
                                                    }
                                                </FormattedMessage>
                                                <FormattedMessage id="productinfo.labelapplyall">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkUrlAllSkuApplied(e)}
                                                            id="2"
                                                            isSelected={ p2m.pai.urlAllSkuApplied === "yes" }
                                                            label={label}
                                                            value="yes"
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('url');
                                                showUrl ? null : smoothScroll(document.getElementById('url'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}><FormattedMessage id="productinfo.paragraph1"/></span>
                                </p>
                                { p2m.pai.urlAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper" style={{margin: '2% 0'}}>
                                        {   p2m.pai.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-wrapper">
                                                    <div className="base-input-label-wrapper">
                                                        <p className="application-input-label" style={{marginTop: '10px'}}>{item.skuCode}</p>
                                                    </div>
                                                    <div className="base-input-with-description-wrapper"
                                                        style={ p2m.pai.urlAllSkuApplied === 'no' ? {marginBottom: '0px'} : null }
                                                    >
                                                    <FormattedMessage id="productinfo.websitelink">
                                                        { placeholder =>
                                                            <input
                                                                className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                placeHolder={placeholder}
                                                                value={item.url}
                                                                onChange={(e) => onUrlChange(e,index)}
                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                        {/*{ index == p2m.pai.appliedSku.length - 1
                                                            ?   <span className="base-input-description">
                                                                    <FormattedMessage id="productinfo.paragraph1"/>
                                                                </span>
                                                            :   null
                                                        }*/}
                                                    </div>
                                                </div>
                                            )
                                        })}
                                        </div>

                                    :   <div className="application-input-wrapper" style={{margin: '2% 0'}}>
                                            <div className="base-input-label-wrapper">
                                                <p className="application-input-label" style={{marginTop: '10px'}}>
                                                    <FormattedMessage id="productinfo.subtitle1"/>
                                                </p>
                                            </div>
                                            <div className="base-input-with-description-wrapper">
                                                <FormattedMessage id="productinfo.websitelink">
                                                    { placeholder =>
                                                        <input
                                                            className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeHolder={placeholder}
                                                            value={p2m.pai.url}
                                                            onChange={(e) => onUrlChange(e)}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    }
                                                </FormattedMessage>
                                                {/*<span className="base-input-description">
                                                    <FormattedMessage id="productinfo.paragraph1"/>
                                                </span>*/}
                                            </div>
                                        </div>
                                }
                                <div id="url">
                                    <Comment
                                        comment={p2m.pai.comment.url}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showUrl}
                                        handleShow={handleShowEditor}
                                        target='url'
                                    />
                                </div>
                            </div>

                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        2.  <FormattedMessage id="productinfo.subtitle2"/>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex"}}>
                                                <FormattedMessage  id="productinfo.labelseparate">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkStartDateAllSkuApplied(e)}
                                                            id="3"
                                                            isSelected={p2m.pai.startDateAllSkuApplied === "no" }
                                                            label={label}
                                                            value="no"
                                                        />
                                                    }
                                                </FormattedMessage>
                                                <FormattedMessage  id="productinfo.labelapplyall">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkStartDateAllSkuApplied(e)}
                                                            id="4"
                                                            isSelected={ p2m.pai.startDateAllSkuApplied === "yes" }
                                                            label={label}
                                                            value="yes"
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('startDate');
                                                showStartDate ? null : smoothScroll(document.getElementById('startDate'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}>新品上架審核流程依您提供的資料齊全度需約進行3-8周，也請加入運送貨品到市場端倉庫的時間。</span>
                                </p>
                                { p2m.pai.startDateAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper" style={{margin: '2% 0'}}>
                                        {   p2m.pai.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-wrapper">
                                                <div className="base-input-label-wrapper">
                                                    <p className="application-input-label" style={{marginTop: '10px'}}>{item.skuCode}</p>
                                                </div>
                                                <div className="base-input-inline-wrapper">
                                                    <DatePicker
                                                        selected={item.startDate == undefined? item.startDate : new Date(item.startDate)}
                                                        onChange={date => onStartDateChange(date,index)}
                                                        dateFormat="yyyy"
                                                        showYearPicker
                                                        className="base-input date-picker"
                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}
                                                    />
                                                    <span className="base-input-label-inline">
                                                        <FormattedMessage id="productinfo.year"/>
                                                    </span>
                                                    <DatePicker
                                                        selected={item.startDate == undefined? item.startDate : new Date(item.startDate) }
                                                        onChange={date => onStartDateChange(date,index)}
                                                        dateFormat="MM"
                                                        showMonthYearPicker
                                                        className="base-input date-picker"
                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}
                                                    />
                                                    <span className="base-input-label-inline">
                                                        <FormattedMessage id="productinfo.month"/>
                                                    </span>
                                                </div>

                                            </div>
                                            )
                                        })}
                                        </div>

                                    :    <div className="application-input-wrapper" style={{margin: '2% 0'}}>
                                            <div className="base-input-label-wrapper">
                                                <p className="application-input-label" style={{marginTop: '10px'}}>
                                                    <FormattedMessage id="productinfo.subtitle2"/>
                                                </p>
                                            </div>
                                            <div className="base-input-inline-wrapper" style={{marginBottom: '10px'}}>
                                                <DatePicker
                                                    selected={p2m.pai.startDate == undefined? p2m.pai.startDate : new Date(p2m.pai.startDate)}
                                                    onChange={date => onStartDateChange(date)}
                                                    dateFormat="yyyy"
                                                    showYearPicker
                                                    className="base-input date-picker"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}
                                                />
                                                <span className="base-input-label-inline">
                                                    <FormattedMessage id="productinfo.year"/>
                                                </span>
                                                <DatePicker
                                                    selected={p2m.pai.startDate == undefined? p2m.pai.startDate : new Date(p2m.pai.startDate)}
                                                    onChange={date => onStartDateChange(date)}
                                                    dateFormat="MM"
                                                    showMonthYearPicker
                                                    className="base-input date-picker"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}

                                                />
                                                <span className="base-input-label-inline">
                                                    <FormattedMessage id="productinfo.month"/>
                                                </span>
                                            </div>
                                        </div>
                                }
                                <div id="startdate">
                                    <Comment
                                        comment={p2m.pai.comment.startDate}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showStartDate}
                                        handleShow={handleShowEditor}
                                        target="startDate"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        3.  <FormattedMessage id="productinfo.subtitle3"/>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex"}}>
                                                <FormattedMessage  id="productinfo.labelseparate">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkManufactureDaysAllSkuApplied(e)}
                                                            id="5"
                                                            isSelected={ p2m.pai.manufactureDaysAllSkuApplied === "no" }
                                                            label={label}
                                                            value="no"
                                                        />
                                                    }
                                                </FormattedMessage>
                                                <FormattedMessage  id="productinfo.labelapplyall">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkManufactureDaysAllSkuApplied(e)}
                                                            id="6"
                                                            isSelected={p2m.pai.manufactureDaysAllSkuApplied === "yes" }
                                                            label={label}
                                                            value="yes"
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('manufactureDays');
                                                showManualfactureDays ? null : smoothScroll(document.getElementById('manufactureDays'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}>此欄資料有助於進行補貨規劃。</span>
                                </p>
                                { p2m.pai.manufactureDaysAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper" style={{margin: '2% 0'}}>
                                        {   p2m.pai.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-wrapper">
                                                    <div className="base-input-label-wrapper">
                                                        <p className="application-input-label" style={{marginTop: '10px'}}>{item.skuCode}</p>
                                                    </div>
                                                    <div>
                                                        <div className="base-input-inline-wrapper" style={{marginBottom: '2px', width: '100%'}}>
                                                            <FormattedMessage id="productinfo.egday">
                                                                { placeholder =>
                                                                    <input
                                                                        style={{width: '60%'}}
                                                                        className={`base-input base-input-inline-short ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        placeHolder={placeholder}
                                                                        value={item.manufactureDays}
                                                                        onChange={(e) => onManufactureDaysChange(e, index) }
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline">
                                                                <FormattedMessage id="productinfo.workingday"/>
                                                            </span>
                                                        </div>
                                                        <div style={{marginBottom: '10px'}}>
                                                            {item.manufactureDays!== '' && !/^[0-9- ]+$/.test(item.manufactureDays)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字，且需要是整數
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                            )
                                        })}

                                        </div>

                                    :    <div className="application-input-wrapper" style={{margin: '2% 0'}}>
                                            <div className="base-input-label-wrapper">
                                                <p className="application-input-label" style={{marginTop: '10px'}}>
                                                    <FormattedMessage id="productinfo.subtitle3"/>
                                                </p>
                                            </div>
                                            <div>
                                                <div className="base-input-inline-wrapper" style={{marginBottom: '2px', width: '100%'}}>
                                                    <FormattedMessage id="productinfo.egday">
                                                        { placeholder =>
                                                            <input
                                                                style={{width: '60%'}}
                                                                className={`base-input base-input-inline-short ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                placeHolder={placeholder}
                                                                value={p2m.result == 'int' ? manufactureDays: p2m.pai.manufactureDays}
                                                                onChange={(e) => onManufactureDaysChange(e)}
                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                            >
                                                            </input>
                                                        }
                                                    </FormattedMessage>
                                                    <span className="base-input-label-inline">
                                                        <FormattedMessage id="productinfo.workingday"/>
                                                    </span>
                                                </div>
                                                <div style={{marginBottom: '24px'}}>
                                                    {p2m.pai.manufactureDays!== '' && !/^[0-9- ]+$/.test(p2m.pai.manufactureDays)
                                                        ?   <p className="base-input-notice drs-notice-red">
                                                                <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                    請輸入數字，且需要是整數
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                            </div>
                                        </div>
                                }
                                <div id="manufactureDays">
                                    <Comment
                                        comment={p2m.pai.comment.manufactureDays}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showManufactureDays}
                                        handleShow={handleShowEditor}
                                        target="manufactureDays"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        4.  <FormattedMessage id="productinfo.subtitle4"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex"}}>
                                                 <FormattedMessage  id="productinfo.labelseparate">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkManufacturePlaceAllSkuApplied(e)}
                                                            id="7"
                                                            isSelected={ p2m.pai.manufacturePlaceAllSkuApplied === "no" }
                                                            label={label}
                                                            value="no"
                                                        />
                                                    }
                                                </FormattedMessage>
                                                <FormattedMessage  id="productinfo.labelapplyall">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkManufacturePlaceAllSkuApplied(e)}
                                                            id="8"
                                                            isSelected={ p2m.pai.manufacturePlaceAllSkuApplied === "yes" }
                                                            label={label}
                                                            value="yes"
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('manufacturePlace');
                                                showManufacturePlace ? null : smoothScroll(document.getElementById('manufacturePlace'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}><FormattedMessage id="productinfo.paragraph2"/></span>
                                </p>
                                { p2m.pai.manufacturePlaceAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper" style={{margin: '2% 0'}}>
                                        {   p2m.pai.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-wrapper" style={{marginTop: '1%'}}>
                                                    <div className="base-input-label-wrapper">
                                                        <p className="application-input-label" style={{marginTop: '10px'}}>{item.skuCode}</p>
                                                    </div>
                                                    <div
                                                        className="base-input-with-description-wrapper"
                                                        style={ p2m.pai.manufacturePlaceAllSkuApplied === 'no' ? {marginBottom: '0px'} : null }
                                                    >
                                                    <FormattedMessage id="productinfo.egplace">
                                                        { placeholder =>
                                                            <input
                                                                className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                placeHolder={placeholder}
                                                                value={item.manufacturePlace}
                                                                onChange={(e) => onManufaturePlaceChange(e,index)}
                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                            >
                                                            </input>
                                                        }
                                                    </FormattedMessage>
                                                    </div>
                                                    { item.manufacturePlace == ''
                                                        ?   <p className="drs-blue" style={{marginTop: '1%', width: '5%', marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="inputField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                            )
                                        })}
                                        </div>

                                    :    <div className="application-input-wrapper" style={{margin: '2% 0'}}>
                                            <div className="base-input-label-wrapper">
                                                <p className="application-input-label" style={{marginTop: '10px'}}>
                                                    <FormattedMessage id="productinfo.subtitle4"/>
                                                </p>
                                            </div>
                                            <div className="base-input-with-description-wrapper">
                                                <FormattedMessage id="productinfo.egplace">
                                                    { placeholder =>
                                                        <input
                                                            className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeHolder={placeholder}
                                                            value={p2m.pai.manufacturePlace}
                                                            onChange={(e) => onManufaturePlaceChange(e)}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            { p2m.pai.manufacturePlace == ''
                                                ?   <p className="drs-blue" style={{marginTop: '1%', width: '5%', marginLeft: '10px', fontSize: '13px'}}>
                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                        <FormattedMessage id="inputField.required"/>
                                                    </p>
                                                :   null
                                            }
                                        </div>
                                }
                                <div id="manufacturePlace">
                                    <Comment
                                        comment={p2m.pai.comment.manufacturePlace}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showManufacturePlace}
                                        handleShow={handleShowEditor}
                                        target="manufacturePlace"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        5.  產品零售價
                                    </span>
                                </p>
                                <div className="application-info-sub-section">
                                    <table role="table" className="table-main table">
                                        <thead>
                                                <tr style={{width: '100%',textAlign:'center',fontSize: '14px', fontWeight: '700'}}>
                                                    <th rowspan='2' style={{textAlign:'left'}}>SKUs</th>
                                                    <th colspan='2'>供應商建議定價</th>
                                                    <th colspan='2'>供應商建議零售價</th>
                                                    <th rowspan='2'>DDP單價</th>
                                                </tr>
                                                <tr style={{width: '100%',textAlign:'center',fontSize: '14px', fontWeight: '700'}}>
                                                    <th>未稅</th>
                                                    <th>含稅</th>
                                                    <th>未稅</th>
                                                    <th>含稅</th>
                                                </tr>
                                        </thead>
                                        <tbody className="table-tbody" role="rowgroup">
                                            { p2m.pai.appliedSku.map((item, index) => (
                                                <tr className="table-tbody-tr" style={{textAlign:'center'}}>
                                                    <td style={{width: '15%',textAlign:'left'}}>{item.skuCode}</td>
                                                    <td style={{width: '17%'}}>
                                                        <input
                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeholder="未稅"
                                                            value={item.suggestedPricingNoTax}
                                                            onChange={(e) => onSuggestedPricingNoTaxChange(e, index)}
                                                            readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    </td>
                                                    <td style={{width: '17%'}}>
                                                        <input
                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeholder="含稅"
                                                            value={item.suggestedPricingTax}
                                                            onChange={(e) => onSuggestedPricingTaxChange(e, index)}
                                                            readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    </td>
                                                    <td style={{width: '17%'}}>
                                                        <input
                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeholder="未稅"
                                                            value={item.suggestedRetailPriceNoTax}
                                                            onChange={(e) => onSuggestedRetailPriceNoTaxChange(e, index)}
                                                            readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    </td>
                                                    <td style={{width: '17%'}}>
                                                        <input
                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeholder="含稅"
                                                            value={item.suggestedRetailPriceTax}
                                                            onChange={(e) => onSuggestedRetailPriceTaxChange(e, index)}
                                                            readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    </td>
                                                    <td style={{width: '17%'}}>
                                                        <input
                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeholder="DDP單價"
                                                            value={item.ddpUnitPrice}
                                                            onChange={(e) => onDdpUnitPriceChange(e, index)}
                                                            readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        6.  <FormattedMessage id="productinfo.subtitle5"/>
                                    </span>
                                    { d.u.isSp
                                        ?  null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() =>{
                                                handleShowEditor('productId');
                                                showProductId ? null : smoothScroll(document.getElementById('productId'))
                                               }}><RateReviewIcon/></button>
                                            : null
                                    }

                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}>如果您的SKU有提供Product ID，請確認包裝上的條碼與建立產品時的 GTIN (全球貿易商品編號)相符。</span>
                                </p>
                                <div className="application-info-sub-section">
                                    <table role="table" className="table-main table">
                                        <thead>
                                            <tr className="application-table-thead-tr">
                                                <th>SKUs</th>
                                                <th style={{display: 'flex', alignItems: 'center'}}>
                                                    <span style={{marginRight: '12px'}}>
                                                        <FormattedMessage id="productinfo.productid"/>
                                                    </span>
                                                    {/*<InfoIcon fontSize='small'/>*/}
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody className="table-tbody" role="rowgroup">
                                            { p2m.pai.appliedSku.map((item, index) => (
                                                <tr className="table-tbody-tr">
                                                    <td style={{width: '40%'}}>{item.skuCode}</td>
                                                    <td style={{display: 'flex', alignItems: 'center',width: '100%'}}>
                                                        <span style={{marginRight: '12px',width: '100%'}}>
                                                            {item.noIdProvide ? "由 DRS 提供": item.productId.value}
                                                        </span>
                                                        { d.u.isSp
                                                            ?  null
                                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                                ?   item.productId.value == ""
                                                                    ?   <span className="base-input-with-description-wrapper" style={{display: 'contents', margin: '8px 0',width: '50%'}}>
                                                                            <FormattedMessage id="productinfo.productIdByDrs">
                                                                                { placeholder =>
                                                                                    <input style={{marginRight: '1%'}}
                                                                                        className={`base-input`}
                                                                                        placeHolder={placeholder}
                                                                                        value={item.productIdByDrs}
                                                                                        onChange={(e) => onProductIdByDrsChange(e,index)}
                                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? false: true}
                                                                                    >
                                                                                    </input>
                                                                                }
                                                                            </FormattedMessage>
                                                                            <button className="drs-btn drs-btn-cta"
                                                                                onClick={()=> {handleProductIdByDrsSave(index)}}
                                                                            >
                                                                                Save
                                                                            </button>
                                                                        </span>
                                                                    : null
                                                                : null
                                                        }
                                                    </td>

                                                </tr>


                                            ))}
                                        </tbody>
                                    </table>
                                    <div id="productId">
                                        <Comment
                                            comment={p2m.pai.comment.productId}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showProductId}
                                            handleShow={handleShowEditor}
                                            target="productId"
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        7.  <FormattedMessage id="productinfo.subtitle15"/>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex"}}>
                                                 <FormattedMessage  id="productinfo.labelseparate">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkModelNumberAllSkuApplied(e)}
                                                            id="15"
                                                            isSelected={ p2m.pai.modelNumberAllSkuApplied === "no" }
                                                            label={label}
                                                            value="no"
                                                        />
                                                    }
                                                </FormattedMessage>
                                                <FormattedMessage  id="productinfo.labelapplyall">
                                                    { label =>
                                                        <RadioButton
                                                            changed={ (e) => checkModelNumberAllSkuApplied(e)}
                                                            id="16"
                                                            isSelected={ p2m.pai.modelNumberAllSkuApplied === "yes" }
                                                            label={label}
                                                            value="yes"
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('modelNumber');
                                                showModelNumber ? null : smoothScroll(document.getElementById('modelNumber'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                <p style={{color: '#0e6296' ,marginTop: '2%', marginLeft: '2%', fontSize: '14px'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}>請填入型號。請確認型號與說明書、包裝及證書等資料相符。</span>
                                </p>
                                { p2m.pai.modelNumberAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper" style={{margin: '2% 0'}}>
                                        {   p2m.pai.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-wrapper" style={{marginTop: '1%'}}>
                                                    <div className="base-input-label-wrapper">
                                                        <p className="application-input-label" style={{marginTop: '10px'}}>{item.skuCode}</p>
                                                    </div>
                                                    <div
                                                        className="base-input-with-description-wrapper"
                                                        style={ p2m.pai.modelNumberAllSkuApplied === 'no' ? {marginBottom: '0px'} : null }
                                                    >
                                                    <FormattedMessage id="productinfo.modelnumber">
                                                        { placeholder =>
                                                            <input
                                                                className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                placeHolder={placeholder}
                                                                value={item.modelNumber}
                                                                onChange={(e) => onModelNumberChange(e,index)}
                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                            >
                                                            </input>
                                                        }
                                                    </FormattedMessage>
                                                        {/* { index == p2m.pai.appliedSku.length - 1
                                                            ?   <span className="base-input-description">
                                                                    <FormattedMessage id="productinfo.paragraph2"/>
                                                                </span>
                                                            :   null
                                                        } */}
                                                    </div>
                                                </div>
                                            )
                                        })}
                                        </div>

                                    :    <div className="application-input-wrapper" style={{margin: '2% 0'}}>
                                            <div className="base-input-label-wrapper">
                                                <p className="application-input-label" style={{marginTop: '10px'}}>
                                                    <FormattedMessage id="productinfo.subtitle15"/>
                                                </p>
                                            </div>
                                            <div className="base-input-with-description-wrapper">
                                                <FormattedMessage id="productinfo.modelnumber">
                                                    { placeholder =>
                                                        <input
                                                            className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            placeHolder={placeholder}
                                                            value={p2m.pai.modelNumber}
                                                            onChange={(e) => onModelNumberChange(e)}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </input>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                }

                                <div id="modelNumber">
                                    <Comment
                                        comment={p2m.pai.comment.modelNumber}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showModelNumber}
                                        handleShow={handleShowEditor}
                                        target="modelNumber"
                                    />
                                </div>
                            </div>

                        </div>
                    </div>
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="productinfo.title2"/>
                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                *
                            </span>
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="productinfo.paragraph3"/>
                            </p>
                        </div>
                        <div className="application-info-section column align-center mt-1">
                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    {/*<div className="application-img-column">
                                        <div className="application-img-wrapper" style={{marginRight: '1%'}}>
                                            <img src={`/${Example}`} alt='sample'/>
                                        </div>
                                        <div className="application-img-wrapper" style={{marginRight: '1%'}}>
                                            <img src={`/${Example}`} alt='sample'/>
                                        </div>
                                        <div className="application-img-wrapper" style={{marginRight: '1%'}}>
                                            <img src={`/${Example}`} alt='sample'/>
                                        </div>
                                    </div>*/}
                                    <p className="application-intro-paragraph enhance">
                                        <span>
                                        <FormattedMessage id="productinfo.subtitle6"/>
                                        <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handlePackageReminderOpen()}}>
                                            <i class="fa fa-question-circle"></i>
                                            <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                        </span>
                                                {/* <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                    *
                                                </span> */}
                                        </span>
                                        { d.u.isSp
                                            ?  null :
                                            p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() =>{
                                                     handleShowEditor('packageImg');
                                                     showPackageImg? null : smoothScroll(document.getElementById('packageImg'))
                                                    }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </p>
                                    <Modal
                                        open={packageReminder}
                                        onClose={handlePackageReminderClose}
                                        aria-labelledby="simple-modal-title"
                                        aria-describedby="simple-modal-description"
                                    >
                                    {learnPackageMoreModal}
                                    </Modal>
                                    { showUploader
                                        ? <MultiImgUploader
                                                onClose={setShowUploader}
                                                onSubmit={handleSubmitImg}
                                                index={showIndex}
                                            />
                                        : null
                                    }
                                    { p2m.pai.appliedSku.map((item, index) => {
                                        return (
                                                <div>
                                                        { item.packageImg.length == 0 || p2m.currentAp.status !== p2m.stMap.get(0)
                                                            ?   <div style={{width: '100%', alignItems: 'center', justifyContent: 'space-between'}}>
                                                                    <span style={{margin: '0 10px 0 6px', fontSize: '1rem', fontWeight: '600'}}>{item.skuCode}</span>
                                                                    <span className="drs-blue" style={{width: '100%', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="picture.required"/>
                                                                    </span>
                                                                </div>
                                                            :   <div style={{width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                                                                    <span style={{margin: '0 10px 0 6px', fontSize: '1rem', fontWeight: '600'}}>{item.skuCode}</span>
                                                                    <div style={{display: 'flex', alignItems: 'center'}}>
                                                                        <button
                                                                            className="drs-btn drs-btn-normal"
                                                                            onClick={() => handleUploadAgain('packageImg', index) }>
                                                                            <span style={{marginLeft:'6px'}}>重新上傳</span>
                                                                        </button>
                                                                        <button
                                                                            className="drs-btn drs-btn-normal"
                                                                            onClick={() => handleRemoveAll( 'packageImg',index) }>
                                                                            <span style={{marginLeft:'6px'}}>清空全部</span>
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                        }
                                                <div>
                                                { item.packageImg.length == 0
                                                    ?   <div className="upload-img-area">
                                                            <button
                                                                className={`upload-img-btn ${!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0) ? 'disabled': ''}`}
                                                                onClick={() => { onUpload(index)}}
                                                                disabled={!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0)}
                                                            >
                                                                <InsertPhotoIcon fontSize="medium"/>
                                                                <span style={{marginLeft:'6px'}}>
                                                                    <FormattedMessage id="productinfo.uploadimage"/>
                                                                </span>
                                                            </button>
                                                        </div>
                                                    :   <div className="upload-img-preview-wrapper">
                                                            { item.packageImg.map((file) => (
                                                                <div className="upload-img-preview-column">
                                                                    {file.preview == '' ?
                                                                     <div className="upload-img-preview"><img src={`${DOMAIN_NAME}/p2m/pai/i/${p2m.currentAp.name}/${file.name}`}></img></div>
                                                                        : <div className="upload-img-preview"><img src={file.preview}></img></div>
                                                                    }
                                                                    <a href={`${DOMAIN_NAME}/p2m/pai/i/${p2m.currentAp.name}/${file.name}`} download={file.name}>
                                                                        <p style={{fontSize: '14px', margin: '8px 0'}}>{file.name}</p>
                                                                    </a>
                                                                </div>
                                                            ))}
                                                        </div>
                                                }
                                                </div>
                                            </div>
                                        )
                                    })}
                                    <div id="packageImg">
                                        <Comment
                                            comment={p2m.pai.comment.packageImg}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showPackageImg}
                                            handleShow={handleShowEditor}
                                            target="packageImg"
                                        />
                                    </div>

                                </div>

                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <span>
                                            <FormattedMessage id="productinfo.subtitle7"/>
                                            {/* <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span> */}
                                        </span>
                                        { d.u.isSp
                                            ? p2m.currentAp.status == p2m.stMap.get(0)
                                                ? <div className="paragraph-inline-btn-wrapper">
                                                    <span style={{marginRight: '5px'}}>
                                                        <FormattedMessage id="productinfo.adduploadspace"/>
                                                    </span>
                                                    <button
                                                        className="paragraph-inline-btn"
                                                        onClick={() => handlePackageFile('add')}
                                                    ><i class="fa fa-plus" aria-hidden="true"></i>
                                                    </button>
                                                </div>
                                                : null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('packageFile');
                                                    showPackageFile ? null : smoothScroll(document.getElementById('packageFile'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </p>

                                    { p2m.pai.packageFile.map((item, index) => (
                                        <div className="application-upload-section-wrapper">
                                            {p2m.pai.packageFile.length > 1
                                                ?   <div className="paragraph-inline-btn-wrapper" >
                                                        <span style={{marginRight: '5px'}}>
                                                            移除此項
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handlePackageFile('minus',index)}
                                                            disabled={p2m.pai.packageFile.length > 1 ? false : true}
                                                        ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                : <div></div>
                                            }
                                            <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                    <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                        <span style={{fontSize: '14px'}}>
                                                            <FormattedMessage id="productinfo.applytosku"/>
                                                        </span>
                                                    </div>
                                                    <FormattedMessage id="productinfo.select">
                                                        { placeholder =>
                                                            <Select
                                                                className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                styles={customStyles}
                                                                closeMenuOnSelect={false}
                                                                value={item.appliedSku}
                                                                placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                onChange={(e)=> handleSelectSku(e,index,'package')}
                                                                options={options}
                                                                classNamePrefix='drsSelector'
                                                                isMulti
                                                                isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                    { item.appliedSku == ''
                                                        ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px', marginTop: '2%'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="selectField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                <div className="base-input-file-wrapper" style={{width: '55%'}}>
                                                    <div className="base-input-file-column" style={{width: '100%'}}>
                                                        <FileUploader
                                                            id={`packageFile-${index}`}
                                                            key={`packageFile-${index}`}
                                                            fileName={item.name}
                                                            index={index}
                                                            href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                            onChange={handlePackageFile}
                                                            disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                        />
                                                    </div>
                                                </div>
                                                { item.name == 'Choose a file'
                                                    ?   <div className="drs-blue"  style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px', marginTop: '1%'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="file.required"/>
                                                        </div>
                                                    :   null
                                                }
                                            </div>
                                        </div>
                                    ))}
                                    <div id="packageFile">
                                        <Comment
                                            comment={p2m.pai.comment.packageFile}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showPackageFile}
                                            handleShow={handleShowEditor}
                                            target="packageFile"
                                        />
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="productinfo.title3"/>
                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                *
                            </span>
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="productinfo.paragraph3-1"/>
                                <Link to="/p2mExplanation/paiExplanation1" target="_blank">
                                    <FormattedMessage id="basedata.learnmore"/><OpenInNewIcon/>
                                </Link>
                            </p>
                        </div>
                        <div className="application-info-section column align-center mt-1">
                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance">
                                        <FormattedMessage id="productinfo.subtitle8"/>
                                        { d.u.isSp
                                            ? null :
                                            p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('manualImg');
                                                    showManualImg ? null : smoothScroll(document.getElementById('manualImg'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </p>
                                    { showManualImgUploader
                                        ?   <MultiImgUploader
                                                onClose={setShowManualImgUploader}
                                                onSubmit={handleManualImgSubmit}
                                                index={showManualImgIndex}
                                            />
                                        : null
                                    }
                                    { p2m.pai.appliedSku.map((item, index) => {
                                        return (
                                            <div>
                                                <div style={{width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                                                    <p style={{margin: '0 10px 0 6px', fontSize: '1rem', fontWeight: '600'}}>{item.skuCode}</p>
                                                    { item.manualImg.length == 0 || p2m.currentAp.status !== p2m.stMap.get(0)
                                                        ?   null
                                                        :   <div style={{display: 'flex', alignItems: 'center'}}>
                                                                <button
                                                                    className="drs-btn drs-btn-normal"
                                                                    onClick={() => handleUploadAgain('manualImg', index) }>
                                                                    <span style={{marginLeft:'6px'}}>重新上傳</span>
                                                                </button>
                                                                <button
                                                                    className="drs-btn drs-btn-normal"
                                                                    onClick={() => handleRemoveAll( 'manualImg',index) }>
                                                                    <span style={{marginLeft:'6px'}}>清空全部</span>
                                                                </button>
                                                            </div>
                                                    }
                                                </div>
                                                <div>
                                                    { item.manualImg.length == 0
                                                        ?   <div className="upload-img-area">
                                                                <button
                                                                    className={`upload-img-btn ${!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0) ? 'disabled': ''}`}
                                                                    disabled={!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    onClick={() => {onManualImgUpload(index)}}
                                                                >
                                                                    <InsertPhotoIcon fontSize="medium"/>
                                                                    <span style={{marginLeft:'6px'}}>
                                                                        <FormattedMessage id="dropzone.description"/>
                                                                    </span>
                                                                </button>
                                                            </div>
                                                        :   <div className="upload-img-preview-wrapper">
                                                            {item.manualImg.map((file) => (
                                                                <div className="upload-img-preview-column">
                                                                    {file.preview == '' ?
                                                                     <div className="upload-img-preview"><img src={`${DOMAIN_NAME}/p2m/pai/i/${p2m.currentAp.name}/${file.name}`}></img></div>
                                                                        : <div className="upload-img-preview"><img src={file.preview}></img></div>
                                                                    }
                                                                    <a href={`${DOMAIN_NAME}/p2m/pai/i/${p2m.currentAp.name}/${file.name}`} download={file.name}>
                                                                        <p style={{fontSize: '14px', margin: '8px 0'}}>{file.name}</p>
                                                                    </a>
                                                                </div>
                                                            ))}
                                                            </div>
                                                    }
                                                </div>
                                            </div>
                                        )
                                    })}
                                    <div id="manualImg">
                                        <Comment
                                            comment={p2m.pai.comment.manualImg}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showManualImg}
                                            handleShow={handleShowEditor}
                                            target="manualImg"
                                        />
                                    </div>
                                </div>
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <span>
                                            <FormattedMessage id="productinfo.subtitle9"/>
                                            {/* <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span> */}
                                        </span>
                                        { d.u.isSp
                                            ? p2m.currentAp.status == p2m.stMap.get(0)
                                                ?   <div className="paragraph-inline-btn-wrapper">
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="productinfo.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleManualFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                : null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('manualFile');
                                                    showManualFile ? null : smoothScroll(document.getElementById('manualFile'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </p>
                                    { p2m.pai.manualFile.map((item, index) => (
                                        <div className="application-upload-section-wrapper">
                                            {p2m.pai.manualFile.length > 1
                                                ?   <div className="paragraph-inline-btn-wrapper" >
                                                        <span style={{marginRight: '5px'}}>
                                                            移除此項
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleManualFile('minus',index)}
                                                            disabled={p2m.pai.manualFile.length > 1 ? false : true}
                                                        ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                : <div></div>
                                            }
                                            <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                    <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                        <span style={{fontSize: '14px'}}>
                                                            <FormattedMessage id="productinfo.applytosku"/>
                                                        </span>
                                                    </div>
                                                    <FormattedMessage id="productinfo.select">
                                                        { placeholder =>
                                                            <Select
                                                                className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                styles={customStyles}
                                                                closeMenuOnSelect={false}
                                                                value={item.appliedSku}
                                                                placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                onChange={(e) => handleSelectSku(e, index, 'manual')}
                                                                options={options}
                                                                classNamePrefix='drsSelector'
                                                                isMulti
                                                                isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                    { item.appliedSku == ''
                                                        ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px', marginTop: '2%'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="selectField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                <div className="base-input-file-wrapper" style={{width: '55%'}}>
                                                    <div className="base-input-file-column" style={{width: '100%'}}>
                                                        <FileUploader
                                                            id={`manualFile-${index}`}
                                                            key={`manualFile-${index}`}
                                                            fileName={item.name}
                                                            index={index}
                                                            href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                            onChange={handleManualFile}
                                                            disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                        />
                                                    </div>
                                                </div>
                                                { item.name == 'Choose a file'
                                                  ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px', marginTop: '1%'}}>
                                                          <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                          <FormattedMessage id="file.required"/>
                                                      </div>
                                                  :   null
                                                }
                                            </div>
                                        </div>
                                    ))}
                                    <div id="manualFile">
                                        <Comment
                                            comment={p2m.pai.comment.manualFile}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showManualFile}
                                            handleShow={handleShowEditor}
                                            target="manualFile"
                                        />
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="productinfo.title4"/>
                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                *
                            </span>
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="productinfo.paragraph4"/>
                                <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handleReminderOpen()}}>
                                    <i class="fa fa-question-circle"></i>
                                    <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                </span>
                            </p>
                        </div>
                        <Modal
                            open={reminder}
                            onClose={handleReminderClose}
                            aria-labelledby="simple-modal-title"
                            aria-describedby="simple-modal-description"
                        >
                          {learnMoreModal}
                        </Modal>
                        <div className="application-info-section column align-center mt-1">

                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <span>
                                            <FormattedMessage id="productinfo.subtitle10"/>
                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span>
                                        </span>
                                        { d.u.isSp
                                            ? null :
                                            p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('hsCode');
                                                    showHsCode? null : smoothScroll(document.getElementById('hsCode'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </p>
                                    <table role="table" className="table-main table">
                                        <thead>
                                            <tr className="application-table-thead-tr">
                                                <th>SKUs</th>
                                                <th><FormattedMessage id="productinfo.supplysidetaxcode"/>
                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                    *
                                                </span></th>
                                                <th><FormattedMessage id="productinfo.salessidetaxcode"/>
                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                    *
                                                </span></th>
                                            </tr>
                                        </thead>
                                        <tbody className="table-tbody" role="rowgroup">
                                            { p2m.pai.appliedSku.map((item, index) => (
                                                <tr className="table-tbody-tr">
                                                    <td>{item.skuCode}</td>
                                                    <td>
                                                        <span>
                                                            <FormattedMessage id="productinfo.supplysidetaxcode">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        placeholder={placeholder}
                                                                        value={item.exportSideHsCode}
                                                                        onChange={(e) => onExportSideHsCodeChange(e, index)}
                                                                        readOnly={p2m.currentAp.status !==p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            { item.exportSideHsCode == ''
                                                                ?   <span className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span>
                                                            <FormattedMessage id="productinfo.salessidetaxcode">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !==p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        placeholder={placeholder}
                                                                        value={item.salesSideHsCode}
                                                                        onChange={(e) => onSalesSideHsCodeChange(e, index)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            { item.salesSideHsCode == ''
                                                                ?   <span className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </span>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                    <div id="hsCode">
                                        <Comment
                                            comment={p2m.pai.comment.hsCode}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showHsCode}
                                            handleShow={handleShowEditor}
                                            target="hsCode"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="productinfo.title5"/>
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="productinfo.paragraph5"/>
                            </p>
                        </div>
                        <div className="application-info-section column align-center mt-1">
                            <div className="application-info-row">
                                <p className="application-intro-paragraph enhance space-between">
                                    <span>
                                        <FormattedMessage id="productinfo.subtitle11"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    { d.u.isSp
                                        ?  null :
                                        p2m.currentAp.status == p2m.stMap.get(1)
                                            ?  <button onClick={() => {
                                                handleShowEditor('ingredient');
                                                showIngredient ? null : smoothScroll(document.getElementById('ingredient'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                {/* <p style={{color: '#0e6296' ,marginTop: '2%', fontSize: '14px', marginLeft: '2%', marginBottom: '2%'}}>
                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                    <span style={{marginLeft: '1%'}}><FormattedMessage id="productinfo.paragraph6"/></span>
                                </p> */}
                                <div className="application-input-wrapper multiple-rows">
                                    <div className="base-input-label-wrapper" style={{width: '15%'}}>
                                        <p className="application-input-label">
                                            <FormattedMessage id="productinfo.productingredients"/>
                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span>
                                        </p>
                                    </div>
                                    <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                        <FormattedMessage id="productinfo.paragraph6">
                                            { placeholder =>
                                                <textarea
                                                    id="ingredient"
                                                    className={`base-textarea ${p2m.currentAp.status !==p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                    name="ingredient"
                                                    rows="4"
                                                    onChange={(e) => onIngredientChange(e)}
                                                    // onBlur={(e) => onIngredientBlur(e)}
                                                    value={p2m.pai.ingredient}
                                                    placeholder={placeholder}
                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                >
                                                </textarea>
                                            }
                                        </FormattedMessage>
                                        { p2m.pai.ingredient == ''
                                            ?   <span className="drs-blue" style={{width: '10%', alignItems: 'center', margin: '10px', fontSize: '13px'}}>
                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                    <FormattedMessage id="inputField.required"/>
                                                </span>
                                            :   null
                                        }
                                    </div>
                                </div>
                                <div className="application-upload-section-wrapper" >
                                            <div className="application-upload-sub-section-wrapper">
                                                <p className="application-upload-intro-title space-between" style={{marginBottom: '1%', padding: '0 1% 1% 1%',borderBottom: '1px solid #d8d8d8'}}>
                                                    <FormattedMessage id="productinfo.paragraph7"/>
                                                    <div className="paragraph-inline-btn-wrapper">
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="productinfo.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleIngredientFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                </p>
                                                {  p2m.pai.ingredientFile.map((item, index) => (
                                                    <div className="application-upload-section-wrapper">
                                                        {p2m.pai.ingredientFile.length > 1
                                                            ?   <div className="paragraph-inline-btn-wrapper" >
                                                                    <span style={{marginRight: '5px'}}>
                                                                        移除此項
                                                                    </span>
                                                                    <button
                                                                        className="paragraph-inline-btn"
                                                                        onClick={() => handleIngredientFile('minus',index)}
                                                                        disabled={p2m.pai.ingredientFile.length > 1 ? false : true}
                                                                    ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                            : <div></div>
                                                        }

                                                        <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                            <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                                <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                                    <span style={{fontSize: '14px'}}>
                                                                        <FormattedMessage id="productinfo.applytosku"/>
                                                                    </span>
                                                                </div>
                                                                <FormattedMessage id="productinfo.select">
                                                                    { placeholder =>
                                                                        <Select
                                                                            className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            styles={customStyles}
                                                                            closeMenuOnSelect={false}
                                                                            value={item.appliedSku}
                                                                            placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                            onChange={(e) => handleSelectSku(e, index, 'ingredient')}
                                                                            options={options}
                                                                            classNamePrefix='drsSelector'
                                                                            isMulti
                                                                            isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                                        />
                                                                    }
                                                                </FormattedMessage>
                                                            </div>
                                                            <div className="base-input-file-wrapper">
                                                                <div className="base-input-file-column">
                                                                    <FileUploader
                                                                        fileName={item.name}
                                                                        index={index}
                                                                        href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                                        onChange={handleIngredientFile}
                                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                <div id="ingredient">
                                    <Comment
                                        comment={p2m.pai.comment.ingredient}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showIngredient}
                                        handleShow={handleShowEditor}
                                        target="ingredient"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="productinfo.subtitle12"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ?   <div className="radio-btn-container" style={{ display: "flex" }}>
                                                    <FormattedMessage id="productinfo.labelno">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsWooden(e)}
                                                                id="9"
                                                                isSelected={p2m.pai.isWooden === "no" }
                                                                label={label}
                                                                value="no"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                    <FormattedMessage id="productinfo.labelyes">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsWooden(e) }
                                                                id="10"
                                                                isSelected={ p2m.pai.isWooden === "yes" }
                                                                label={label}
                                                                value="yes"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('woodenFile');
                                                showWoodenFile ? null : smoothScroll(document.getElementById('woodenFile'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>
                                { p2m.pai.isWooden == 'yes'
                                    ?   <div className="application-input-wrapper" style={{backgroundColor: '#dee0e3', padding: '1%'}}>
                                            <div className="application-upload-sub-section-wrapper">
                                                <p style={{color: '#0e6296' ,margin: '2% 0', fontSize: '14px'}}>
                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                    <span style={{lineHeight : '1.5rem', marginLeft: '1%'}}>如果您的產品包含木材成分且目標銷售市場為美國地區，請至美國農業部 USDA 網站下載「
                                                        <a href="https://www.aphis.usda.gov/library/forms/pdf/ppq505.pdf" download>
                                                            PPQ Form 505 - Plant and Plant Product Declaration Form
                                                        </a>
                                                        」，填寫並簽名後上傳至本平台。
                                                    建議您不論是否在美國地區銷售，仍須在銷售至目標市場前充分知悉產品使用之木材的來源及品種。</span>
                                                </p>
                                                <p className="application-upload-intro-title space-between" style={{marginBottom: '1%', padding: '0 1% 1% 1%',borderBottom: '1px solid #d8d8d8'}}>
                                                    <span>
                                                        <FormattedMessage id="productinfo.paragraph7"/>
                                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                            *
                                                        </span>
                                                    </span>
                                                    <div className="paragraph-inline-btn-wrapper">
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="productinfo.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleWoodenFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                </p>
                                                {  p2m.pai.woodenFile.map((item, index) => (
                                                    <div className="application-upload-section-wrapper">
                                                        {p2m.pai.woodenFile.length > 1
                                                            ?   <div className="paragraph-inline-btn-wrapper" >
                                                                    <span style={{marginRight: '5px'}}>
                                                                        移除此項
                                                                    </span>
                                                                    <button
                                                                        className="paragraph-inline-btn"
                                                                        onClick={() => handleWoodenFile('minus',index)}
                                                                        disabled={p2m.pai.woodenFile.length > 1 ? false : true}
                                                                    ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                            : <div></div>
                                                        }
                                                        <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                            <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                                <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                                    <span style={{fontSize: '14px'}}>
                                                                        <FormattedMessage id="productinfo.applytosku"/>
                                                                    </span>
                                                                </div>
                                                                <FormattedMessage id="productinfo.select">
                                                                    { placeholder =>
                                                                        <Select
                                                                            className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            styles={customStyles}
                                                                            closeMenuOnSelect={false}
                                                                            value={item.appliedSku}
                                                                            placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                            onChange={(e) => handleSelectSku(e, index, 'wooden')}
                                                                            options={options}
                                                                            classNamePrefix='drsSelector'
                                                                            isMulti
                                                                            isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                                        />
                                                                    }
                                                                </FormattedMessage>
                                                                { item.appliedSku == ''
                                                                    ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px', marginTop: '2%'}}>
                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                            <FormattedMessage id="selectField.required"/>
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div className="base-input-file-wrapper" style={{width: '55%'}}>
                                                                <div className="base-input-file-column" style={{width: '100%'}}>
                                                                    <FileUploader
                                                                        fileName={item.name}
                                                                        index={index}
                                                                        href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                                        onChange={handleWoodenFile}
                                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    />
                                                                </div>
                                                            </div>
                                                            { item.name == 'Choose a file'
                                                                ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px', marginTop: '1%'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="file.required"/>
                                                                    </div>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    : null
                                }
                                <div id="woodenFile">
                                    <Comment
                                        comment={p2m.pai.comment.woodenFile}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showWoodenFile}
                                        handleShow={handleShowEditor}
                                        target="woodenFile"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="productinfo.subtitle13"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ? <div className="radio-btn-container" style={{ display: "flex" }}>
                                                    <FormattedMessage id="productinfo.labelno">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsWireless(e)}
                                                                id="11"
                                                                isSelected={ p2m.pai.isWireless === "no" }
                                                                label={label}
                                                                value="no"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                    <FormattedMessage id="productinfo.labelyes">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsWireless(e) }
                                                                id="12"
                                                                isSelected={ p2m.pai.isWireless === "yes" }
                                                                label={label}
                                                                value="yes"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                            </div>
                                            : null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('wirelessFile');
                                                showWirelessFile? null : smoothScroll(document.getElementById('wirelessFile'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }

                                </p>

                                { p2m.pai.isWireless == 'yes'
                                    ?   <div className="application-input-wrapper" style={{backgroundColor: '#dee0e3', padding: '1%'}}>
                                            <div className="application-upload-sub-section-wrapper">
                                                <p className="application-upload-intro-title space-between" style={{marginBottom: '1%', padding: '0 1% 1% 1%',borderBottom: '1px solid #d8d8d8'}}>
                                                    <span>
                                                        <FormattedMessage id="productinfo.paragraph7"/>
                                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                            *
                                                        </span>
                                                    </span>
                                                    <div className="paragraph-inline-btn-wrapper">
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="productinfo.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleWirelessFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                </p>
                                                { p2m.pai.wirelessFile.map((item, index) => (
                                                    <div className="application-upload-section-wrapper">
                                                        {p2m.pai.wirelessFile.length > 1
                                                            ?   <div className="paragraph-inline-btn-wrapper" >
                                                                    <span style={{marginRight: '5px'}}>
                                                                        移除此項
                                                                    </span>
                                                                    <button
                                                                        className="paragraph-inline-btn"
                                                                        onClick={() => handleWirelessFile('minus',index)}
                                                                        disabled={p2m.pai.wirelessFile.length > 1 ? false : true}
                                                                    ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                            : <div></div>
                                                        }
                                                        <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                            <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                                <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                                    <span style={{fontSize: '14px'}}>
                                                                        <FormattedMessage id="productinfo.applytosku"/>
                                                                    </span>
                                                                </div>
                                                                <FormattedMessage id="productinfo.select">
                                                                    { placeholder =>
                                                                        <Select
                                                                            className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                            styles={customStyles}
                                                                            closeMenuOnSelect={false}
                                                                            value={item.appliedSku}
                                                                            placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                            onChange={(e) => handleSelectSku(e, index, 'wireless')}
                                                                            options={options}
                                                                            classNamePrefix='drsSelector'
                                                                            isMulti
                                                                            isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                                        />
                                                                    }
                                                                </FormattedMessage>
                                                                { item.appliedSku == ''
                                                                    ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px', marginTop: '2%'}}>
                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                            <FormattedMessage id="selectField.required"/>
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div className="base-input-file-wrapper" style={{width: '55%'}}>
                                                                <div className="base-input-file-column" style={{width: '100%'}}>
                                                                    <FileUploader
                                                                        fileName={item.name}
                                                                        index={index}
                                                                        href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                                        onChange={handleWirelessFile}
                                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    />
                                                                </div>
                                                            </div>
                                                            { item.name == 'Choose a file'
                                                                ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px', marginTop: '1%'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="file.required"/>
                                                                    </div>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    : null
                                }
                                <div id="wirelessFile">
                                    <Comment
                                        comment={p2m.pai.comment.wirelessFile}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showWirelessFile}
                                        handleShow={handleShowEditor}
                                        target="wirelessFile"
                                    />
                                </div>
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="productinfo.subtitle14"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    { d.u.isSp
                                        ? p2m.currentAp.status == p2m.stMap.get(0)
                                            ?   <div className="radio-btn-container" style={{ display: "flex" }}>
                                                    <FormattedMessage id="productinfo.labelno">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsBattery(e)}
                                                                id="13"
                                                                isSelected={p2m.pai.isBattery === "no" }
                                                                label={label}
                                                                value="no"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                    <FormattedMessage id="productinfo.labelyes">
                                                        { label=>
                                                            <RadioButton
                                                                changed={ (e) => checkIsBattery(e) }
                                                                id="14"
                                                                isSelected={ p2m.pai.isBattery === "yes" }
                                                                label={label}
                                                                value="yes"
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                </div>
                                            :   null
                                        : p2m.currentAp.status == p2m.stMap.get(1)
                                            ? <button onClick={() => {
                                                handleShowEditor('batteryFile');
                                                showBatteryFile? null : smoothScroll(document.getElementById('batteryFile'))
                                            }}><RateReviewIcon/></button>
                                            : null
                                    }
                                </p>

                                { p2m.pai.isBattery == 'yes'
                                    ?   <div className="application-input-wrapper" style={{backgroundColor: '#dee0e3', padding: '1%'}}>
                                            <div className="application-upload-sub-section-wrapper">
                                                <p className="space-between" style={{marginBottom: '1%', padding: '0 1% 1% 1%',borderBottom: '1px solid #d8d8d8'}}>
                                                    <div className="application-upload-intro-wrapper">
                                                        <p className="application-upload-intro-title" style={{marginBottom: '8px'}}>
                                                            <FormattedMessage id="productinfo.paragraph8"/>
                                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                *
                                                            </span>
                                                            <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handleBatteryReminderOpen()}}>
                                                                <i class="fa fa-question-circle"></i>
                                                                <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                                            </span>

                                                        </p>
                                                        <Modal
                                                            open={batteryReminder}
                                                            onClose={handleBatteryReminderClose}
                                                            aria-labelledby="simple-modal-title"
                                                            aria-describedby="simple-modal-description"
                                                        >
                                                          {learnBatteryMoreModal}
                                                        </Modal>
                                                        <p className="application-upload-intro-text">
                                                            <FormattedMessage id="productinfo.subparagraph1"/> <strong>MSDS </strong>
                                                            <FormattedMessage id="productinfo.subparagraph2"/> <strong>UN38.3 </strong>
                                                            <FormattedMessage id="productinfo.subparagraph3"/>
                                                        </p>
                                                        <p className="application-upload-intro-text">
                                                            <FormattedMessage id="productinfo.subparagraph4"/> <strong>MSDS </strong>
                                                            <FormattedMessage id="productinfo.subparagraph2"/> <strong>Vibration Test</strong>、<strong>Pressure Differential Test </strong>
                                                            <FormattedMessage id="productinfo.subparagraph3"/>
                                                        </p>
                                                    </div>
                                                    <div className="paragraph-inline-btn-wrapper">
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="productinfo.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleBatteryFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                </p>
                                                {  p2m.pai.batteryFile.map((item, index) => (
                                                    <div className="application-upload-section-wrapper">
                                                        {p2m.pai.batteryFile.length > 1
                                                            ?   <div className="paragraph-inline-btn-wrapper" >
                                                                    <span style={{marginRight: '5px'}}>
                                                                        移除此項
                                                                    </span>
                                                                    <button
                                                                        className="paragraph-inline-btn"
                                                                        onClick={() => handleBatteryFile('minus',index)}
                                                                        disabled={p2m.pai.batteryFile.length > 1 ? false : true}
                                                                    ><i class="fa fa-minus-square" aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                            : <div></div>
                                                        }
                                                        <p style={{color: '#f0142f', margin: '1% 0',fontSize: '14px'}}>{notification}</p>
                                                        <div className="base-input-row-wrapper" style={{flexWrap: 'wrap'}}>
                                                            <div className="selector-inline-wrapper" style={{width: '60%', marginBottom: '1%',alignItems:'normal'}}>
                                                                <div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
                                                                    <span style={{fontSize: '14px'}}>
                                                                        <FormattedMessage id="productinfo.applytosku"/>
                                                                    </span>
                                                                </div>
                                                                <FormattedMessage id="productinfo.select">
                                                                    { placeholder =>
                                                                        <Select
                                                                            className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                            styles={customStyles}
                                                                            closeMenuOnSelect={false}
                                                                            value={item.appliedSku}
                                                                            placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                            onChange={(e) => handleSelectSku(e, index, 'battery')}
                                                                            options={options}
                                                                            classNamePrefix='drsSelector'
                                                                            isMulti
                                                                            isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                                        />
                                                                    }
                                                                </FormattedMessage>
                                                            </div>
                                                            { item.appliedSku == ''
                                                                ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="selectField.required"/>
                                                                    </p>
                                                                :   null
                                                            }
                                                            <div className="selector-inline-wrapper" style={{width: '60%', marginBottom: '1%'}}>
                                                                <div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
                                                                    <span style={{fontSize: '14px'}}>
                                                                        <FormattedMessage id="productinfo.batterytype"/>
                                                                    </span>
                                                                </div>
                                                                <FormattedMessage id="productinfo.select">
                                                                    { placeholder =>
                                                                        <Select styles={{alignItems:'normal'}}
                                                                            className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                            styles={customStyles}
                                                                            value={item.batteryCategory}
                                                                            onChange={(e)=> onBatteryCategoryChange(e,index)}
                                                                            placeholder={item.batteryCategory == ''? placeholder: item.batteryCategory}
                                                                            options={batteryCategoryOptions}
                                                                            classNamePrefix='mySelector'
                                                                            isDisabled={p2m.currentAp.status == p2m.stMap.get(1)}
                                                                        />
                                                                    }
                                                                </FormattedMessage>
                                                            </div>
                                                            { item.batteryCategory == ''
                                                                ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="selectField.required"/>
                                                                    </div>
                                                                :   null
                                                            }

                                                            <div className="base-input-file-wrapper" style={{width: '60%', marginBottom: '1%'}}>
                                                                <div className="base-input-file-column" style={{width: '100%'}}>
                                                                    <FileUploader
                                                                        fileName={item.name}
                                                                        index={index}
                                                                        href={`${DOMAIN_NAME}/p2m/pai/f/${p2m.currentAp.name}/${item.name}`}
                                                                        onChange={handleBatteryFile}
                                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    />
                                                                </div>
                                                            </div>
                                                            { item.name == 'Choose a file'
                                                                ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="file.required"/>
                                                                    </div>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    : null
                                }
                                <div id="batteryFile">
                                    <Comment
                                        comment={p2m.pai.comment.batteryFile}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showBatteryFile}
                                        handleShow={handleShowEditor}
                                        target="batteryFile"
                                    />
                                </div>
                            </div>
                        </div>
                        <div>
                            {showValidWarning
                                ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column', marginTop: '2px'}}>
                                        <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                            <FormattedMessage id="productinfo.warning"/>
                                        </p>
                                    </div>
                                :   null
                            }
                        </div>
                    </div>
                    <div className="operation-pannel-footer" style={{display:'flex',justifyContent:'end'}}>
                        <div className="operation-btn-group">
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn" style={{backgroundColor:'#0085CC'}} onClick={() => {handleSave("Draft")}}>
                                        儲存草稿
                                    </button>
                                :   null
                            }
                        </div>
                        <div className="operation-btn-group">
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn drs-btn-cta" onClick={() => {handleValid()}}>
                                        <FormattedMessage id="save"/>
                                    </button>
                                // :   <Link to={`/product/application/${id}`}>
                                :   <Link to='/product/application'>
                                        <button className="drs-btn drs-btn-normal">
                                            <FormattedMessage id="back"/>
                                        </button>
                                    </Link>
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ProductInfo