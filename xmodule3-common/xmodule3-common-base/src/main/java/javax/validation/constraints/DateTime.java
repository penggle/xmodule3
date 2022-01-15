package javax.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 约束日期时间有效性的自定义校验注解
 *
 * @author pengpeng
 * @version 1.0
 * @since 2020/6/14 10:49
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy= DateTimeConstraintValidator.class)
public @interface DateTime {

	/**
	 * @return 实际的日期时间类型
	 */
	Type type() default Type.DATETIME;
	
	/**
	 * @return 日期时间的格式
	 */
	String pattern() default "yyyy-MM-dd HH:mm:ss";

	/**
	 * @return the error message template
	 */
	String message() default "{javax.validation.constraints.DateTimePattern.message}";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default { };

	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default { };
	
	enum Type {
		
		DATE(LocalDate.class), TIME(LocalTime.class), DATETIME(LocalDateTime.class);
		
		private final Class<? extends TemporalAccessor> type;

		Type(Class<? extends TemporalAccessor> type) {
			this.type = type;
		}

		public Class<? extends TemporalAccessor> getType() {
			return type;
		}

	}
	
}
