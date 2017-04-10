package controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.util.RPNCalculator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import models.OperatorQueryString;
import models.entities.Calculation;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Main controller class contains an action to handle HTTP requests.
 */
@Api(tags = "Reverse Polish Notation")
public class AppController extends Controller {

	/** The Constant LOG. */
	private final static Logger LOG = LoggerFactory.getLogger(AppController.class);

	/** The Constant SESSION_TOKEN. */
	private static final String SESSION_TOKEN = "st";

	/** The Constant STATUS_PASS. */
	private static final String STATUS_PASS = "pass";

	/** The Constant STATUS_FAIL. */
	private static final String STATUS_FAIL = "fail";

	/** The ws client. */
	@Inject
	private WSClient wsClient;


	/**
	 * An action that renders an HTML page with a welcome message. The configuration in the <code>routes</code> file
     * means that this method will be called when the application
	 * receives a <code>GET</code> request with a path of <code>/</code>.
	 *
	 * @return the result
	 */
	@ApiOperation(hidden = true, value = "Running status.")
	public Result index() {
		setAndGetCookie();
		return ok("Application is running.");
	}


	/**
	 * Dashboard view.
     * binds to /dashboard route in routes.conf
	 *
	 * @return the result
	 */
	@ApiOperation(hidden = true, value = "Dashboard view.")
	public Result dashboardView() {
		setAndGetCookie();
		return ok(views.html.dashboard.render());
	}


	/**
	 * Gets the all records.
     * This is used in the dashboard to retrive data to render the charts.
	 *
	 * @return the all records
	 */
	@ApiOperation(value = "Get list of all the equations executed.", produces = "application/json")
	@ApiResponses({ @ApiResponse(code = OK, message = "") })
	public Result getAllRecords() {
		final List<Calculation> allRecords = Calculation.find.all();e
		return ok(Json.toJson(allRecords));
	}

	// Note: Two different methods functions to satisfy swagger.

	/**
	 * Calculate RPN.
	 *
	 * @param operators the operators
	 * @return the result
	 */
	@ApiOperation(value = "Calculate reverse polish notation.")
	@ApiResponses({ @ApiResponse(code = OK, message = "Success.", response = Double.class), //
			@ApiResponse(code = BAD_REQUEST, message = "Invalid request.") })
	public Result calculateRPN(@ApiParam(name = "op", allowMultiple = true) final OperatorQueryString operators) {
		return calculate(operators);
	}



	/**
	 * Calculate reverse polish notation.
	 *
	 * @param operators the operators
	 * @return the result
	 */
	@ApiOperation(value = "Calculate reverse polish notation.")
	@ApiResponses({ @ApiResponse(code = OK, message = "Success.", response = Double.class), //
			@ApiResponse(code = BAD_REQUEST, message = "Invalid request.") })
	public Result calculate(@ApiParam(name = "op", allowMultiple = true) final OperatorQueryString operators) {
		if (!operators.isQueryValid()) {
			LOG.info("Query string is not valid.");
			persistCalculation(operators.getMathematicalFormat(), null, STATUS_FAIL);
			return badRequest(operators.getInvalidQueryDataMessage());
		}
		Double result = 0.0;
		try {
			result = new RPNCalculator(operators.getQueryData()).calculate();
		} catch (final IllegalArgumentException e) {
			LOG.error("Exception: {}", e.getMessage());
			persistCalculation(operators.getMathematicalFormat(), null, STATUS_FAIL);
			return badRequest(e.getMessage());
		}
		persistCalculation(operators.getMathematicalFormat(), result, STATUS_PASS);
		return ok(String.valueOf(result));
	}

	/**
	 * Persist calculation.
	 *
	 * @param mathematicalFormat the mathematical format
	 * @param result the result
	 * @param status the status
	 */
	private void persistCalculation(final String mathematicalFormat, final Double result, final String status) {
		final String dbToken = setAndGetCookie();
		final Calculation calc = new Calculation();
		calc.inputEquation = mathematicalFormat;
		calc.result = result;
		calc.status = status;
		calc.createdDate = new Timestamp(System.currentTimeMillis());
		calc.sessionToken = dbToken;
		calc.save();
	}

	/**
	 * Execute test.
	 *
	 * @return the result
	 */
	@ApiOperation(value = "Execute test cases.", produces = "application/json")
	@ApiResponses({ @ApiResponse(code = OK, message = "Test results.") })
	public Result executeTest() {
		final String host = (request().secure() ? "https://" : "http://") + request().host() + "/rpn?";
		final List<String> sample = new ArrayList<>();
		sample.add("op=14&op=3&op=x&op=8&op=+");
		sample.add("op=7&op=3&op=x&op=8&op=+");
		sample.add("op=$$$");
		sample.add("op=7&op=3&op=x");
		sample.add("op=_&op=8&op=+");
		sample.add("op=5&op=1&op=2&op=+&op=4&op=*&op=+&op=3&op=-");
		sample.add("op=12&op=3&op=/");
		final Map<String, String> testResult = new HashMap<>();
		for (final String test : sample) {
			LOG.info("Executing test {}", (host + test));
			final CompletionStage<WSResponse> response = this.wsClient.url(host + test).execute();
			try {
				final String result = response.toCompletableFuture().get().getBody();
				testResult.put(test, result);
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Exception: ", e);
				testResult.put(test, null);
			}
		}
		return ok(Json.toJson(testResult));
	}

	/**
	 * Sets the session cookie if not available, and return the cookie value.
	 */
	private String setAndGetCookie() {
		String userToken = session().get(SESSION_TOKEN);
		if (Strings.isNullOrEmpty(userToken)) {
			userToken = RandomStringUtils.randomAlphanumeric(15);
			session().put(SESSION_TOKEN, userToken);
		}
		return userToken;
	}
}
