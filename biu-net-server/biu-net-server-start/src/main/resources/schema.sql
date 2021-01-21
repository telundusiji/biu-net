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
    private_key VARCHAR(1024)    COMMENT '私钥' ,
    public_key VARCHAR(1024)    COMMENT '公钥' ,
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

INSERT INTO biu_net.client (id, name, password, private_key, public_key) VALUES ('461875bb-e5da-4a10-8d19-68613e18a2db', 'pc-xxx', '4QrcOUm6Wau+VuBX8g+IPg==', 'MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANY2Z9JqyTeEQYts/nvU+IR5yWZgAD/h5BZ8IYi1Eq2JiZC5VGdaRzo0TWfEmSGReM4fBU12mF1TeiSRsYwFkSDms00p6PqXBULrqqbqNTXEae751tMd7uKJ7IggrFAqL1DCZYuu73NtYApVyL0tzdKDtGDo7hMtkek9GUXU2yX1AgMBAAECgYBjaeZvCe4OXMvKeC/2qlUXrimg4GbdPumK4dryRg7ACPeV+dzJBmXOBl3yDZR/trKY/l6In8SAc3BZLmB2ulKqV2rtMbOQyTR4mSHi3qsNFj7mJtno007rr4ouFdAWbMMspjBmbM7LZA9I+wcMsgHDOTbi8rTDP+rlZv566Y9AdQJBAPqWBYCvRWlIxLHsDKdDQQytf+vGnnzCi6bEFudkOFORqJ4JgYdCuSOoZnpd2LbHYriJwx3p7slIkTnOKiSYG6cCQQDa1zQ/EtFLBDthw0hI3w268WozUwwB/rP5cErGgd+FqlzkUsaiAzOEjeAEujlbeQflRlccgKuplOQlkWgqBPUDAkAbxUqA02vO2jiqOy2/z61C2WuG1rEzxF+zsKGVLT8sy8SzFvya3+Mit4P75bChuQEQ0HumMgm7luY5UMbM3WqbAkAljyn4aVJifadzcADIAQBqRWTkyiwVa5GYDh3HVCL43fRze4F77PC6n18DcLLHcE5am2f4DF1qDZPansox7AfhAkBVVDD9T/EDXD3P5lfTGRbPbj5Hzva6/pQIqJIYlzLxlScEaDauGZVWZCCvu7bL7JINlPZN8Krdhbk7jFrQRGMd', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWNmfSask3hEGLbP571PiEeclmYAA/4eQWfCGItRKtiYmQuVRnWkc6NE1nxJkhkXjOHwVNdphdU3okkbGMBZEg5rNNKej6lwVC66qm6jU1xGnu+dbTHe7iieyIIKxQKi9QwmWLru9zbWAKVci9Lc3Sg7Rg6O4TLZHpPRlF1Nsl9QIDAQAB');
INSERT INTO biu_net.proxy (id, client_id, proxy_server_id, target_host, target_port, enable) VALUES ('c5ff2170-3e97-46f5-aa87-3156db3a95c3', '461875bb-e5da-4a10-8d19-68613e18a2db', 'f105a731-80c4-47eb-99c7-72878e30dcdc', 'te-amo.site', 9010, 1);
INSERT INTO biu_net.proxy (id, client_id, proxy_server_id, target_host, target_port, enable) VALUES ('c5ff2170-3e97-46f5-aa87-3156db3a95c4', '461875bb-e5da-4a10-8d19-68613e18a2db', 'f105a731-80c4-47eb-99c7-72878e30dcdd', 'te-amo.site', 80, 1);
INSERT INTO biu_net.proxy_server (id, port) VALUES ('f105a731-80c4-47eb-99c7-72878e30dcdc', 8090);
INSERT INTO biu_net.proxy_server (id, port) VALUES ('f105a731-80c4-47eb-99c7-72878e30dcdd', 8091);