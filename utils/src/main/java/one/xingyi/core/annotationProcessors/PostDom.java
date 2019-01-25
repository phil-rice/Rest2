package one.xingyi.core.annotationProcessors;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Post;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class PostDom {
    public final String action;
    public final String path;
    public final List<String> states;
    public static PostDom create(String action, Post annotation, String url) {
        return new PostDom(action, url + annotation.url(), Arrays.asList(annotation.state()));
    }
}
