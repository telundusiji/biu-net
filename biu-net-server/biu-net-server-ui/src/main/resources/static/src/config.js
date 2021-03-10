/**

 @Name：全局配置
 @License：LPPL

 */

layui.define(['laytpl', 'layer', 'element', 'util'], function (exports) {
    exports('setter', {
        container: 'LAY_app' //容器ID
        , base: layui.cache.base //记录layuiAdmin文件夹所在路径
        , views: layui.cache.base + 'views/' //视图所在目录
        , entry: 'index' //默认视图文件名
        , engine: '.html' //视图文件后缀名
        , pageTabs: false //是否开启页面选项卡功能。单页版不推荐开启

        , name: 'BIU-NET'
        , tableName: 'layuiAdmin' //本地存储表名
        , MOD_NAME: 'admin' //模块事件名

        , debug: true //是否开启调试模式。如开启，接口异常时会抛出异常 URL 等信息

        , interceptor: false //是否开启未登入拦截

        //自定义请求字段
        , request: {
            tokenName: 'token' //自动携带 token 的字段名。可设置 false 不携带。
        }

        //自定义响应字段
        , response: {
            statusName: 'code' //数据状态的字段名称
            , statusCode: {
                ok: 0 //数据状态一切正常的状态码
                , logout: 400 //登录状态失效的状态码
            }
            , msgName: 'msg' //状态信息的字段名称
            , dataName: 'data'
        }

        //独立页面路由，可随意添加（无需写参数）
        , indPage: [
            '/user/login' //登入页
        ]

        //扩展的第三方模块
        , extend: [
            'echarts', //echarts 核心包
            'echartsTheme' //echarts 主题
        ]

        //主题配置
        , theme: {
            //内置主题配色方案
            color: [{
                main: '#20222A' //主题色
                , selected: '#009688' //选中色
                , alias: 'default' //默认别名
            }, {
                main: '#03152A'
                , selected: '#3B91FF'
                , alias: 'dark-blue' //藏蓝
            }, {
                main: '#2E241B'
                , selected: '#A48566'
                , alias: 'coffee' //咖啡
            }, {
                main: '#50314F'
                , selected: '#7A4D7B'
                , alias: 'purple-red' //紫红
            }, {
                main: '#344058'
                , logo: '#1E9FFF'
                , selected: '#1E9FFF'
                , alias: 'ocean' //海洋
            }, {
                main: '#3A3D49'
                , logo: '#2F9688'
                , selected: '#5FB878'
                , alias: 'green' //墨绿
            }, {
                main: '#20222A'
                , logo: '#F78400'
                , selected: '#F78400'
                , alias: 'red' //橙色
            }, {
                main: '#28333E'
                , logo: '#AA3130'
                , selected: '#AA3130'
                , alias: 'fashion-red' //时尚红
            }, {
                main: '#24262F'
                , logo: '#3A3D49'
                , selected: '#009688'
                , alias: 'classic-black' //经典黑
            }, {
                logo: '#226A62'
                , header: '#2F9688'
                , alias: 'green-header' //墨绿头
            }, {
                main: '#344058'
                , logo: '#0085E8'
                , selected: '#1E9FFF'
                , header: '#1E9FFF'
                , alias: 'ocean-header' //海洋头
            }, {
                header: '#393D49'
                , alias: 'classic-black-header' //经典黑
            }, {
                main: '#50314F'
                , logo: '#50314F'
                , selected: '#7A4D7B'
                , header: '#50314F'
                , alias: 'purple-red-header' //紫红头
            }, {
                main: '#28333E'
                , logo: '#28333E'
                , selected: '#AA3130'
                , header: '#AA3130'
                , alias: 'fashion-red-header' //时尚红头
            }, {
                main: '#28333E'
                , logo: '#009688'
                , selected: '#009688'
                , header: '#009688'
                , alias: 'green-header' //墨绿头
            }]

            //初始的颜色索引，对应上面的配色方案数组索引
            //如果本地已经有主题色记录，则以本地记录为优先，除非请求本地数据（localStorage）
            , initColorIndex: 0
        }
    });
});

layui.use(['table', 'admin'], function () {
    var setter = layui.setter, admin = layui.admin, table = layui.table, $ = layui.jquery;
    var headers = {};
    headers[setter.request.tokenName] = layui.data(setter.tableName)[layui.setter.request.tokenName];
    console.log(headers)
    console.log(layui.data(setter.tableName)[layui.setter.request.tokenName])
    table.set({
        request: {
            pageName: 'pageNo', //页码的参数名称，默认：page
            limitName: 'pageSize' //每页数据量的参数名，默认：limit
        },
        response: {
            statusName: setter.response.statusName //规定数据状态的字段名称，默认：code
            , statusCode: setter.response.statusCode.ok //规定成功的状态码，默认：0
            , msgName: setter.response.msgName //规定状态信息的字段名称，默认：msg
            , dataName: setter.response.dataName //规定数据列表的字段名称，默认：data
            , countName: 'count' //规定数据总数的字段名称，默认：count.这个根据自己项目设置
        },
        parseData: function (res) {
            return {
                "code": res.code,
                "msg": res.msg,
                "count": res.data.count,
                "data": res.data.list
            };
        },
        where: headers//table在地址中同时传递token
    });
    //同时更新储存的token和table的token
    var refreshToken = function (token) {
        console.log("header刷新"+token)
        //请求成功后，写入 token
        layui.data(setter.tableName, {
            key: setter.request.tokenName
            , value: token
        });
        headers[setter.request.tokenName] = token;
        table.set({
            where: headers//table在地址中同时传递token
        });
    };
    $.ajaxSetup({
        headers: headers,
        error: function () {
            layui.admin.popup({
                title: '提示'
                , area: ['250px']
                , content: '系统出错,请稍后再试'
                , btn: ['确定']
                , yes: function (index) {
                    layer.close(index);
                }
            });
        },
        complete: function (xhr) {

            //从header取token
            var token = xhr.getResponseHeader(setter.request.tokenName);
            //token不为空,则保存
            if (token != null && token.trim().length !== 0) {
                refreshToken(token);
            }
            //返回有json格式数据，则从中取需要的参数
            if (xhr.hasOwnProperty('responseJSON')) {
                if (token == null || token.trim().length === 0) {
                    //请求成功后，写入 access_token
                    if (xhr.responseJSON.hasOwnProperty(setter.response.dataName)) {
                        if (xhr.responseJSON[setter.response.dataName].hasOwnProperty(setter.request.tokenName)) {
                            token = xhr.responseJSON[setter.response.dataName][setter.request.tokenName];
                            if (token != null && token.trim().length !== 0) {
                                refreshToken(token);
                            }
                        }
                    }
                }
                if (xhr.responseJSON[setter.response.statusName] === setter.response.statusCode.logout) {
                    layui.admin.popup({
                        title: '提示'
                        , area: ['250px']
                        , content: xhr.responseJSON[setter.response.msgName] || '登录超时'
                        , btn: ['确定']
                        , yes: function (index) {
                            layer.close(index);
                        }
                        , end: function () {
                            admin.exit();
                        }
                    });
                }
            }
        }
    });
});