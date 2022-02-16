package com.kindminds.drs.core.biz.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class BizProcClient{

    private static String camundaUrl = "http://ec2-18-179-112-14.ap-northeast-1.compute.amazonaws.com:8080/engine-rest";


   public static String startIsnstance(String processKey , String processVars ) {

       CloseableHttpClient httpClient = HttpClients.createDefault();
       HttpPost httpPost =
               new HttpPost(camundaUrl + "/process-definition/key/"+ processKey +"/start");


        /*
        val json = "{\"variables\":\n" +
                "    {\"creditor\" : {\"value\" : \"1\", \"type\": \"string\"},\n" +
                "     \"amount\" : {\"value\" : 1200, \"type\": \"integer\"},\n" +
                "     \"invoiceCategory\" : {\"value\" : \"Misc\", \"type\": \"string\"},\n" +
                "     \"testProcVar\" : {\"value\" : \"Misc\", \"type\": \"string\"},\n" +
                "     \"invoiceNumber\" : {\"value\" : \"1200\", \"type\": \"string\"} " +
                "}" +
                //",\n" +
                //" \"businessKey\" : \"invoice\"\n" +
                "}"*/
       ObjectMapper mapper = new  ObjectMapper();
       StringEntity entity = null;
       String procInstanceId ="";
       try {
           entity = new StringEntity(processVars);

           httpPost.setEntity(entity);
           httpPost.setHeader("Accept", "application/json");
           httpPost.setHeader("Content-type", "application/json");

           HttpResponse response = httpClient.execute(httpPost);
           String r = EntityUtils.toString(response.getEntity());

           JsonNode jn = mapper.readValue(r,JsonNode.class);
           procInstanceId = jn.get("id").asText();
           //println(jn)
           //println(procInstanceId)


           // assertThat(response.getStatusLine().getStatusCode(), equalTo(200))
           httpClient.close();
       } catch (Exception e) {
           e.printStackTrace();
       }


        return procInstanceId;

    }


    public static void completeTask(String taskId , String processVars ){


        CloseableHttpClient httpClient = HttpClients.createDefault();
        //user_task_review_tweet:5e1e1f46-1bbe-11ea-8130-06d9e270d0d8
        //  http@ //localhost:8080/engine-rest/task/95af1b22-3a7a-11e5-85b6-dafa20524153/complete
        HttpPost httpPost = new HttpPost(camundaUrl + "/task/"+ taskId + "/complete");


        /*
        val json2 = "{\"variables\":\n" +
                "    {\"approved\" : {\"value\" : \"false\", \"type\": \"boolean\"},\n" +
                "     \"amount\" : {\"value\" : 1200, \"type\": \"integer\"},\n" +
                "     \"invoiceCategory\" : {\"value\" : \"Misc\", \"type\": \"string\"},\n" +
                "     \"testProcVar\" : {\"value\" : \"Misc\", \"type\": \"string\"},\n" +
                "     \"invoiceNumber\" : {\"value\" : \"1200\", \"type\": \"string\"} " +
                "}" +
                //",\n" +
                //" \"businessKey\" : \"invoice\"\n" +
                "}"
                */

        StringEntity entity = null;
        try {
            entity = new StringEntity(processVars);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);
            // val r = EntityUtils.toString(response.entity)


            // assertThat(response.getStatusLine().getStatusCode(), equalTo(200))
            httpClient.close();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public static String getTaskId(String processInstanceId) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(camundaUrl + "/task?processInstanceId="+processInstanceId );

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);

            String r = EntityUtils.toString(response.getEntity());

            httpClient.close();
            ObjectMapper mapper = new  ObjectMapper();
            JsonNode jn = mapper.readValue(r,JsonNode.class);

            return jn.get(0).get("id").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  "";

    }




}