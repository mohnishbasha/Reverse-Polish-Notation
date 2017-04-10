package models.operator;

class DivisonOperator implements Operator {

	@Override
	public Double apply(final Double operand1, final Double operand2) {
		return operand2 / operand1;
	}

}
