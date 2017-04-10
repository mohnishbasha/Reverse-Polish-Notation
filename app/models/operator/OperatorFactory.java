package models.operator;

/**
 * Factory Method to manage operators objects and to invoke appropriate operations.
 */

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OperatorFactory {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(OperatorFactory.class);

	/** The Constant VALID_OPERATORS. */
	private static final String VALID_OPERATORS = "+a-*x/ _";

	private static Map<String, String> supportedToActualMap = new HashMap<>();

	static {
		supportedToActualMap.put(" ", "+");
		supportedToActualMap.put("a", "+");
		supportedToActualMap.put("x", "*");
	}

	/**
	 * Gets the operator based on operator parameter.
	 *
	 * @param op the operator
	 * @return the operator
	 */
	public static Operator getOperator(final String op) {
		if (StringUtils.equalsIgnoreCase(op, "+") || StringUtils.equalsIgnoreCase(op, "a") || StringUtils.equalsIgnoreCase(op, " ")) {
			return new AddOperator();
		} else if (StringUtils.equalsIgnoreCase(op, "-")) {
			return new SubtractOperator();
		} else if (StringUtils.equalsIgnoreCase(op, "*") || StringUtils.equalsIgnoreCase(op, "x")) {
			return new MultiplyOperator();
		} else if (StringUtils.equalsIgnoreCase(op, "/")) {
			return new DivisonOperator();
		} else {
			LOG.error("Unsupported operator {}", op);
			throw new IllegalArgumentException(op + " is not supported operator.");
		}
	}

	/**
	 * Checks if supported operator.
	 *
	 * @param op the op
	 * @return true, if is supported operator
	 */
	public static boolean isSupportedOperator(final String op) {
		return StringUtils.containsIgnoreCase(VALID_OPERATORS, op);
	}

	/**
	 * Gets the mathematical operator if given operator is supported.
	 *
	 * @param op the operator
	 * @return the actual operator
	 */
	public static String getMathemeticalOperator(final String op) {
		final String actual = supportedToActualMap.get(op);
		return actual == null ? op : actual;
	}
}
