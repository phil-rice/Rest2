package one.xingyi.core.annotationProcessors;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Post;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
@RequiredArgsConstructor
@ToString@EqualsAndHashCode

public class PostDom {
final String path;
final List<String> states;
    public static PostDom create(Post annotation, String url) {
        return new PostDom(url+annotation.url(), Arrays.asList(annotation.state()));
    }
}
