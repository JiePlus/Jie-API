import {ElMessage} from "element-plus";
import * as imageConversion from 'image-conversion';

export function beforeImgUpload(file: any) {
    console.log('原图大小:', file.size);
    const isJpgOrPng =
        file.type === "image/jpeg" || file.type === "image/png";
    const isLt100K = file.size / 1024 / 1024 < 0.1;
    if (!isJpgOrPng) {
        ElMessage.error("上传图片只能是 JPG 或 PNG 格式!");
        return false;
    }
    return new Promise((resolve) => {
        // 小于100K 不压缩
        if (isLt100K) {
            resolve(file)
        }
        // 压缩到400KB,这里的400就是要压缩的大小,可自定义
        imageConversion.compressAccurately(file, 100).then((res) => {
            // console.log('原图大小:', file.size);
            // console.log('压缩后大小:', res.size);
            resolve(res);
        });
    });
}
