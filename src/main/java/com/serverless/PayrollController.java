package com.serverless;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.serverless.models.Payroll;

public class PayrollController implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private static final String PAY_RATE_TEXT = "payRate";
	private static final String WORKING_HOURS_TEXT = "workingHours";

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		Response responseBody;
		Map<String, Object> queryMap = (Map<String, Object>)input.get("queryStringParameters");
		if (!isValid(queryMap)) responseBody = new Response("Wrong inputs. The payRate should be a Double type and workingHours should be an Integer type.", null);
		else {
      Payroll myPayroll = new Payroll(Integer.parseInt((String)queryMap.get(WORKING_HOURS_TEXT)), Double.parseDouble((String)queryMap.get(PAY_RATE_TEXT)));
      double grossPay = myPayroll.calculateGrossPay();
      responseBody = new Response(Double.toString(grossPay) , null);
		}
		
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("X-Powered-By", "AWS Lambda & serverless");
		headersMap.put("Access-Control-Allow-Origin", "*");
		headersMap.put("Access-Control-Allow-Credentials", "true");

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(headersMap)
				.build();
	}

	private boolean isValid(Map<String, Object> input) {
		if (input == null || !input.containsKey(PAY_RATE_TEXT) || !input.containsKey(WORKING_HOURS_TEXT)) return false;
		try {
			double payRate = Double.parseDouble((String)input.get(PAY_RATE_TEXT));
			int workingHours = Integer.parseInt((String)input.get(WORKING_HOURS_TEXT));
			if (payRate <= 0 || workingHours <= 0) return false;
		} catch(NumberFormatException error) {
			LOG.info(error);
			return false;
		}

		return true;
	}
}
