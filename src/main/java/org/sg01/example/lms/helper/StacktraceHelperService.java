package org.sg01.example.lms.helper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@NoArgsConstructor
public class StacktraceHelperService {

    public String toStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
