/**
 * 深度克隆
 * @param baseData
 * @param keys 排除的key列表
 */
export const deepCopy = (baseData: any[], keys: string[] = []): object[] => {
    const data = JSON.parse(JSON.stringify(baseData))
    for (let key of keys) {
        // for循环遍历 如果对象中key的值为url 则剔除
        for (let i = 0; i < data.length; i++) {
            if (data[i].key === key) {
                data.splice(i, 1)
            }
        }
    }
    // 返回结果数组
    return data;
};

/**
 * 生成接口请求参数
 *
 * @param input
 */
export const generateOutput = (input: string): string => {
    let inputObjects: any[] = JSON.parse(input);
    let output: any = {};
    inputObjects.forEach((item) => {
        switch (item.type) {
            case "string":
                output[item.name] = "";
                break;
            case "number":
                output[item.name] = 0;
                break;
            case "boolean":
                output[item.name] = false;
                break;
            default:
                console.log(`Unknown type: ${item.type}`);
                break;
        }
    });
    return JSON.stringify(output);
}