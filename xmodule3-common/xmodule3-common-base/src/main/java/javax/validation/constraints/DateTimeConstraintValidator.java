package javax.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @DateTimePattern自定义注解校验器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2020/6/14 10:49
 */
public class DateTimeConstraintValidator implements ConstraintValidator<DateTime, String> {

	private DateTime constraint;
	
	@Override
	public void initialize(DateTime constraint) {
		this.constraint = constraint;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value != null) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(constraint.pattern());
				if(DateTime.Type.DATE.equals(constraint.type())) {
					LocalDate date = LocalDate.parse(value, formatter);
					return date != null;
				} else if (DateTime.Type.TIME.equals(constraint.type())) {
					LocalTime time = LocalTime.parse(value, formatter);
					return time != null;
				} else {
					LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
					return dateTime != null;
				}
			} catch (Exception e) {}
			return false;
		}
		return true;
	}
	
}
