package com.sports.athleticax.services;

import com.sports.athleticax.dto.EligibilityResultDTO;
import com.sports.athleticax.entity.Athlete;
import com.sports.athleticax.entity.Event;
import com.sports.athleticax.entity.EligibilityStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationRulesEngineService
{
    public EligibilityResultDTO calculateEligibility(Athlete athlete, Event event)
    {
        EligibilityResultDTO result = new EligibilityResultDTO();
        List<String> rejectionReasons = new ArrayList<>();

        result.setAthleteId(athlete.getId());
        result.setWeight(athlete.getWeight());
        result.setHeight(athlete.getHeight());
        result.setGender(athlete.getGender());

        String birthDateStr = athlete.getBirthDate();
        int age = 0;

        // 1. Safe parsing to prevent NullPointerException and format crashes
        if (birthDateStr == null || birthDateStr.trim().isEmpty())
        {
            rejectionReasons.add("Birth date is missing, cannot verify age.");
        }
        else
        {
            try
            {
                LocalDate today = LocalDate.now();
                LocalDate birthDate = LocalDate.parse(birthDateStr);
                age = Period.between(birthDate, today).getYears();
                result.setAge(age);

                if (event.getMinAge() != null && age < event.getMinAge())
                {
                    rejectionReasons.add("Age " + age + " (Minimum allowed age is " + event.getMinAge() + ")");
                }

                if (event.getMaxAge() != null && age > event.getMaxAge())
                {
                    rejectionReasons.add("Age " + age + " (Maximum allowed age is " + event.getMaxAge() + ")");
                }
            }
            catch (DateTimeParseException e)
            {
                rejectionReasons.add("Invalid birth date format provided.");
            }
        }

        // 2. Remaining eligibility checks
        if (event.getGenderRestriction() != null &&
                !event.getGenderRestriction().isEmpty() &&
                !event.getGenderRestriction().equalsIgnoreCase("All") &&
                !event.getGenderRestriction().equalsIgnoreCase(athlete.getGender()))
        {
            rejectionReasons.add("Gender: " + event.getGenderRestriction() + " only");
        }

        if (event.getMinWeight() != null && athlete.getWeight() != null && athlete.getWeight() < event.getMinWeight())
        {
            rejectionReasons.add("Weight " + athlete.getWeight() + "kg (Minimum allowed weight is " + event.getMinWeight() + "kg)");
        }

        if (event.getMaxWeight() != null && athlete.getWeight() != null && athlete.getWeight() > event.getMaxWeight())
        {
            rejectionReasons.add("Weight " + athlete.getWeight() + "kg (Maximum allowed weight is " + event.getMaxWeight() + "kg)");
        }

        if (event.getMinHeight() != null && athlete.getHeight() != null && athlete.getHeight() < event.getMinHeight())
        {
            rejectionReasons.add("Height " + athlete.getHeight() + "cm (Minimum allowed height is " + event.getMinHeight() + "cm)");
        }

        if (event.getMaxHeight() != null && athlete.getHeight() != null && athlete.getHeight() > event.getMaxHeight())
        {
            rejectionReasons.add("Height " + athlete.getHeight() + "cm (Maximum allowed height is " + event.getMaxHeight() + "cm)");
        }

        // 3. Final Evaluation
        if (!rejectionReasons.isEmpty())
        {
            result.setEligibilityStatus(EligibilityStatus.INELIGIBLE);
            result.setRejectionReason(String.join("\n", rejectionReasons));
            return result;
        }

        result.setEligibilityStatus(EligibilityStatus.AUTO_QUALIFIED);
        result.setRejectionReason(null);
        return result;
    }
}