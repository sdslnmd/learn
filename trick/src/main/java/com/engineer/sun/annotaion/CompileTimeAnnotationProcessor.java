package com.engineer.sun.annotaion;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.engineer.sun.annotaion.CustomAnnotation")
public class CompileTimeAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CustomAnnotation.class);
        for (Element e : elements) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "@CustomAnnotation annotated fields must be of type ParticularType");
        }
        return true;
    }

}