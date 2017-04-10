package models.operator;

class AddOperator implements Operator {

	@Override
	public Double apply(final Double operand1, final Double operand2) {
		return operand1 + operand2;
	}
}