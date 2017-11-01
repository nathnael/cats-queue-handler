package com.cats.queue.handler.rabbitmq;

import okhttp3.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.boot.json.JsonParser;

import static org.springframework.http.HttpHeaders.USER_AGENT;

public class MessageReceiver {
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public void receiveMsg(byte[] message) throws IOException, ParseException {
		System.out.println("Message Received: " + message);
		String postJsonData = new String(message);
		String replaceString=postJsonData.replaceAll("null","");
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
					"http://localhost:8080/operation");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(postJsonData);
//			StringEntity input = new StringEntity(replaceString);
			StringEntity input = new StringEntity(json.toString());
//
			input.setContentType("application/json");
			postRequest.setEntity(input);
			postRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(
					new InputStreamReader((response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			httpClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}

		countDownLatch.countDown();
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
}
 