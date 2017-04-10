package models.operator;

public interface Operator {

	/**
	 * Apply the operator on the operands.
	 *
	 * @param operand1 the operand 1
	 * @param operand2 the operand 2
	 * @return the double value
	 */
	Double apply(Double operand1, Double operand2);
}
