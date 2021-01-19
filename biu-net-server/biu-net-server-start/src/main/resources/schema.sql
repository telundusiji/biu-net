drop table client;
drop table proxy;
drop table proxy_server;

CREATE TABLE if not exists proxy_server(
    id VARCHAR(128) NOT NULL   COMMENT '代理服务id' ,
    port integer   COMMENT '代理服务监听端口' ,
    PRIMARY KEY (id)
    ) COMMENT = '代理服务器信息 代理服务器信息';;

CREATE TABLE if not exists client(
    id VARCHAR(128) NOT NULL   COMMENT '客户端id' ,
    name VARCHAR(128)    COMMENT '客户端名称' ,
    password VARCHAR(128)    COMMENT '密码' ,
    PRIMARY KEY (id)
    ) COMMENT = '客户端 客户端';

CREATE TABLE if not exists proxy(
    id VARCHAR(128) NOT NULL   COMMENT '代理Id 代理id' ,
    client_id VARCHAR(128) NOT NULL   COMMENT '客户端Id 客户端id' ,
    proxy_server_id VARCHAR(128) NOT NULL   COMMENT '代理服务端id 代理服务端id' ,
    target_host VARCHAR(128) NOT NULL  DEFAULT "localhost" COMMENT '目标主机 目标主机名' ,
    target_port integer NOT NULL  COMMENT '目标端口 目标端口' ,
    enable integer NOT NULL  DEFAULT 0 COMMENT '代理是否启用 0-未启用; 1-启用' ,
    PRIMARY KEY (id)
    ) COMMENT = '代理 ';

INSERT INTO biu_net.client (id, name, password) VALUES ('461875bb-e5da-4a10-8d19-68613e18a2db', 'pc-xxx', '123456');
INSERT INTO biu_net.proxy (id, client_id, proxy_server_id, target_host, target_port, enable) VALUES ('c5ff2170-3e97-46f5-aa87-3156db3a95c3', '461875bb-e5da-4a10-8d19-68613e18a2db', 'f105a731-80c4-47eb-99c7-72878e30dcdc', 'te-amo.site', 9010, 1);
INSERT INTO biu_net.proxy (id, client_id, proxy_server_id, target_host, target_port, enable) VALUES ('c5ff2170-3e97-46f5-aa87-3156db3a95c4', '461875bb-e5da-4a10-8d19-68613e18a2db', 'f105a731-80c4-47eb-99c7-72878e30dcdd', 'te-amo.site', 80, 1);
INSERT INTO biu_net.proxy_server (id, port) VALUES ('f105a731-80c4-47eb-99c7-72878e30dcdc', 9091);
INSERT INTO biu_net.proxy_server (id, port) VALUES ('f105a731-80c4-47eb-99c7-72878e30dcdd', 9092);