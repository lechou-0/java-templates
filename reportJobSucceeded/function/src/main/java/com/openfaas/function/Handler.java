package com.openfaas.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfaas.model.IRequest;
import com.openfaas.model.IResponse;
import com.openfaas.model.Response;
import java.util.HashMap;
import java.util.Map;

public class Handler extends com.openfaas.model.AbstractHandler {

    private final ObjectMapper mapper = new ObjectMapper();


    public IResponse Handle(IRequest req) {
        String jobuid = "";
        try {
            Map<String, Object> mapFromStr = mapper.readValue(req.getBody(),
                    new TypeReference<Map<String, Object>>() {});

            if(null!=mapFromStr && mapFromStr.containsKey("jobuid")) {
                jobuid = String.valueOf(mapFromStr.get("jobuid"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String success = "";
        if(!jobuid.equals("")){
            success = "job" + jobuid + " success";
        }

        // 响应内容也是JSON格式，所以先存入map，然后再序列化
        Map<String, Object> rlt = new HashMap<>();
        rlt.put("success", success);

        String rltStr = null;

        try {
            rltStr = mapper.writeValueAsString(rlt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response res = new Response();
        res.setContentType("application/json;charset=utf-8");
        res.setBody(rltStr);

        return res;
    }
}
