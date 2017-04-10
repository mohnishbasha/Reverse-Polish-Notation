/*
 * Entity class to hold the calculations in-memory to render the charts in dashboard.
 */
package models.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

import play.data.format.Formats.DateTime;

@Entity
public class Calculation extends Model {

	@Id
	private long id;

	/** The input rpn format equation. */
	@JsonProperty("equation")
	public String inputEquation;

	/** The result. */
	public Double result;

	/** The created date. */
	@JsonProperty("created_date")
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@DateTime(pattern = "dd-MM-yyyy hh:mm:ss")
	public Timestamp createdDate;

	/** The session token. */
	public String sessionToken;

	/** The status, failed or passed. */
	public String status;

	public static Finder<Long, Calculation> find = new Finder<>(Calculation.class);

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Calculation [id=");
		builder.append(this.id);
		builder.append(", inputEquation=");
		builder.append(this.inputEquation);
		builder.append(", result=");
		builder.append(this.result);
		builder.append("]");
		return builder.toString();
	}

}