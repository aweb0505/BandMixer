package validation.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.*;

import validation.validators.ExistentOwnerValidator;

@Constraint(validatedBy = ExistentOwnerValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface ExistentOwner 
{
	String message() default "The owner does not exist.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
