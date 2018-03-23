# Run-With-You

[![Build Status](https://travis-ci.org/shensky711/Run-With-You.svg?branch=master)](https://travis-ci.org/shensky711/Run-With-You)

----------

# 描述
一款支持双人跑步的应用

# 应用安装
[豌豆荚下载地址](http://www.wandoujia.com/apps/site.hanschen.runwithyou)

# 功能需求

- [ ] 用户中心
    - [ ] 登录
    - [ ] 注册
    - [ ] 个人信息设置
- [ ] 记步功能
    - [x] 设置运动目标
    - [x] 当前步数
    - [ ] 运动统计，按天/周/月展示历史运动数据
    - [ ] 健康提示：根据个人信息、运动规律、当前时间天气等因素给出运动建议
- [ ] 双人同跑
    - [x] 双方设备连接
    - [ ] 断线重连
    - [ ] 同时展示双方的运动数据（步数、位置）
    - [ ] 运动暂停/开始
    - [ ] 结束后生成双人运动记录
    - [ ] 记录分享，如朋友圈
- [ ] 发现与记录
    - [ ] 运动信息、轨迹记录
    - [ ] 运动过程中可插入照片、小视频，插入后在运动轨迹中展示
    - [ ] 记录分享
- [ ] 设置
    - [ ] 版本更新
    - [ ] 帮助与反馈
    - [ ] 后台常驻：通知栏&白名单

# 软件架构
MVP + Dagger2 + Retrofit + RxJava + Protobuf

# LICENSE
Apache License 2.0