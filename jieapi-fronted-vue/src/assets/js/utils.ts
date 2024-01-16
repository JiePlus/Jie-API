import {getAvatar} from "../../api/user.ts";

const getImgUrl = async (url: string | null) => {
    // 如果url为空, 设置一个默认头像
    if (!url) {
        return 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg'
    }
    // 检查url是否是file开头
    if (!url.startsWith('file')) {
        return url
    }
    // 把ossFilePath的所有/替换成%2F
    url = url.replace(/\//g, '%2F')
    const res = await getAvatar(url)
    if (res.code === 0) {
        return res.data
    }
    return ''
}

export default getImgUrl