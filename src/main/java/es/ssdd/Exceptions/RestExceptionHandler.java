package es.ssdd.Exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    
	/*
    @Autowired
    private MessageSource messageSource;
    
    
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorInfo> typeMismatch(HttpServletRequest req, TypeMismatchException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage("error.bad.employee.id", null, locale);
         
        errorMessage += ex.getValue();
        String errorURL = req.getRequestURL().toString();
         
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
*/

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorInfo> typeMismatch(HttpServletRequest req, TypeMismatchException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFound(HttpServletRequest req, NotFoundException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
   
    @ExceptionHandler(NotValidException.class)
    public ResponseEntity<ErrorInfo> notValid(HttpServletRequest req, NotValidException ex) {
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorInfo> badRequest(HttpServletRequest req, BadRequestException ex) {
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
 
}