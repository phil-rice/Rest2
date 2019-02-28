package one.xingyi.core.annotationProcessors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import java.util.Optional;

@ToString
@EqualsAndHashCode
public class ElementFail {
    public final String message;
    public final Optional<Element> optElement;
    public ElementFail(String message, Element element) {
        this.message = message;
        this.optElement = Optional.of(element);
    }

    public void logMe(LoggerAdapter log) {
        Optionals.doit(optElement, () -> log.error(message), e -> log.error(e, message));
    }

    public static <T> Result<ElementFail, T> lift(Element element, Result<String, T> result) {
        return result.failMap(s -> new ElementFail(s, element));
    }
}
