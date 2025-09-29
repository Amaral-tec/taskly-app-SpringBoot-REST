package com.amaral.taskly.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import com.amaral.taskly.dto.request.CalendarRequestDTO;

public class DateRangeValidator implements ConstraintValidator<DateRange, CalendarRequestDTO> {

    @Override
    public boolean isValid(CalendarRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        LocalDateTime start = dto.startDateTime();
        LocalDateTime end = dto.endDateTime();

        // If null, let field-level @NotNull handle it
        if (start == null || end == null) {
            return true;
        }

        boolean valid = !end.isBefore(start); // end >= start

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date/time must be after or equal to start date/time")
                   .addPropertyNode("endDateTime")
                   .addConstraintViolation();
        }

        return valid;
    }
}
