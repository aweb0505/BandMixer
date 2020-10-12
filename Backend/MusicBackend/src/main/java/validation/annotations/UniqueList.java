package validation.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.*;

import validation.validators.UniqueListValidator;

@Constraint(validatedBy = UniqueListValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface UniqueList 
{
	String message() default "The list of tags contains duplicates.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
