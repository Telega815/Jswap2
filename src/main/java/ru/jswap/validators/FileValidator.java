package ru.jswap.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.jswap.objects.UploadedFile;

@Component
public class FileValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object uploadedFile, Errors errors) {
        UploadedFile file = (UploadedFile) uploadedFile;
        if(file.getFiles()[0].getSize() == 0 ){
            errors.rejectValue("files", "page.selectFile", "select a file!");
        }
    }
}
