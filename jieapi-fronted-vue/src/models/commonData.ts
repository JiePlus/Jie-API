export interface commonData {
    name: string;
    key: string;
    value: any;
    type: string;
}

export interface commonDataList {
    [index: number]: commonData
}