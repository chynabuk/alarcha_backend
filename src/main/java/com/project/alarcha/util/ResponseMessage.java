package com.project.alarcha.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResponseMessage<T> {
    private T value;
    private String status;
    private T details;
    private int valueAmount;

    public ResponseMessage<T> prepareSuccessMessage(T value) {
        ResponseMessage<T> successMessage = new ResponseMessage<>();
        successMessage.setValue(value);
        successMessage.setStatus("OK");
        try {
            List<T> values = (List<T>) value;
            valueAmount = values.size();
        }
        catch (Exception e){

        }
        successMessage.setDetails(null);
        return successMessage;
    }

    public ResponseMessage<T> prepareFailMessage(T details) {
        ResponseMessage<T> failMessage = new ResponseMessage<>();
        failMessage.setStatus("FAIL");
        failMessage.setValue(null);
        failMessage.setDetails(details);
        return failMessage;
    }

    public ResponseMessage<T> prepareErrorMessage(T details) {
        ResponseMessage<T> errorMessage = new ResponseMessage<>();
        errorMessage.setStatus("ERROR");
        errorMessage.setValue(null);
        errorMessage.setDetails(details);
        return errorMessage;
    }
}