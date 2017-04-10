package controllers.util;

import java.util.List;
import java.util.Stack;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.entities.Calculation;
import models.operator.OperatorFactory;
import play.mvc.Http;

/**
 * Utility Class to calculates Reverse Polish notation.
 */
public final class RPNCalculator {

	/** The Constant LOG. */
	private final static Logger LOG = LoggerFactory.getLogger(RPNCalculator.class);

	private final List<String> data;

	private final Stack<String> stack = new Stack<>();

	public RPNCalculator(final List<String> inputData) {
		this.data = inputData;
	}

	/**
	 * RPN Function to implment RPN Algorithm
	 * https://en.wikipedia.org/wiki/Reverse_Polish_notation
	 *
	 * @return the double
	 */
	public Double calculate() {
		for (final String token : this.data) {
			if (OperatorFactory.isSupportedOperator(token) && !token.equals("_")) {
				if (this.stack.size() < 2) {
					LOG.error("Operator found but no operands avalable.");
					throw new IllegalArgumentException("An operator found before there were enough numericals to calculate.");
				}
				final String op1 = replacePreviousResult(this.stack.pop());
				final String op2 = replacePreviousResult(this.stack.pop());
				final Double calcValue = OperatorFactory.getOperator(token).apply(Double.valueOf(op1), Double.valueOf(op2));
				this.stack.push(String.valueOf(calcValue));
			} else {
				this.stack.push(token);
			}

		}
		if (this.stack.size() != 1) {
			LOG.debug("Stack after processing {}", this.stack);
			throw new IllegalArgumentException("There are too many numbers and not enough operators to calculate them.");
		}
		return Double.valueOf(this.stack.pop());
	}

	private String replacePreviousResult(final String operator) {
		if (operator.equals("_")) {
			final String token = Http.Context.current().session().get("st");
			if (!Strings.isNullOrEmpty(token)) {
				LOG.info("Finding result for token {}", token);
				final List<Calculation> list = Calculation.find.where().eq("sessionToken", token).orderBy("id desc").findList();
				if (!list.isEmpty()) {
					final Calculation calculated = list.get(0);
					if (calculated != null) {
						return String.valueOf(calculated.result);
					}
				}
			}
			return "0";
		}
		return operator;
	}
}
