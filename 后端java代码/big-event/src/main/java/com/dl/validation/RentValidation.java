package com.dl.validation;

import com.dl.anno.Rent;
import com.dl.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RentValidation implements ConstraintValidator<Rent, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        //提供校验规则
        if (s == null){
            return false;
        }
        if (s.equals("在售") || s.equals("已售空")){
            return true;
        }
        return false;
    }
}
