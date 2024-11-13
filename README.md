# wx-bot-java 微信刷题娱乐机器人

[![Powered by Wechaty](https://img.shields.io/badge/Powered%20By-Wechaty-brightgreen.svg)](https://github.com/Wechaty/wechaty)

github：<https://github.com/Turbolisten/wx-bot-java>

## 视频展示

B站：https://www.bilibili.com/video/BV1X64y1W73R/

## 项目背景

> 2021年7月初，在Github上闲逛时，无意间发现了wechaty机器人的开源项目。一番浏览， 发现竟然还支持微信的各种协议，顿时产生了兴趣。众所周知，微信功能花里胡哨又简陋至 极，虽然微信已经成了日常工作生活中离不开的沟通工具，但微信群缺少各种管理、娱乐功能。我开始构想能够像QQ一样写一个机器人，群里推送一些工作开发提醒事件，以及签到，闲聊等娱乐功能。


## 项目功能

- 对话闲聊：闲聊、闹钟提醒，彩虹屁、土味情话(沙雕网站API<https://chp.shadiao.app>)

- 刷题功能：选择题、判断题


## 项目主要技术

[![Powered by Wechaty](https://img.shields.io/badge/java--wechaty-0.1.5-brightgreen)](https://github.com/Wechaty/wechaty)

[![](https://img.shields.io/badge/java-1.8-brightgreen)](https://www.xiaomifeng.vip)

[![spring-boot](https://img.shields.io/badge/spring--boot-2.5.2-brightgreen)](https://www.xiaomifeng.vip)


## 项目运行

- 替换配置文件 application.properties 中wechaty token 以及 服务地址
- 执行数据库脚本 resources/sql 下的 wx_bot.sql
- 启动 WxBotApplication 


## 项目短板

目前对话闲聊是对接的微信机器人API，功能比较单薄。

刷题是导入财务方面的word题目，经过程序硬编码处理后，保存到mysql数据库中，程序基于已有题目出题等。

虽然已有了基础的实用性，但和AI基本没有什么关系，所以就想到了自然语义处理NLP，了解了一下PaddleNLP飞桨AI平台，尝试能否赋予机器人一点真正智能聪明的感觉。

由于本人是个JAVA开发者，能力有限，NLP这块需要使用Python开发，所以目前只能在工作的业余时间学习Python，以及如何使用Paddle平台。


## 功能展示
![闲聊.png](https://i.loli.net/2021/07/29/W4Ht1dEFk7LTXZu.png)
![闹钟.png](https://i.loli.net/2021/07/29/WHTiwOItaC45GxJ.png)
![刷题1.png](https://i.loli.net/2021/07/29/Sxv68aYhDdVZKBM.png)
![刷题2.png](https://i.loli.net/2021/07/29/uTf29ctX8gnDwFO.png)
![刷题3.png](https://i.loli.net/2021/07/29/ywlcdVPFRtoCihs.png)
![刷题4.png](https://i.loli.net/2021/07/29/7BbGEmMINdxL25h.png)
