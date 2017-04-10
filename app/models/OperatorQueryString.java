package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.operator.OperatorFactory;
import play.mvc.QueryStringBindable;

/**
 * The OperatorQueryString which will bind the operators from request query.
 */
public class OperatorQueryString implements QueryStringBindable<OperatorQueryString> {

	/** The Constant LOG. */
	private final static Logger LOG = LoggerFactory.getLogger(OperatorQueryString.class);

	private static final String OPERATOR_KEY = "op";

	/** The query data. */
	private final List<String> queryData = new ArrayList<>();

	/** The is query valid. */
	private boolean isQueryValid = true;

	/** The invalid query data. */
	private String invalidQueryDataMsg;

	@Override
	public Optional<OperatorQueryString> bind(final String routeKey, final Map<String, String[]> data) {
		for (final Entry<String, String[]> entry : data.entrySet()) {
			final String key = entry.getKey();
			if (StringUtils.equalsIgnoreCase(key, OPERATOR_KEY)) {
				for (final String value : entry.getValue()) {
					LOG.debug("Received value {}", value);
					if (isValidNumberOrOperator(value)) {
						this.queryData.add(getActualOperatorValue(value));
					} else {
						this.isQueryValid = false;
						this.invalidQueryDataMsg = "Received query parameter's value [" + value + "] is invalid.";
						LOG.info("Received query parameter's value {} is invalid", value);
						break;
					}
				}
			} else {
				this.isQueryValid = false;
				this.invalidQueryDataMsg = "Received query parameter's key [" + key + "] is unexpected.";
				LOG.info("Received query parameter's key {} is unexpected", key);
				break;
			}
			if (!this.isQueryValid) {
				break;
			}
		}
		return Optional.of(this);
	}

	/**
	 * Gets the actual operator or last value.
	 *
	 * @param operator the operator
	 * @return the actual value
	 */
	private String getActualOperatorValue(final String operator) {
		return OperatorFactory.getMathemeticalOperator(operator);
	}

	/**
	 * Checks if value is valid number or operator.
	 *
	 * @param value the value
	 * @return true, if valid number or operator
	 */
	private boolean isValidNumberOrOperator(final String value) {
		boolean isValid = false;
		try {
			Long.parseLong(value);
			isValid = true;
		} catch (final NumberFormatException e) {
			if (OperatorFactory.isSupportedOperator(value)) {
				isValid = true;
			}
		}
		LOG.debug("The value {} is valid operator or number." + value);
		return isValid;
	}

	@Override
	public String javascriptUnbind() {
		return null;
	}

	@Override
	public String unbind(final String key) {
		return "op=" + key;
	}

	public boolean isQueryValid() {
		return this.isQueryValid;
	}

	public String getInvalidQueryDataMessage() {
		return this.invalidQueryDataMsg;
	}

	public List<String> getQueryData() {
		return this.queryData;
	}

	/**
	 * Gets the mathematical format.
	 *
	 * @return the mathematical format
	 */
	public String getMathematicalFormat() {
		final StringBuilder sb = new StringBuilder();
		this.queryData.forEach(each -> sb.append(each + " "));
		return sb.toString().trim();
	}

}
