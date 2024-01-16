import {createStore} from "vuex";

const store =  createStore({
    state:{
        user:{
            /**
             * 用户id
             */
            id: "",

            /**
             * 用户昵称
             */
            userName: "",

            /**
             * 账号
             */
            userAccount: "",

            /**
             * 用户头像
             */
            userAvatar: "",

            /**
             * 性别
             */
            gender: "",

            /**
             * 用户角色: user, admin
             */
            userRole: "",

            /**
             * accessKey
             */
            accessKey: "",

            /**
             * secretKey
             */
            secretKey: "",

            /**
             * 创建时间
             */
            createTime: "",

            /**
             * 更新时间
             */
            updateTime: "",
        }
    },
    getters:{},
    mutations:{
        setUser(state, user){
            state.user.id = user.id;
            state.user.userAccount = user.userAccount;
            state.user.userName = user.userName;
            state.user.userAvatar = user.userAvatar;
            state.user.gender = user.gender;
            state.user.userRole = user.userRole;
            state.user.accessKey = user.accessKey;
            state.user.secretKey = user.secretKey;
            state.user.createTime = user.createTime;
            state.user.updateTime = user.updateTime;
        }
    },
    actions:{
        setUser(context, user){
            context.commit("setUser", user);
        }
    },
    modules:{}
})

export default store;