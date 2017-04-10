package models.operator;

class SubtractOperator implements Operator {

	@Override
	public Double apply(final Double operand1, final Double operand2) {
		return operand2 - operand1;
	}

}
