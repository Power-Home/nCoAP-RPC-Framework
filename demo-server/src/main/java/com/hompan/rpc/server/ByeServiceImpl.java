package com.hompan.rpc.server;


import com.hompan.rpc.main.annotation.Service;
import com.hompan.rpc.services.ByeService;
import com.hompan.rpc.services.Info;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String sayBye(Info clientInfo) {
        return clientInfo.getMsg()+ " " +clientInfo.getName();
    }
}
